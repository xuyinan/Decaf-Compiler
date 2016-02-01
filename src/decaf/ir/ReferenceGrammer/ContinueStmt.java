package decaf.ir.ReferenceGrammer;

import decaf.ir.ASTvisitor;

public class ContinueStmt extends Statement {

    public ContinueStmt(){}

    @Override
    public String toString(){
        return "continue";
    }

    @Override
    public <T> T accept(ASTvisitor<T> v) {
        return v.visit(this);
    }    
}