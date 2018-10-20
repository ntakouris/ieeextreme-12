package com.company;

// Don't place your source in a package
import java.util.*;
        import java.lang.*;
        import java.io.*;

class Troll {
    public static void main (String[] args) throws java.lang.Exception {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();

        boolean[] b = new boolean[n];

        for(int i = 0; i < b.length; i++){
            b[i] = false;
        }

        System.out.println("Q " + q(b));
        System.out.flush();

        int c = in.nextInt();
        in.nextLine();

        for(int i = 0; i < n; i++){
            b[i] = true;

            System.out.println("Q " + q(b));
            System.out.flush();

            int cn = in.nextInt();
            in.nextLine();

            if(cn == n){
                break;
            }

            if(cn <= c){
                b[i] = !b[i];
            }

            c = cn;
        }

        System.out.println("A " + q(b));
        System.out.flush();

    }

    static String q(boolean[] b){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < b.length; i++){
            sb.append(b[i] ? "1 " : "0 ");
        }

        return sb.substring(0, sb.length() - 1);
    }

}