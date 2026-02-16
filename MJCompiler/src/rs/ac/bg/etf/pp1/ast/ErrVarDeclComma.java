// generated with ast extension for cup
// version 0.8
// 16/1/2026 19:47:41


package rs.ac.bg.etf.pp1.ast;

public class ErrVarDeclComma extends VarDecl {

    private Type Type;
    private String nextVar;

    public ErrVarDeclComma (Type Type, String nextVar) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.nextVar=nextVar;
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public String getNextVar() {
        return nextVar;
    }

    public void setNextVar(String nextVar) {
        this.nextVar=nextVar;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ErrVarDeclComma(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+nextVar);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ErrVarDeclComma]");
        return buffer.toString();
    }
}
