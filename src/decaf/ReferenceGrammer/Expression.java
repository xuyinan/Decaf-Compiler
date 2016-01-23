package decaf.ReferenceGrammer;


public class Expression extends AST{
    protected Expression _expr;
    protected Type _op;

    public void setType(Type op){
        _op = op;
    }

    public Type getType(){
        return _op;
    }

}
