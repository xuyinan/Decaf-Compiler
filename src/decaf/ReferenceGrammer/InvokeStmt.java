package decaf.ReferenceGrammer;


public class InvokeStmt extends Statement {
	private CallExpr _methodCall;
	
	public InvokeStmt(CallExpr e) {
		_methodCall = e;
	}

	public CallExpr getMethodCall() {
		return _methodCall;
	}

	public void setMethodCall(CallExpr methodCall) {
		_methodCall = methodCall;
	}
	
	@Override
	public String toString() {
		return _methodCall.toString();
	}

}
