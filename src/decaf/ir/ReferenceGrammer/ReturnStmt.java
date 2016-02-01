package decaf.ir.ReferenceGrammer;

import decaf.ir.ASTvisitor;

public class ReturnStmt extends Statement {
    private Expression _expr;

    public ReturnStmt(){
        _expr = null;
    }

    public ReturnStmt(Expression expr){
        _expr = expr;
    }

    public void setExpression(Expression expr){
        _expr = expr;
    }

    public Expression getExpression(){
        return _expr;
    }

    @Override
    public String toString(){
        if (_expr==null){
            return "return";
        }else{
            return "return " + _expr;
        }
    }

    @Override
    public <T> T accept(ASTvisitor<T> v) {
        return v.visit(this);
    }
}