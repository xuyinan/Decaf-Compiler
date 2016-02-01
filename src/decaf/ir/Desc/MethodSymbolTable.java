package decaf.ir.Desc;


public class MethodSymbolTable extends SymbolTable<String, MethodDescriptor>{
    
    public MethodSymbolTable(){
        super();
    }

    @Override
    public String toString(){
        String rst = "";
        for (String key: this.keySet()) {
            rst += "" + key + "=" + this.get(key) + "\n";
        }
        return rst;
    }
}

