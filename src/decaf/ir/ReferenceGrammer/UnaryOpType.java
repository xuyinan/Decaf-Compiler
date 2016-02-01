package decaf.ir.ReferenceGrammer;


public enum UnaryOpType{
    NOT,
    MINUS;
    
    @Override
    public String toString() {
        switch(this) {
            case NOT:
                return "!";
            case MINUS: 
                return "-";
        }
        return null;
    }

}