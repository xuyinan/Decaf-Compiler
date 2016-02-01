package decaf.ir.Semantic;

import decaf.ir.ReferenceGrammer.*;
import decaf.ir.Desc.*;
import decaf.ir.ASTvisitor;


public class UnaryCollapseVisitor implements ASTvisitor<Boolean>{

    private Expression _exprToReplace;

    public UnaryCollapseVisitor(){
        _exprToReplace = null;
    }

    @Override
    public Boolean visit(ArrayLocation loc){
        if (loc.getExpression().accept(this)){
            loc.setExpression(_exprToReplace);
            _exprToReplace = null;
            loc.accept(this);
        }
        return false;
    }

    @Override
    public Boolean visit(AssignStmt stmt){
        stmt.getLocation().accept(this);
        if (stmt.getExpression().accept(this)){
            stmt.setExpression(_exprToReplace);
            _exprToReplace = null;
            stmt.accept(this);
        }
        return false;
    }

    @Override
    public Boolean visit(BinOpExpr expr){
        if (expr.getLeftOperand().accept(this)){
            expr.setLeftOperand(_exprToReplace);
            _exprToReplace = null;
            expr.accept(this);
        }
        if (expr.getRightOperand().accept(this)){
            expr.setRightOperand(_exprToReplace);
            _exprToReplace = null;
            expr.accept(this);
        }
        return false;
    }

    @Override
    public Boolean visit(Block block){
        for (Statement stmt: block.getStatements()){
            stmt.accept(this);
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
        if (arg.getString()==null){
            if (arg.getExpression().accept(this)){
                arg.setExpression(_exprToReplace);
                _exprToReplace = null;
                arg.accept(this);
            }
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
        if (f.getArraySize()!=null){
            f.getArraySize().accept(this);
        }
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
        if (stmt.getInitVal().accept(this)){
            stmt.setInitVal(_exprToReplace);
            _exprToReplace = null;
            stmt.accept(this);
        }
        if (stmt.getFinalVal().accept(this)){
            stmt.setFinalVal(_exprToReplace);
            _exprToReplace = null;
            stmt.accept(this);
        }
        stmt.getBlock().accept(this);
        return false;
    }

    @Override
    public Boolean visit(IfStmt stmt){
        if (stmt.getCondition().accept(this)){
            stmt.setCondition(_exprToReplace);
            _exprToReplace = null;
            stmt.accept(this);
        }
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
        for (int i = 0; i < expr.getArg().size(); i++) {
            if (expr.getArg().get(i).accept(this)) {
                expr.getArg().set(i, _exprToReplace);
                _exprToReplace = null;
                expr.accept(this);
            }
        }
        return false;
    }

    @Override
    public Boolean visit(MethodDecl md){
        md.getBlock().accept(this);
        return false;
    }

    @Override
    public Boolean visit(Parameter param){
        return false;
    }

    @Override
    public Boolean visit(ReturnStmt stmt){
        if (stmt.getExpression()!=null){
            if (stmt.getExpression().accept(this)){
                stmt.setExpression(_exprToReplace);
                _exprToReplace = null;
                stmt.accept(this);
            }
        }
        return false;
    }

    @Override
    public Boolean visit(UnaryOpExpr expr){
        if (expr.getExpression().getClass().equals(UnaryOpExpr.class)){
            UnaryOpExpr e = (UnaryOpExpr)expr.getExpression();
            _exprToReplace = e.getExpression();
            return true;
        }else if (expr.getExpression().accept(this)){
            expr.setExpression(_exprToReplace);
            _exprToReplace = null;
            expr.accept(this);
        }
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