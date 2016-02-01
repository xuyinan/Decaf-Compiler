package decaf.ir.Semantic;

import java.util.ArrayList;
import java.util.List;

import decaf.ir.ASTvisitor;
import decaf.ir.ReferenceGrammer.*;
import decaf.ir.Desc.*;
import decaf.test.Error;


public class BooleanCollapseVisitor implements ASTvisitor<Boolean>{
    private Expression _exprToReplace;

    public BooleanCollapseVisitor(){
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
        if (expr.getOperator() == BinOpType.AND){
            if (expr.getLeftOperand().getClass().equals(BoolLiteral.class) || 
                    expr.getRightOperand().getClass().equals(BoolLiteral.class)){
                 _exprToReplace = optimizeAnd(expr);
                return true;
            }
        }

        if (expr.getOperator() == BinOpType.OR){
            if (expr.getLeftOperand().getClass().equals(BoolLiteral.class) || 
                    expr.getRightOperand().getClass().equals(BoolLiteral.class)){
                _exprToReplace = optimizeOr(expr);
                return true;
            }
        }

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
        if (!arg.isString()){
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
        if (expr.getOperator()==UnaryOpType.NOT){
            if (expr.getExpression().getClass().equals(BoolLiteral.class)){
                BoolLiteral lit = (BoolLiteral) expr.getExpression();
                if (lit.getValue()==1){
                    _exprToReplace = new BoolLiteral("false");
                }else{
                    _exprToReplace = new BoolLiteral("true");
                }
                return true;
            }
        }
        if (expr.getExpression().accept(this)){
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

    private Expression optimizeOr(BinOpExpr expr){
        BoolLiteral arg1=null, arg2=null;
        Boolean a=false, b=false;

        if (expr.getLeftOperand().getClass().equals(BoolLiteral.class))
            arg1 = (BoolLiteral) expr.getLeftOperand();

        if (expr.getRightOperand().getClass().equals(BoolLiteral.class))
            arg2 = (BoolLiteral) expr.getRightOperand();

        if (arg1!=null && arg2!=null){
            a = arg1.getValue()==1? true: false;
            b = arg2.getValue()==1? true: false;

            if (a||b) return new BoolLiteral("true");
            else return new BoolLiteral("false");
        }else if(arg1!=null){
            a = arg1.getValue()==1? true: false;
            if (a) return new BoolLiteral("true");
            else return expr.getRightOperand();
        }else{
            b = arg2.getValue()==1? true: false;
            if (b) return new BoolLiteral("true");
            else return expr.getLeftOperand();
        }
    }

    private Expression optimizeAnd(BinOpExpr expr){
        BoolLiteral arg1=null, arg2=null;
        Boolean a=false, b=false;

        if (expr.getLeftOperand().getClass().equals(BoolLiteral.class))
            arg1 = (BoolLiteral) expr.getLeftOperand();

        if (expr.getRightOperand().getClass().equals(BoolLiteral.class))
            arg2 = (BoolLiteral) expr.getRightOperand();

        if (arg1!=null && arg2!=null){
            a = arg1.getValue()==1? true: false;
            b = arg2.getValue()==1? true: false;

            if (a&&b) return new BoolLiteral("true");
            else return new BoolLiteral("false");
        }else if(arg1!=null){
            a = arg1.getValue()==1? true: false;
            if (!a) return new BoolLiteral("false");
            else return expr.getRightOperand();
        }else{
            b = arg2.getValue()==1? true: false;
            if (!b) return new BoolLiteral("false");
            else return expr.getLeftOperand();
        }
    }

}