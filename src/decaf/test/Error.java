package decaf.test;

import decaf.ir.ReferenceGrammer.*;

public class Error {
	public static String fileName = null;
	private int _lineNumber;
	private int _columnNumber;
	private String _description;

	public Error(){
		_lineNumber = -1;
		_columnNumber = -1;
		_description = "Unspecified Error";
	}

	public Error(int lineNumber, int columnNumber, String description){
		_lineNumber = lineNumber;
		_columnNumber = columnNumber;
		_description = description;
	}

	public Error(AST ast, String description) {
		_lineNumber = ast.getLineNumber();
		_columnNumber = ast.getColumnNumber();
		_description = description;
	}

	public void setLineNumber(int lineNumber){
		_lineNumber = lineNumber;
	}

	public void setColumnNumber(int columnNumber){
		_columnNumber = columnNumber;
	}

	public void setDescription(String description){
		_description = description;
	}

	public int getLineNumber(){
		return _lineNumber;
	}

	public int getColumnNumber(){
		return _columnNumber;
	}

	public String getDescription(){
		return _description;
	}

	@Override
	public String toString(){
		return fileName + ": (" + _lineNumber + ", " + _columnNumber + ")" + " " + _description;
	}
}
