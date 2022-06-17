parser grammar DecaParser;

options {
    // Default language but name it anyway
    //
    language  = Java;

    // Use a superclass to implement all helper
    // methods, instance variables and overrides
    // of ANTLR default methods, such as error
    // handling.
    //
    superClass = AbstractDecaParser;

    // Use the vocabulary generated by the accompanying
    // lexer. Maven knows how to work out the relationship
    // between the lexer and parser and will build the
    // lexer before the parser. It will also rebuild the
    // parser if the lexer changes.
    //
    tokenVocab = DecaLexer;

}

// which packages should be imported?
@header {
    import fr.ensimag.deca.tree.*;
    import java.io.PrintStream;
}

@members {
    @Override
    protected AbstractProgram parseProgram() {
        return prog().tree;
    }
}

prog returns[AbstractProgram tree]
    : list_classes main EOF {
            assert($list_classes.tree != null);
            assert($main.tree != null);
            $tree = new Program($list_classes.tree, $main.tree);
            setLocation($tree, $list_classes.start);
        }
    ;

main returns[AbstractMain tree]
    : /* epsilon */ {
            $tree = new EmptyMain();
        }
    | block {
            assert($block.decls != null);
            assert($block.insts != null);
            $tree = new Main($block.decls, $block.insts);
            setLocation($tree, $block.start);
        }
    ;

block returns[ListDeclVar decls, ListInst insts]
    : OBRACE list_decl list_inst CBRACE {
            assert($list_decl.tree != null);
            assert($list_inst.tree != null);
            $decls = $list_decl.tree;
            $insts = $list_inst.tree;
        }
    ;

list_decl returns[ListDeclVar tree]
@init   {
            $tree = new ListDeclVar();
        }
    : decl_var_set[$tree]*
    ;

decl_var_set[ListDeclVar l]
    : type list_decl_var[$l,$type.tree] SEMI
    ;

list_decl_var[ListDeclVar l, AbstractIdentifier t]
    : dv1=decl_var[$t] {
        assert($dv1.tree != null);
        $l.add($dv1.tree);
        } (COMMA dv2=decl_var[$t] {
            assert($dv2.tree != null);
            $l.add($dv2.tree);
        }
      )*
    ;

decl_var[AbstractIdentifier t] returns[AbstractDeclVar tree]
@init   {
        }
    : i=ident {
            assert($i.tree != null);
            $tree = new DeclVar($t, $i.tree, new NoInitialization());
            setLocation($tree, $i.start);
        }
      (EQUALS e=expr {
          assert($i.tree != null);
          assert($e.tree != null);
          AbstractInitialization newTree = new Initialization($e.tree);
          $tree = new DeclVar($t, $i.tree, newTree);
          setLocation($tree, $i.start);
          setLocation(newTree, $e.start);
        }
      )? {
        }
    ;

list_inst returns[ListInst tree]
@init {
    $tree = new ListInst();
}
    : (i=inst {
            $tree.add($i.tree);
        }
      )*
    ;

inst returns[AbstractInst tree]
    : e1=expr SEMI {
            assert($e1.tree != null);
            $tree = $e1.tree;
        }
    | SEMI {
            $tree = new NoOperation();
            setLocation($tree, $SEMI);
        }
    | PRINT OPARENT list_expr CPARENT SEMI {
            assert($list_expr.tree != null);
            $tree = new Print(false, $list_expr.tree);
            setLocation($tree, $PRINT);
        }
    | PRINTLN OPARENT list_expr CPARENT SEMI {
            assert($list_expr.tree != null);
            $tree = new Println(false, $list_expr.tree);
            setLocation($tree, $PRINTLN);
        }
    | PRINTX OPARENT list_expr CPARENT SEMI {
            assert($list_expr.tree != null);
            $tree = new Print(true, $list_expr.tree);
            setLocation($tree, $PRINTX);
        }
    | PRINTLNX OPARENT list_expr CPARENT SEMI {
            assert($list_expr.tree != null);
            $tree = new Println(true, $list_expr.tree);
            setLocation($tree, $PRINTLNX);
        }
    | if_then_else {
            assert($if_then_else.tree != null);
            $tree = $if_then_else.tree;
            setLocation($tree, $if_then_else.start);
        }
    | WHILE OPARENT condition=expr CPARENT OBRACE body=list_inst CBRACE {
            assert($condition.tree != null);
            assert($body.tree != null);
            $tree = new While($condition.tree, $body.tree);
            setLocation($tree, $WHILE);
        }
    | RETURN expr SEMI {
            assert($expr.tree != null);
            $tree = new Return($expr.tree);
            setLocation($tree, $RETURN);
        }
    ;

