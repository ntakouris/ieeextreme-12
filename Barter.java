package com.company;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class Barter {
    static HashMap<String, Integer> rates;
    static HashMap<String, List<String>> convs;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        convs = new HashMap<>(n);
        rates = new HashMap<>(n);

        in.nextLine();

        for(int i = 0; i < n; i++){
            String line = in.nextLine();
            String[] parts = line.split(" ");


            
            String from = parts[0];
            String to = parts[1];
            int r = Integer.parseInt(parts[2]);

            if(convs.get(from) != null){
                List<String> v = convs.get(from);
                v.add(to);
                convs.put(from, v);
            }else{
                List<String> v = new ArrayList<>();
                v.add(to);
                convs.put(from, v);
            }

            rates.put(from.concat(to), r);
        }

        int q = in.nextInt();

        in.nextLine();

        for(int i = 0; i < q;i++){
            String line = in.nextLine();
            String[] parts = line.split(" ");

            String from = parts[0];
            String to = parts[1];

            if(from.equals(to)){
                System.out.println(1);
                continue;
            }

            //if not connected, -1
            boolean found = false;

            Deque<String> nodes = new ArrayDeque<>();
            nodes.add(from);

            HashMap<String, Boolean> visited = new HashMap<>();
            HashMap<String, String> parents = new HashMap<>();

            while(!nodes.isEmpty()){
                String node = nodes.pollFirst();

                if(visited.get(node) == null){
                    visited.put(node, true);
                }

                if(convs.get(node) == null){
                    continue;
                }

                HashMap<String, Boolean> finalVisited = visited;
                List<String> nonVisitedKids = convs.get(node).stream().filter(x -> finalVisited.get(x) == null).collect(Collectors.toList());

                for (String kid : nonVisitedKids) {
                    nodes.addFirst(kid);
                    parents.put(kid, node);
                    if(kid.equals(to)){
                        //Destination found
                        found = true;
                        break;
                    }
                }

                if(found){
                    break;
                }
            }

            if(!found){
                nodes = new ArrayDeque<>();
                nodes.add(to);

                visited = new HashMap<>();
                parents = new HashMap<>();

                while(!nodes.isEmpty()){
                    String node = nodes.pollFirst();

                    if(visited.get(node) == null){
                        visited.put(node, true);
                    }

                    if(convs.get(node) == null){
                        continue;
                    }

                    HashMap<String, Boolean> finalVisited1 = visited;
                    List<String> nonVisitedKids = convs.get(node).stream().filter(x -> finalVisited1.get(x) == null).collect(Collectors.toList());

                    for (String kid : nonVisitedKids) {
                        nodes.addFirst(kid);
                        parents.put(kid, node);
                        if(kid.equals(from)){
                            //Destination found
                            found = true;
                            break;
                        }
                    }

                    if(found){
                        break;
                    }
                }

                if (!found) {
                    System.out.println(-1);
                }else{
                    String curr = from;

                    BigInteger rate = BigInteger.valueOf(0);

                    while((!curr.equals(to))){
                        String prev = parents.get(curr);

                        int r = rates.get(prev.concat(curr));

                        if(rate.equals(BigInteger.ZERO)){
                            rate = BigInteger.valueOf(r);
                        }else{
                            rate = rate.multiply(BigInteger.valueOf(r)).modInverse(BigInteger.valueOf(998244353));
                            //rate %= r % 998244353;
                        }

                        curr = prev;
                    }

                    System.out.println(rate);
                }
                continue;
            }else{
                String curr = to;

                BigInteger rate = BigInteger.valueOf(0);

                while((!curr.equals(from))){
                    String prev = parents.get(curr);

                    int r = rates.get(prev.concat(curr));

                    if(rate.equals(BigInteger.ZERO)){
                        rate = BigInteger.valueOf(r);
                    }else{
                        rate = rate.multiply(BigInteger.valueOf(r)).mod(BigInteger.valueOf(998244353));
                        //rate %= r % 998244353;
                    }

                    curr = prev;
                }

                System.out.println(rate);
            }
        }
    }
}
