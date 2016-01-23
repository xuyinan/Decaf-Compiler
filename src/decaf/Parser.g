header {
    package decaf;
    import decaf.ReferenceGrammer.*;
    import java.util.List;
    import java.util.ArrayList;
}

options {
  mangleLiteralPrefix = "TK_";
  language="Java";
}

{
    class StringToken{
        String str;
        int linNumber;
        int ColNumber;

        public StringToken(String string){
            str = string;
        }

        public void setString(String string){
            str = string;
        }

        public void setLineNumber(int linNum){
            linNumber = linNum;
        }

        public void setColumnNumber(int colNum){
            ColNumber = colNum;
        }

        public String getString(){
            return str;
        }

        public int getLineNumber(){
            return linNumber;
        }

        public int getColumnNumber(){
            return ColNumber;
        }

        // class BlockId{
        //     public static int blockId = 0;
        // }

    }
}

class DecafParser extends Parser;
options {
  importVocab=DecafScanner;
  k=3;
  buildAST=true;
  defaultErrorHandler=false;
}

// ident
id returns [StringToken s] { 
    s = null; 
} : 
(myId:ID { 
    s = new StringToken(myId.getText()); 
    s.setLineNumber(myId.getLine());
    s.setColumnNumber(myId.getColumn());
}) ;

// intLit
intLit returns [StringToken s] { 
    s = null; 
} : 
(myInt:INTLIT { 
    s = new StringToken(myInt.getText()); 
    s.setLineNumber(myInt.getLine());
    s.setColumnNumber(myInt.getColumn());
}) ;

// char
charLit returns [StringToken s] { 
    s = null; 
} : 
(myChar:CHAR { 
    s = new StringToken(myChar.getText()); 
    s.setLineNumber(myChar.getLine());
    s.setColumnNumber(myChar.getColumn());
}) ;

// String
strLit returns [StringToken s] { 
    s = null; 
} : 
(myString:STRING { 
    s = new StringToken(myString.getText()); 
    s.setLineNumber(myString.getLine());
    s.setColumnNumber(myString.getColumn());
}) ;

// Type
type returns [Type t] {
    t = null;   
} : 
(TK_int { t = Type.INT; }) | 
(TK_boolean { t = Type.BOOLEAN; }) ;

// program
program returns [ClassDecl classDecl]{
    List<FieldDecl> fields = new ArrayList<FieldDecl>();
    List<MethodDecl> methods = new ArrayList<MethodDecl>();
    classDecl = new ClassDecl(fields, methods);
    FieldDecl f;
    MethodDecl m;
    StringToken className;
}:
(clas:TK_class className=id LCURLY (f=field_decl { fields.add(f); })* (m=method_decl { methods.add(m); })* RCURLY EOF)
{
    classDecl.setLineNumber(clas.getLine());
    classDecl.setColumnNumber(clas.getColumn());
    if (!className.getString().equals("Program")){
        classDecl = null;
    }
};

// variable declaration
var_decl returns [VarDecl varDecl]{
    Type t;
    List<String> ids = new ArrayList<String>();
    StringToken id1, id2;
    varDecl = null;
}:
(t = type {varDecl=new VarDecl(t, ids);} ((id1=id {ids.add(id1.getString());} COMMA)* 
    id2=id {
        ids.add(id2.getString());
        varDecl.setLineNumber(id2.getLineNumber());
        varDecl.setColumnNumber(id2.getColumnNumber());
    }) SEMI
);

// Field declaration group
field_decl_group returns [Field f]{
    StringToken fieldId;
    StringToken intString;
    IntLiteral int_lit;
    f = null;
}:
(fieldId=id {
    f = new Field(fieldId.getString());
    f.setLineNumber(fieldId.getLineNumber());
    f.setColumnNumber(fieldId.getColumnNumber());
}) |
(fieldId=id LSQUARE intString=intLit RSQUARE {
    int_lit = new IntLiteral(intString.getString());
    int_lit.setLineNumber(intString.getLineNumber());
    int_lit.setColumnNumber(intString.getColumnNumber());
    f = new Field(fieldId.getString());
    f.setLineNumber(fieldId.getLineNumber());
    f.setColumnNumber(fieldId.getColumnNumber());
});

