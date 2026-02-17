package rs.ac.bg.etf.pp1;

import java_cup.runtime.Symbol;

%%

%{

	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type) {
		return new Symbol(type, yyline+1, yycolumn);
	}
	
	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type, Object value) {
		return new Symbol(type, yyline+1, yycolumn, value);
	}

%}

%cup
%line
%column

%xstate COMMENT

%eofval{
	return new_symbol(sym.EOF);
%eofval}

%%

" " 	{ }
"\b" 	{ }
"\t" 	{ }
"\r\n" 	{ }
"\n"   { }
"\r"   { }
"\f" 	{ }

/* Keywords-------------------------------------------------------------------------------------------------------------------------------------------------------*/

"program"  	{ return new_symbol(sym.PROG, yytext()); }
"const"    	{ return new_symbol(sym.CONST, yytext()); }
"enum"     	{ return new_symbol(sym.ENUM, yytext()); }
"print"    	{ return new_symbol(sym.PRINT, yytext()); }
"read"     	{ return new_symbol(sym.READ, yytext()); }
"return"   	{ return new_symbol(sym.RETURN, yytext()); }
"void"     	{ return new_symbol(sym.VOID, yytext()); }
"if"       	{ return new_symbol(sym.IF, yytext()); }
"else"     	{ return new_symbol(sym.ELSE, yytext()); }
"new"      	{ return new_symbol(sym.NEW, yytext()); }

"true" 		{ return new_symbol(sym.BOOLCONST, 1); }
"false" 	{ return new_symbol(sym.BOOLCONST, 0); }

"length" 	{ return new_symbol(sym.LENGTH, yytext()); }

/* Operators-------------------------------------------------------------------------------------------------------------------------------------------*/

">="		{ return new_symbol(sym.GE, yytext()); }
">"			{ return new_symbol(sym.GT, yytext()); }
"<="		{ return new_symbol(sym.LE, yytext()); }
"<"			{ return new_symbol(sym.LT, yytext()); }
"=="		{ return new_symbol(sym.EQ, yytext()); }
"!="		{ return new_symbol(sym.NE, yytext()); }

"++"    	{ return new_symbol(sym.INC, yytext()); }
"--"     	{ return new_symbol(sym.DEC, yytext()); }

"+"        	{ return new_symbol(sym.PLUS, yytext()); }
"-"        	{ return new_symbol(sym.MINUS, yytext()); }
"*"        	{ return new_symbol(sym.MUL, yytext()); }
"/"        	{ return new_symbol(sym.DIV, yytext()); }
"%"        	{ return new_symbol(sym.MOD, yytext()); }

"="        	{ return new_symbol(sym.EQUAL, yytext()); }

"?"        	{ return new_symbol(sym.QUESTION, yytext()); }
":"        	{ return new_symbol(sym.COLON, yytext()); }

"."        	{ return new_symbol(sym.DOT, yytext()); }

"["        	{ return new_symbol(sym.LBRACKET, yytext()); }
"]"        	{ return new_symbol(sym.RBRACKET, yytext()); }

/* Delimiters----------------------------------------------------------------------------------------------------------------------------------------------------*/

";"        	{ return new_symbol(sym.SEMI, yytext()); }
","        	{ return new_symbol(sym.COMMA, yytext()); }
"("        	{ return new_symbol(sym.LPAREN, yytext()); }
")"        	{ return new_symbol(sym.RPAREN, yytext()); }
"{"        	{ return new_symbol(sym.LBRACE, yytext()); }
"}"        	{ return new_symbol(sym.RBRACE, yytext()); }


/* Comments-----------------------------------------------------------------------------------------------------------------------------------------------------*/

<YYINITIAL> "//"	{ yybegin(COMMENT); }
<COMMENT> .       	{ yybegin(COMMENT); }
<COMMENT> "\r\n"   	{ yybegin(YYINITIAL); }
<COMMENT> "\n"      { yybegin(YYINITIAL); }
<COMMENT> "\r"      { yybegin(YYINITIAL); }

/* Constants-----------------------------------------------------------------------------------------------------------------------------------------------------*/

[0-9]+    { return new_symbol(sym.NUMBER, Integer.valueOf(yytext())); }

"'"[^']"'"  { return new_symbol(sym.CHARCONST, yytext().charAt(1)); }


/* Identifiers---------------------------------------------------------------------------------------------------------------------------------------------------*/

([a-z]|[A-Z])[a-zA-Z0-9_]*  
{ 
    return new_symbol(sym.IDENT, yytext()); 
}


/* Error--------------------------------------------------------------------------------------------------------------------------------------------------------*/

. {
    System.err.println("Leksicka greska ("+yytext()+") u liniji "+(yyline+1)+", kolona "+(yycolumn+1));
}
