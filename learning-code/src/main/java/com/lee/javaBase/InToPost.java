package com.lee.javaBase;

import java.util.Stack;

/**
 * 模拟中缀（infix）转 后缀（suffix）并根据后缀计算结果
 * */
public class InToPost {
    private Stack<Character> opStack;
    private Stack<Character> outStack;
    private String input;

    public InToPost(String in) {
        input = in;
        opStack = new Stack<Character>();
        outStack = new Stack<Character>();
    }

    public Stack<Character> doTrans() { //其他类型自行转换
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            switch (ch) {
                case '+':
                case '-':
                    operationOpStack(ch, 1);
                    break;
                case '*':
                case '/':
                    operationOpStack(ch, 2);
                    break;
                case '(':
                    opStack.push(ch);
                    break;
                case ')':
                    operationParen();
                    break;
                default:
                    outStack.push(ch);
                    break;
            }
        }
        System.out.println("opStack is " + opStack + '\n');
        System.out.println("outStack is " + outStack + '\n');
        while (!opStack.isEmpty()) {
            outStack.push(opStack.pop());
        }
        return outStack;
    }

    public void operationOpStack(char opThis, int prec1) {//运算符栈操作  "1+2*4/5-7+3/6"
        while (!opStack.isEmpty()) {
            char opTop = opStack.pop();
            if (opTop == '(') {
                opStack.push(opTop);
            } else {
                int prec2;
                if (opTop == '+' || opTop == '-')
                    prec2 = 1;
                else
                    prec2 = 2;
                if (prec2 < prec1) {
                    opStack.push(opTop);
                    break;
                } else
                    outStack.push(opTop);//1 2 4 * 5 / + 7 - 3 6 / +
            }
        }
        opStack.push(opThis);
    }

    public void operationParen() {
        while (!opStack.isEmpty()) {
            char c = opStack.pop();
            if (c == '(')
                break;
            else
                outStack.push(c);
        }
    }

    public double clac(Stack<Character> suffixStack) {
        Stack<Character> cache = new Stack<>();
        while (!suffixStack.isEmpty()) {
            cache.push(suffixStack.pop());
        }
        Stack<Integer> result = new Stack<>();
        while (!cache.isEmpty()) {
            Character c = cache.pop();
            switch (c) {
                case '+':
                    result.push(result.pop() + result.pop());
                    break;
                case '-':
                    int a = result.pop();
                    int b = result.pop();
                    result.push(b - a);
                    break;
                case '*':
                    result.push(result.pop() * result.pop());
                    break;
                case '/':
                    int e = result.pop();
                    int d = result.pop();
                    result.push(d / e);
                    break;
                default:
                    result.push(Integer.parseInt(String.valueOf(c)));
                    break;
            }
            System.out.println(result);
        }
        return result.peek();
    }

    public static void main(String[] args) {
        String input = "1+2*4-7+9/3-2/2";
        InToPost theTrans = new InToPost(input);
        Stack<Character> output = theTrans.doTrans();
        System.out.println("Postfix is " + output + '\n');
        System.out.println(theTrans.clac(output));
    }
}
