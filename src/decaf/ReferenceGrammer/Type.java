package decaf.ReferenceGrammer;


public enum Type{
    INT,
    INTARRAY,
    BOOLEAN,
    BOOLEANARRAY,
    CHAR,
    VOID,
    UNDEFINED;

    @Override
    public String toString(){
        switch(this){
            case INT:
                return "int";
            case INTARRAY:
                return "int[]";
            case BOOLEAN:
                return "bool";
            case BOOLEANARRAY:
                return "boolean[]";
            case CHAR:
                return "char";
            case VOID:
                return "void";
            case UNDEFINED:
                return "undefined";
        }
        return null;
    }

    public boolean isArray(){
        if (this == Type.INTARRAY || this == Type.BOOLEANARRAY){
            return true;
        }
        return false;
    }
}