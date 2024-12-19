package lox;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import static lox.TokenType.*; 

class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;

    private static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("and",    AND);
        keywords.put("class",  CLASS);
        keywords.put("else",   ELSE);
        keywords.put("false",  FALSE);
        keywords.put("for",    FOR);
        keywords.put("fun",    FUN);
        keywords.put("if",     IF);
        keywords.put("nil",    NIL);
        keywords.put("or",     OR);
        keywords.put("print",  PRINT);
        keywords.put("return", RETURN);
        keywords.put("super",  SUPER);
        keywords.put("this",   THIS);
        keywords.put("true",   TRUE);
        keywords.put("var",    VAR);
        keywords.put("while",  WHILE);
    }

    Scanner(String source)
    {
        this.source = source;
    }

    List<Token> scanTokens() {
        while (!isAtEnd()) {
          // We are at the beginning of the next lexeme.
          start = current;
          scanToken();
        }
    
        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }


    private void scanToken() {
        char c = advance();
        switch (c) {
          case '(': addToken(LEFT_PAREN); break;
          case ')': addToken(RIGHT_PAREN); break;
          case '{': addToken(LEFT_BRACE); break;
          case '}': addToken(RIGHT_BRACE); break;
          case ',': addToken(COMMA); break;
          case '.': addToken(DOT); break;
          case '-': addToken(MINUS); break;
          case '+': addToken(PLUS); break;
          case ';': addToken(SEMICOLON); break;
          case '*': addToken(STAR); break; 
          case '!':
                addToken(match('=') ? BANG_EQUAL : BANG);
                break;
          case '=':
                addToken(match('=') ? EQUAL_EQUAL : EQUAL);
                break;
          case '<':
                addToken(match('=') ? LESS_EQUAL : LESS);
                break;
          case '>':
                addToken(match('=') ? GREATER_EQUAL : GREATER);
                break;

          case '/':
                if (match('/')) { //sinle line comment found, ignore the whole line
                  // A comment goes until the end of the line. Keep consuming charecters until you detect EOL
                  while (peek(0) != '\n' && !isAtEnd()) advance();
                }
                else if(match('*'))//support for multiple line comments . TODO : add support for nesting
                {
                    while(!(peek(0) == '*' && peek(1) == '/')) 
                    {
                        if(peek(0) == '\n') line++; //add new line if encountered

                        advance();
                    }
                    advance(); //consumes the * charecter
                    advance(); //consuems the / charecter
                } else { //standard division, add a slash token to signigy it
                  addToken(SLASH);
                }
                break;
          case '?':
                addToken(QUESTION_MARK);
                break;
          case ':':
                addToken(COLON);
                break;
          case ' ': //neat overflow trick, control keeps executes everything after this until a break statement is encountered.
          case '\r':
          case '\t':
            // Ignore whitespace.
            break;

          case '\n':
                line++;
                break;
          
          case '"':  //Time for some string handling
                string();
                break;

          default:
                if(isDigit(c))
                {
                    number();
                }
                else if(isAlpha(c))
                {
                    while(isAlphaNumeric(peek(0))) advance();
                    
                    String text = source.substring(start, current);
                    TokenType type = keywords.get(text);
                    if (type == null) type = IDENTIFIER;
                    addToken(type);

                }
                else
                {
                    Lox.error(line, "Unexpected charecter found.");

                }
                break;
        }
    }

    private void string()
    {
        while((peek(0) != '"') && !isAtEnd())
        {
                    if(peek(0) == '\n') line++;
                    advance();
        }

        if(isAtEnd()) Lox.error(line, "String is not terminated.");

        String val = source.substring(start + 1, current);

        advance(); //skip over the closing double quote

        addToken(STRING, val);
    }

    private void number()
    {
        while(isDigit(peek(0))) advance();

        //check for fractional part
        if(peek(0) == '.' && isDigit(peek(1))) 
        {
            advance();
            while(isDigit(peek(0))) advance();
        }

        addToken(NUMBER, Double.parseDouble(source.substring(start, current)));

    }

    private char peek(int step)
    {
        if(isAtEnd())return '\0';
        if(current + step >= source.length())return '\0';
        return source.charAt(current+step);
    }

    private boolean match(char next_expected)
    {
        if(isAtEnd())return false;
        if(source.charAt(current) != next_expected)return false;

        current ++;
        return true;
    }

    private char advance() {
        return source.charAt(current++);
    }
    
    private void addToken(TokenType type) {
        addToken(type, null);
    }
    
    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

    private boolean isAtEnd()
    {
        return current >= source.length();
    }

    private boolean isDigit(char c)
    {
        return c >= '0' && c <= '9';
    }
    
    private boolean isAlpha(char c)
    {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c == '_');
    }

    private boolean isAlphaNumeric(char c)
    {
        return isAlpha(c) || isDigit(c);
    }

}
