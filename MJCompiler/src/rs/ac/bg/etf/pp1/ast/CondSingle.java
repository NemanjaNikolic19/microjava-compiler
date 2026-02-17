// generated with ast extension for cup
// version 0.8
// 17/1/2026 11:36:40


package rs.ac.bg.etf.pp1.ast;

public class CondSingle extends CondExpr {

    private RelExpr RelExpr;

    public CondSingle (RelExpr RelExpr) {
        this.RelExpr=RelExpr;
        if(RelExpr!=null) RelExpr.setParent(this);
    }

    public RelExpr getRelExpr() {
        return RelExpr;
    }

    public void setRelExpr(RelExpr RelExpr) {
        this.RelExpr=RelExpr;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(RelExpr!=null) RelExpr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(RelExpr!=null) RelExpr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(RelExpr!=null) RelExpr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("CondSingle(\n");

        if(RelExpr!=null)
            buffer.append(RelExpr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [CondSingle]");
        return buffer.toString();
    }
}
