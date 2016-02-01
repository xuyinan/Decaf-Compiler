package decaf.ir.ReferenceGrammer;

import decaf.ir.ASTvisitor;

public class InvokeStmt extends Statement {
	private CallExpr _methodCall;
	
	public InvokeStmt(CallExpr e) {
		_methodCall = e;
	}

	public void setMethodCall(CallExpr methodCall) {
		_methodCall = methodCall;
	}

	public CallExpr getMethodCall() {
		return _methodCall;
	}
	
	@Override
	public String toString() {
		return _methodCall.toString();
	}

	@Override
	public <T> T accept(ASTvisitor<T> v) {
		return v.visit(this);
	}
}
