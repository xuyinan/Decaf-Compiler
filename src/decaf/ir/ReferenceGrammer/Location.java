package decaf.ir.ReferenceGrammer;

import decaf.ir.ASTvisitor;

public abstract class Location extends Expression {
    protected String _id;

    public void setId(String id){
        _id = id;
    }

    public String getId(){
        return _id;
    }

}