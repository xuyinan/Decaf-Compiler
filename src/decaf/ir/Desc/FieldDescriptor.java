package decaf.ir.Desc;

import decaf.ir.ReferenceGrammer.*;


public class FieldDescriptor extends GeneralDescriptor{
    private int _arraySize;

    public FieldDescriptor(Type type, String id, int arraySize){
        _type = type;
        _id = id;
        _arraySize = arraySize;
    }

    public FieldDescriptor(Type type, String id){
        _type = type;
        _id = id;
        _arraySize = -1;
    }

    public void setArraySize(int arraySize){
        _arraySize = arraySize;
    }

    public int getArraySize(){
        return _arraySize;
    }
}