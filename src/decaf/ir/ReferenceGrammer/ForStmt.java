package decaf.ir.ReferenceGrammer;

import decaf.ir.ASTvisitor;

public class ForStmt extends Statement {
    private String _id;
    private Expression _initExpr;
    private Expression _finExpr;
    private Block _block;

    public ForStmt(String id, Expression initExpr, Expression finExpr, Block block){
        _id = id;
        _initExpr = initExpr;
        _finExpr = finExpr;
        _block = block;
    }

    public void setId(String id){
        _id = id;
    }

    public void setInitVal(Expression initExpr){
        _initExpr = initExpr;
    }

    public void setFinalVal(Expression finExpr){
        _finExpr = finExpr;
    }

    public void setBlock(Block block){
        _block = block;
    }

    public String getId(){
        return _id;
    }

    public Expression getInitVal(){
        return _initExpr;
    }

    public Expression getFinalVal(){
        return _finExpr;
    }

    public Block getBlock(){
        return _block;
    }
    
    @Override
    public String toString(){
        return "for " + _id + "=" + _initExpr + "," + _finExpr + '\n' + _block.toString();
    }

    @Override
    public <T> T accept(ASTvisitor<T> v) {
        return v.visit(this);
    }
}