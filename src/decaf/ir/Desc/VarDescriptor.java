package decaf.ir.Desc;

import decaf.ir.ReferenceGrammer.*;


public class VarDescriptor extends GeneralDescriptor{
    private Object _value;

    public VarDescriptor(Type type, String id){
        _type = type;
        _id = id;
    }

    public void setValue(Object value){
        _value = value;
    }

    public Object getValue(){
        return _value;
    }
}