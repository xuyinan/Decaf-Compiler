package decaf.ReferenceGrammer;


public class BoolLiteral extends Literal{
    private static int TRUE = 1;
    private static int FALSE = 0;
    private int _value;

    public BoolLiteral(String value){
        if (value.equals("true")){
            _value = BoolLiteral.TRUE;
        }else if (value.equals("false")){
            _value = BoolLiteral.FALSE;
        }else{
            _value = -1;
        }
    }

    public void setValue(String value){
        if (value.equals("true")){
            _value = BoolLiteral.TRUE;
        }else if (value.equals("false")){
            _value = BoolLiteral.FALSE;
        }else{
            _value = -1;
        }
    }

    public String getValue(){
        if (_value == 1){
            return "true";
        }else if (_value == 0){
            return "false";
        }else{
            return "Invalid boolean!";
        }
    }

    public Type getType() {
        return Type.BOOLEAN;
    }

    @Override
    public String toString(){
        if (_value == 1){
            return "true";
        }else if (_value == 0){
            return "false";
        }else{
            return "Invalid boolean!";
        }
    }
}