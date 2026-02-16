// generated with ast extension for cup
// version 0.8
// 16/1/2026 20:43:29


package rs.ac.bg.etf.pp1.ast;

public class GeOp extends Relop {

    public GeOp () {
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("GeOp(\n");

        buffer.append(tab);
        buffer.append(") [GeOp]");
        return buffer.toString();
    }
}
