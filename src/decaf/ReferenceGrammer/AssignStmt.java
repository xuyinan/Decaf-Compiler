package decaf.ReferenceGrammer;



public class AssignStmt extends Statement {
    private Location _loc;
    private AssignOpType _assignOp;
    private Expression _expr;
    
    public AssignStmt(Location loc, AssignOpType assignOp, Expression expr) {
        _loc = loc;
        _assignOp = assignOp;
        _expr = expr;
    }
    
    public void setLocation(Location loc) {
        _loc = loc;
    }

    public void setOperator(AssignOpType assignOp) {
        _assignOp = assignOp;
    }

    public void setExpression(Expression expr) {
        _expr = expr;
    }

    public Location getLocation() {
        return _loc;
    }

    public AssignOpType getOperator() {
        return _assignOp;
    }

    public Expression getExpression() {
        return _expr;
    }

    @Override
    public String toString() {
        return _loc + " " + _assignOp + " " + _expr;
    }
}