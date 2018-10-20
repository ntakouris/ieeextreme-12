package com.company;

import java.util.Scanner;
import java.util.Stack;

public class TweedledeeBrackets {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        String s = in.nextLine();

        //if valid
        if (s.length() >= 4 && s.length() % 4 != 0) {
            System.out.println("impossible");
            return;
        }

        int parenthesisCount = 0, bracketCount = 0;

        for (char c : s.toCharArray()) {
            if(c == '('){
                parenthesisCount++;
            }else if(c == ')'){
                parenthesisCount--;
            }

            if(c == '['){
                bracketCount++;
            }else if(c ==']'){
                bracketCount--;
            }
        }

        if (parenthesisCount != 0 || bracketCount != 0) {
            System.out.println("impossible");
            return;
        }

        if(s.charAt(0) == ')' || s.charAt(0) == ']'){
            System.out.println("impossible");
            return;
        }

        //split into the 2 sequences
        int length = s.length();

        StringBuilder sb = new StringBuilder();
        Stack<Character> s1 = new Stack<>();
        Stack<Character> s2 = new Stack<>();

        boolean[] mustPush2 = new boolean[s.length()];

        for (int i = 0; i < mustPush2.length; i++) {
            mustPush2[i] = false;
        }

        char[] chars = s.toCharArray();

        compute2(chars, mustPush2);

        boolean impos = false;
        for (int i = 0; i < chars.length; i++) {
            char curr = chars[i];

//            if(mustPush2[i] || s1.size() == length / 2){
//                sb.append("2 ");
//                s2.push(curr);
//                continue;
//            }

            //If s1 empty && s2 empty
            if (s1.isEmpty() && s2.isEmpty()) {
                sb.append("1 ");
                s1.push(curr);
                continue;
            }

            Stack<Character> s1red = reduce(s1);
            Stack<Character> s2red = reduce(s2);

            if(s1red.peek() == '(' && curr != ')' || s1red.peek() == '[' && curr != ']'){
                sb.append("1 ");
                s1.push(curr);
                continue;
            }

            if(s2red.peek() == '(' && curr != ')' || s2red.peek() == '[' && curr != ']'){
                sb.append("1 ");
                s2.push(curr);
                continue;
            }

            System.out.println("impossible");
            impos = true;
            break;
        }

        if(impos){
            return;
        }

        System.out.println(sb.subSequence(0, sb.length() - 2));
    }

    static Stack<Character> reduce(String chars){
        Stack<Character> accepts = new Stack<>();

        for (Character c : chars.toCharArray()) {
            if (accepts.isEmpty()) {
                accepts.push(c);
                continue;
            }

            if (accepts.peek().equals('[') && c == ']' || accepts.peek().equals('(') && c == ')') {
                accepts.pop();
            }
        }

        return accepts;
    }

    static Stack<Character> reduce(Stack<Character> chars){
        Stack<Character> accepts = new Stack<>();

        for (Character c : chars) {
            if (accepts.isEmpty()) {
                accepts.push(c);
                continue;
            }

            if (accepts.peek().equals('[') && c == ']' || accepts.peek().equals('(') && c == ')') {
                accepts.pop();
            }
        }

        return accepts;
    }

    static void compute2(char[] chars, boolean[] mustPush2) {
        Stack<Character> test = new Stack<>();

        for (int i = 0; i < chars.length; i++) {
            if(mustPush2[i]){
                continue;
            }

            if (test.isEmpty()) {
                test.push(chars[i]);
                continue;
            }

            if(test.size() > chars.length / 2 && test.peek() == '(' && chars[i] != ')' || test.peek() == '[' && chars[i] != ']') {
                char lookFor = chars[i] == ')' ? '(' : '[';

                for (int j = i; j > 0; j--) {
                    if (chars[j] == lookFor) {
                        mustPush2[j] = true;
                        mustPush2[i] = true;
                    }
                }
            }
        }
    }

    static boolean isValidSequence(String seq){
        Stack<Character> accepts = new Stack<>();

        for (Character c : seq.toCharArray()) {
            if (accepts.isEmpty()) {
                accepts.push(c);
                continue;
            }

            if (accepts.peek().equals('[') && c == ']' || accepts.peek().equals('(') && c == ')') {
                accepts.pop();
            }
        }

        return accepts.isEmpty();
    }
}
