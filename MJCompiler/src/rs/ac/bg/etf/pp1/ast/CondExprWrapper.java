// generated with ast extension for cup
// version 0.8
// 17/1/2026 11:36:40


package rs.ac.bg.etf.pp1.ast;

public class CondExprWrapper extends Expr {

    private CondExpr CondExpr;

    public CondExprWrapper (CondExpr CondExpr) {
        this.CondExpr=CondExpr;
        if(CondExpr!=null) CondExpr.setParent(this);
    }

    public CondExpr getCondExpr() {
        return CondExpr;
    }

    public void setCondExpr(CondExpr CondExpr) {
        this.CondExpr=CondExpr;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(CondExpr!=null) CondExpr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(CondExpr!=null) CondExpr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(CondExpr!=null) CondExpr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("CondExprWrapper(\n");

        if(CondExpr!=null)
            buffer.append(CondExpr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [CondExprWrapper]");
        return buffer.toString();
    }
}
