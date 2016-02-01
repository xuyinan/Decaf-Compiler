package decaf.ir.ReferenceGrammer;


public enum BinOpType{
    PLUS,
    MINUS,
    MULTIPLY,
    DIVIDE,
    MOD,
    LE,
    LEQ,
    GE,
    GEQ,
    EQ,
    NEQ,
    AND,
    OR;

    @Override
    public String toString(){
        switch(this){
            case PLUS:
                return "+";
            case MINUS:
                return "-";
            case MULTIPLY:
                return "*";
            case DIVIDE:
                return "/";
            case MOD:
                return "%";
            case LE:
                return "<";
            case LEQ:
                return "<=";
            case GE:
                return ">";
            case GEQ:
                return ">=";
            case EQ:
                return "==";
            case NEQ:
                return "!=";
            case AND:
                return "&&";
            case OR:
                return "||";
        }
        return null;
    }

}
