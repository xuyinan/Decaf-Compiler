package decaf.ir.Desc;

import decaf.ir.ReferenceGrammer.*;

public abstract class GeneralDescriptor {
    protected Type _type;
    protected String _id;

    public void setType(Type type){
        _type = type;
    }

    public void setId(String id){
        _id = id;
    }

    public Type getType(){
        return _type;
    }

    public String getId(){
        return _id;
    }

    @Override
    public String toString(){
        return "(" + _id + ", " + _type + ")";
    }
}
