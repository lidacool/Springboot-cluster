package com.lee.util.parseFormula;

public class ParseException extends RuntimeException {

    private static final long serialVersionUID = 4494160542988762303L;

    private int row_;
    private int col_;
    private int id_;
    private String msg_;

    public ParseException(final int id, final String message) {
        this.row_ = -1;
        this.col_ = -1;
        this.id_ = id;
        this.msg_ = String.format(errorMsg(id_), message);
    }

    public ParseException(int id) {
        this.row_ = -1;
        this.col_ = -1;
        this.id_ = id;
        this.msg_ = errorMsg(id_);
    }

    public ParseException(int row_, int col_, int id, String msg_) {
        this.row_ = row_;
        this.col_ = col_;
        this.id_ = id;
        this.msg_ = String.format(errorMsg(id_), msg_);
    }

    public ParseException(int row_, int col_, int id) {
        this.row_ = row_;
        this.col_ = col_;
        this.id_ = id;
        this.msg_ = errorMsg(id_);
    }

    public final String get() {
        String result;
        if (row_ == -1) {
            if (col_ == -1) {
                result = String.format("Error: %s", msg_);
            } else {
                result = String.format("Error: %s(col %d)", msg_, col_);
            }
        } else {
            result = String.format("Error: %s(row %d,col %d)", msg_, row_, col_);
        }
        return result;
    }

    public int getId_(){
        return id_;
    }

    private String errorMsg(final int id) {
        switch (id) {
            // syntax errors
            case 1:
                return "Syntax error in part \"%s\"";
            case 2:
                return "Syntax error";
            case 3:
                return "Parentesis ) missing";
            case 4:
                return "Empty expression";
            case 5:
                return "Unexpected part \"%s\"";
            case 6:
                return "Unexpected end of expression";
            case 7:
                return "Value expected";

            // wrong or unknown operators, functions, variables
            case 101:
                return "Unknown operator %s";
            case 102:
                return "Unknown function %s";
            case 103:
                return "Unknown variable %s";
            case 104:
                return "Unknown operator";
            case 105:
                return "operator dose not had args";
            case 106:
                return "Unknown constant %s";
            // domain errors
            case 200:
                return "Too long expression, maximum number of characters exceeded";

            // error in assignments of variables
            case 300:
                return "Defining variable failed";

            // error in functions
            case 400:
                return "Integer value expected in function %s";

            // unknown error
            case 500:
                return "%s";
        }

        return "Unknown error";
    }
}
