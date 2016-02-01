package decaf.ir.Semantic;

import java.util.ArrayList;
import java.util.List;

import decaf.ir.ASTvisitor;
import decaf.ir.ReferenceGrammer.*;
import decaf.ir.Desc.*;
import decaf.test.Error;


public class SymbolTableGenerationVisitor implements ASTvisitor<Integer>{
    private ClassDescriptor _classDesc;
    private GeneralSymbolTable _currentScope;
    private List<Error> _errors;
    private MethodDescriptor _inMethod;
    private String _loopId;
    private int _inLocalScope;

    public SymbolTableGenerationVisitor(){
        _classDesc = new ClassDescriptor();
        _currentScope = null;
        _errors = new ArrayList<Error>();
        _inMethod = null;
        _loopId = null;
        _inLocalScope = 0;
    }

    public List<Error> getError(){
        return _errors;
    }

    public ClassDescriptor getClassDescriptor() {
        return _classDesc;
    }

    @Override
    public Integer visit(ArrayLocation loc){
        FieldDescriptor fd = (FieldDescriptor) _classDesc.getFieldSymbolTable().get(loc.getId());
        if (fd==null){
            String msg = "'" + loc.getId() + "'" + " is not declared";
            _errors.add(new Error(loc, msg));
        }else if (!fd.getType().isArray()){
            String msg = "'" + loc.getId() + "'" + " is not an array";
            _errors.add(new Error(loc, msg));
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
        _inLocalScope++;
        GeneralSymbolTable newScope = new GeneralSymbolTable(_currentScope);
        newScope.setScopeId(block.getBlockId());
        _currentScope = newScope;

        _classDesc.getScopeTable().put(block.getBlockId(), _currentScope);
        
        // Hack to set local symbol table of method descriptor
        if (_inMethod != null) {
            _inMethod.setLocalSymbolTable(_currentScope);
            _inMethod = null;
        }
        
        // Hack to add loop variable into block scope
        if (_loopId != null) {
            _currentScope.put(_loopId, new VarDescriptor(Type.INT, _loopId));
            _loopId = null;
        }
        
        for (VarDecl var : block.getVarDecls()) {
            var.accept(this);
        }

        for (Statement stmt : block.getStatements()) {
            stmt.accept(this);
        }
        _currentScope = _currentScope.getParent();
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
        if (!arg.isString()){
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
        _currentScope = _classDesc.getFieldSymbolTable();
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
        if (_currentScope.containsKey(f.getId())){
            _errors.add(new Error(f, "'" + f.getId() + "'" + " is already declared"));
        }else{
            int len = -1;
            if (f.getType().isArray()) {
                len = f.getArraySize().getValue();
            }
            _currentScope.put(f.getId(), new FieldDescriptor(f.getType(), f.getId(), len));
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
        _loopId = stmt.getId();
        stmt.getInitVal().accept(this);
        stmt.getFinalVal().accept(this);
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
        MethodSymbolTable methodTable = _classDesc.getMethodSymbolTable();
        if (!methodTable.containsKey(expr.getId())) {
            _errors.add(new Error(expr, "'" + expr.getId() + "'" + " method is not defined"));
        }
        for (Expression arg: expr.getArg()){
            arg.accept(this);
        }
        return 0;
    }

    @Override
    public Integer visit(MethodDecl md){
        MethodSymbolTable methodTable = _classDesc.getMethodSymbolTable();
        if (_currentScope.containsKey(md.getId())){
            _errors.add(new Error(md, "'"+md.getId()+"'"+" is a field"));
        }else if(methodTable.containsKey(md.getId())){
            _errors.add(new Error(md, "'"+md.getId()+"'"+" is already declared"));
        }else{
            MethodDescriptor mDesc = new MethodDescriptor(md.getId(), md.getType(), md.getParams());
            methodTable.put(md.getId(), mDesc);

            GeneralSymbolTable gTab = new GeneralSymbolTable(_currentScope);
            gTab.setScopeId(-2);
            mDesc.setParamSymbolTable(gTab);
            _currentScope = gTab;
            for (Parameter param: md.getParams()){
                param.accept(this);
            }
            _inMethod = mDesc;
            _inLocalScope = 0;
            md.getBlock().accept(this);
            _currentScope = _currentScope.getParent();
        }
        return 0;
    }

    @Override
    public Integer visit(Parameter param){
        if (_currentScope.containsKey(param.getId())){
            String msg = "'"+param.getId()+"'"+" is used multiple times as method parameter";
            _errors.add(new Error(param, msg));
        }else{
            _currentScope.put(param.getId(), new ParamDescriptor(param.getType(), param.getId()));
        }
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
    public Integer visit(VarDecl vd){
        for (String var: vd.getVar()){
            if (_inLocalScope==1 && _currentScope.getParent().containsKey(var)){
                String msg = "'" + var + "'" + " is already declared in method params";
                _errors.add(new Error(vd, msg));
                return 0;
            }
            if (_currentScope.containsKey(var)){
                String msg = "'" + var + "'" + " is already declared";
                _errors.add(new Error(vd, msg));
            }else{
                _currentScope.put(var, new VarDescriptor(vd.getType(), var));
            }
        }
        return 0;
    }

    private boolean isIdDeclared(VarLocation loc) {
        GeneralSymbolTable scope = _currentScope;
        while (scope != null) {
            if (scope.containsKey(loc.getId())) {
                loc.setBlockId(scope.getScopeId());
                return true;
            }
            scope = scope.getParent();
        }
        return false;
    }

    @Override
    public Integer visit(VarLocation loc){
        if (!isIdDeclared(loc)){
            String msg = "'" + loc.getId() + "'" + " is not declared";
            _errors.add(new Error(loc, msg));
        }
        return 0;
    }
}