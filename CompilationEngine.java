package compilerone;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author craddockj03
 */
public class CompilationEngine {

    private final Tokenizer token;
    private final PrintWriter pw;

    CompilationEngine(String input) throws FileNotFoundException, IOException {

        token = new Tokenizer("SquareGame.jack");
        pw = new PrintWriter("output.xml");
        CompileClass();
        pw.close();

    }

    public void CompileClass() throws IOException {
        token.advance();
        pw.write("<class>");
        pw.println();

        if (token.keyWord().equals("class")) {
            pw.write("<keyword> class </keyword>");
            pw.println();

        }

        token.advance();
        if (token.tokenType().equals("IDENTIFIER")) {
            pw.write("<identifier> " + token.identifier() + " </identifier>");
            pw.println();

        }

        token.advance();

        if (token.symbol() == '{') {
            pw.write("<symbol> { </symbol>");
            pw.println();

        }

        token.advance();
        while ((token.keyWord().equals("static") || token.keyWord().equals("field"))) {
            CompileClassVarDec();
        }

        while ((token.keyWord().equals("constructor") | token.keyWord().equals("function") | token.keyWord().equals("method"))) {
            CompileSubroutine();
            token.advance();

        }

        if (token.symbol() == '}') {
            pw.write("<symbol> } </symbol>");
            pw.println();

        }

        pw.write("</class>");
        pw.println();

    }

    public void CompileClassVarDec() throws IOException {
        pw.write("<classVarDec>");
        pw.println();

        pw.write("<keyword> " + token.keyWord() + " </keyword>");
        pw.println();

        token.advance();

        if ((token.keyWord().equals("int") | token.keyWord().equals("char") | token.keyWord().equals("boolean"))) {
            pw.write("<keyword> " + token.keyWord() + " </keyword>");
            pw.println();

        } else if (token.tokenType().equals("IDENTIFIER")) {
            pw.write("<identifier> " + token.identifier() + " </identifier>");
            pw.println();

        }

        token.advance();
        if (token.tokenType().equals("IDENTIFIER")) {
            pw.write("<identifier> " + token.identifier() + " </identifier>");
            pw.println();

        }

        token.advance();
        while (token.symbol() == ',') {
            pw.write("<symbol> , </symbol>");
            pw.println();

            token.advance();
            pw.write("<identifier> " + token.identifier() + " </identifier>");
            pw.println();

            token.advance();
        }

        pw.write("<symbol> ; </symbol>");
        pw.println();

        token.advance();

        pw.write("</classVarDec>");
        pw.println();

    }

    public void CompileSubroutine() throws IOException {
        pw.write("<subroutineDec>");
        pw.println();

        pw.write("<keyword> " + token.keyWord() + " </keyword>");
        pw.println();

        token.advance();

        if (token.keyWord().equals("void")) {
            pw.write("<keyword> " + token.keyWord() + " </keyword>");
            pw.println();

        } else {
            pw.write("<identifier> " + token.identifier() + " </identifier>");
        }
        pw.println();

        token.advance();

        pw.write("<identifier> " + token.identifier() + " </identifier>");
        pw.println();

        token.advance();

        pw.write("<symbol> ( </symbol>");
        pw.println();

        token.advance();
        compileParameterList();

        pw.write("<symbol> ) </symbol>");
        pw.println();

        pw.write("<subroutineBody>");
        pw.println();

        token.advance();

        pw.write("<symbol> { </symbol>");
        pw.println();

        token.advance();

        while (token.keyWord().equals("var")) {
            compileVarDec();
            token.advance();
        }
        compileStatements();

        pw.write("<symbol> } </symbol>");
        pw.println();

        pw.write("</subroutineBody>");
        pw.println();
        pw.write("</subroutineDec>");
        pw.println();

    }

    public void compileParameterList() throws IOException {
        pw.write("<parameterList>");
        pw.println();

        if ((token.keyWord().equals("int") || token.keyWord().equals("char")
                || token.keyWord().equals("boolean")
                || token.tokenType().equals("IDENTIFIER"))) {

            pw.write("<keyword> " + token.keyWord() + "</keyword>");
            pw.println();
            token.advance();

            pw.write("<identifier> " + token.identifier() + "</identifier>");
            pw.println();
            token.advance();
        }

        while (token.symbol() == ',') {
            pw.write("<symbol> , </symbol>");
            pw.println();
            token.advance();

            pw.write("<keyword> " + token.keyWord() + "</keyword>");
            pw.println();
            token.advance();

            pw.write("<identifier> " + token.identifier() + "</identifier>");
            pw.println();
            token.advance();

        }

        pw.write("</parameterList>");
        pw.println();

    }

