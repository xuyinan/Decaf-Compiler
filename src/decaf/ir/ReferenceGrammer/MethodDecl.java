package decaf.ir.ReferenceGrammer;

import decaf.ir.ASTvisitor;

import java.util.ArrayList;
import java.util.List;

public class MethodDecl extends Decl {
    private Type _type;
    private String _id;
    private List<Parameter> _params;
    private Block _block;

    public MethodDecl() {}

    public MethodDecl(Type type, String id){
        _type = type;
        _id = id;
        _params = new ArrayList<Parameter>();
    }

    public MethodDecl(Type type, String id, List<Parameter> params){
        _type = type;
        _id = id;
        _params = params;
    }

    public void setType(Type type){
        _type = type;
    }

    public void setId(String id){
        _id = id;
    }

    public void setParams(List<Parameter> params) {
        _params = params;
    }

    public void addParam(Parameter param){
        _params.add(param);
    }

    public void setBlock(Block block){
        _block = block;
    }
    
    public Type getType(){
        return _type;
    }

    public String getId(){
        return _id;
    }

    public List<Parameter> getParams() {
        return _params;
    }

    public Block getBlock(){
        return _block;
    }

    @Override
    public String toString(){
        String rst = _type + " " + _id + "(" + _params + ")\n";
        rst += _block.toString();
        return rst;
    }

    @Override
    public <T> T accept(ASTvisitor<T> v) {
        return v.visit(this);
    }
}