if_then_else returns[IfThenElse tree]
@init {
    ListInst else_parent = new ListInst();
    IfThenElse current;
}
    : if1=IF OPARENT condition=expr CPARENT OBRACE li_if=list_inst CBRACE {
            IfThenElse if_parent = new IfThenElse($condition.tree,$li_if.tree, else_parent);
            $tree = if_parent;
            if_parent.setLocation(tokenLocation($if1));
            setLocation($tree, $if1);
            current = if_parent;
        }
      (ELSE elsif=IF OPARENT elsif_cond=expr CPARENT OBRACE elsif_li=list_inst CBRACE {
            ListInst newElse = new ListInst();
            current = new IfThenElse($elsif_cond.tree,$elsif_li.tree, newElse);
            else_parent.add(current);
            else_parent = newElse;
            current.setLocation(tokenLocation($elsif));
     }
      )*
      (e=ELSE OBRACE li_else=list_inst CBRACE {
            current.setElse($li_else.tree);
            current.setLocation(tokenLocation($e));
        }
      )?
    ;
    
list_expr returns[ListExpr tree]
@init   {
            $tree = new ListExpr();
        }
    : (e1=expr {
            $tree.add($e1.tree);
        }
       (COMMA e2=expr {
           $tree.add($e2.tree);
        }
       )* )?
    ;

expr returns[AbstractExpr tree]
    : assign_expr {
            assert($assign_expr.tree != null);
            $tree = $assign_expr.tree;
            setLocation($tree, $assign_expr.start);
        }
    ;

assign_expr returns[AbstractExpr tree]
    : e=or_expr (
        /* condition: expression e must be a "LVALUE" */ {
            if (! ($e.tree instanceof AbstractLValue)) {
                throw new InvalidLValue(this, $ctx, $e.tree.getClass().getSimpleName());
            }
        }
        EQUALS e2=assign_expr {
            assert($e.tree != null);
            assert($e2.tree != null);
            $tree = new Assign((AbstractLValue) $e.tree, $e2.tree);
            setLocation($tree, $e.start);
        }
      | /* epsilon */ {
            assert($e.tree != null);
            $tree = $e.tree;
        }
      )
    ;

or_expr returns[AbstractExpr tree]
    : e=and_expr {
            assert($e.tree != null);
            $tree = $e.tree;
        }
    | e1=or_expr OR e2=and_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Or($e1.tree, $e2.tree);
            setLocation($tree, $OR);
       }
    ;

and_expr returns[AbstractExpr tree]
    : e=eq_neq_expr {
            assert($e.tree != null);
            $tree = $e.tree;
        }
    |  e1=and_expr AND e2=eq_neq_expr {
            assert($e1.tree != null);                         
            assert($e2.tree != null);
            $tree = new And($e1.tree, $e2.tree);
            setLocation($tree, $AND);
        }
    ;

eq_neq_expr returns[AbstractExpr tree]
    : e=inequality_expr {
            assert($e.tree != null);
            $tree = $e.tree;
        }
    | e1=eq_neq_expr EQEQ e2=inequality_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Equals($e1.tree, $e2.tree);
            setLocation($tree, $EQEQ);
        }
    | e1=eq_neq_expr NEQ e2=inequality_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new NotEquals($e1.tree, $e2.tree);
            setLocation($tree, $NEQ);
        }
    ;

inequality_expr returns[AbstractExpr tree]
    : e=sum_expr {
            assert($e.tree != null);
            $tree = $e.tree;
        }
    | e1=inequality_expr LEQ e2=sum_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new LowerOrEqual($e1.tree, $e2.tree);
            setLocation($tree, $LEQ);
        }
    | e1=inequality_expr GEQ e2=sum_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new GreaterOrEqual($e1.tree, $e2.tree);
            setLocation($tree, $GEQ);
        }
    | e1=inequality_expr GT e2=sum_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Greater($e1.tree, $e2.tree);
            setLocation($tree, $GT);
        }
    | e1=inequality_expr LT e2=sum_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Lower($e1.tree, $e2.tree);
            setLocation($tree, $LT);
        }
    | e1=inequality_expr INSTANCEOF type {
            assert($e1.tree != null);
            assert($type.tree != null);
            $tree = new InstanceOf($type.tree, $e1.tree);
            setLocation($tree, $INSTANCEOF);
        }
    ;