    public void compileVarDec() throws IOException {
        pw.write("<varDec>");
        pw.println();

        pw.write("<keyword> var </keyword>");
        pw.println();

        token.advance();

        pw.write("<keyword> " + token.keyWord() + " </keyword>");
        pw.println();
        token.advance();

        pw.write("<identifier> " + token.identifier() + " </identifier>");
        pw.println();
        token.advance();

        while (token.symbol() == ',') {
            pw.write("<symbol> , </symbol>");
            pw.println();
            token.advance();

            pw.write("<identifier> " + token.identifier() + " </identifier>");
            pw.println();

            token.advance();
        }

        pw.write("<symbol> ; </symbol>");
        pw.println();

        pw.write("</varDec>");
        pw.println();

    }

    public void compileStatements() throws IOException {
        pw.write("<statements>");
        pw.println();

        while ((token.keyWord().equals("let") || token.keyWord().equals("if")
                || token.keyWord().equals("while") || token.keyWord().equals("do") || token.keyWord().equals("return"))) {
            if (token.keyWord().equals("let")) {
                compileLet();
                token.advance();
            } else if (token.keyWord().equals("if")) {
                compileIf();
            } else if (token.keyWord().equals("while")) {
                compileWhile();
                token.advance();
            } else if (token.keyWord().equals("do")) {
                compileDo();
                token.advance();
            } else if (token.keyWord().equals("return")) {
                compileReturn();
                token.advance();
            }

        }

        pw.write("</statements>");
        pw.println();

    }

    public void compileDo() throws IOException {
        pw.write("<doStatement>");
        pw.println();

        pw.write("<keyword> do </keyword>");
        pw.println();

        token.advance();

        pw.write("<identifier> " + token.identifier() + " </identifier>");
        pw.println();

        token.advance();

        if (token.symbol() == '(') {
            pw.write("<symbol> ( </symbol>");
            pw.println();

        } else if (token.symbol() == '.') {
            pw.write("<symbol> . </symbol>");
            pw.println();

            token.advance();

            pw.write("<identifier> " + token.identifier() + " </identifier>");
            pw.println();

            token.advance();

            pw.write("<symbol> ( </symbol>");
            pw.println();

        }

        token.advance();
        CompileExpressionList();

        pw.write("<symbol> ) </symbol>");
        pw.println();

        token.advance();

        pw.write("<symbol> ; </symbol>");
        pw.println();

        pw.write("</doStatement>");
        pw.println();

    }

    public void compileLet() throws IOException {
        pw.write("<letStatement>");
        pw.println();

        pw.write("<keyword> let </keyword>");
        pw.println();

        token.advance();

        pw.write("<identifier> " + token.identifier() + " </identifier>");
        pw.println();

        token.advance();

        if (token.symbol() == '[') {

            pw.write("<symbol> [ </symbol>");
            pw.println();

            token.advance();
            CompileExpression();

            pw.write("<symbol> ] </symbol>");
            pw.println();

            token.advance();
        }

        pw.write("<symbol> = </symbol>");
        pw.println();

        token.advance();
        CompileExpression();

        pw.write("<symbol> ; </symbol>");
        pw.println();

        pw.write("</letStatement>");
        pw.println();

    }

    public void compileWhile() throws IOException {
        pw.write("<whileStatement>");
        pw.println();

        pw.write("<keyword> while </keyword>");
        pw.println();

        token.advance();

        pw.write("<symbol> ( </symbol>");
        pw.println();

        token.advance();
        CompileExpression();

        pw.write("<symbol> ) </symbol>");
        pw.println();
        token.advance();

        pw.write("<symbol> { </symbol>");
        pw.println();

        token.advance();
        compileStatements();

        pw.write("<symbol> } </symbol>");
        pw.println();

        pw.write("</whileStatement>");
        pw.println();

    }

    public void compileReturn() throws IOException {
        pw.write("<returnStatement>");
        pw.println();

        pw.write("<keyword> return </keyword>");
        pw.println();

        token.advance();

        if (token.symbol() != ';') {

            CompileExpression();
        }

        pw.write("<symbol> ; </symbol>");
        pw.println();

        pw.write("</returnStatement>");
        pw.println();

    }

