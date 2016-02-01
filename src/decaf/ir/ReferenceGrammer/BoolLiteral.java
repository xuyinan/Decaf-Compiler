package decaf.ir.ReferenceGrammer;

import decaf.ir.ASTvisitor;

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

    public int getValue(){
        return _value;
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

    @Override
    public <T> T accept(ASTvisitor<T> v) {
        return v.visit(this);
    }
}