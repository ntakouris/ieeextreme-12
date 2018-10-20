package com.company;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class BearSums {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int tests = in.nextInt();

        in.nextLine();

        for (int tt = 0; tt < tests; tt++) {

            int s = in.nextInt();
            int e = in.nextInt();

            if (e == 0) {
                in.nextLine();
                System.out.println("!OK");
                continue;
            }

            in.nextLine();

            int[] nums = new int[e];

            HashMap<Integer, Integer> numtable = new HashMap<>(e);

            for (int i = 0; i < e; i++) {
                nums[i] = in.nextInt();
                numtable.putIfAbsent(nums[i], 0);
                int count = numtable.get(nums[i]);
                count++;

                numtable.put(nums[i], count);
            }

            if(nums.length == 1){
                int val = nums[0];

                if (val != s) {
                    System.out.println("!OK");
                    continue;
                }else if(val == s){
                    System.out.println(val + " " + val);
                    continue;
                }
            }

            boolean found = false;

            HashMap<Integer, Integer> candidates = new HashMap<>();

            for (int i = 0; i < nums.length; i++) {
                int val = nums[i];
                int complement = s - val;

                Integer content = numtable.get(val);

                if (complement == val && content < 2) {
                    continue;
                }

                if (numtable.containsKey(complement)) {
                    candidates.put(val, i);
                    found = true;
                }
            }

            if (found) {
                if (candidates.size() > 1) {
                    if(s % 2 == 0 && candidates.containsKey(s / 2)){
                        candidates.remove(s / 2);
                    }
                }

                int completeCount = Integer.MAX_VALUE;
                int tar = 0;

                for (Integer key : candidates.keySet()) {
                    int xi = candidates.get(key);
                    int yi = candidates.get(s - key);

                    int i = Math.max(xi, yi);

                    if(i < completeCount){
                        completeCount = i;
                        tar = key;
                    }
                }

                int compl = s - tar;

                int first = tar > compl ? compl : tar;
                int second = tar > compl ? tar : compl;

                System.out.println(first + " " + second);
                continue;
            }

            System.out.println("!OK");
        }
    }
}
