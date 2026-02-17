package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class SemanticAnalyzer extends VisitorAdaptor {

	boolean errorDetected = false;
	int printCallCount = 0;
	Obj currentMethod = null;
	boolean returnFound = false;
	boolean mainFound = false;
	int nVars;

	public static Obj ternaryCondTmp;
	public static Obj ternaryLeftTmp;
	public static Obj ternaryRightTmp;

	private Struct boolType = Tab.intType;

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

	public void report_symbol_use(String symbolName, Obj symbolObj, SyntaxNode info) {
		StringBuilder msg = new StringBuilder("Detektovan simbol");
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		msg.append(": naziv=").append(symbolName);
		msg.append(", obj=").append(symbolObj);
		log.info(msg.toString());
	}

	public void visit(Program program) {
		if (!mainFound) {
			report_error("U programu mora postojati void main() bez argumenata", program);
		}
		nVars = Tab.currentScope.getnVars();
		Tab.chainLocalSymbols(program.getProgName().obj);
		Tab.closeScope();
	}

	public void visit(ProgName progName) {
		progName.obj = Tab.insert(Obj.Prog, progName.getPName(), Tab.noType);
		Tab.openScope();
		Tab.insert(Obj.Type, "bool", boolType);
		ternaryCondTmp = Tab.insert(Obj.Var, "__ternary_cond", Tab.intType);
		ternaryLeftTmp = Tab.insert(Obj.Var, "__ternary_left", Tab.intType);
		ternaryRightTmp = Tab.insert(Obj.Var, "__ternary_right", Tab.intType);
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

	public void visit(ConstDeclNum cnst) {
		if (cnst.getType().struct != Tab.intType) {
			report_error("Numericka konstanta mora biti tipa int", cnst);
			return;
		}
		Obj c = Tab.insert(Obj.Con, cnst.getConstName(), Tab.intType);
		c.setAdr(cnst.getN1());
	}

	public void visit(ConstDeclChar cnst) {
		if (cnst.getType().struct != Tab.charType) {
			report_error("Karakterna konstanta mora biti tipa char", cnst);
			return;
		}
		Obj c = Tab.insert(Obj.Con, cnst.getConstName(), Tab.charType);
		c.setAdr(cnst.getC1());
	}

	public void visit(ConstDeclBool cnst) {
		if (cnst.getType().struct != boolType) {
			report_error("Bool konstanta mora biti tipa bool", cnst);
			return;
		}
		Obj c = Tab.insert(Obj.Con, cnst.getConstName(), boolType);
		c.setAdr(cnst.getB1());
	}

	public void visit(EnumDeclStmt enumDecl) {
		Obj enumTypeObj = Tab.insert(Obj.Type, enumDecl.getEnumName(), Tab.intType);
		Tab.openScope();
		insertEnumItems(enumDecl.getEnumItemList(), 0);
		Tab.chainLocalSymbols(enumTypeObj);
		Tab.closeScope();
	}

	private int insertEnumItems(EnumItemList list, int nextValue) {
		if (list instanceof EnumItemListSingle) {
			return insertEnumItem(((EnumItemListSingle) list).getEnumItem(), nextValue);
		}
		if (list instanceof EnumItemListMany) {
			EnumItemListMany many = (EnumItemListMany) list;
			int afterLeft = insertEnumItems(many.getEnumItemList(), nextValue);
			return insertEnumItem(many.getEnumItem(), afterLeft);
		}
		return nextValue;
	}

	private int insertEnumItem(EnumItem item, int nextValue) {
		if (item instanceof EnumItemSimple) {
			EnumItemSimple simple = (EnumItemSimple) item;
			Obj c = Tab.insert(Obj.Con, simple.getItemName(), Tab.intType);
			c.setAdr(nextValue);
			return nextValue + 1;
		}
		if (item instanceof EnumItemAssigned) {
			EnumItemAssigned assigned = (EnumItemAssigned) item;
			Obj c = Tab.insert(Obj.Con, assigned.getItemName(), Tab.intType);
			c.setAdr(assigned.getValue());
			return assigned.getValue() + 1;
		}
		return nextValue;
	}

	public void visit(SimpleVarDecl varDecl) {
		report_info("Deklarisana promenljiva " + varDecl.getVarName(), varDecl);
		Tab.insert(Obj.Var, varDecl.getVarName(), varDecl.getType().struct);
	}

	public void visit(ArrayVarDecl varDecl) {
		report_info("Deklarisana nizovna promenljiva " + varDecl.getVarName(), varDecl);
		Tab.insert(Obj.Var, varDecl.getVarName(), new Struct(Struct.Array, varDecl.getType().struct));
	}

	public void visit(MultiVarDecl varDecl) {
		report_info("Deklarisana promenljiva " + varDecl.getFirstName(), varDecl);
		Tab.insert(Obj.Var, varDecl.getFirstName(), varDecl.getType().struct);
		insertVarDeclTail(varDecl.getVarDeclTail(), varDecl.getType().struct);
	}

	public void visit(MultiArrayVarDecl varDecl) {
		report_info("Deklarisana nizovna promenljiva " + varDecl.getFirstName(), varDecl);
		Tab.insert(Obj.Var, varDecl.getFirstName(), new Struct(Struct.Array, varDecl.getType().struct));
		insertVarDeclTail(varDecl.getVarDeclTail(), varDecl.getType().struct);
	}

	private void insertVarDeclTail(VarDeclTail tail, Struct baseType) {
		if (tail instanceof VarDeclTailSingle) {
			insertVarDeclTailItem(((VarDeclTailSingle) tail).getVarDeclTailItem(), baseType);
			return;
		}
		if (tail instanceof VarDeclTailList) {
			VarDeclTailList list = (VarDeclTailList) tail;
			insertVarDeclTail(list.getVarDeclTail(), baseType);
			insertVarDeclTailItem(list.getVarDeclTailItem(), baseType);
		}
	}

	private void insertVarDeclTailItem(VarDeclTailItem item, Struct baseType) {
		if (item instanceof VarDeclTailSimple) {
			String name = ((VarDeclTailSimple) item).getVarName();
			report_info("Deklarisana promenljiva " + name, item);
			Tab.insert(Obj.Var, name, baseType);
		} else if (item instanceof VarDeclTailArray) {
			String name = ((VarDeclTailArray) item).getVarName();
			report_info("Deklarisana nizovna promenljiva " + name, item);
			Tab.insert(Obj.Var, name, new Struct(Struct.Array, baseType));
		}
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
		if (currentMethod != null && "main".equals(currentMethod.getName())) {
			boolean noArgs = methodDecl.getFormPars() instanceof NoFormParam;
			if (currentMethod.getType() == Tab.noType && noArgs) {
				mainFound = true;
			} else {
				report_error("main mora biti deklarisan kao void main() bez argumenata", methodDecl);
			}
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
		if (obj != null && obj != Tab.noObj) {
			report_symbol_use(designator.getName(), obj, designator);
		}
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
		designator.obj = new Obj(Obj.Elem, "$length", Tab.intType);
	}

	public void visit(FieldDesignator designator) {
		Obj base = designator.getDesignator().obj;
		if (base == null || base == Tab.noObj || base.getKind() != Obj.Type) {
			report_error("Leva strana pristupa polju mora biti tip (enum)", designator);
			designator.obj = Tab.noObj;
			return;
		}
		Obj found = null;
		for (Obj member : base.getLocalSymbols()) {
			if (designator.getI2().equals(member.getName())) {
				found = member;
				break;
			}
		}
		if (found == null) {
			report_error("Polje " + designator.getI2() + " nije pronadjeno u " + base.getName(), designator);
			designator.obj = Tab.noObj;
			return;
		}
		designator.obj = found;
	}

	public void visit(NumConst cnst) {
		cnst.struct = Tab.intType;
	}

	public void visit(CharConst cnst) {
		cnst.struct = Tab.charType;
	}

	public void visit(BoolConst cnst) {
		cnst.struct = boolType;
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
		report_symbol_use(func.getName(), func, funcCall);
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
			relExpr.struct = boolType;
		} else {
			report_error("Nekompatibilni tipovi u relacionom izrazu", relExpr);
			relExpr.struct = Tab.noType;
		}
	}

	public void visit(CondSingle condExpr) {
		condExpr.struct = condExpr.getRelExpr().struct;
	}

	public void visit(CondTernary condExpr) {
		Struct condType = condExpr.getRelExpr().struct;
		if (condType != boolType && condType != Tab.intType) {
			report_error("Uslov ternarnog operatora mora biti bool/int", condExpr);
		}
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
		if (d != null && d != Tab.noObj && d.getKind() != Obj.Var && d.getKind() != Obj.Elem && d.getKind() != Obj.Fld) {
			report_error("Leva strana dodele mora biti promenljiva, element niza ili polje", assignment);
			return;
		}
		if (d != null && d != Tab.noObj && !assignment.getExpr().struct.assignableTo(d.getType())) {
			report_error("Nekompatibilni tipovi u dodeli", assignment);
		}
	}

	public void visit(IncStmt incStmt) {
		Obj d = incStmt.getDesignator().obj;
		if (d == null || d == Tab.noObj) {
			return;
		}
		if (d.getKind() != Obj.Var && d.getKind() != Obj.Elem && d.getKind() != Obj.Fld) {
			report_error("Operand ++ mora biti promenljiva, element niza ili polje", incStmt);
			return;
		}
		if (d.getType() != Tab.intType) {
			report_error("Operand ++ mora biti tipa int", incStmt);
		}
	}

	public void visit(DecStmt decStmt) {
		Obj d = decStmt.getDesignator().obj;
		if (d == null || d == Tab.noObj) {
			return;
		}
		if (d.getKind() != Obj.Var && d.getKind() != Obj.Elem && d.getKind() != Obj.Fld) {
			report_error("Operand -- mora biti promenljiva, element niza ili polje", decStmt);
			return;
		}
		if (d.getType() != Tab.intType) {
			report_error("Operand -- mora biti tipa int", decStmt);
		}
	}

	public void visit(ReadStmt readStmt) {
		Obj d = readStmt.getDesignator().obj;
		if (d == null || d == Tab.noObj) {
			return;
		}
		if (d.getKind() != Obj.Var && d.getKind() != Obj.Elem && d.getKind() != Obj.Fld) {
			report_error("read argument mora biti promenljiva, element niza ili polje", readStmt);
			return;
		}
		Struct t = d.getType();
		if (t != Tab.intType && t != Tab.charType && t != boolType) {
			report_error("read podrzava samo int, char i bool", readStmt);
		}
	}

	public void visit(ProcCall procCall) {
		Obj func = procCall.getDesignator().obj;
		if (func == null || func.getKind() != Obj.Meth) {
			report_error("Ime nije procedura/funkcija", procCall);
		} else {
			report_symbol_use(func.getName(), func, procCall);
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
		Struct t = printStmt.getExpr().struct;
		if (t != Tab.intType && t != Tab.charType && t != boolType) {
			report_error("print podrzava samo int, char i bool", printStmt);
		}
	}

	public void visit(PrintWidthStmt printWidthStmt) {
		printCallCount++;
		Struct t = printWidthStmt.getExpr().struct;
		if (t != Tab.intType && t != Tab.charType && t != boolType) {
			report_error("print podrzava samo int, char i bool", printWidthStmt);
		}
	}

	public boolean passed() {
		return !errorDetected;
	}
}
