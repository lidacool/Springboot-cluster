package com.lee.util.parseFormula;

import com.lee.util.log.Logging;
import com.lee.util.parseFormula.express.*;
import com.lee.util.parseFormula.operate.OpContext;
import com.lee.util.parseFormula.operate.Operator;
import com.lee.util.parseFormula.operate.Operator.OP;

import java.util.ArrayList;
import java.util.List;

/**
 * @description learning formula parser
 */
public class Parser {

    private enum TokenType {
        NOTHING, DELIMETER, NUMBER, VARIABLE, FUNCTION, UNKNOWN, MULTI_PARAM, TERNARY_CON, TERNARY_BR,
        ;
    }

    private String express_str;
    private int expr_pos;
    private char expr_c;
    private String token;
    private TokenType token_type;
    private Express express;

    public Parser() {
        express_str = "";
        expr_pos = -1;
        expr_c = '\0';
        token = "";
        token_type = TokenType.NOTHING;
    }

    public Express parse(final String new_express) {

        try {
            this.express_str = new_express;
            this.express = null;

            getFirstChar();
            getToken();

            if (token_type == TokenType.DELIMETER && expr_c == '\0') {
                throw new ParseException(row(), col(), 4);//empty express
            }

            express = parse_level0();

            if (token_type != TokenType.DELIMETER || token.length() > 0) {
                if (token_type == TokenType.DELIMETER) {
                    throw new ParseException(row(), col(), 101, token);
                } else {
                    throw new ParseException(row(), col(), 5, token);
                }
            }


        } catch (ParseException e) {
            Logging.error(new_express + "\t" + e.get());
        }
        return express;
    }

    void getToken() throws ParseException {
        token_type = TokenType.NOTHING;
        token = ""; // set token empty

        // skip over whitespaces
        while (isWhiteSpace(expr_c)) // space or tab
        {
            getChar();
        }

        // check for end of expression
        if (expr_c == '\0') {
            // token is empty
            token_type = TokenType.DELIMETER;
            return;
        }

        // check for minus
        if (expr_c == '-') {
            token_type = TokenType.DELIMETER;
            token += expr_c;
            getChar();
            return;
        }

        // check for parentheses
        if (expr_c == '(' || expr_c == ')') {
            token_type = TokenType.DELIMETER;
            token += expr_c;
            getChar();
            return;
        }

        if (expr_c == '?') {
            token_type = TokenType.TERNARY_CON;
            token += expr_c;
            getChar();
            return;
        }

        if (expr_c == ':') {
            token_type = TokenType.TERNARY_BR;
            token += expr_c;
            getChar();
            return;
        }

        // check for operators (delimeters)
        if (isDelimeter(expr_c)) {
            token_type = TokenType.DELIMETER;
            while (isDelimeter(expr_c)) {
                token += expr_c;
                getChar();
            }
            return;
        }

        // check for a value
        if (isDigitDot(expr_c)) {
            token_type = TokenType.NUMBER;
            while (isDigitDot(expr_c)) {
                token += expr_c;
                getChar();
            }

            // check for scientific notation like "2.3e-4" or "1.23e50"
            if (expr_c == 'e' || expr_c == 'E') {
                token += expr_c;
                getChar();

                if (expr_c == '+' || expr_c == '-') {
                    token += expr_c;
                    getChar();
                }

                while (isDigit(expr_c)) {
                    token += expr_c;
                    getChar();
                }
            }

            return;
        }

        // check for variables or functions
        if (isAlpha(expr_c)) {
            while (isAlpha(expr_c) || isDigit(expr_c)) {
                token += expr_c;
                getChar();
            }

            // skip whitespaces
            while (isWhiteSpace(expr_c)) // space or tab
            {
                getChar();
            }

            // check the next non-whitespace character
            if (expr_c == '(') {
                token_type = TokenType.FUNCTION;
            } else {
                token_type = TokenType.VARIABLE;
            }

            return;
        }

        if (isComma(expr_c)) {
            token_type = TokenType.MULTI_PARAM;
            token += expr_c;
            getChar();
            return;
        }

        // something unknown is found, wrong characters -> a syntax ParseException
        token_type = TokenType.UNKNOWN;
        while (expr_c != '\0') {
            token += expr_c;
            getChar();
        }

        throw new ParseException(row(), col(), 1, token);
    }

    void getFirstChar() {
        expr_pos = 0;
        if (expr_pos < express_str.length()) {
            expr_c = express_str.charAt(expr_pos);
        } else {
            expr_c = '\0';
        }
    }

    boolean isWhiteSpace(final char c) {
        return c == 32 || c == 9; // space or tab
    }

    void getChar() {
        expr_pos++;
        if (expr_pos < express_str.length()) {
            expr_c = express_str.charAt(expr_pos);
        } else {
            expr_c = '\0';
        }
    }

    boolean isDelimeter(final char c) {
        return "&|<>=+/*%^!".indexOf(c) != -1;
    }

    boolean isDigitDot(final char c) {
        return "0123456789.".indexOf(c) != -1;
    }

