/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilerone;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/**
 *
 * @author Jared
 */
public class Tokenizer {

    static String keywords[] = {"class", "constructor", "function",
        "method", "field", "static", "var", "int", "char", "boolean", "void", "true",
        "false", "null", "this", "let", "do", "if", "else", "while", "return"};

    private final static String symbols = "{}()[].,;+-*/&|<>=~";

    static char[] symbolslist = {'{', '}', '(', ')', '[', ']', '.', ',', ';', '+', '-', '*', '/', '&', '|', '<', '>', '=', '~'};

    static String token = "";

    static Scanner s;

    static ArrayList<String> tokens = new ArrayList<String>();

    static Iterator iter = tokens.iterator();

    Tokenizer(String path) throws FileNotFoundException {
        boolean multiComment = false;
        boolean comment = false;
        s = new Scanner(new File(path));
        ArrayList<String> list = new ArrayList<String>();

        while (s.hasNext()) {

            // Strips comments
            do {
                comment = false;
                token = s.next();

                if (token.startsWith("//")) {
                    comment = true;
                    token = s.nextLine();
                }

                if (token.startsWith("/*") && multiComment == false) {
                    multiComment = true;

                } else if (token.endsWith("*/") && multiComment == true) {
                    multiComment = false;
                    token = s.next();
                }

            } while (multiComment || comment); // End comment stripping

            boolean containsSymbol = false;
            for (int i = 0; i < symbolslist.length; i++) {
                // if token is alone break
                if (token.equals(symbolslist[i] + "")) {
                    break;

                } else if (token.contains(symbolslist[i] + "")) {
                    containsSymbol = true;
                    int prev = 0;
                    for (int j = 0; j < token.length(); j++) {
                        for (int k = 0; k < symbolslist.length; k++) {
                            if (token.charAt(j) == symbolslist[k]) {
                                if (prev != j) {
                                    list.add(token.substring(prev, j));
                                }
                                list.add(symbolslist[k] + "");
                                prev = j + 1;
                            }
                        }

                        if (prev < token.length() && j == token.length() - 1) {
                            list.add(token.substring(prev, token.length()));
                        }
                    }
                    break;
                }
            }

            if (!containsSymbol) {
                list.add(token);
            }

        }  // All tokens are added.
        s.close();
        tokens = list;
        iter = tokens.iterator();

    }

    boolean hasMoreTokens() {
        return iter.hasNext();
    }

    void advance() {
        if (iter.hasNext()) {
            token = (String) iter.next();

        }
    }

    String tokenType() {

        for (String keyword : keywords) {
            if (token.equals(keyword)) {
                return "KEYWORD";
            }
        }

        if (symbols.contains(token) && token != "") {
            return "SYMBOL";
        }

        // "regex. \\d+" is any digit from 0-9, + adds support for > 1 digit"
        if (token.matches("\\d+")) {
            return "INT_CONST";
        }

        // double quotes, \ is escape char
        if (token.startsWith("\"")) {
            return "STRING_CONST";
        } else {

            return "IDENTIFIER";
        }
    }

    String keyWord() {
        for (String keyword : keywords) {
            if (token.equals(keyword)) {
                return keyword;
            }
        }

        return "";
    }

    char symbol() {
        return token.charAt(0);
    }

    String identifier() {
        return token;
    }

    int intVal() {
        return Integer.parseInt(token);
    }

    String stringVal() {
        token = token.replaceAll("\"", "");
        return token;
    }

}
