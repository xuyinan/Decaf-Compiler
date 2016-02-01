package decaf.ir.Semantic;

import java.util.ArrayList;
import java.util.List;

import decaf.ir.ASTvisitor;
import decaf.ir.ReferenceGrammer.*;
import decaf.ir.Desc.*;
import decaf.test.Error;


public class TypeEvaluationVisitor implements ASTvisitor<Type>{

    private ArrayList<Error> _errors;
    private GeneralSymbolTable _currentScope;
    private ClassDescriptor _classDesc;

    public TypeEvaluationVisitor(ClassDescriptor cd){
        _currentScope = cd.getFieldSymbolTable();
        _classDesc = cd;
        _errors = new ArrayList<Error>();
    }

    public void setError(ArrayList<Error> errors){
        _errors = errors;
    }

    public ArrayList<Error> getError(){
        return _errors;
    }

    private GeneralDescriptor getDescriptor(String id){
        GeneralSymbolTable scope = _currentScope;
        while (scope!=null){
            if (scope.containsKey(id)){
                return scope.get(id);
            }
            scope = scope.getParent();
        }
        return null;
    }

    @Override
    public Type visit(ArrayLocation loc){
        if (loc.getExpression().accept(this)!=Type.INT){
            String msg = "'" + loc.getExpression() + "' must be of int type (array index)";
            _errors.add(new Error(loc, msg));
        }
        GeneralDescriptor desc = getDescriptor(loc.getId());
        Type myType = Type.UNDEFINED;
        if (desc != null) {
            if (desc.getType() == Type.INTARRAY) myType = Type.INT;
            else if (desc.getType() == Type.BOOLEANARRAY)  myType = Type.BOOLEAN;
            else {
                String msg = "'" + loc.getId() + "' must be of an array";
                _errors.add(new Error(loc, msg));
            }
        }
        loc.setType(myType);
        return myType;
    }

    @Override
    public Type visit(AssignStmt stmt){
        Type lhs = stmt.getLocation().accept(this);
        Type rhs = stmt.getExpression().accept(this);
        if (lhs!=Type.UNDEFINED && rhs!=Type.UNDEFINED){
            if (stmt.getOperator()==AssignOpType.ASSIGN){
                if (lhs != rhs) {
                    String msg = "'" + stmt.getLocation() + "' is of " + lhs
                            + " type, but is being assigned '" + stmt.getExpression()
                            + "' of " + rhs + "' type";
                    _errors.add(new Error(stmt, msg));
                }
            }else{
                if (lhs != Type.INT){
                    String msg = stmt.getLocation() + "' must be of int type";
                    _errors.add(new Error(stmt, msg));
                }
                if (rhs != Type.INT){
                    String msg = stmt.getExpression() + "' must be of int type";
                    _errors.add(new Error(stmt, msg));
                }
            }
        }
        return Type.UNDEFINED;
    }