// Field list declaration
field_decl_list returns [List<Field> fields]{
    fields = new ArrayList<Field>();
    Field f;
}:
(f=field_decl_group {fields.add(f);} (COMMA f=field_decl_group { fields.add(f); })*);

// Field declaration
field_decl returns [FieldDecl fieldDecl]{
    Type t;
    List<Field> fields;
    fieldDecl = null;
}:
(t=type fields=field_decl_list {fieldDecl=new FieldDecl(t, fields);} s:SEMI)
{
    fieldDecl.setLineNumber(s.getLine());
    fieldDecl.setColumnNumber(s.getColumn());
};

// Parameter declaration 
parameter_decl returns [Parameter param]{
    Type t;
    StringToken ident;
    param = null;
}:
(t=type ident=id {
    param = new Parameter(t, ident.getString());
    param.setLineNumber(ident.getLineNumber());
    param.setColumnNumber(ident.getColumnNumber());
});

// Parameter list declaration
parameter_decl_list returns [List<Parameter> params]{
    params = new ArrayList<Parameter>();
    Parameter param;
}:
(param=parameter_decl {params.add(param);} (COMMA param=parameter_decl { params.add(param); })*)?;

// Method declaration
method_decl returns [MethodDecl methodDecl]{
    Type t;
    StringToken ident;
    List<Parameter> params;
    Block blk;
    methodDecl = new MethodDecl();
}:
((t=type {methodDecl.setType(t);}) | (TK_void {methodDecl.setType(Type.VOID);})) (ident=id lp: LPAREN params=parameter_decl_list RPAREN blk=block
{
    methodDecl.setId(ident.getString());
    methodDecl.setParams(params);
    methodDecl.setBlock(blk);
    methodDecl.setLineNumber(lp.getLine());
    methodDecl.setColumnNumber(lp.getColumn());
});

// boolean
boolLit returns [BoolLiteral bl]{
    bl = null;
}:
(bl1:TK_true{
    bl = new BoolLiteral(bl1.getText());
    bl.setLineNumber(bl1.getLine());
    bl.setColumnNumber(bl1.getColumn());
}) |
(bl2:TK_false{
    bl = new BoolLiteral(bl2.getText());
    bl.setLineNumber(bl2.getLine());
    bl.setColumnNumber(bl2.getColumn());
});

// literal
literal returns [Literal lit]{
    StringToken intToken, charToken;
    lit = null;
}:
(intToken=intLit {
    lit = new IntLiteral(intToken.getString());
    lit.setLineNumber(intToken.getLineNumber());
    lit.setColumnNumber(intToken.getColumnNumber());
})|
(charToken=charLit {
    lit = new CharLiteral(charToken.getString());
    lit.setLineNumber(charToken.getLineNumber());
    lit.setColumnNumber(charToken.getColumnNumber());
})|
(lit = boolLit);

// arith operator
add_op returns [BinOpType bt]{
    bt = null;
}:
(PLUS {bt=BinOpType.PLUS;} | MINUS {bt=BinOpType.MINUS;});

mul_op returns [BinOpType bt]{
    bt=null;
}:
(m:MULDIV { 
    if (m.getText().equals("*")) bt = BinOpType.MULTIPLY;
    else bt=BinOpType.DIVIDE;
} | MOD { bt = BinOpType.MOD; });

// Relation operators
rel_op returns [BinOpType bt] {
    bt = null;  
} : 
(LESS { bt = BinOpType.LE; } | 
 MORE { bt = BinOpType.GE; } | 
 LEQ { bt = BinOpType.LEQ; } | 
 GEQ { bt = BinOpType.GEQ; });

// equality operator
eq_op returns [BinOpType bt] {
    bt = null;  
} : 
(CEQ { bt = BinOpType.EQ; } | 
 NEQ { bt = BinOpType.NEQ; });

// Assignment operator
assign_op returns [AssignOpType bt] {
    bt = null;  
} : 
(ASSIGNEQ { bt = AssignOpType.ASSIGN; } | 
 ASSIGNPLUSEQ { bt = AssignOpType.INCREMENT; }|
 ASSIGNMINUSEQ { bt = AssignOpType.DECREMENT; });

