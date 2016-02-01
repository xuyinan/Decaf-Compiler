package decaf.ReferenceGrammer;

import java.util.ArrayList;
import java.util.List;

public class Block extends Statement {
    private List<Statement> _stmts;
    private List<VarDecl> _varDecls;


    public Block() {
        _stmts = new ArrayList<Statement>();
        _varDecls = new ArrayList<VarDecl>();
    }

    public Block(List<Statement> stmts, List<VarDecl> varDecls) {
        _stmts = stmts;
        _varDecls = varDecls;
    }

    public void addStatement(Statement stmt){
        _stmts.add(stmt);
    }


    public void addVarDecl(VarDecl varDecl){
        _varDecls.add(varDecl);
    }

    public List<Statement> getStatement(){
        return _stmts;
    }

    public List<VarDecl> getVarDecl(){
        return _varDecls;
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
    
}