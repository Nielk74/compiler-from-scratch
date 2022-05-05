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
SPACES: ('\n'|' ') { skip(); };
//Syntaxe
OBRACE: '{';
CBRACE: '}';
OPARENT: '(';
CPARENT: ')';
COMMA: ',';
EQUALS: '=';
SEMI: ';';
EOL: '\n';
//Mots réservés
NEW: 'new';
//Fonctions
PRINT: 'print';
PRINTLN: 'println';
PRINTX: 'printx';
PRINTLNX: 'printlnx';
//Types
STRING_CAR: ~('"' | '\\' | '\n');
STRING: '"' (STRING_CAR | '\\"' | '\\\\')* '"';
MULTI_LINE_STRING: '"' (STRING_CAR | EOL | '\\"' | '\\\\')* '"';

// Deca lexer rules.
DUMMY_TOKEN: .; // A FAIRE : Règle bidon qui reconnait tous les caractères.
                // A FAIRE : Il faut la supprimer et la remplacer par les vraies règles.