// Location
location returns [Location loc]{
    StringToken ident;
    Expression expr;
    loc = null;
} :
( ident=id {
    loc = new VarLocation(ident.getString());
    loc.setLineNumber(ident.getLineNumber());
    loc.setColumnNumber(ident.getColumnNumber());
} | 
ident=id LSQUARE expr = expression RSQUARE{
    loc = new ArrayLocation(ident.getString(), expr);
    loc.setLineNumber(ident.getLineNumber());
    loc.setColumnNumber(ident.getColumnNumber());
});

// Expression list
expr_list returns [List<Expression> exprs]{
    exprs = new ArrayList<Expression>();
    Expression expr;
}:
(expr=expression {exprs.add(expr);} (COMMA expr=expression {exprs.add(expr);})*)?;

// Callout argument
callout_arg returns [CalloutArg arg]{
    Expression expr;
    StringToken str;
    arg = null;
}:
(expr=expression {
    arg = new CalloutArg(expr);
    arg.setLineNumber(expr.getLineNumber());
    arg.setColumnNumber(expr.getColumnNumber());
} |
str=strLit {
    arg = new CalloutArg(str.getString());
    arg.setLineNumber(str.getLineNumber());
    arg.setColumnNumber(str.getColumnNumber());
});

// Callout argument list
callout_arg_list returns [List<CalloutArg> args]{
    args = new ArrayList<CalloutArg>();
    CalloutArg arg;
}:
(COMMA arg=callout_arg {args.add(arg);} (COMMA arg=callout_arg {args.add(arg);})*)?;

// Method call
method_call returns [CallExpr callExpr]{
    StringToken ident1;
    StringToken ident2;
    List<CalloutArg> args;
    List<Expression> exprs;
    callExpr = null;
}:
(ident1=id lp:LPAREN exprs=expr_list RPAREN {
    callExpr = new MethodCall(ident1.getString(), exprs);
    callExpr.setLineNumber(ident1.getLineNumber());
    callExpr.setColumnNumber(ident1.getColumnNumber());
}) |
(calout:TK_callout LPAREN ident2=strLit args=callout_arg_list RPAREN {
    callExpr = new CalloutExpr(ident2.getString(), args);
    callExpr.setLineNumber(calout.getLine());
    callExpr.setColumnNumber(calout.getColumn());
});

// Statement
statement returns [Statement stmt]{
    Location loc;
    AssignOpType assignOp;
    Expression assignExpr, condExpr, initExpr, finExpr, rtnExpr;
    CallExpr methods;
    Block ifBlock, elseBlock, forBlock;
    StringToken ident;
    stmt = null;

}:
( loc=location assignOp=assign_op assignExpr=expression semi1:SEMI {
    stmt = new AssignStmt(loc, assignOp, assignExpr);
    stmt.setLineNumber(semi1.getLine());
    stmt.setColumnNumber(semi1.getColumn());
}) |
( methods=method_call semi2:SEMI {
    stmt = new InvokeStmt((CallExpr)methods); 
    stmt.setLineNumber(semi2.getLine());
    stmt.setColumnNumber(semi2.getColumn());
}) |
( ift: TK_if LPAREN condExpr=expression RPAREN ifBlock=block
{
    stmt = new IfStmt(condExpr, ifBlock);
    stmt.setLineNumber(ift.getLine());
    stmt.setColumnNumber(ift.getColumn());
} (TK_else elseBlock=block { ((IfStmt)stmt).setElseBlock(elseBlock);})?
) |
( fort: TK_for LPAREN ident=id ASSIGNEQ initExpr=expression COMMA finExpr=expression RPAREN forBlock=block
{
    stmt = new ForStmt(ident.getString(), initExpr, finExpr, forBlock);
    stmt.setLineNumber(fort.getLine());
    stmt.setColumnNumber(fort.getColumn());
}) |
(( rett: TK_return
{
    stmt = new ReturnStmt();
    stmt.setLineNumber(rett.getLine());
    stmt.setColumnNumber(rett.getColumn());
} (rtnExpr=expression {((ReturnStmt)stmt).setExpression(rtnExpr);})?) SEMI) |
( breakt: TK_break
{
    stmt = new BreakStmt();
    stmt.setLineNumber(breakt.getLine());
    stmt.setColumnNumber(breakt.getColumn());
}SEMI) |
( contdt: TK_continue
{
    stmt = new ContinueStmt();
    stmt.setLineNumber(contdt.getLine());
    stmt.setColumnNumber(contdt.getColumn());
}SEMI) |
stmt = block;

