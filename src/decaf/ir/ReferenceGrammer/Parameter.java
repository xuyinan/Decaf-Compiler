package decaf.ir.ReferenceGrammer;

import decaf.ir.ASTvisitor;

public class Parameter extends AST{
    private Type _type;
    private String _id;

    public Parameter(Type type, String id){
        _type = type;
        _id = id;
    }

    public void setType(Type type){
        _type = type;
    }

    public void setId(String id){
        _id = id;
    }

    public Type getType(){
        return _type;
    }

    public String getId(){
        return _id;
    }

    @Override
    public String toString(){
        return _type.toString() + " " + _id;
    }

    @Override
    public <T> T accept(ASTvisitor<T> v) {
        return v.visit(this);
    }
}
