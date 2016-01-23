package decaf.ReferenceGrammer;


public class CharLiteral extends Literal{
    private String _value;

    public CharLiteral(String value){
        _value = value;
    }

    public void setValue(String value){
        _value = value;
    }

    public String getValue(){
        return _value;
    }

    @Override
    public Type getType() {
        return Type.CHAR;
    }

    @Override
    public String toString(){
        return _value;
    }

}