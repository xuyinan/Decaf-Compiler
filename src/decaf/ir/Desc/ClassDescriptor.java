package decaf.ir.Desc;

import java.util.HashMap;

public class ClassDescriptor extends Descriptor{
    private GeneralSymbolTable _fieldSymbolTable;
    private MethodSymbolTable _methodSymbolTable;
    private HashMap<Integer, GeneralSymbolTable> _scopeTable;

    public ClassDescriptor(){
        _fieldSymbolTable = new GeneralSymbolTable();
        _methodSymbolTable = new MethodSymbolTable();
        _scopeTable = new HashMap<Integer, GeneralSymbolTable>();
    }

    public void setFieldsymbolTable(GeneralSymbolTable fieldSymbolTable){
        _fieldSymbolTable = fieldSymbolTable;
    }

    public void setMethodSymbolTable(MethodSymbolTable methodSymbolTable){
        _methodSymbolTable = methodSymbolTable;
    }

    public void setScopeTable(HashMap<Integer, GeneralSymbolTable> scopeTable){
        _scopeTable = scopeTable;
    }

    public GeneralSymbolTable getFieldSymbolTable(){
        return _fieldSymbolTable;
    }

    public MethodSymbolTable getMethodSymbolTable(){
        return _methodSymbolTable;
    }

    public HashMap<Integer, GeneralSymbolTable> getScopeTable(){
        return _scopeTable;
    }

    @Override
    public String toString() {
        String rtn = "Field Symbol Table: ";
        rtn += "{KEY: <id>=(<id>,<type>)}\n";
        rtn += _fieldSymbolTable;
        rtn += "\n\n";
        rtn += "Method Symbol Table: ";
        rtn += "{KEY: <id>=(<id>, <rtntype>, <paramtable>, <localtable>}\n";
        rtn += _methodSymbolTable;
        return rtn;
    }
}
