package decaf.ir.ReferenceGrammer;

import decaf.ir.ASTvisitor;

import java.util.ArrayList;
import java.util.List;

public class Block extends Statement {
    private List<Statement> _stmts;
    private List<VarDecl> _varDecls;
    private int _blockId;

    public Block(int blockId) {
        _stmts = new ArrayList<Statement>();
        _varDecls = new ArrayList<VarDecl>();
        _blockId = blockId;
    }

    public Block(List<Statement> stmts, List<VarDecl> varDecls, int blockId) {
        _stmts = stmts;
        _varDecls = varDecls;
        _blockId = blockId;
    }

    public void addStatement(Statement stmt){
        _stmts.add(stmt);
    }

    public void setBlockId(int blockId) {
        _blockId = blockId;
    }

    public void addVarDecl(VarDecl varDecl){
        _varDecls.add(varDecl);
    }

    public List<Statement> getStatements(){
        return _stmts;
    }

    public List<VarDecl> getVarDecls(){
        return _varDecls;
    }

    public int getBlockId() {
        return _blockId;
    }

    @Override
    public String toString(){
        String rst = "";
        
        for (VarDecl v: _varDecls) {
            rst += v.toString() + '\n';
        }
        
        for (Statement s: _stmts) {
            rst += s.toString() + '\n';
        }
        
        if (rst.length() > 0) return rst.substring(0, rst.length() - 1);
        
        return rst; 
    }

    @Override
    public <T> T accept(ASTvisitor<T> v) {
        return v.visit(this);
    }
    
}