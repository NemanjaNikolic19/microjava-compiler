// generated with ast extension for cup
// version 0.8
// 16/1/2026 19:47:41


package rs.ac.bg.etf.pp1.ast;

public interface Visitor { 

    public void visit(Designator Designator);
    public void visit(RelExpr RelExpr);
    public void visit(Factor Factor);
    public void visit(Mulop Mulop);
    public void visit(ActualParamList ActualParamList);
    public void visit(CondExpr CondExpr);
    public void visit(Expr Expr);
    public void visit(FormalParamList FormalParamList);
    public void visit(Type Type);
    public void visit(FormPars FormPars);
    public void visit(VarDeclList VarDeclList);
    public void visit(AddExpr AddExpr);
    public void visit(Unmatched Unmatched);
    public void visit(Addop Addop);
    public void visit(MethodDeclList MethodDeclList);
    public void visit(Statement Statement);
    public void visit(Relop Relop);
    public void visit(Term Term);
    public void visit(StatementList StatementList);
    public void visit(Matched Matched);
    public void visit(ActualPars ActualPars);
    public void visit(LengthDesignator LengthDesignator);
    public void visit(FieldDesignator FieldDesignator);
    public void visit(ArrayDesignator ArrayDesignator);
    public void visit(IdentDesignator IdentDesignator);
    public void visit(ActualParam ActualParam);
    public void visit(ActualParams ActualParams);
    public void visit(NoActuals NoActuals);
    public void visit(Actuals Actuals);
    public void visit(NeOp NeOp);
    public void visit(EqOp EqOp);
    public void visit(LeOp LeOp);
    public void visit(LtOp LtOp);
    public void visit(GeOp GeOp);
    public void visit(GtOp GtOp);
    public void visit(ModOp ModOp);
    public void visit(DivOp DivOp);
    public void visit(MultOp MultOp);
    public void visit(MinusOp MinusOp);
    public void visit(PlusOp PlusOp);
    public void visit(NewArray NewArray);
    public void visit(UnaryMinus UnaryMinus);
    public void visit(ExprFactor ExprFactor);
    public void visit(FuncCallFactor FuncCallFactor);
    public void visit(DesignatorFactor DesignatorFactor);
    public void visit(BoolConst BoolConst);
    public void visit(CharConst CharConst);
    public void visit(NumConst NumConst);
    public void visit(TermFactor TermFactor);
    public void visit(MulOperation MulOperation);
    public void visit(AddTerm AddTerm);
    public void visit(AddOperation AddOperation);
    public void visit(RelOperation RelOperation);
    public void visit(RelSingle RelSingle);
    public void visit(CondTernary CondTernary);
    public void visit(CondSingle CondSingle);
    public void visit(CondExprWrapper CondExprWrapper);
    public void visit(ProcCall ProcCall);
    public void visit(MatchedIf MatchedIf);
    public void visit(DecStmt DecStmt);
    public void visit(IncStmt IncStmt);
    public void visit(ReturnNoExpr ReturnNoExpr);
    public void visit(ReturnExpr ReturnExpr);
    public void visit(ReadStmt ReadStmt);
    public void visit(PrintWidthStmt PrintWidthStmt);
    public void visit(PrintStmt PrintStmt);
    public void visit(ErrAssignment ErrAssignment);
    public void visit(Assignment Assignment);
    public void visit(UnmatchedIfElse UnmatchedIfElse);
    public void visit(UnmatchedIf UnmatchedIf);
    public void visit(UnmachedStmt UnmachedStmt);
    public void visit(MatchedStmt MatchedStmt);
    public void visit(NoStmt NoStmt);
    public void visit(Statements Statements);
    public void visit(FormalParamDecl FormalParamDecl);
    public void visit(SingleFormalParamDecl SingleFormalParamDecl);
    public void visit(FormalParamDecls FormalParamDecls);
    public void visit(NoFormParam NoFormParam);
    public void visit(FormParams FormParams);
    public void visit(MethodTypeName MethodTypeName);
    public void visit(MethodDecl MethodDecl);
    public void visit(NoMethodDecl NoMethodDecl);
    public void visit(MethodDeclarations MethodDeclarations);
    public void visit(VoidType VoidType);
    public void visit(ActualType ActualType);
    public void visit(ErrVarDeclComma ErrVarDeclComma);
    public void visit(ErrVarDeclSemi ErrVarDeclSemi);
    public void visit(VarDecl VarDecl);
    public void visit(NoVarDecl NoVarDecl);
    public void visit(VarDeclarations VarDeclarations);
    public void visit(ProgName ProgName);
    public void visit(Program Program);

}
