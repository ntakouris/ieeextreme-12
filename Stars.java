package com.company;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Stars {
    public static void main(String[] args) {
        new Stars().run();
    }

    public void run() {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();

        int[] des = new int[n];
        int[] s = new int[n];
        int[] f = new int[n];

        in.nextLine();
        for (int i = 0; i < n; i++) {
            s[i] = in.nextInt();
            f[i] = in.nextInt();
            des[i] = in.nextInt();
            in.nextLine();
        }

        List<Integer> stars = IntStream.range(0, n).boxed().sorted(Comparator.comparingInt(x -> s[x])).sorted(Comparator.comparingInt(x -> des[x])).collect(Collectors.toList());

        int[] md = new int[n];

        md[0] = des[0];

        for (int i = 1; i < n; i++) {
            int finalI = i;

            int newDes = des[i];

            Optional<Integer> latestNonConflicting = stars.stream().filter(x -> s[x] > f[finalI]).findFirst();

            if (latestNonConflicting.isPresent()) {
                newDes += des[latestNonConflicting.get()];
            }

            md[i] = Math.max(newDes, md[i]);
        }

        System.out.println(Arrays.stream(md).max().getAsInt());
    }
}
