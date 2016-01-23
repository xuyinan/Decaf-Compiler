package decaf.ReferenceGrammer;

import java.util.List;

public class CalloutExpr extends CallExpr {
    
    private String _methodName;
    private List<CalloutArg> _args;
    
    public CalloutExpr(String name, List<CalloutArg> a) {
        _methodName = name;
        _args = a;
    }
    
    public void addArgument(String arg) {
        _args.add(new CalloutArg(arg));
    }
    
    public void addArgument(Expression arg) {
        _args.add(new CalloutArg(arg));
    }

    public String getMethodName() {
        return _methodName;
    }

    public void setMethodName(String methodName) {
        _methodName = methodName;
    }
    
    public List<CalloutArg> getArguments() {
        return _args;
    }

    public void setArgs(List<CalloutArg> args) {
        _args = args;
    }

    @Override
    public String toString() {
        return "callout (" + _methodName + ", " + _args + ")";
    }

}
