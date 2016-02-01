package decaf.ir.Semantic;

import java.util.ArrayList;
import java.util.List;

import decaf.ir.ASTvisitor;
import decaf.ir.ReferenceGrammer.*;
import decaf.ir.Desc.*;
import decaf.test.Error;


public class MethodCheckVisitor implements ASTvisitor<Boolean>{

    private ArrayList<Error> _errors;
    private Type _returnType;
    private ClassDescriptor _classDesc;

    public MethodCheckVisitor(ClassDescriptor classDesc){
        _errors = new ArrayList<Error>();
        _returnType = Type.UNDEFINED;
        _classDesc = classDesc;
    }

    public ArrayList<Error> getError(){
        return _errors;
    }

    @Override
    public Boolean visit(ArrayLocation loc){
        loc.getExpression().accept(this);
        return false;
    }

    @Override
    public Boolean visit(AssignStmt stmt){
        stmt.getLocation().accept(this);
        stmt.getExpression().accept(this);
        return false;
    }

    @Override
    public Boolean visit(BinOpExpr expr){
        expr.getLeftOperand().accept(this);
        expr.getRightOperand().accept(this);
        return false;
    }

    @Override
    public Boolean visit(Block block){
        for (Statement stmt: block.getStatements()){
            stmt.accept(this);
        }
        for (VarDecl varDecl: block.getVarDecls()){
            varDecl.accept(this);
        }
        return false;
    }

    @Override
    public Boolean visit(BoolLiteral lit){
        return false;
    }

    @Override
    public Boolean visit(BreakStmt stmt){
        return false;
    }

    @Override
    public Boolean visit(CalloutArg arg){
        if (!arg.isString()){
            arg.getExpression().accept(this);
        }
        return false;
    }

    @Override
    public Boolean visit(CalloutExpr expr){
        for (CalloutArg arg: expr.getArguments()){
            arg.accept(this);
        }
        return false;
    }

    @Override
    public Boolean visit(CharLiteral lit){
        return false;
    }

    @Override
    public Boolean visit(ClassDecl cd){
        for (FieldDecl fieldDecl:cd.getFieldDecls()){
            fieldDecl.accept(this);
        }

        for (MethodDecl methodDecl:cd.getMethodDecls()){
            methodDecl.accept(this);
        }
        return false;
    }

    @Override
    public Boolean visit(ContinueStmt stmt){
        return false;
    }

    @Override
    public Boolean visit(Field f){
        return false;
    }

    @Override
    public Boolean visit(FieldDecl fd){
        for (Field field: fd.getFields()){
            field.accept(this);
        }
        return false;
    }

    @Override
    public Boolean visit(ForStmt stmt){
        stmt.getInitVal().accept(this);
        stmt.getFinalVal().accept(this);
        stmt.getBlock().accept(this);
        return false;
    }

    @Override
    public Boolean visit(IfStmt stmt){
        stmt.getCondition().accept(this);
        stmt.getIfBlock().accept(this);
        if (stmt.getElseBlock()!=null){
            stmt.getElseBlock().accept(this);
        }
        return false;
    }

    @Override
    public Boolean visit(IntLiteral lit){
        return false;
    }

    @Override
    public Boolean visit(InvokeStmt stmt){
        stmt.getMethodCall().accept(this);
        return false;
    }

    @Override
    public Boolean visit(MethodCall expr){
        for (Expression arg: expr.getArg()){
            arg.accept(this);
        }

        // Check appropriate return type for each argument 
        MethodSymbolTable methodSymbolTable = _classDesc.getMethodSymbolTable();
        MethodDescriptor methodDesc = methodSymbolTable.get(expr.getId());

        if (methodDesc!=null){
            List<Type> methodParamTypes = methodDesc.getParamType();
            // Too few arguments error
            if (methodParamTypes.size() != expr.getArg().size()) {
                String msg = "Expecting "+Integer.toString(methodParamTypes.size())+" arguments but found "+expr.getArg().size();
                _errors.add(new Error(expr.getLineNumber(),expr.getColumnNumber(), msg));
                return false;
            }

            // Otherwise, type check the arguments
            for (int i = 0; i < methodParamTypes.size(); i++) {
                Type desiredType = methodParamTypes.get(i);
                Type argType = expr.getArg().get(i).getType();
                if (desiredType != argType) {
                    String msg = "Expecting "+desiredType.toString()+" but found "+argType.toString()+" for argument "+Integer.toString(i + 1);
                    _errors.add(new Error(expr.getLineNumber(),expr.getColumnNumber(), msg));
                }
            }
        }
        return false;
    }

    @Override
    public Boolean visit(MethodDecl md){
        for (Parameter param: md.getParams()){
            param.accept(this);
        }
        _returnType = md.getType();
        md.getBlock().accept(this);
        return false;
    }

    @Override
    public Boolean visit(Parameter param){
        return false;
    }

    @Override
    public Boolean visit(ReturnStmt stmt){
        if (stmt.getExpression() != null) {
            Type retType = stmt.getExpression().getType();
            if (_returnType != retType) {
                // Returning wrong type
                String msg = "Returning " + retType + " when expecting "+_returnType;
                _errors.add(new Error(stmt.getLineNumber(), stmt.getColumnNumber(), msg));
            }
            stmt.getExpression().accept(this);
        } else if (!_returnType.equals(Type.VOID)) {
            // Trying to return void when there is a current return type
            String msg = "Returning void when expecting " + _returnType;
            _errors.add(new Error(stmt, msg));
        }
        return true;
    }

    @Override
    public Boolean visit(UnaryOpExpr expr){
        expr.getExpression().accept(this);
        return false;
    }

    @Override
    public Boolean visit(VarDecl var){
        return false;
    }

    @Override
    public Boolean visit(VarLocation loc){
        return false;
    }
}