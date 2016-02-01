package decaf.ir.ReferenceGrammer;

import decaf.ir.ASTvisitor;

import java.util.ArrayList;
import java.util.List;

public class MethodCall extends CallExpr {
    private String _name;
    private List<Expression> _args;

    public MethodCall(String name) {
        _name = name;
        _args = new ArrayList<Expression>();
    }

    public MethodCall(String name, List<Expression> args) {
        _name = name;
        _args = args;
    }

    public void setId(String name){
        _name = name;
    }

    public void setArg(List<Expression> args){
        _args = args;
    }

    public String getId(){
        return _name;
    }

    public List<Expression> getArg(){
        return _args;
    }

    @Override
    public String toString(){
        String rst = _name + "(";
        if (!_args.isEmpty()){
            for (Expression e:_args){
                rst += (e.toString()+",");
            }
            rst = rst.substring(0, rst.length()-2);
        }
        rst += ")";
        return rst;
    }

    @Override
    public <T> T accept(ASTvisitor<T> v) {
        return v.visit(this);
    }
}



