package decaf.ir.ReferenceGrammer;

import decaf.ir.ASTvisitor;

public class BinOpExpr extends Expression{

    private Expression _lexpr;
    private BinOpType _op;
    private Expression _rexpr;

    public BinOpExpr(Expression lexpr, BinOpType op, Expression rexpr){
        _lexpr = lexpr;
        _op = op;
        _rexpr = rexpr;
    }

    public BinOpExpr(Expression lexpr, TempExpression temp){
        _lexpr = lexpr;
        _op = temp.getOperator();
        _rexpr = temp.getExpression();
    }

    public void setLeftOperand(Expression lexpr){
        _lexpr = lexpr;
    }

    public void setOperator(BinOpType op){
        _op = op;
    }

    public void setRightOperand(Expression rexpr){
        _rexpr = rexpr;
    }

    public Expression getLeftOperand(){
        return _lexpr;
    }

    public BinOpType getOperator(){
        return _op;
    }

    public Expression getRightOperand(){
        return _rexpr;
    }

    @Override
    public String toString(){
        return _lexpr + " " + _op.toString() + " " + _rexpr;
    }

    @Override
    public <T> T accept(ASTvisitor<T> v) {
        return v.visit(this);
    }
}

