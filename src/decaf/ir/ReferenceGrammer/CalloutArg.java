package decaf.ir.ReferenceGrammer;

import decaf.ir.ASTvisitor;

public class CalloutArg extends AST{
    private Expression _expr = null;
    private String _str = null;

    public CalloutArg(Expression expr){
        _expr = expr;
    }

    public CalloutArg(String str){
        _str = str;
    }

    public void setString(String str){
        _str = str;
    }

    public void setExpression(Expression expr){
        _expr = expr;
    }

    public String getString(){
        return _str;
    }

    public Expression getExpression(){
        return _expr;
    }

    public boolean isString() {
        if (_str != null) return true;
        return false;
    }

    @Override
    public String toString(){
        if(_str == null){
            return _expr.toString();
        }
        return _str;
    }

    @Override
    public <T> T accept(ASTvisitor<T> v) {
        return v.visit(this);
    }
}