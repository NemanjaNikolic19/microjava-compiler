package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.ActualType;
import rs.ac.bg.etf.pp1.ast.AddOperation;
import rs.ac.bg.etf.pp1.ast.AddTerm;
import rs.ac.bg.etf.pp1.ast.ArrayDesignator;
import rs.ac.bg.etf.pp1.ast.ArrayVarDecl;
import rs.ac.bg.etf.pp1.ast.Assignment;
import rs.ac.bg.etf.pp1.ast.BoolConst;
import rs.ac.bg.etf.pp1.ast.CharConst;
import rs.ac.bg.etf.pp1.ast.CondExprWrapper;
import rs.ac.bg.etf.pp1.ast.CondSingle;
import rs.ac.bg.etf.pp1.ast.CondTernary;
import rs.ac.bg.etf.pp1.ast.DesignatorFactor;
import rs.ac.bg.etf.pp1.ast.ExprFactor;
import rs.ac.bg.etf.pp1.ast.FieldDesignator;
import rs.ac.bg.etf.pp1.ast.FuncCallFactor;
import rs.ac.bg.etf.pp1.ast.IdentDesignator;
import rs.ac.bg.etf.pp1.ast.LengthDesignator;
import rs.ac.bg.etf.pp1.ast.MethodDecl;
import rs.ac.bg.etf.pp1.ast.MethodTypeName;
import rs.ac.bg.etf.pp1.ast.MulOperation;
import rs.ac.bg.etf.pp1.ast.NewArray;
import rs.ac.bg.etf.pp1.ast.NumConst;
import rs.ac.bg.etf.pp1.ast.PrintStmt;
import rs.ac.bg.etf.pp1.ast.ProcCall;
import rs.ac.bg.etf.pp1.ast.ProgName;
import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.ast.RelOperation;
import rs.ac.bg.etf.pp1.ast.RelSingle;
import rs.ac.bg.etf.pp1.ast.ReturnExpr;
import rs.ac.bg.etf.pp1.ast.SimpleVarDecl;
import rs.ac.bg.etf.pp1.ast.SyntaxNode;
import rs.ac.bg.etf.pp1.ast.TermFactor;
import rs.ac.bg.etf.pp1.ast.UnaryMinus;
import rs.ac.bg.etf.pp1.ast.VoidType;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class SemanticPass extends VisitorAdaptor {

	boolean errorDetected = false;
	int printCallCount = 0;
	Obj currentMethod = null;
	boolean returnFound = false;
	int nVars;


	Logger log = Logger.getLogger(getClass());

	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		log.info(msg.toString());
	}

	public void visit(Program program) {
		nVars = Tab.currentScope.getnVars();
		Tab.chainLocalSymbols(program.getProgName().obj);
		Tab.closeScope();
	}

	public void visit(ProgName progName) {
		progName.obj = Tab.insert(Obj.Prog, progName.getPName(), Tab.noType);
		Tab.openScope();
	}

	public void visit(ActualType type) {
		Obj typeNode = Tab.find(type.getI1());
		if (typeNode == Tab.noObj) {
			report_error("Nije pronadjen tip " + type.getI1() + " u tabeli simbola", type);
			type.struct = Tab.noType;
		} else if (Obj.Type == typeNode.getKind()) {
			type.struct = typeNode.getType();
		} else {
			report_error("Ime " + type.getI1() + " ne predstavlja tip", type);
			type.struct = Tab.noType;
		}
	}

	public void visit(VoidType type) {
		type.struct = Tab.noType;
	}

	public void visit(SimpleVarDecl varDecl) {
		report_info("Deklarisana promenljiva " + varDecl.getVarName(), varDecl);
		Tab.insert(Obj.Var, varDecl.getVarName(), varDecl.getType().struct);
	}

	public void visit(ArrayVarDecl varDecl) {
		report_info("Deklarisana nizovna promenljiva " + varDecl.getVarName(), varDecl);
		Tab.insert(Obj.Var, varDecl.getVarName(), new Struct(Struct.Array, varDecl.getType().struct));
	}

	public void visit(MethodTypeName methodTypeName) {
		currentMethod = Tab.insert(Obj.Meth, methodTypeName.getMethName(), methodTypeName.getType().struct);
		methodTypeName.obj = currentMethod;
		Tab.openScope();
		returnFound = false;
		report_info("Obradjuje se funkcija " + methodTypeName.getMethName(), methodTypeName);
	}

	public void visit(MethodDecl methodDecl) {
		if (!returnFound && currentMethod != null && currentMethod.getType() != Tab.noType) {
			report_error("Funkcija " + currentMethod.getName() + " nema return iskaz", methodDecl);
		}
		if (currentMethod != null) {
			Tab.chainLocalSymbols(currentMethod);
		}
		Tab.closeScope();
		currentMethod = null;
		returnFound = false;
	}

	public void visit(IdentDesignator designator) {
		Obj obj = Tab.find(designator.getName());
		if (obj == Tab.noObj) {
			report_error("Ime " + designator.getName() + " nije deklarisano", designator);
		}
		designator.obj = obj;
	}

	public void visit(ArrayDesignator designator) {
		Obj arrObj = designator.getDesignator().obj;
		if (arrObj == null || arrObj == Tab.noObj || arrObj.getType().getKind() != Struct.Array) {
			report_error("Leva strana indeksiranja nije niz", designator);
			designator.obj = Tab.noObj;
			return;
		}
		if (designator.getExpr().struct != Tab.intType) {
			report_error("Indeks niza mora biti int", designator);
		}
		designator.obj = new Obj(Obj.Elem, "$arrElem", arrObj.getType().getElemType());
	}

	public void visit(LengthDesignator designator) {
		Obj base = designator.getDesignator().obj;
		if (base == null || base == Tab.noObj || base.getType().getKind() != Struct.Array) {
			report_error("length se moze koristiti samo nad nizom", designator);
			designator.obj = Tab.noObj;
			return;
		}
		designator.obj = new Obj(Obj.Con, "$length", Tab.intType);
	}

	public void visit(FieldDesignator designator) {
		report_error("Pristup polju nije podrzan za nivo A", designator);
		designator.obj = Tab.noObj;
	}

	public void visit(NumConst cnst) {
		cnst.struct = Tab.intType;
	}

	public void visit(CharConst cnst) {
		cnst.struct = Tab.charType;
	}

	public void visit(BoolConst cnst) {
		cnst.struct = Tab.intType;
	}

	public void visit(DesignatorFactor factor) {
		factor.struct = factor.getDesignator().obj != null ? factor.getDesignator().obj.getType() : Tab.noType;
	}

	public void visit(FuncCallFactor funcCall) {
		Obj func = funcCall.getDesignator().obj;
		if (func == null || func.getKind() != Obj.Meth) {
			report_error("Ime nije funkcija", funcCall);
			funcCall.struct = Tab.noType;
			return;
		}
		report_info("Pronadjen poziv funkcije " + func.getName(), funcCall);
		funcCall.struct = func.getType();
	}

	public void visit(ExprFactor expr) {
		expr.struct = expr.getExpr().struct;
	}

	public void visit(UnaryMinus expr) {
		if (expr.getFactor().struct != Tab.intType) {
			report_error("Unary minus se moze primeniti samo nad int", expr);
			expr.struct = Tab.noType;
		} else {
			expr.struct = Tab.intType;
		}
	}

	public void visit(NewArray expr) {
		if (expr.getExpr().struct != Tab.intType) {
			report_error("Velicina niza mora biti int", expr);
		}
		expr.struct = new Struct(Struct.Array, expr.getType().struct);
	}

	public void visit(TermFactor term) {
		term.struct = term.getFactor().struct;
	}

	public void visit(MulOperation term) {
		if (term.getTerm().struct == Tab.intType && term.getFactor().struct == Tab.intType) {
			term.struct = Tab.intType;
		} else {
			report_error("Nekompatibilni tipovi u mul izrazu", term);
			term.struct = Tab.noType;
		}
	}

	public void visit(AddTerm addExpr) {
		addExpr.struct = addExpr.getTerm().struct;
	}

	public void visit(AddOperation addExpr) {
		if (addExpr.getAddExpr().struct == Tab.intType && addExpr.getTerm().struct == Tab.intType) {
			addExpr.struct = Tab.intType;
		} else {
			report_error("Nekompatibilni tipovi u add izrazu", addExpr);
			addExpr.struct = Tab.noType;
		}
	}

	public void visit(RelSingle relExpr) {
		relExpr.struct = relExpr.getAddExpr().struct;
	}

	public void visit(RelOperation relExpr) {
		if (relExpr.getAddExpr().struct.compatibleWith(relExpr.getAddExpr1().struct)) {
			relExpr.struct = Tab.intType;
		} else {
			report_error("Nekompatibilni tipovi u relacionom izrazu", relExpr);
			relExpr.struct = Tab.noType;
		}
	}

	public void visit(CondSingle condExpr) {
		condExpr.struct = condExpr.getRelExpr().struct;
	}

	public void visit(CondTernary condExpr) {
		Struct left = condExpr.getCondExpr().struct;
		Struct right = condExpr.getCondExpr1().struct;
		if (left.compatibleWith(right)) {
			condExpr.struct = left;
		} else {
			report_error("Tipovi grana ternarnog operatora moraju biti kompatibilni", condExpr);
			condExpr.struct = Tab.noType;
		}
	}

	public void visit(CondExprWrapper expr) {
		expr.struct = expr.getCondExpr().struct;
	}

	public void visit(Assignment assignment) {
		Obj d = assignment.getDesignator().obj;
		if (d != null && d != Tab.noObj && !assignment.getExpr().struct.assignableTo(d.getType())) {
			report_error("Nekompatibilni tipovi u dodeli", assignment);
		}
	}

	public void visit(ProcCall procCall) {
		Obj func = procCall.getDesignator().obj;
		if (func == null || func.getKind() != Obj.Meth) {
			report_error("Ime nije procedura/funkcija", procCall);
		} else {
			report_info("Pronadjen poziv funkcije " + func.getName(), procCall);
		}
	}

	public void visit(ReturnExpr returnExpr) {
		returnFound = true;
		if (currentMethod != null && !currentMethod.getType().compatibleWith(returnExpr.getExpr().struct)) {
			report_error("Tip izraza u return naredbi ne odgovara povratnom tipu funkcije", returnExpr);
		}
	}

	public void visit(PrintStmt printStmt) {
		printCallCount++;
	}

	public boolean passed() {
		return !errorDetected;
	}
}
