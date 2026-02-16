package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.CounterVisitor.FormParamCounter;
import rs.ac.bg.etf.pp1.CounterVisitor.VarCounter;
import rs.ac.bg.etf.pp1.ast.AddOperation;
import rs.ac.bg.etf.pp1.ast.ArrayDesignator;
import rs.ac.bg.etf.pp1.ast.Assignment;
import rs.ac.bg.etf.pp1.ast.BoolConst;
import rs.ac.bg.etf.pp1.ast.CharConst;
import rs.ac.bg.etf.pp1.ast.DecStmt;
import rs.ac.bg.etf.pp1.ast.Designator;
import rs.ac.bg.etf.pp1.ast.FuncCallFactor;
import rs.ac.bg.etf.pp1.ast.IncStmt;
import rs.ac.bg.etf.pp1.ast.MethodDecl;
import rs.ac.bg.etf.pp1.ast.MethodTypeName;
import rs.ac.bg.etf.pp1.ast.MulOperation;
import rs.ac.bg.etf.pp1.ast.NumConst;
import rs.ac.bg.etf.pp1.ast.PrintStmt;
import rs.ac.bg.etf.pp1.ast.PrintWidthStmt;
import rs.ac.bg.etf.pp1.ast.ProcCall;
import rs.ac.bg.etf.pp1.ast.ReadStmt;
import rs.ac.bg.etf.pp1.ast.ReturnExpr;
import rs.ac.bg.etf.pp1.ast.ReturnNoExpr;
import rs.ac.bg.etf.pp1.ast.SyntaxNode;
import rs.ac.bg.etf.pp1.ast.UnaryMinus;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;

public class CodeGenerator extends VisitorAdaptor {

	private int mainPc;

	public int getMainPc() {
		return mainPc;
	}

	@Override
	public void visit(MethodTypeName methodTypeName) {
		if ("main".equalsIgnoreCase(methodTypeName.getMethName())) {
			mainPc = Code.pc;
		}
		methodTypeName.obj.setAdr(Code.pc);

		SyntaxNode methodNode = methodTypeName.getParent();
		VarCounter varCnt = new VarCounter();
		methodNode.traverseTopDown(varCnt);
		FormParamCounter fpCnt = new FormParamCounter();
		methodNode.traverseTopDown(fpCnt);

		Code.put(Code.enter);
		Code.put(fpCnt.getCount());
		Code.put(varCnt.getCount() + fpCnt.getCount());
	}

	@Override
	public void visit(MethodDecl methodDecl) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	@Override
	public void visit(ReturnExpr returnExpr) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	@Override
	public void visit(ReturnNoExpr returnNoExpr) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	@Override
	public void visit(Assignment assignment) {
		Code.store(assignment.getDesignator().obj);
	}

	@Override
	public void visit(NumConst cnst) {
		Code.loadConst(cnst.getN1());
	}

	@Override
	public void visit(CharConst cnst) {
		Code.loadConst(cnst.getC1());
	}

	@Override
	public void visit(BoolConst cnst) {
		Code.loadConst(cnst.getValue());
	}

	@Override
	public void visit(Designator designator) {
		SyntaxNode parent = designator.getParent();
		if (!(parent instanceof Assignment) && !(parent instanceof ProcCall) && !(parent instanceof FuncCallFactor)
				&& !(parent instanceof IncStmt) && !(parent instanceof DecStmt) && !(parent instanceof ReadStmt)) {
			Code.load(designator.obj);
		}
	}

	@Override
	public void visit(FuncCallFactor funcCall) {
		Obj functionObj = funcCall.getDesignator().obj;
		int offset = functionObj.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);
	}

	@Override
	public void visit(ProcCall procCall) {
		Obj functionObj = procCall.getDesignator().obj;
		int offset = functionObj.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);
	}

	@Override
	public void visit(PrintStmt printStmt) {
		Code.loadConst(5);
		if (printStmt.getExpr().struct == Tab.charType) {
			Code.put(Code.bprint);
		} else {
			Code.put(Code.print);
		}
	}

	@Override
	public void visit(PrintWidthStmt printStmt) {
		Code.loadConst(printStmt.getN2());
		if (printStmt.getExpr().struct == Tab.charType) {
			Code.put(Code.bprint);
		} else {
			Code.put(Code.print);
		}
	}

	@Override
	public void visit(ReadStmt readStmt) {
		if (readStmt.getDesignator().obj.getType() == Tab.charType) {
			Code.put(Code.bread);
		} else {
			Code.put(Code.read);
		}
		Code.store(readStmt.getDesignator().obj);
	}

	@Override
	public void visit(IncStmt incStmt) {
		Designator d = incStmt.getDesignator();
		Code.load(d.obj);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(d.obj);
	}

	@Override
	public void visit(DecStmt decStmt) {
		Designator d = decStmt.getDesignator();
		Code.load(d.obj);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.store(d.obj);
	}

	@Override
	public void visit(AddOperation addExpr) {
		if (addExpr.getAddop() instanceof rs.ac.bg.etf.pp1.ast.PlusOp) {
			Code.put(Code.add);
		} else {
			Code.put(Code.sub);
		}
	}

	@Override
	public void visit(MulOperation mulExpr) {
		if (mulExpr.getMulop() instanceof rs.ac.bg.etf.pp1.ast.MultOp) {
			Code.put(Code.mul);
		} else if (mulExpr.getMulop() instanceof rs.ac.bg.etf.pp1.ast.DivOp) {
			Code.put(Code.div);
		} else {
			Code.put(Code.rem);
		}
	}

	@Override
	public void visit(UnaryMinus unaryMinus) {
		Code.put(Code.neg);
	}

	@Override
	public void visit(ArrayDesignator arrayDesignator) {
		// no-op; index/address handling is emitted by Code.load/store on designator obj context
	}
}
