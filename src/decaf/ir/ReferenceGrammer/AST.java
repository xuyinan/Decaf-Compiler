package decaf.ir.ReferenceGrammer;

import decaf.ir.ASTvisitor;

public abstract class AST {
    private int lineNumber;
    private int colNumber;
    
    public void setLineNumber(int ln) {
        lineNumber = ln;
    }

    public void setColumnNumber(int cn) {
        colNumber = cn;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getColumnNumber() {
        return colNumber;
    }

    public abstract <T> T accept(ASTvisitor<T> v);
}