    @Override
    public Type visit(BinOpExpr expr){
        Type lhs = expr.getLeftOperand().accept(this);
        Type rhs = expr.getRightOperand().accept(this);
        BinOpType op = expr.getOperator();
        Type myType = Type.UNDEFINED;

        // Already produced error somewhere in the child subtree (avoid multiple
        // errors)
        if (lhs != Type.UNDEFINED && rhs != Type.UNDEFINED) {
            switch (op) {
                case AND: // boolean only
                case OR:
                    if (lhs == Type.BOOLEAN && rhs == Type.BOOLEAN) myType = Type.BOOLEAN;
                    if (lhs != Type.BOOLEAN) {
                        String msg = "'" + expr.getLeftOperand() + "' must be of boolean type";
                        _errors.add(new Error(expr.getLeftOperand(), msg));
                    }
                    if (rhs != Type.BOOLEAN) {
                        String msg = "'" + expr.getRightOperand() + "' must be of boolean type";
                        _errors.add(new Error(expr.getRightOperand(), msg));
                    }
                    break;
                case PLUS: // int only
                case MINUS:
                case MULTIPLY:
                case DIVIDE:
                case MOD:
                    if (lhs == Type.INT && rhs == Type.INT) myType = Type.INT;
                    if (lhs != Type.INT) {
                        String msg = "'" + expr.getLeftOperand() + "' must be of int type";
                        _errors.add(new Error(expr.getLeftOperand(), msg));
                    }
                    if (rhs != Type.INT) {
                        String msg = "'" + expr.getRightOperand() + "' must be of int type";
                        _errors.add(new Error(expr.getRightOperand(), msg));
                    }
                    break;
                case LE:
                case LEQ:
                case GE:
                case GEQ:
                    if (lhs == Type.INT && rhs == Type.INT) myType = Type.BOOLEAN;
                    if (lhs != Type.INT) {
                        String msg = "'" + expr.getLeftOperand() + "' must be of int type";
                        _errors.add(new Error(expr.getLeftOperand(), msg));
                    }
                    if (rhs != Type.INT) {
                        String msg = "'" + expr.getRightOperand() + "' must be of int type";
                        _errors.add(new Error(expr.getRightOperand(), msg));
                    }
                    break;
                case NEQ: // int or boolean (same type though)
                case EQ:
                    if (lhs == Type.VOID) {
                        String msg = "'" + expr.getLeftOperand() + "' cant be of void type";
                        _errors.add(new Error(expr.getLeftOperand(), msg));
                    }else if (rhs == Type.VOID) {
                        String msg = "'" + expr.getRightOperand() + "' cant be of void type";
                        _errors.add(new Error(expr.getRightOperand(), msg));
                    }else if (lhs.isArray()) {
                        String msg = "'" + expr.getLeftOperand() + "' cant be an array";
                        _errors.add(new Error(expr.getLeftOperand(), msg));
                    }else if (rhs.isArray()) {
                        String msg = "'" + expr.getRightOperand() + "' cant be an array";
                        _errors.add(new Error(expr.getRightOperand(), msg));
                    }else if (lhs != rhs) {
                        String msg = "'" + expr.getLeftOperand() + "' and '"
                                + expr.getRightOperand() + "' must be of same type";
                        _errors.add(new Error(expr.getLeftOperand(), msg));
                    }else {
                        myType = Type.BOOLEAN;
                    }
                    break;
            }
        }
        expr.setType(myType);
        return myType;
    }

    @Override
    public Type visit(Block block){
        GeneralSymbolTable oldScope = _currentScope;
        _currentScope = _classDesc.getScopeTable().get(block.getBlockId());
        for (VarDecl vd : block.getVarDecls()) {
            vd.accept(this);
        }
        for (Statement s : block.getStatements()) {
            s.accept(this);
        }
        _currentScope = oldScope;
        return Type.UNDEFINED;
    }

    @Override
    public Type visit(BoolLiteral lit){
        return Type.BOOLEAN;
    }

    @Override
    public Type visit(BreakStmt stmt){
        return Type.UNDEFINED;
    }

    @Override
    public Type visit(CalloutArg arg){
        if (arg.getString()==null){
            return arg.getExpression().accept(this);
        }
        return Type.UNDEFINED;
    }

    @Override
    public Type visit(CalloutExpr expr){
        for (CalloutArg arg: expr.getArguments()){
            arg.accept(this);
        }
        expr.setType(Type.INT);
        return Type.INT;
    }

    @Override
    public Type visit(CharLiteral lit){
        return Type.CHAR;
    }

    @Override
    public Type visit(ClassDecl cd){
        for (FieldDecl fieldDecl:cd.getFieldDecls()){
            fieldDecl.accept(this);
        }
        for (MethodDecl methodDecl:cd.getMethodDecls()){
            methodDecl.accept(this);
        }
        return Type.UNDEFINED;
    }

    @Override
    public Type visit(ContinueStmt stmt){
        return Type.UNDEFINED;
    }

    @Override
    public Type visit(Field f){
        return f.getType();
    }

