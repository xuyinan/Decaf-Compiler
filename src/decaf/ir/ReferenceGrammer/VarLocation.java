package decaf.ReferenceGrammer;


public class VarLocation extends Location {
    private int _blockId;

    public VarLocation(String id){
        _id = id;
        _blockId = -1;
    }

    public void setBlockId(int blockId){
        _blockId = blockId;
    }

    public int getBlockId(){
        return _blockId;
    }

    @Override
    public String toString(){
        return _id;
    }


}