sum_expr returns[AbstractExpr tree]
    : e=mult_expr {
            assert($e.tree != null);
            $tree = $e.tree;
        }
    | e1=sum_expr PLUS e2=mult_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Plus($e1.tree, $e2.tree);
            setLocation($tree, $PLUS);
        }
    | e1=sum_expr MINUS e2=mult_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Minus($e1.tree, $e2.tree);
            setLocation($tree, $MINUS);
        }
    ;

mult_expr returns[AbstractExpr tree]
    : e=unary_expr {
            assert($e.tree != null);
            $tree = $e.tree;
        }
    | e1=mult_expr TIMES e2=unary_expr {
            assert($e1.tree != null);                                         
            assert($e2.tree != null);
            $tree = new Multiply($e1.tree, $e2.tree);
            setLocation($tree, $TIMES);
        }
    | e1=mult_expr SLASH e2=unary_expr {
            assert($e1.tree != null);                                         
            assert($e2.tree != null);
            $tree = new Divide($e1.tree, $e2.tree);
            setLocation($tree, $SLASH);
        }
    | e1=mult_expr PERCENT e2=unary_expr {
            assert($e1.tree != null);                                                                          
            assert($e2.tree != null);
            $tree = new Modulo($e1.tree, $e2.tree);
            setLocation($tree, $PERCENT);
        }
    ;

unary_expr returns[AbstractExpr tree]
    : op=MINUS e=unary_expr {
            assert($e.tree != null);
            $tree = new UnaryMinus($e.tree);
            setLocation($tree, $op);

        }
    | op=EXCLAM e=unary_expr {
            assert($e.tree != null);
            $tree = new Not($e.tree);
            setLocation($tree, $op);
        }
    | select_expr {
            assert($select_expr.tree != null);
            $tree = $select_expr.tree;
        }
    ;

select_expr returns[AbstractExpr tree]
    : e=primary_expr {
            assert($e.tree != null);
            $tree = $e.tree;
        }
    | e1=select_expr DOT i=ident {
            assert($e1.tree != null);
            assert($i.tree != null);
            $tree = new Selection($e1.tree, $i.tree);
            setLocation($tree, $DOT);
        }
        (o=OPARENT args=list_expr CPARENT {
            // we matched "e1.i(args)"
            assert($args.tree != null);
            $tree = new MethodCall($e1.tree, $i.tree, $args.tree);
        }
        | /* epsilon */ {
            // we matched "e.i"
        }
        )
    ;

primary_expr returns[AbstractExpr tree]
    : ident {
            assert($ident.tree != null);
            $tree = $ident.tree;
        }
    | m=ident OPARENT args=list_expr CPARENT {
            assert($args.tree != null);
            assert($m.tree != null);
        }
    | OPARENT expr CPARENT {
            assert($expr.tree != null);
            $tree = $expr.tree;
        }
    | READINT OPARENT CPARENT {
            $tree = new ReadInt();
            setLocation($tree, $READINT);
        }
    | READFLOAT OPARENT CPARENT {
            $tree = new ReadFloat();
            setLocation($tree, $READFLOAT);
        }
    | NEW ident OPARENT CPARENT {
            assert($ident.tree != null);
            $tree = new New($ident.tree);
            setLocation($tree, $NEW);
        }
    | cast=OPARENT type CPARENT OPARENT expr CPARENT {
            assert($type.tree != null);
            assert($expr.tree != null);
            $tree = new Cast($type.tree, $expr.tree);
            setLocation($tree, $cast);
        }
    | literal {
            assert($literal.tree != null);
            $tree = $literal.tree;
            setLocation($tree, $literal.start);
        }
    ;

type returns[AbstractIdentifier tree]
    : ident {
            assert($ident.tree != null);
            $tree = $ident.tree;
        }
    ;

literal returns[AbstractExpr tree]
    : INT {
            try { 
                $tree = new IntLiteral(Integer.parseInt($INT.text)); 
                
            } catch (NumberFormatException e) { 
                throw new InvalidRangeValue(this, $ctx);
            }
        }
    | fd=FLOAT {
            try { 
                $tree = new FloatLiteral(Float.parseFloat($fd.text));
            } catch (Exception e) { 
                throw new InvalidRangeValue(this, $ctx);
            }
        }
    | str=STRING {
            $tree = new StringLiteral($str.text.substring(1, $str.text.length() - 1));
        }
    | TRUE {
            $tree = new BooleanLiteral(true);
        }
    | FALSE {
            $tree = new BooleanLiteral(false);
        }
    | THIS {
            $tree = new This();
        }
    | NULL {
            $tree = new Null();
        }
    ;

