package decaf.ReferenceGrammer;

import java.util.ArrayList;
import java.util.List;

public class VarDecl extends AST{
    private Type _type;
    private List<String> _ids;

    public VarDecl(){
        _type = Type.VOID;
        _ids = new ArrayList<String>();
    }

    public VarDecl(Type type, List<String> ids){
        _type = type;
        _ids = ids;
    }

    public void addVar(String var){
        _ids.add(var);
    }

    public void setType(Type type){
        _type = type;
    }

    public List<String> getVar(){
        return _ids;
    }

    public Type getType(){
        return _type;
    }

    @Override
    public String toString() {
        return _type + _ids.toString();
    }
}