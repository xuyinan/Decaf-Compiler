package decaf.ir.ReferenceGrammer;

import decaf.ir.ASTvisitor;

public class IfStmt extends Statement {
    private Expression _cond;
    private Block _ifBlock;
    private Block _elseBlock;

    public IfStmt(Expression cond, Block ifBlock){
        _cond = cond;
        _ifBlock = ifBlock;
        _elseBlock = null;
    }

    public IfStmt(Expression cond, Block ifBlock, Block elseBlock){
        _cond = cond;
        _ifBlock = ifBlock;
        _elseBlock = elseBlock;
    }

    public void setCondition(Expression cond) {
        _cond = cond;
    }

    public void setIfBlock(Block ifBlock) {
        _ifBlock = ifBlock;
    }
    public void setElseBlock(Block elseBlock) {
        _elseBlock = elseBlock;
    }

    public Expression getCondition() {
        return _cond;
    }

    public Block getIfBlock() {
        return _ifBlock;
    }
    
    public Block getElseBlock() {
        return _elseBlock;
    }

    @Override
    public String toString(){
        String rst = "if " + _cond.toString() + '\n' + _ifBlock.toString();
        if (_elseBlock!=null){
            rst += _elseBlock.toString();
        }
        return rst;
    }

    @Override
    public <T> T accept(ASTvisitor<T> v) {
        return v.visit(this);
    }
}