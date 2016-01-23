package decaf.ReferenceGrammer;


public class UnaryOpExpr extends Expression{

    private Expression _epxr;
    private UnaryOpType _op;

    public UnaryOpExpr(Expression expr, UnaryOpType op){
        _expr = expr;
        _op = op;
    }

    public void setExpression(Expression expr){
        _expr = expr;
    }

    public void setOperator(UnaryOpType op){
        _op = op;
    }

    public Expression getExpression(){
        return _expr;
    }

    public UnaryOpType getOperator(){
        return _op;
    }

    @Override
    public String toString(){
        return _op.toString() + _expr;
    }

}

