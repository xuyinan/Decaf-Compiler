package decaf.ir.ReferenceGrammer;

import decaf.ir.ASTvisitor;

public class BreakStmt extends Statement {
    
    public BreakStmt() { }
    
    @Override
    public String toString() {
        return "break";
    }

    @Override
    public <T> T accept(ASTvisitor<T> v) {
        return v.visit(this);
    }
}