    @Override
    public Type visit(FieldDecl fd){
        for (Field field: fd.getFields()){
            field.accept(this);
        }
        return fd.getType();
    }

    @Override
    public Type visit(ForStmt stmt){
        GeneralDescriptor desc = getDescriptor(stmt.getId());
        Type myType = Type.UNDEFINED;
        if (desc!=null) myType = desc.getType();

        if (myType != Type.INT && myType != Type.UNDEFINED) {
            String msg = "Loop variable '" + stmt.getId()  + "' must be of type int";
            _errors.add(new Error(stmt, msg));
        }

        if (stmt.getInitVal().accept(this) != Type.INT) {
            String msg = "'" + stmt.getInitVal() + "' must be of int type";
            _errors.add(new Error(stmt, msg));
        }
        if (stmt.getFinalVal().accept(this) != Type.INT) {
            String msg = "'" + stmt.getFinalVal() + "' must be of int type";
            _errors.add(new Error(stmt, msg));
        }
        stmt.getBlock().accept(this);
        return Type.UNDEFINED;
    }

    @Override
    public Type visit(IfStmt stmt){
        Type condType = stmt.getCondition().accept(this);
        if (condType!=Type.BOOLEAN && condType!=Type.UNDEFINED){
            String msg = "'" + stmt.getCondition() + "' must be of boolean type";
            _errors.add(new Error(stmt, msg));
        }
        stmt.getIfBlock().accept(this);
        if (stmt.getElseBlock()!=null){
            stmt.getElseBlock().accept(this);
        }
        return Type.UNDEFINED;
    }

    @Override
    public Type visit(IntLiteral lit){
        return Type.INT;
    }

    @Override
    public Type visit(InvokeStmt stmt){
        stmt.getMethodCall().accept(this);
        return Type.UNDEFINED;
    }

    @Override
    public Type visit(MethodCall expr){
        for (Expression e: expr.getArg()){
            e.accept(this);
        }
        if (!_classDesc.getMethodSymbolTable().containsKey(expr.getId())){
            return Type.UNDEFINED;
        }
        expr.setType(_classDesc.getMethodSymbolTable().get(expr.getId()).getReturnType());
        return expr.getType();
    }

    @Override
    public Type visit(MethodDecl md){
        md.getBlock().accept(this);
        for (Parameter param: md.getParams()){
            param.accept(this);
        }
        return md.getType();
    }

    @Override
    public Type visit(Parameter param){
        return param.getType();
    }

    @Override
    public Type visit(ReturnStmt stmt){
        if (stmt.getExpression()==null) return Type.VOID;
        else return stmt.getExpression().accept(this);
    }

    @Override
    public Type visit(UnaryOpExpr expr){
        Type type = expr.getExpression().accept(this);
        Type myType = Type.UNDEFINED;
        if (type != Type.UNDEFINED) {
            if (type.isArray()) {
                String msg = "'" + expr.getExpression() + "' cannot be an array";
                _errors.add(new Error(expr.getExpression(), msg));
            } else if (expr.getOperator() == UnaryOpType.NOT) {
                if (type != Type.BOOLEAN) {
                    String msg = "'" + expr.getExpression() + "' must be of boolean type4";
                    _errors.add(new Error(expr, msg));
                } else {
                    myType = Type.BOOLEAN;
                }
            } else {
                if (type != Type.INT) {
                    String msg = "'" + expr.getExpression() + "' must be of int type";
                    _errors.add(new Error(expr, msg));
                } else {
                    myType = Type.INT;
                }
            }
        }
        expr.setType(myType);
        return myType;
    }

    @Override
    public Type visit(VarDecl var){
        return var.getType();
    }

    @Override
    public Type visit(VarLocation loc){
        GeneralDescriptor desc = getDescriptor(loc.getId());
        Type myType = Type.UNDEFINED;
        if (desc != null) myType = desc.getType();
        loc.setType(myType);
        return myType;
    }
}