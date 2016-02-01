package decaf.ir.Semantic;

import java.util.ArrayList;
import java.util.List;

import decaf.ir.ASTvisitor;
import decaf.ir.ReferenceGrammer.*;
import decaf.ir.Desc.*;
import decaf.test.Error;


public class IntOverflowCheckVisitor implements ASTvisitor<Boolean>{

    private ArrayList<Error> _errors;
    private boolean _inUnaryMinus;

    public IntOverflowCheckVisitor() {
        _errors = new ArrayList<Error>();
        _inUnaryMinus = false;
    }

    public ArrayList<Error> getError(){
        return _errors;
    }

    private Boolean isHex(String intStr){
        if (intStr.length() < 2)
            return false;
        return intStr.startsWith("0x");
    }
     
    private Expression getNegativeIntLiteral(Expression expr) {
        UnaryOpExpr expr_unary = (UnaryOpExpr) expr;
        return expr_unary.getExpression();
    }

    @Override
    public Boolean visit(ArrayLocation loc){
        if (loc.getExpression().accept(this)){
            loc.setExpression(getNegativeIntLiteral(loc.getExpression()));
        }
        return false;
    }

    @Override
    public Boolean visit(AssignStmt stmt){
        stmt.getLocation().accept(this);
        if (stmt.getExpression().accept(this)){
            stmt.setExpression(getNegativeIntLiteral(stmt.getExpression()));
        }
        return false;
    }

    @Override
    public Boolean visit(BinOpExpr expr){
        if (expr.getLeftOperand().accept(this)){
            expr.setLeftOperand(getNegativeIntLiteral(expr.getLeftOperand()));
        }
        if (expr.getRightOperand().accept(this)){
            expr.setRightOperand(getNegativeIntLiteral(expr.getRightOperand()));
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
                arg.setExpression(getNegativeIntLiteral(arg.getExpression()));
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
            stmt.setInitVal(getNegativeIntLiteral(stmt.getInitVal()));
        }
        if (stmt.getFinalVal().accept(this)){
            stmt.setFinalVal(getNegativeIntLiteral(stmt.getFinalVal()));
        }
        stmt.getBlock().accept(this);
        return false;
    }

    @Override
    public Boolean visit(IfStmt stmt){
        if (stmt.getCondition().accept(this)){
            stmt.setCondition(getNegativeIntLiteral(stmt.getCondition()));
        }
        stmt.getIfBlock().accept(this);
        if (stmt.getElseBlock()!=null){
            stmt.getElseBlock().accept(this);
        }
        return false;
    }

    @Override
    public Boolean visit(IntLiteral lit){
        String rawValue = lit.getRawValue();
        
        if (lit.getValue() == null) { // Checking int literal for first time
            int value = -1;
            boolean isHex = false;
            
            if (isHex(rawValue)) { // Check for hex string
                rawValue = rawValue.substring(2); // Remove '0x'
                isHex = true;
            }
            if (_inUnaryMinus) {
                rawValue = "-" + rawValue;
                lit.setRawValue("-" + lit.getRawValue());
            }
            
            try {
                if (isHex) {
                    value = Integer.parseInt(rawValue, 16);
                } else {
                    value = Integer.parseInt(rawValue);
                }
            } catch (Exception e) {
                String msg = "Int literal " + lit.getRawValue() + " is out of range";
                _errors.add(new Error(lit, msg));
            }
            lit.setValue(value);
            lit.setRawValue(Integer.toString(value));
        }
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
                expr.getArg().set(i, getNegativeIntLiteral(expr.getArg().get(i)));
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
                stmt.setExpression(getNegativeIntLiteral(stmt.getExpression()));
            }
        }
        return false;
    }

    @Override
    public Boolean visit(UnaryOpExpr expr){
        if (expr.getOperator() == UnaryOpType.MINUS && expr.getExpression().getClass() == IntLiteral.class) {
            _inUnaryMinus = true;
            expr.getExpression().accept(this);
            _inUnaryMinus = false;
            return true;
        } else if (expr.getExpression().accept(this)) {
            expr.setExpression(getNegativeIntLiteral(expr.getExpression()));
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