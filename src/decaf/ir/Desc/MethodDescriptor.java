package decaf.ir.Desc;

import java.util.ArrayList;
import java.util.List;

import decaf.ir.ReferenceGrammer.*;

public class MethodDescriptor extends Descriptor{
    private GeneralSymbolTable _paramSymbolTable;
    private GeneralSymbolTable _localSymbolTable;
    private List<Type> _paramTypes;
    private Type _returnType;
    private String _id;
    private Object _body;

    public MethodDescriptor(String id, Type returnType, List<Parameter> params){
        _paramSymbolTable = null;
        _localSymbolTable = null;
        _id = id;
        _returnType = returnType;
        _paramTypes = new ArrayList<Type>();
        for (Parameter param: params){
            _paramTypes.add(param.getType());
        }
    }

    public void setParamSymbolTable(GeneralSymbolTable paramSymbolTable){
        _paramSymbolTable = paramSymbolTable;
    }

    public void setLocalSymbolTable(GeneralSymbolTable localSymbolTable){
        _localSymbolTable = localSymbolTable;
    }

    public void setParamType(List<Type> paramTypes){
        _paramTypes = paramTypes;
    }

    public void setReturnType(Type returnType){
        _returnType = returnType;
    }

    public void setId(String id){
        _id = id;
    }

    public void setBody(Object body){
        _body = body;
    }

    public GeneralSymbolTable getParamSymbolTable(){
        return _paramSymbolTable;
    }

    public GeneralSymbolTable getLocalSymbolTable(){
        return _localSymbolTable;
    }

    public List<Type> getParamType(){
        return _paramTypes;
    }

    public Type getReturnType(){
        return _returnType;
    }

    public String getId(){
        return _id;
    }

    public Object getBody(){
        return _body;
    }

    @Override
    public String toString() {
        return "("+_id+", "+_returnType+", "+_paramSymbolTable+", "+_localSymbolTable+")";
    }
}