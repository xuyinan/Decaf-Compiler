package decaf.ir.Desc;

import decaf.ir.ReferenceGrammer.*;


public class ParamDescriptor extends GeneralDescriptor{ 

    public ParamDescriptor(Type type, String id){
        _type = type;
        _id = id;
    }
}