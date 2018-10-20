package com.company;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws java.lang.Exception {

        new Main().run();
    }

    HashMap<String, Division> map = new HashMap<>();

    HashMap<String, Estimate> allEstimates = new HashMap<>();
    HashMap<String, Estimate> divEstimates = new HashMap<>();


    public void run() {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int q = in.nextInt();

        in.nextLine();

        for (int i = 0; i < n; i++) {
            String line = in.nextLine();

            String[] parts = line.split(" ");

            Division div = new Division(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
            map.put(div.name, div);
        }

        //n lines div name, n lines div parent cursize belowsize
        for (int i = 0; i < q; i++) {
            String line = in.nextLine();

            String[] parts = line.split(" ");

            String divName = parts[0];

            int type = Integer.parseInt(parts[1]);

            if (type == 1) {
                //If found, upper lower is same
                Division curr = map.get(divName);

                type1Query(curr);

                System.out.println(divEstimates.get(curr.name).min + " " + divEstimates.get(curr.name).max);
            } else {// is 2

            }
        }
    }

    void findEstimateBelow(Division div) {
        if (!div.isBelowSizeMissing()) {
            allEstimates.put(div.name, new Estimate(div.belowSize, div.belowSize));
            return;
        }

        List<Division> kids = map.values().stream().filter(x -> x.parentName.equals(div.name)).collect(Collectors.toList());

        if (kids.size() == 0) {
            //find allEstimates for near
            if (!div.isSizeMissing()) {
                allEstimates.put(div.name, new Estimate(div.curSize, div.curSize));
                return;

            } else {
                type1Query(div);
                return;
            }
        }

        kids.forEach(this::findEstimateBelow);
        Stream<Estimate> kidEst = kids.stream().map(x -> allEstimates.get(x.name));

        int lower = kidEst.mapToInt(x -> x.min).sum();
        int upper = kidEst.mapToInt(x -> x.max).sum();

        allEstimates.put(div.name, new Estimate(lower, upper));
    }

    void type1Query(Division curr) {
        if (!curr.isCurSizeMissing()) {
            //System.out.println(curr.curSize);
            divEstimates.put(curr.name, new Estimate(curr.belowSize, curr.belowSize));
        } else {
            //If not upper and lower is calculated from next non-empty in hierarchy

            //for nodes of same parent except ours
            List<Division> sameParent = map.values().stream().filter(x -> x.parentName.equals(curr.parentName)
                    && !x.parentName.equals(curr.name)).collect(Collectors.toList());

            for (Division div : sameParent) {
                if (div.isSizeMissing()) {
                    findEstimateBelow(div);
                }
            }

            int sizeNotMissingSame = sameParent.stream().filter(x -> !x.isSizeMissing()).mapToInt(x -> x.belowSize).sum();

            Stream<Estimate> est = sameParent.stream().map(x -> allEstimates.get(x.name));

            int sameParentLower = est.mapToInt(x -> x.min).sum();
            int sameParentUpper = est.mapToInt(x -> x.max).sum();

            Division parent = map.get(curr.parentName);

            //allEstimates for parent
            findEstimateUpper(parent);

            Estimate parentEst = allEstimates.get(curr.parentName);

            Estimate parentDivEstimate = divEstimates.get(curr.parentName);

            int parentAllUpper = parentEst.max;
            int parentAllLower = parentEst.min;

            int lowerOfSameParent = sizeNotMissingSame - sameParentLower;

            int upperOfSameParent = sizeNotMissingSame - sameParentUpper;

            int estimateUpper = parentAllUpper - parentDivEstimate.min - lowerOfSameParent;

            int estimateLower = parentAllLower - parentDivEstimate.max - upperOfSameParent;

            //allEstimates for kids
            List<Division> kids = map.values().stream().filter(x -> x.parentName.equals(curr.name)).collect(Collectors.toList());

            for (Division div : kids) {
                if (div.isSizeMissing()) {
                    findEstimateBelow(div);
                }
            }

            int sizeNotMissingKids = kids.stream().filter(x -> !x.isSizeMissing()).mapToInt(x -> x.belowSize).sum();

            Stream<Estimate> estKids = kids.stream().map(x -> allEstimates.get(x.name));

            int lowerKids = estKids.mapToInt(x -> x.min).sum();
            int upperKids = estKids.mapToInt(x -> x.max).sum();

            int finalEstimateUpper = estimateUpper - sizeNotMissingKids - lowerKids;
            int finaleEstimateLower = estimateLower - sizeNotMissingKids - upperKids;

            allEstimates.put(curr.name, new Estimate(finaleEstimateLower, finalEstimateUpper));
        }

    }

    void findEstimateUpper(Division div) {
        //estimates for levels of src already known

        //if no data
        if (!div.isSizeMissing()) {
            allEstimates.put(div.name, new Estimate(div.belowSize, div.belowSize));
            divEstimates.put(div.name, new Estimate(div.curSize, div.curSize));
            return;
        }

        Division parent = map.get(div.parentName);

        map.values().stream().filter(x -> x.parentName.equals(div.parentName)
                && !x.parentName.equals(div.name)).forEach(this::findEstimateBelow);
        findEstimateUpper(parent);

        Estimate allParentEstimate = allEstimates.get(div.parentName);

        List<Estimate> allKidEstimates = map.values().stream().filter(x -> x.parentName.equals(div.name)).map(x -> allEstimates.get(x.name)).collect(Collectors.toList());

        int lowerAllEstimate = allParentEstimate.min - allKidEstimates.stream().mapToInt(x -> x.max).sum();

        int upperAllEstimate = allParentEstimate.max - allKidEstimates.stream().mapToInt(x -> x.min).sum();

        allEstimates.put(div.name, new Estimate(lowerAllEstimate, upperAllEstimate));

        Estimate divParentEstimate = divEstimates.get(div.parentName);

        int divLowerEstimate = allParentEstimate.min - divParentEstimate.max - allKidEstimates.stream().mapToInt(x -> x.max).sum();

        int divUpperEstimate = allParentEstimate.max - divParentEstimate.min - allKidEstimates.stream().mapToInt(x -> x.min).sum();

        divEstimates.put(div.name, new Estimate(divLowerEstimate, divUpperEstimate));
    }

    public class Estimate {
        public int min, max;

        public Estimate(int min, int max) {
            this.min = min;
            this.max = max;
        }

        @Override
        public String toString() {
            return min + " " + max;
        }
    }

    public class Division {
        public String name, parentName;
        public int curSize, belowSize;

        public Division(String name, String parentName, int curSize, int belowSize) {
            this.name = name;
            this.parentName = parentName;
            this.curSize = curSize;
            this.belowSize = belowSize;
        }

        public boolean hasParent() {
            return !parentName.equals("NONE");
        }

        public boolean isCurSizeMissing() {
            return curSize == 0;
        }

        public boolean isBelowSizeMissing() {
            return belowSize == 0;
        }

        public boolean isSizeMissing() {
            return isCurSizeMissing() || isBelowSizeMissing();
        }
    }

}