// Block
block returns [Block b]{
    List<Statement> stmts = new ArrayList<Statement>();
    List<VarDecl> varDecls = new ArrayList<VarDecl>();
    Statement stmt;
    VarDecl var;
    b = null;
}:
(lc: LCURLY (var=var_decl {varDecls.add(var);})* (stmt=statement {stmts.add(stmt);})* RCURLY
{
    b = new Block(stmts, varDecls);
    b.setLineNumber(lc.getLine());
    b.setColumnNumber(lc.getColumn());
});

// Expressions
unary_minus_term returns [Expression expr]{
    expr = null;
}:
( mt: MINUS expr=unary_minus_term{
    expr = new UnaryOpExpr(expr, UnaryOpType.MINUS);
    expr.setLineNumber(mt.getLine());
    expr.setColumnNumber(mt.getColumn());
}) | expr = expr_static;

unary_not_term returns [Expression expr]{
    expr = null;
}:
( nt: NOT expr=unary_not_term{
    expr = new UnaryOpExpr(expr, UnaryOpType.NOT);
    expr.setLineNumber(nt.getLine());
    expr.setColumnNumber(nt.getColumn());
}) | expr = unary_minus_term;

// multiplication
mul_term_temp returns [TempExpression tempExpr]{
    Expression expr;
    BinOpType operator;
    TempExpression tempExprChild;
    tempExpr = null;
}:
( operator=mul_op expr=unary_not_term tempExprChild=mul_term_temp 
{
    tempExpr = new TempExpression(expr, operator);
    if (tempExprChild!=null) tempExpr.setTempExpr(tempExprChild);
    tempExpr.setLineNumber(expr.getLineNumber());
    tempExpr.setColumnNumber(expr.getColumnNumber());
}) |
({tempExpr=null;});

mul_term returns [Expression expr]{
    Expression exprLeaf, temp;
    TempExpression tempExpr;
    expr = null;
}:
( exprLeaf=unary_not_term tempExpr=mul_term_temp 
{
    if (tempExpr==null) expr = exprLeaf;
    else{
        temp = exprLeaf;
        while (tempExpr.isLeaf()){
            temp = new BinOpExpr(temp, tempExpr);
            tempExpr = tempExpr.getTempExpr();
        }
        expr = new BinOpExpr(temp, tempExpr);
    }
    expr.setLineNumber(exprLeaf.getLineNumber());
    expr.setColumnNumber(exprLeaf.getColumnNumber());
});

// Addition
add_termtemp returns [TempExpression tempExpr]{
    Expression expr;
    BinOpType operator;
    TempExpression tempExprChild;
    tempExpr = null;
}:
( operator=add_op expr=mul_term tempExprChild=add_termtemp 
{
    tempExpr = new TempExpression(expr, operator);
    if (tempExprChild!=null) tempExpr.setTempExpr(tempExprChild);
    tempExpr.setLineNumber(expr.getLineNumber());
    tempExpr.setColumnNumber(expr.getColumnNumber());
}) |
({tempExpr=null;});

add_term returns [Expression expr]{
    Expression exprLeaf, temp;
    TempExpression tempExpr;
    expr = null;
}:
( exprLeaf=mul_term tempExpr=add_termtemp 
{
    if (tempExpr==null) expr = exprLeaf;
    else{
        temp = exprLeaf;
        while (tempExpr.isLeaf()){
            temp = new BinOpExpr(temp, tempExpr);
            tempExpr = tempExpr.getTempExpr();
        }
        expr = new BinOpExpr(temp, tempExpr);
    }
    expr.setLineNumber(exprLeaf.getLineNumber());
    expr.setColumnNumber(exprLeaf.getColumnNumber());
});

// Relations
rel_termtemp returns [TempExpression tempExpr]{
    Expression expr;
    BinOpType operator;
    TempExpression tempExprChild;
    tempExpr = null;
}:
( operator=rel_op expr=add_term tempExprChild=rel_termtemp 
{
    if (tempExprChild==null) tempExpr = new TempExpression(expr, operator);
    else tempExpr = new TempExpression(new BinOpExpr(expr, tempExprChild), operator);
    tempExpr.setLineNumber(expr.getLineNumber());
    tempExpr.setColumnNumber(expr.getColumnNumber());
}) |
({tempExpr=null;});

