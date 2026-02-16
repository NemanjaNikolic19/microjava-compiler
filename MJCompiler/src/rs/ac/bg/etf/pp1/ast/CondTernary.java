// generated with ast extension for cup
// version 0.8
// 16/1/2026 19:47:41


package rs.ac.bg.etf.pp1.ast;

public class CondTernary extends CondExpr {

    private RelExpr RelExpr;
    private CondExpr CondExpr;
    private CondExpr CondExpr1;

    public CondTernary (RelExpr RelExpr, CondExpr CondExpr, CondExpr CondExpr1) {
        this.RelExpr=RelExpr;
        if(RelExpr!=null) RelExpr.setParent(this);
        this.CondExpr=CondExpr;
        if(CondExpr!=null) CondExpr.setParent(this);
        this.CondExpr1=CondExpr1;
        if(CondExpr1!=null) CondExpr1.setParent(this);
    }

    public RelExpr getRelExpr() {
        return RelExpr;
    }

    public void setRelExpr(RelExpr RelExpr) {
        this.RelExpr=RelExpr;
    }

    public CondExpr getCondExpr() {
        return CondExpr;
    }

    public void setCondExpr(CondExpr CondExpr) {
        this.CondExpr=CondExpr;
    }

    public CondExpr getCondExpr1() {
        return CondExpr1;
    }

    public void setCondExpr1(CondExpr CondExpr1) {
        this.CondExpr1=CondExpr1;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(RelExpr!=null) RelExpr.accept(visitor);
        if(CondExpr!=null) CondExpr.accept(visitor);
        if(CondExpr1!=null) CondExpr1.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(RelExpr!=null) RelExpr.traverseTopDown(visitor);
        if(CondExpr!=null) CondExpr.traverseTopDown(visitor);
        if(CondExpr1!=null) CondExpr1.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(RelExpr!=null) RelExpr.traverseBottomUp(visitor);
        if(CondExpr!=null) CondExpr.traverseBottomUp(visitor);
        if(CondExpr1!=null) CondExpr1.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("CondTernary(\n");

        if(RelExpr!=null)
            buffer.append(RelExpr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(CondExpr!=null)
            buffer.append(CondExpr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(CondExpr1!=null)
            buffer.append(CondExpr1.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [CondTernary]");
        return buffer.toString();
    }
}
