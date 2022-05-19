lexer grammar DecaLexer;

options {
   language=Java;
   // Tell ANTLR to make the generated lexer class extend the
   // the named class, which is where any supporting code and
   // variables will be placed.
   superClass = AbstractDecaLexer;
}

@members {
}

//Ignore
LINE_COMMENT: ('//' ~[\r\n]*) { skip(); };
SPACES: ('\r'|'\t'|'\n'|' ') { skip(); };
//Syntaxe
OBRACE: '{';
CBRACE: '}';
OPARENT: '(';
CPARENT: ')';
COMMA: ',';
EQUALS: '=';
SEMI: ';';
EOL: '\n';
//Logique
AND: '&&';
OR: '||';
EQEQ: '==';
NEQ: '!=';
EXCLAM: '!';
LT: '<';
LEQ: '<=';
GT: '>';
GEQ: '>=';
//Mots réservés
WHILE: 'while';
IF: 'if';
ELSE: 'else';
ELSEIF: 'elseif';
//Fonctions
PRINT: 'print';
PRINTLN: 'println';
PRINTX: 'printx';
PRINTLNX: 'printlnx';
READINT: 'readInt';
READFLOAT: 'readFloat';

//Types
fragment NUM: DIGIT+;
fragment SIGN: ('+' | '-')?;
fragment EXP:  ('E' | 'e') SIGN NUM;
fragment DEC: NUM '.' NUM;
fragment FLOATDEC: (DEC | DEC EXP) ('F' | 'f')?;
fragment DIGITHEX: '0' .. '9' | 'A' .. 'F' | 'a' .. 'f';
fragment NUMHEX: DIGITHEX+;
fragment FLOATHEX: ('0x' | '0X') NUMHEX '.' NUMHEX ('P' | 'p') SIGN NUM ('F' | 'f')?;
FLOAT: FLOATDEC | FLOATHEX;

fragment DIGIT: '0' .. '9';
fragment POSITIVE_DIGIT: '1' .. '9';
INT: '0' | (POSITIVE_DIGIT DIGIT*);

fragment STRING_CAR: ~('"' | '\\' | '\n');
STRING: '"' (STRING_CAR | '\\"' | '\\\\')* '"';
fragment MULTI_LINE_STRING: '"' (STRING_CAR | EOL | '\\"' | '\\\\')* '"';

fragment LETTER: 'a' .. 'z' | 'A' .. 'Z';
IDENT: (LETTER | DIGIT | '$' | '_')+;

// Deca lexer rules.
DUMMY_TOKEN: .; // A FAIRE : Règle bidon qui reconnait tous les caractères.
                // A FAIRE : Il faut la supprimer et la remplacer par les vraies règles.