rel_term returns [Expression expr]{
    Expression exprLeaf;
    TempExpression tempExpr;
    expr = null;
}:
( exprLeaf=add_term tempExpr=rel_termtemp 
{
    if (tempExpr==null) expr = exprLeaf;
    else expr = new BinOpExpr(exprLeaf, tempExpr);
    expr.setLineNumber(exprLeaf.getLineNumber());
    expr.setColumnNumber(exprLeaf.getColumnNumber());
});

// Equality
eq_termtemp returns [TempExpression tempExpr]{
    Expression expr;
    BinOpType operator;
    TempExpression tempExprChild;
    tempExpr = null;
}:
( operator=eq_op expr=rel_term tempExprChild=eq_termtemp 
{
    if (tempExprChild==null) tempExpr = new TempExpression(expr, operator);
    else tempExpr = new TempExpression(new BinOpExpr(expr, tempExprChild), operator);
    tempExpr.setLineNumber(expr.getLineNumber());
    tempExpr.setColumnNumber(expr.getColumnNumber());
}) |
({tempExpr=null;});

eq_term returns [Expression expr]{
    Expression exprLeaf;
    TempExpression tempExpr;
    expr = null;
}:
( exprLeaf=rel_term tempExpr=eq_termtemp 
{
    if (tempExpr==null) expr = exprLeaf;
    else expr = new BinOpExpr(exprLeaf, tempExpr);
    expr.setLineNumber(exprLeaf.getLineNumber());
    expr.setColumnNumber(exprLeaf.getColumnNumber());
});

// Condition AND
cond_and_termtemp returns [TempExpression tempExpr]{
    Expression expr;
    TempExpression tempExprChild;
    tempExpr = null;
}:
( at:AND expr=eq_term tempExprChild=cond_and_termtemp 
{
    if (tempExprChild==null) tempExpr = new TempExpression(expr, BinOpType.AND);
    else tempExpr = new TempExpression(new BinOpExpr(expr, tempExprChild), BinOpType.AND);
    tempExpr.setLineNumber(expr.getLineNumber());
    tempExpr.setColumnNumber(expr.getColumnNumber());
}) |
({tempExpr=null;});

cond_and_term returns [Expression expr]{
    Expression exprLeaf;
    TempExpression tempExpr;
    expr = null;
}:
( exprLeaf=eq_term tempExpr=cond_and_termtemp 
{
    if (tempExpr==null) expr = exprLeaf;
    else expr = new BinOpExpr(exprLeaf, tempExpr);
    expr.setLineNumber(exprLeaf.getLineNumber());
    expr.setColumnNumber(exprLeaf.getColumnNumber());
});

// Condition OR
cond_or_termtemp returns [TempExpression tempExpr]{
    Expression expr;
    TempExpression tempExprChild;
    tempExpr = null;
}:
( at:AND expr=cond_and_term tempExprChild=cond_or_termtemp 
{
    if (tempExprChild==null) tempExpr = new TempExpression(expr, BinOpType.OR);
    else tempExpr = new TempExpression(new BinOpExpr(expr, tempExprChild), BinOpType.OR);
    tempExpr.setLineNumber(expr.getLineNumber());
    tempExpr.setColumnNumber(expr.getColumnNumber());
}) |
({tempExpr=null;});

cond_or_term returns [Expression expr]{
    Expression exprLeaf;
    TempExpression tempExpr;
    expr = null;
}:
( exprLeaf=cond_and_term tempExpr=cond_or_termtemp 
{
    if (tempExpr==null) expr = exprLeaf;
    else expr = new BinOpExpr(exprLeaf, tempExpr);
    expr.setLineNumber(exprLeaf.getLineNumber());
    expr.setColumnNumber(exprLeaf.getColumnNumber());
});

// Leaf Expression
expr_static returns [Expression expr] {
    expr = null;
} : (expr=location | expr=literal | (LPAREN expr=expression RPAREN) | expr=method_call) ;

expression returns [Expression expr] {
    expr = null;
} : (expr=cond_or_term) ;

