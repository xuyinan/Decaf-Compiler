package decaf.ir.ReferenceGrammer;

import decaf.ir.ASTvisitor;

public class Field extends AST{
    private String _id;
    private IntLiteral _arraySize;
    private Type _type;
    

    public Field(String id){
        _id = id;
        _arraySize = null;
    }

    public Field(String id, IntLiteral arraySize){
        _id = id;
        _arraySize = arraySize;
    }

    public void setId(String id){
        _id = id;
    }

    public void setArraySize(IntLiteral arraySize){
        _arraySize = arraySize;
    }

    public void setType(Type type){
        _type = type;
    }

    public String getId(){
        return _id;
    }

    public IntLiteral getArraySize(){
        return _arraySize;
    }

    public Type getType(){
        return _type;
    }

    @Override
    public String toString(){
        if (_arraySize!=null){
            return _id + "[" + _arraySize.toString() + "]";
        }else{
            return _id;
        }
    }

    @Override
    public <T> T accept(ASTvisitor<T> v) {
        return v.visit(this);
    }
}