    public void compileIf() throws IOException {
        pw.write("<ifStatement>");
        pw.println();

        pw.write("<keyword> if </keyword>");
        pw.println();
        token.advance();

        pw.write("<symbol> ( </symbol>");
        pw.println();

        token.advance();
        CompileExpression();

        pw.write("<symbol> ) </symbol>");
        pw.println();

        token.advance();

        pw.write("<symbol> { </symbol>");
        pw.println();

        token.advance();
        compileStatements();

        pw.write("<symbol> } </symbol>");
        pw.println();

        token.advance();
        if (token.keyWord().equals("else")) {
            pw.write("<keyword> else </keyword>");
            pw.println();

            token.advance();

            pw.write("<symbol> { </symbol>");
            pw.println();

            token.advance();
            compileStatements();

            pw.write("<symbol> } </symbol>");
            pw.println();

            token.advance();
        }

        pw.write("</ifStatement>");
        pw.println();

    }

    public void CompileExpression() throws IOException {
        pw.write("<expression>");
        pw.println();

        CompileTerm();

        char sym = token.symbol();
        while ((sym == '+' || sym == '-' || sym == '*' || sym == '/' || sym == '&' || sym == '|' || sym == '<'
                || sym == '>' || sym == '=')) {
            String temp = "";

            if (sym == '<') {
                temp = "&lt;";
            } else if (sym == '>') {
                temp = "&gt;";
            } else if (sym == '&') {
                temp = "&amp;";
            } else {
                temp = sym + "";
            }

            pw.write("<symbol> " + temp + " </symbol>");
            pw.println();

            token.advance();
            CompileTerm();
            sym = token.symbol();
        }

        pw.write("</expression>");
        pw.println();

    }

    public void CompileTerm() throws IOException {
        pw.write("<term>");
        pw.println();

        if (token.tokenType().equals("INT_CONST")) {
            pw.write("<integerConstant> " + token.intVal() + " </integerConstant>");
            pw.println();

            token.advance();
        } else if (token.tokenType().equals("STRING_CONST")) {
            pw.write("<stringConstant> " + token.stringVal() + " </stringConstant>");
            pw.println();

            token.advance();
        } else if (token.tokenType().equals("KEYWORD")) {
            pw.write("<keyword> " + token.keyWord() + " </keyword>");
            pw.println();

            token.advance();
        } else if (token.tokenType().equals("IDENTIFIER")) {

            pw.write("<identifier> " + token.identifier() + " </identifier>");
            pw.println();

            token.advance();
            if (token.symbol() == '[') {
                pw.write("<symbol> [ </symbol>");
                pw.println();

                token.advance();
                CompileExpression();

                pw.write("<symbol> ] </symbol>");
                pw.println();

                token.advance();
            } else if ((token.symbol() == '(' || token.symbol() == '.')) {
                if (token.symbol() == '(') {
                    pw.write("<symbol> ( </symbol>");
                    pw.println();

                } else if (token.symbol() == '.') {
                    pw.write("<symbol> . </symbol>");
                    pw.println();

                    token.advance();

                    pw.write("<identifier> " + token.identifier() + " </identifier>");
                    pw.println();

                    token.advance();

                    pw.write("<symbol> ( </symbol>");
                    pw.println();

                }

                token.advance();
                CompileExpressionList();

                if (token.symbol() == ')') {
                    pw.write("<symbol> ) </symbol>");
                    pw.println();

                }

                token.advance();
            } else {

            }
        } else if (token.tokenType().equals("SYMBOL") && token.symbol() == '(') {
            pw.write("<symbol> ( </symbol>");
            pw.println();

            token.advance();
            CompileExpression();

            if (token.symbol() == ')') {
                pw.write("<symbol> ) </symbol>");
                pw.println();

            }

            token.advance();
        } else if (token.tokenType().equals("SYMBOL") && (token.symbol() == '-' || token.symbol() == '~')) {
            pw.write("<symbol> " + token.symbol() + " </symbol>");
            pw.println();

            token.advance();
            CompileTerm();
        }

        pw.write("</term>");
        pw.println();

    }

    public void CompileExpressionList() throws IOException {
        pw.write("<expressionList>");
        pw.println();

        if (token.symbol() != ')') {

            CompileExpression();

            while (token.symbol() == ',') {
                pw.write("<symbol> , </symbol>");
                pw.println();

                token.advance();
                CompileExpression();
            }
        }

        pw.write("</expressionList>");
        pw.println();

    }
}
