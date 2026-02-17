// generated with ast extension for cup
// version 0.8
// 17/1/2026 11:36:40


package rs.ac.bg.etf.pp1.ast;

public class VarDeclTailList extends VarDeclTail {

    private VarDeclTail VarDeclTail;
    private VarDeclTailItem VarDeclTailItem;

    public VarDeclTailList (VarDeclTail VarDeclTail, VarDeclTailItem VarDeclTailItem) {
        this.VarDeclTail=VarDeclTail;
        if(VarDeclTail!=null) VarDeclTail.setParent(this);
        this.VarDeclTailItem=VarDeclTailItem;
        if(VarDeclTailItem!=null) VarDeclTailItem.setParent(this);
    }

    public VarDeclTail getVarDeclTail() {
        return VarDeclTail;
    }

    public void setVarDeclTail(VarDeclTail VarDeclTail) {
        this.VarDeclTail=VarDeclTail;
    }

    public VarDeclTailItem getVarDeclTailItem() {
        return VarDeclTailItem;
    }

    public void setVarDeclTailItem(VarDeclTailItem VarDeclTailItem) {
        this.VarDeclTailItem=VarDeclTailItem;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarDeclTail!=null) VarDeclTail.accept(visitor);
        if(VarDeclTailItem!=null) VarDeclTailItem.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDeclTail!=null) VarDeclTail.traverseTopDown(visitor);
        if(VarDeclTailItem!=null) VarDeclTailItem.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDeclTail!=null) VarDeclTail.traverseBottomUp(visitor);
        if(VarDeclTailItem!=null) VarDeclTailItem.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclTailList(\n");

        if(VarDeclTail!=null)
            buffer.append(VarDeclTail.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclTailItem!=null)
            buffer.append(VarDeclTailItem.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclTailList]");
        return buffer.toString();
    }
}
