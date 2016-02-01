package decaf.ir.ReferenceGrammer;


public enum AssignOpType{
    ASSIGN,
    INCREMENT,
    DECREMENT;

    @Override
    public String toString(){
        switch(this){
            case ASSIGN:
                return "=";
            case INCREMENT:
                return "+=";
            case DECREMENT:
                return "-=";
        }
        return null;
    }

}
