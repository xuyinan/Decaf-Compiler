package decaf.ReferenceGrammer;


import java.util.ArrayList;
import java.util.List;

public class FieldDecl extends Decl {
    private List<Field> _fields;
    private Type _type;

    public FieldDecl(Type type, List<Field> fields){
        _type = type;
        _fields = new ArrayList<Field>();

    }

    public void setType(Type type){
        _type = type;
    }

    public Type getType(Type type){
        return _type;
    }

    @Override
    public String toString(){
        String rst = _type.toString() + " ";
        for (Field f: _fields) {
            rst += f.toString() + ", ";
        }
        if (_fields.size() > 0) {
            // return last ","
            rst = rst.substring(0, rst.length() - 2);
        }
        
        return rst;
    }
}