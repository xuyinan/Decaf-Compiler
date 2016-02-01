package decaf.ir.Semantic;

import java.util.ArrayList;
import java.util.List;

import decaf.ir.ASTvisitor;
import decaf.ir.ReferenceGrammer.*;
import decaf.ir.Desc.*;
import decaf.test.Error;


public class ArraySizeCheckVisitor implements ASTvisitor<Integer>{

    private ClassDecl _cd;
    private ArrayList<Error> _errors;

    public ArraySizeCheckVisitor(){
        _errors = new ArrayList<Error>();
    }

    public ArrayList<Error> getError(){
        return _errors;
    }

    @Override
    public Integer visit(ArrayLocation loc){
        for (FieldDecl fieldDecl: _cd.getFieldDecls()){
            for (Field field: fieldDecl.getFields()){
                if (field.getId().equals(loc.getId())){
                    loc.setSize(field.getArraySize().getValue());
                }
            }
        }
        loc.getExpression().accept(this);
        return 0;
    }

    @Override
    public Integer visit(AssignStmt stmt){
        stmt.getLocation().accept(this);
        stmt.getExpression().accept(this);
        return 0;
    }

    @Override
    public Integer visit(BinOpExpr expr){
        expr.getLeftOperand().accept(this);
        expr.getRightOperand().accept(this);
        return 0;
    }

    @Override
    public Integer visit(Block block){
        for (Statement stmt: block.getStatements()){
            stmt.accept(this);
        }
        for (VarDecl varDecl: block.getVarDecls()){
            varDecl.accept(this);
        }
        return 0;
    }

    @Override
    public Integer visit(BoolLiteral lit){
        return 0;
    }

    @Override
    public Integer visit(BreakStmt stmt){
        return 0;
    }

    @Override
    public Integer visit(CalloutArg arg){
        if (!arg.isString()) {
            arg.getExpression().accept(this);
        }
        return 0;
    }

    @Override
    public Integer visit(CalloutExpr expr){
        for (CalloutArg arg: expr.getArguments()){
            arg.accept(this);
        }
        return 0;
    }

    @Override
    public Integer visit(CharLiteral lit){
        return 0;
    }

    @Override
    public Integer visit(ClassDecl cd){
        _cd = cd;
        for (FieldDecl fieldDecl:cd.getFieldDecls()){
            fieldDecl.accept(this);
        }
        for (MethodDecl methodDecl:cd.getMethodDecls()){
            methodDecl.accept(this);
        }
        return 0;
    }

    @Override
    public Integer visit(ContinueStmt stmt){
        return 0;
    }

    @Override
    public Integer visit(Field f){
        if (f.getType().isArray()){
            if (f.getArraySize().getValue()<1){
                String msg = "Size of array '" + f.getId() + "' is less than 1";
                _errors.add(new Error(f, msg));
            }
        }
        return 0;
    }

    @Override
    public Integer visit(FieldDecl fd){
        for (Field field: fd.getFields()){
            field.accept(this);
        }
        return 0;
    }

    @Override
    public Integer visit(ForStmt stmt){
        stmt.getBlock().accept(this);
        return 0;
    }

    @Override
    public Integer visit(IfStmt stmt){
        stmt.getCondition().accept(this);
        stmt.getIfBlock().accept(this);
        if (stmt.getElseBlock()!=null){
            stmt.getElseBlock().accept(this);
        }
        return 0;
    }

    @Override
    public Integer visit(IntLiteral lit){
        return 0;
    }

    @Override
    public Integer visit(InvokeStmt stmt){
        stmt.getMethodCall().accept(this);
        return 0;
    }

    @Override
    public Integer visit(MethodCall expr){
        for (Expression arg: expr.getArg()){
            arg.accept(this);
        }
        return 0;
    }

    @Override
    public Integer visit(MethodDecl md){
        for (Parameter param: md.getParams()){
            param.accept(this);
        }
        md.getBlock().accept(this);
        return 0;
    }

    @Override
    public Integer visit(Parameter param){
        return 0;
    }

    @Override
    public Integer visit(ReturnStmt stmt){
        if (stmt.getExpression()!=null){
            stmt.getExpression().accept(this);
        }
        return 0;
    }

    @Override
    public Integer visit(UnaryOpExpr expr){
        expr.getExpression().accept(this);
        return 0;
    }

    @Override
    public Integer visit(VarDecl var){
        return 0;
    }

    @Override
    public Integer visit(VarLocation loc){
        return 0;
    }
}