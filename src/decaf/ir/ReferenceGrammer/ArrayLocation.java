package decaf.ir.ReferenceGrammer;

import decaf.ir.ASTvisitor;

public class ArrayLocation extends Location {
    private Expression _expr;
    private int _size;

    public ArrayLocation(String id, Expression expr){
        _id = id;
        _expr = expr;
        _size = -1;
    }

    public void setExpression(Expression expr){
        _expr = expr;
    }

    public void setSize(int size){
        _size = size;
    }

    public Expression getExpression(){
        return _expr;
    }

    public int getSize(){
        return _size;
    }

    @Override
    public String toString(){
        return _id + "[" + _expr + "]";
    }

    @Override
    public <T> T accept(ASTvisitor<T> v) {
        return v.visit(this);
    }
}