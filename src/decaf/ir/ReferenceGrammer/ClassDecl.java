package decaf.ReferenceGrammer;

import java.util.ArrayList;
import java.util.List;


public class ClassDecl extends AST {
    private List<FieldDecl> _fields;
    private List<MethodDecl> _methods;

    public ClassDecl(){
        _fields = new ArrayList<FieldDecl>();
        _methods = new ArrayList<MethodDecl>();
    }

    public ClassDecl(List<FieldDecl> fields, List<MethodDecl> methods){
        _fields = fields;
        _methods = methods;
    }

    public void addField(FieldDecl field){
        _fields.add(field);
    }

    public void addMethod(MethodDecl method){
        _methods.add(method);
    }

    public List<FieldDecl> getField(){
        return _fields;
    }

    public List<MethodDecl> getMethod(){
        return _methods;
    }

    @Override
    public String toString(){
        String rst = "class " + '\n';
        for (FieldDecl f:_fields){
            rst += f.toString() + '\n';
        }
        for (MethodDecl m:_methods){
            rst += m.toString() + '\n';
        }
        return rst;
    }

}