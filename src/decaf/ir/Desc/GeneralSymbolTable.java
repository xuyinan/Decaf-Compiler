package decaf.ir.Desc;


public class GeneralSymbolTable extends SymbolTable<String, GeneralDescriptor>{
    private GeneralSymbolTable _parent;
    private int _scopeId;
    
    public GeneralSymbolTable(){
        _parent = null;
        _scopeId = -1;
    }

    public GeneralSymbolTable(GeneralSymbolTable parent){
        _parent = parent;
        _scopeId = -1;
    }

    public void setParent(GeneralSymbolTable parent){
        _parent = parent;
    }

    public void setScopeId(int scopeId){
        _scopeId = scopeId;
    }

    public GeneralSymbolTable getParent(){
        return _parent;
    }

    public int getScopeId(){
        return _scopeId;
    }
}

