// generated with ast extension for cup
// version 0.8
// 17/1/2026 11:36:40


package rs.ac.bg.etf.pp1.ast;

public class VarDeclTailSingle extends VarDeclTail {

    private VarDeclTailItem VarDeclTailItem;

    public VarDeclTailSingle (VarDeclTailItem VarDeclTailItem) {
        this.VarDeclTailItem=VarDeclTailItem;
        if(VarDeclTailItem!=null) VarDeclTailItem.setParent(this);
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
        if(VarDeclTailItem!=null) VarDeclTailItem.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDeclTailItem!=null) VarDeclTailItem.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDeclTailItem!=null) VarDeclTailItem.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclTailSingle(\n");

        if(VarDeclTailItem!=null)
            buffer.append(VarDeclTailItem.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclTailSingle]");
        return buffer.toString();
    }
}
