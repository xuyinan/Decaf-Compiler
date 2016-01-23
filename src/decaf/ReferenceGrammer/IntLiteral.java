package decaf.ReferenceGrammer;


public class IntLiteral extends Literal{
    private String _rawValue;
    private Integer _value;

    public IntLiteral(String value){
        _rawValue = value;
        _value = null;
    }

    public void setStringValue(String value){
        _rawValue = value;
    }

    public void setRawValue(String value){
        _rawValue = value;
    }

    public void setValue(int value){
        _value = value;
    }

    public String getStringValue(){
        return _rawValue;
    }

    public String getRawValue(){
        return _rawValue;
    }

    public int getValue(){
        return _value;
    }

    @Override
    public Type getType() {
        return Type.INT;
    }

    @Override
    public String toString(){
        return _rawValue;
    }

}