    boolean isDigit(final char c) {
        return "0123456789".indexOf(c) != -1;
    }

    boolean isAlpha(final char c) {
        char cUpper = Character.toUpperCase(c);
        return "ABCDEFGHIJKLMNOPQRSTUVWXYZ_".indexOf(cUpper) != -1;
    }

    boolean isComma(final char c) {
        return c == ',';
    }

    int row() {
        return -1;
    }

    int col() {
        return expr_pos - token.length() + 1;
    }

    Express parse_level0() throws ParseException {
        if (token_type == TokenType.VARIABLE) {
            // skip whitespaces
            while (isWhiteSpace(expr_c)) { // space or tab
                getChar();
            }

            // check the next non-whitespace character
            if (expr_c == '=') {
                String var_name = token;

                // get the token '='
                getToken();

                // assignment
                getToken();
                Express ans = parse_level1();

                // check whether the token is a legal name
                if (isLegalVariableName(var_name)) {
//					this.evaluator.addVar(var_name.toUpperCase(), this.evaluator.exval(ans);//TODO
                } else {
                    throw new ParseException(row(), col(), 300);
                }
                return ans;
            }
        }

        return parse_level1();
    }

    boolean isLegalVariableName(String name) {
        String nameUpper = name.toUpperCase();
        if (nameUpper.equals("E"))
            return false;
        if (nameUpper.equals("PI"))
            return false;

        return true;
    }

    /**
     * ternary
     */
    Express parse_level1() throws ParseException {
        Express ans = parse_level2();

        if(token_type == TokenType.TERNARY_CON) {// '?'
            getToken();
            Express ternaryCon = ans;
            ans = parse_level2();
            assert(token_type == TokenType.TERNARY_BR); // ':'
            getToken();
            ans = new OpExpr(OpContext.build(Operator.OP.TERNARY), ternaryCon, ans, parse_level2());
        }
        return ans;
    }

    Express parse_level2() throws ParseException {
        Express ans = parse_level3();

        OP op_id = get_operator_id(token);
        while (op_id == OP.AND || op_id == OP.OR || op_id == OP.XOR) {
            getToken();
            ans = new OpExpr(OpContext.build(op_id), ans, parse_level3());
            op_id = get_operator_id(token);
        }

        return ans;
    }

    /**
     * logic and / or
     */
    Express parse_level3() throws ParseException {
        Express ans = parse_level4();

        OP op_id = get_operator_id(token);
        while(op_id == OP.LOGICAND || op_id == OP.LOGICOR) {
            getToken();
            ans = new OpExpr(OpContext.build(op_id), ans, parse_level4());
            op_id = get_operator_id(token);
        }
        return ans;
    }

    /**
     * conditional operators
     */
    Express parse_level4() throws ParseException {
        Express ans = parse_level5();

        OP op_id = get_operator_id(token);
        while (op_id == OP.EQUAL || op_id == OP.UNEQUAL || op_id == OP.SMALLER
                || op_id == OP.LARGER || op_id == OP.SMALLEREQ || op_id == OP.LARGEREQ) {
            getToken();
            ans = new OpExpr(OpContext.build(op_id), ans, parse_level5());
            op_id = get_operator_id(token);
        }
        return ans;
    }

    /**
     * logic not
     */
    Express parse_level5() throws ParseException {
        Express ans;
        OP op_id = get_operator_id(token);
        if (op_id != OP.LOGICNOT) {
            ans = parse_level6();
        } else {
            do {
                getToken();
                ans = new OpExpr(OpContext.build(op_id), parse_level6());
                op_id = get_operator_id(token);
            } while (op_id == OP.FACTORIAL);
        }
        return ans;
    }

    /**
     * bitshift
     */
    Express parse_level6() throws ParseException {
        Express ans = parse_level7();

        OP op_id = get_operator_id(token);
        while (op_id == OP.BITSHIFTLEFT || op_id == OP.BITSHIFTRIGHT) {
            getToken();
            ans = new OpExpr(OpContext.build(op_id), ans, parse_level7());
            op_id = get_operator_id(token);
        }
        return ans;
    }

    /**
     * add or subtract
     */
    Express parse_level7() throws ParseException {
        Express ans = parse_level8();

        OP op_id = get_operator_id(token);
        while (op_id == OP.PLUS || op_id == OP.MINUS) {
            getToken();
            ans = new OpExpr(OpContext.build(op_id), ans, parse_level8());
            op_id = get_operator_id(token);
        }
        return ans;
    }

    /**
     * multiply, divide, modulus, xor
     */
    Express parse_level8() throws ParseException {
        Express ans = parse_level9();

        OP op_id = get_operator_id(token);
        while (op_id == OP.MULTIPLY || op_id == OP.DIVIDE || op_id == OP.MODULUS) {
            getToken();
            ans = new OpExpr(OpContext.build(op_id), ans, parse_level9());
            op_id = get_operator_id(token);
        }
        return ans;
    }

