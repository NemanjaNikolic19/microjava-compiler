// generated with ast extension for cup
// version 0.8
// 17/1/2026 11:36:40


package rs.ac.bg.etf.pp1.ast;

public class MultiVarDecl extends VarDecl {

    private Type Type;
    private String firstName;
    private VarDeclTail VarDeclTail;

    public MultiVarDecl (Type Type, String firstName, VarDeclTail VarDeclTail) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.firstName=firstName;
        this.VarDeclTail=VarDeclTail;
        if(VarDeclTail!=null) VarDeclTail.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName=firstName;
    }

    public VarDeclTail getVarDeclTail() {
        return VarDeclTail;
    }

    public void setVarDeclTail(VarDeclTail VarDeclTail) {
        this.VarDeclTail=VarDeclTail;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(VarDeclTail!=null) VarDeclTail.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(VarDeclTail!=null) VarDeclTail.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(VarDeclTail!=null) VarDeclTail.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MultiVarDecl(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+firstName);
        buffer.append("\n");

        if(VarDeclTail!=null)
            buffer.append(VarDeclTail.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MultiVarDecl]");
        return buffer.toString();
    }
}