ident returns[AbstractIdentifier tree]
    : IDENT {
            $tree = new Identifier(getDecacCompiler().createSymbol($IDENT.text));
            setLocation($tree, $IDENT);
        } 
    ;

/****     Class related rules     ****/

list_classes returns[ListDeclClass tree]
@init   {
            $tree = new ListDeclClass();
        }    :
      (c1=class_decl {
            assert($c1.tree != null);
            $tree.add($c1.tree);
        }
      )*
    ;

class_decl returns[AbstractDeclClass tree]
    : CLASS name=ident superclass=class_extension OBRACE class_body CBRACE {
            assert($name.tree != null);
            assert($superclass.tree != null);
            assert($class_body.fields != null);
            assert($class_body.methods != null);
            $tree = new DeclClass($name.tree, $superclass.tree, $class_body.fields, $class_body.methods);
            setLocation($tree, $CLASS);
        }
    ;

class_extension returns[AbstractIdentifier tree]
    : EXTENDS ident {
            assert($ident.tree != null);
            $tree = $ident.tree;
        }
    | /* epsilon */ {
            $tree = new Identifier(getDecacCompiler().environmentType.OBJECT.getName());
            $tree.setLocation(Location.BUILTIN);
        }
    ;

class_body returns[ListDeclField fields, ListDeclMethod methods]
@init   {
            $methods = new ListDeclMethod();
            $fields = new ListDeclField();
        }
    : (m=decl_method {
            assert($m.tree != null);
            $methods.add($m.tree);
        }
      | decl_field_set[$fields]
      )*
    ;

decl_field_set[ListDeclField l]
    : v=visibility t=type list_decl_field[$l, $type.tree, $v.v]
      SEMI
    ;

visibility returns [Visibility v]
    : /* epsilon */ {
            $v = Visibility.PUBLIC;
        }
    | PROTECTED {
            $v = Visibility.PROTECTED;
        }
    ;

list_decl_field[ListDeclField l, AbstractIdentifier t, Visibility v]
    : dv1=decl_field[$t, $v] {
            assert($dv1.tree != null);
            $l.add($dv1.tree);
        } (COMMA dv2=decl_field[$t, $v] {
            assert($dv2.tree != null);
            $l.add($dv2.tree);
        }
      )*
    ;

decl_field[AbstractIdentifier t, Visibility v] returns[AbstractDeclField tree]
    : i=ident {
            assert($i.tree != null);
            $tree = new DeclField($t, $i.tree, new NoInitialization(), $v);
            setLocation($tree, $i.start);
        }
      (EQUALS e=expr {
            assert($i.tree != null);
            assert($e.tree != null);
            AbstractInitialization newTree = new Initialization($e.tree);
            $tree = new DeclField($t, $i.tree, newTree, $v);
            setLocation($tree, $i.start);
            setLocation(newTree, $e.start);
        }
      )? {
        }
    ;

decl_method returns[AbstractDeclMethod tree]
@init {
    ListDeclParam params = new ListDeclParam();
    AbstractMethodBody body = null;
}
    : type ident OPARENT params=list_params[params] CPARENT (block {
            assert($block.decls != null);
            assert($block.insts != null);
            body = new MethodBody($block.decls, $block.insts);
            setLocation(body, $block.start);
        }
      | ASM OPARENT code=multi_line_string CPARENT SEMI {
            assert($code.text != null);
            body = new MethodAsmBody($code.text);
            setLocation($tree, $ASM);

        }
      ) {
            $tree = new DeclMethod($type.tree, $ident.tree, params, body);
            setLocation($tree, $type.start);
        }
    ;

list_params[ListDeclParam params]
    : (p1=param {
            assert($p1.tree != null);
            $params.add($p1.tree);
        } (COMMA p2=param {
            assert($p2.tree != null);
            $params.add($p2.tree);
        }
      )*)?
    ;
    
multi_line_string returns[String text, Location location]
    : s=STRING {
            $text = $s.text;
            $location = tokenLocation($s);
        }
    | s=MULTI_LINE_STRING {
            $text = $s.text;
            $location = tokenLocation($s);
        }
    ;

param returns [AbstractDeclParam tree]
    : type ident {
            assert($type.tree != null);
            assert($ident.tree != null);
            $tree = new DeclParam($type.tree, $ident.tree);
            setLocation($tree, $type.start);
        }
    ;
