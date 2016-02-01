package decaf.ir.ReferenceGrammer;


public class TempExpression extends AST {
    private Expression _expr;
    private BinOpType _operator;
    private TempExpression _tempExpr;
    private boolean _isLeaf;

    public TempExpression(Expression expr, BinOpType operator){
        _expr = expr;
        _operator = operator;
        _tempExpr = null;
        _isLeaf = false;
    }

    public void setExpression(Expression expr){
        _expr = expr;
    }

    public void setOperator(BinOpType operator){
        _operator = operator;
    }

    public void setTempExpr(TempExpression tempExpr){
        _tempExpr = tempExpr;
    }

    public void setLeaf(boolean isLeaf){
        _isLeaf = isLeaf;
    }

    public Expression getExpression(){
        return _expr;
    }

    public BinOpType getOperator(){
        return _operator;
    }

    public TempExpression getTempExpr(){
        return _tempExpr;
    }

   public boolean isLeaf(){
        return _isLeaf;
    }
 

}