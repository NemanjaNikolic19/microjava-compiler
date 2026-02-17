// generated with ast extension for cup
// version 0.8
// 17/1/2026 11:36:40


package rs.ac.bg.etf.pp1.ast;

public class EnumDeclStmt extends VarDecl {

    private String enumName;
    private EnumItemList EnumItemList;

    public EnumDeclStmt (String enumName, EnumItemList EnumItemList) {
        this.enumName=enumName;
        this.EnumItemList=EnumItemList;
        if(EnumItemList!=null) EnumItemList.setParent(this);
    }

    public String getEnumName() {
        return enumName;
    }

    public void setEnumName(String enumName) {
        this.enumName=enumName;
    }

    public EnumItemList getEnumItemList() {
        return EnumItemList;
    }

    public void setEnumItemList(EnumItemList EnumItemList) {
        this.EnumItemList=EnumItemList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(EnumItemList!=null) EnumItemList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(EnumItemList!=null) EnumItemList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(EnumItemList!=null) EnumItemList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("EnumDeclStmt(\n");

        buffer.append(" "+tab+enumName);
        buffer.append("\n");

        if(EnumItemList!=null)
            buffer.append(EnumItemList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [EnumDeclStmt]");
        return buffer.toString();
    }
}
