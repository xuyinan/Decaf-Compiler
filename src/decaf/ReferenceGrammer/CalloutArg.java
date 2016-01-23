package decaf.ReferenceGrammer;


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

    public boolean isString(){
        if (_str==null){
            return false;
        }
        return true;
    }

    @Override
    public String toString(){
        if(_str == null){
            return _expr.toString();
        }
        return _str;
    }
}