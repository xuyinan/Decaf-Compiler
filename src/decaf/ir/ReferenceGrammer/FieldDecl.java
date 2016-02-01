package decaf.ir.ReferenceGrammer;

import decaf.ir.ASTvisitor;

import java.util.ArrayList;
import java.util.List;

public class FieldDecl extends Decl {
    private List<Field> _fields;
    private Type _type;

    public FieldDecl(){
        _fields = new ArrayList<Field>();
    }

    public FieldDecl(Type type, Field field){
        _type = type;
        _fields = new ArrayList<Field>();

    }

    public FieldDecl(Type type, List<Field> fields){
        _type = type;
        if (fields!=null){
            _fields = fields;
            for (Field field: fields) {
                if (field.getArraySize() != null) {
                    // Array
                    if (type.equals(Type.INT)) {
                        field.setType(Type.INTARRAY);
                    } else if (type.equals(Type.BOOLEAN)) {
                        field.setType(Type.BOOLEANARRAY);
                    }
                }else{
                    field.setType(type);
                }
            }
        }else{
            _fields = new ArrayList<Field>();
        }
    }

    public void setType(Type type){
        _type = type;
    }

    public void addField(Field field){
        _fields.add(field);
        if (field.getArraySize() != null) {
            // Array
            if (_type.equals(Type.INT)) {
                field.setType(Type.INTARRAY);
            } else if (_type.equals(Type.BOOLEAN)) {
                field.setType(Type.BOOLEANARRAY);
            }
        } else {
            field.setType(_type);
        }
    }

    public void setFields(List<Field> fields){
        _fields = fields;
        for (Field field: fields) {
            if (field.getArraySize() != null) {
                // Array
                if (_type.equals(Type.INT)) {
                    field.setType(Type.INTARRAY);
                } else if (_type.equals(Type.BOOLEAN)) {
                    field.setType(Type.BOOLEANARRAY);
                }
            } else {
                field.setType(_type);
            }
        }
    }

    public Type getType(){
        return _type;
    }

    public List<Field> getFields(){
        return _fields;
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

    @Override
    public <T> T accept(ASTvisitor<T> v) {
        return v.visit(this);
    }
}