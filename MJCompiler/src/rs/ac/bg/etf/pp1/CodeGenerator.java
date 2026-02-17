package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.CounterVisitor.FormParamCounter;
import rs.ac.bg.etf.pp1.CounterVisitor.VarCounter;
import rs.ac.bg.etf.pp1.ast.*;
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
	public void visit(NewArray newArray) {
		Code.put(Code.newarray);
		Code.put(newArray.getType().struct == Tab.charType ? 0 : 1);
	}

	@Override
	public void visit(Designator designator) {
		SyntaxNode parent = designator.getParent();
		if (!(parent instanceof Assignment) && !(parent instanceof ProcCall) && !(parent instanceof FuncCallFactor)
				&& !(parent instanceof IncStmt) && !(parent instanceof DecStmt) && !(parent instanceof ReadStmt)
				&& !(parent instanceof FieldDesignator)) {
			Code.load(designator.obj);
		}
	}

	@Override
	public void visit(IdentDesignator identDesignator) {
		SyntaxNode parent = identDesignator.getParent();
		if (!(parent instanceof Assignment) && !(parent instanceof ProcCall) && !(parent instanceof FuncCallFactor)
				&& !(parent instanceof IncStmt) && !(parent instanceof DecStmt) && !(parent instanceof ReadStmt)
				&& !(parent instanceof FieldDesignator)) {
			Code.load(identDesignator.obj);
		}
	}

	@Override
	public void visit(ArrayDesignator arrayDesignator) {
		SyntaxNode parent = arrayDesignator.getParent();
		boolean lvalue = (parent instanceof Assignment && ((Assignment) parent).getDesignator() == arrayDesignator)
				|| (parent instanceof ReadStmt && ((ReadStmt) parent).getDesignator() == arrayDesignator)
				|| (parent instanceof IncStmt && ((IncStmt) parent).getDesignator() == arrayDesignator)
				|| (parent instanceof DecStmt && ((DecStmt) parent).getDesignator() == arrayDesignator);
		if (!lvalue) {
			Code.load(arrayDesignator.obj);
		}
	}

	@Override
	public void visit(LengthDesignator lengthDesignator) {
		Code.put(Code.arraylength);
	}

	@Override
	public void visit(FieldDesignator fieldDesignator) {
		SyntaxNode parent = fieldDesignator.getParent();
		if (!(parent instanceof Assignment) && !(parent instanceof ProcCall) && !(parent instanceof FuncCallFactor)
				&& !(parent instanceof IncStmt) && !(parent instanceof DecStmt) && !(parent instanceof ReadStmt)) {
			Code.load(fieldDesignator.obj);
		}
	}

	@Override
	public void visit(FuncCallFactor funcCall) {
		Obj functionObj = funcCall.getDesignator().obj;
		String name = functionObj.getName();
		if ("ord".equals(name) || "chr".equals(name)) {
			return;
		}
		if ("len".equals(name)) {
			Code.put(Code.arraylength);
			return;
		}
		int offset = functionObj.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);
	}

	@Override
	public void visit(ProcCall procCall) {
		Obj functionObj = procCall.getDesignator().obj;
		String name = functionObj.getName();
		if ("ord".equals(name) || "chr".equals(name)) {
			return;
		}
		if ("len".equals(name)) {
			Code.put(Code.arraylength);
			return;
		}
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
		if (d.obj.getKind() == Obj.Elem) {
			Code.put(Code.dup2);
		}
		Code.load(d.obj);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(d.obj);
	}

	@Override
	public void visit(DecStmt decStmt) {
		Designator d = decStmt.getDesignator();
		if (d.obj.getKind() == Obj.Elem) {
			Code.put(Code.dup2);
		}
		Code.load(d.obj);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.store(d.obj);
	}

	@Override
	public void visit(AddOperation addExpr) {
		if (addExpr.getAddop() instanceof PlusOp) {
			Code.put(Code.add);
		} else {
			Code.put(Code.sub);
		}
	}

	@Override
	public void visit(MulOperation mulExpr) {
		if (mulExpr.getMulop() instanceof MultOp) {
			Code.put(Code.mul);
		} else if (mulExpr.getMulop() instanceof DivOp) {
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
	public void visit(RelOperation relOp) {
		int op = Code.eq;
		if (relOp.getRelop() instanceof EqOp) {
			op = Code.eq;
		} else if (relOp.getRelop() instanceof NeOp) {
			op = Code.ne;
		} else if (relOp.getRelop() instanceof GtOp) {
			op = Code.gt;
		} else if (relOp.getRelop() instanceof GeOp) {
			op = Code.ge;
		} else if (relOp.getRelop() instanceof LtOp) {
			op = Code.lt;
		} else if (relOp.getRelop() instanceof LeOp) {
			op = Code.le;
		}
		Code.putFalseJump(op, 0);
		int falsePatch = Code.pc - 2;
		Code.loadConst(1);
		Code.putJump(0);
		int endPatch = Code.pc - 2;
		Code.fixup(falsePatch);
		Code.loadConst(0);
		Code.fixup(endPatch);
	}

	@Override
	public void visit(CondTernary condTernary) {
		Code.store(SemanticAnalyzer.ternaryRightTmp);
		Code.store(SemanticAnalyzer.ternaryLeftTmp);
		Code.store(SemanticAnalyzer.ternaryCondTmp);

		Code.load(SemanticAnalyzer.ternaryCondTmp);
		Code.loadConst(0);
		Code.putFalseJump(Code.ne, 0);
		int falsePatch = Code.pc - 2;

		Code.load(SemanticAnalyzer.ternaryLeftTmp);
		Code.putJump(0);
		int endPatch = Code.pc - 2;

		Code.fixup(falsePatch);
		Code.load(SemanticAnalyzer.ternaryRightTmp);
		Code.fixup(endPatch);
	}
}