    /**
     * power
     */
    Express parse_level9() throws ParseException {
        Express ans = parse_level10();

        OP op_id = get_operator_id(token);
        while (op_id == OP.POW) {
            getToken();
            ans = new OpExpr(OpContext.build(op_id), ans, parse_level10());
            op_id = get_operator_id(token);
        }

        return ans;
    }

    /**
     * Factorial
     */
    Express parse_level10() throws ParseException {
        Express ans = parse_level11();
        OP op_id = get_operator_id(token);
        while (op_id == OP.FACTORIAL) {
            getToken();
            // factorial does not need a value right from the
            // operator, so zero is filled in.
            ans = new OpExpr(OpContext.build(op_id), ans);
            op_id = get_operator_id(token);
        }
        return ans;
    }

    /**
     * Unary minus
     */
    Express parse_level11() throws ParseException {
        Express ans;
        OP op_id = get_operator_id(token);
        if (op_id == OP.MINUS) {
            getToken();
            ans = new NegativeExpr(parse_level12());
        } else {
            ans = parse_level12();
        }
        return ans;
    }

    /**
     * functions
     */
    Express parse_level12() throws ParseException {
        Express ans;
        if (token_type == TokenType.FUNCTION) {
            String fn_name = token;
            getToken();
            ans = new OpExpr(OpContext.build(fn_name), parse_functionVars());
        } else {
            ans = parse_level13();
        }

        return ans;
    }

    Express parse_level13() throws ParseException {
        // check if it is a parenthesized expression
        if (token_type == TokenType.DELIMETER) {
            if (token.equals("(")) {
                getToken();
                Express ans = parse_level1();

                if (token_type != TokenType.DELIMETER || !token.equals(")")) {
                    throw new ParseException(row(), col(), 3);
                }

                getToken();
                return new BracketExpr(ans);
            }
        }

        // if not parenthesized then the expression is a value
        return parse_number();
    }

    Express parse_number() throws ParseException {
        Express ans = null;
        switch (token_type) {
            case NUMBER:
                // this is a number
                ans = new ConstExpr(Double.parseDouble(token));
                getToken();
                break;
            case VARIABLE:
                if(ConstExpr.isConst(token)) {
                    // this is a constant
                    ans = ConstExpr.buildByName(token);
                } else {
                    // this is a variable
                    ans = new VarExpr(token);
                }
                getToken();
                break;

            default:
                // syntax ParseException or unexpected end of expression
                if (token.length() == 0) {
                    throw new ParseException(row(), col(), 6);
                } else {
                    throw new ParseException(row(), col(), 7);
                }
        }

        return ans;
    }

    Express[] parse_functionVars() throws ParseException {
        if (token.equals("(")) {
            getToken();
            List<Express> nodes = new ArrayList<Express>();

            nodes.add(parse_level1());

            while(token_type == TokenType.MULTI_PARAM) {
                getToken();
                nodes.add(parse_level1());
            }

            if (token_type != TokenType.DELIMETER || !token.equals(")")) {
                throw new ParseException(row(), col(), 3);
            }

            getToken();
            return nodes.toArray(new Express[nodes.size()]);
        }
        //can not be here, may throw an exception.
        return null;
    }

    OP get_operator_id(final String op_name) {
        // level 1
        if (op_name.equals("?")) { // con ? br1 : br2
            return OP.TERNARY;
        }

        // level 2
        if (op_name.equals("&")) {
            return OP.AND;
        }
        if (op_name.equals("|")) {
            return OP.OR;
        }
        //OP.XOR

        // level 3
        if (op_name.equals("&&")) {
            return OP.LOGICAND;
        }
        if (op_name.equals("||")) {
            return OP.LOGICOR;
        }

        // level 4
        if (op_name.equals("=") || op_name.equals("==")) {
            return OP.EQUAL;
        }
        if (op_name.equals("<>") || op_name.equals("!=")) {
            return OP.UNEQUAL;
        }
        if (op_name.equals("<")) {
            return OP.SMALLER;
        }
        if (op_name.equals(">")) {
            return OP.LARGER;
        }
        if (op_name.equals("<=")) {
            return OP.SMALLEREQ;
        }
        if (op_name.equals(">=")) {
            return OP.LARGEREQ;
        }

        // level 5
        if (op_name.equals("!")) {
            return OP.LOGICNOT;
        }

        // level 6
        if (op_name.equals("<<")) {
            return OP.BITSHIFTLEFT;
        }
        if (op_name.equals(">>")) {
            return OP.BITSHIFTRIGHT;
        }

        // level 7
        if (op_name.equals("+")) {
            return OP.PLUS;
        }
        if (op_name.equals("-")) {
            return OP.MINUS;
        }

        // level 8
        if (op_name.equals("*")) {
            return OP.MULTIPLY;
        }
        if (op_name.equals("/")) {
            return OP.DIVIDE;
        }
        if (op_name.equals("%")) {
            return OP.MODULUS;
        }

        // level 9
        if (op_name.equals("^")) {
            return OP.POW;
        }

        // level 10
        if (op_name.equals("!!")) {
            return OP.FACTORIAL;
        }
        return OP.UNKNOWN;
    }


}
