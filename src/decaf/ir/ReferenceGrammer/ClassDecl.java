package decaf.ir.ReferenceGrammer;

import decaf.ir.ASTvisitor;

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

    public void addFieldDecl(FieldDecl field){
        _fields.add(field);
    }

    public void addMethodDecl(MethodDecl method){
        _methods.add(method);
    }

    public List<FieldDecl> getFieldDecls(){
        return _fields;
    }

    public List<MethodDecl> getMethodDecls(){
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

    @Override
    public <T> T accept(ASTvisitor<T> v) {
        return v.visit(this);
    }
}