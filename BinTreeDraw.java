package com.company;

import java.util.Scanner;

public class BinTreeDraw {


    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String infix = in.nextLine();
        String prefix = in.nextLine();

        new BinTreeDraw().printRec(infix, prefix, 0);
    }

    public void printRec(String infix, String prefix, int depth){
        char root = prefix.charAt(0);

        String[] infxs = prefix.split(String.valueOf(root));

        if (infxs[0].length() == 1) {
            //print root && padding
            printWithPadding(root, depth);
            ln();
            return;
        } else if (infxs[0].length() == 2) {
                //if right
            if (infix.charAt(0) == root) {
               //print padding
                printPadding(depth);
            }

            printRec(infxs[0], prefix.substring(1), depth+1);
            return;
        }

        char prefixSeparator = infxs[0].charAt(infxs[0].length() - 1);

        String[] prefixs = prefix.split(String.valueOf(prefixSeparator));

        printRec(infxs[1], prefixs[1], depth+1);

        //print root

        printWithPadding(root, depth);
        ln();

        printRec(infxs[0], prefixs[0], depth+1);
    }

    public void printWithPadding(char s, int padding){
        printPadding(padding);
        System.out.print(s);
    }

    private void printPadding(int padding) {
        StringBuilder sb = new StringBuilder();
        for (int p = 0; p < padding; p++) {
            sb.append(" ");
        }
        System.out.print(sb.toString());

    }

    public void ln() {
        System.out.println();
    }
}
