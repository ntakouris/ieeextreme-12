package com.company;

import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class PIEEEXman {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        int n = in.nextInt();
        int m = in.nextInt();

        in.nextLine();

        char[][] tiles = new char[2*n][2*m];

        int myX = 0, myY = 0;
        int bobX = 0, bobY = 0;
        for(int i = 0; i < 2*n; i++){
            String line = in.nextLine();

            char[] tile = line.toCharArray();


            for (int k = 0; k < tile.length; k ++) {
                char c = tile[k];
                if(c == '1'){
                    myX = i;
                    myY = k;
                    tile[k] = '.';
                }else if(c == '2'){
                    bobX = i;
                    bobY = k;
                    tile[k] = '.';
                }
            }

            tiles[i] = tile;
        }


        //i go to the cherry where bob is furthest from
        Stack<Pair<Integer, Integer>> tileStack = new Stack<>();
        HashMap<Pair<Integer, Integer>, Integer> myDist = new HashMap<>();
        HashMap<Pair<Integer, Integer>, Integer> myCherryDist = new HashMap<>();

        HashMap<Pair<Integer, Integer>, Integer> bobDist = new HashMap<>();
        HashMap<Pair<Integer, Integer>, Integer> bobCherryDist = new HashMap<>();

        tileStack.push(new Pair<>(myX, myY));
        myDist.put(new Pair<>(myX, myY), 0);

        while(!tileStack.isEmpty()){
            Pair<Integer, Integer> tile = tileStack.peek();

            int dist = myDist.get(tile);
            dist++;

            List<Pair<Integer, Integer>> neigh = new ArrayList<>();

            int cx = tile.getKey();
            int cy = tile.getValue();

            if(cx > 0 && tiles[cx+1][cy] != '#'  && cx+1 != bobX && cy != bobY){
                //R
                neigh.add(new Pair<>(cx+1,cy));
            }

            if(cx < 2*n && tiles[cx-1][cy] != '#'  && cx-1 != bobX && cy != bobY){
                //L
                neigh.add(new Pair<>(cx-1,cy));
            }

            if(cy > 0 && tiles[cx][cy+1] != '#' && cx != bobX && cy+1 != bobY){
                //D
                neigh.add(new Pair<>(cx,cy + 1));
            }

            if(cx < 2*m && tiles[cx][cy-1] != '#' && cx != bobX && cy-1 != bobY){
                //L
                neigh.add(new Pair<>(cx,cy - 1));
            }

            for(Pair<Integer, Integer> p : neigh.stream().filter(x -> myDist.get(x) != null).collect(Collectors.toSet())){
                myDist.put(p, dist);

                if(tiles[p.getKey()][p.getValue()] == '@'){
                    myCherryDist.put(p, dist);
                }

                tileStack.push(p);
            }

            tileStack.pop();
        }

        tileStack.clear();
        tileStack.push(new Pair<>(bobX, bobY));
        bobDist.put(new Pair<>(bobX, bobY), 0);

        while(!tileStack.isEmpty()){
            Pair<Integer, Integer> tile = tileStack.peek();

            int dist = bobDist.get(tile);
            dist++;

            List<Pair<Integer, Integer>> neigh = new ArrayList<>();

            int cx = tile.getKey();
            int cy = tile.getValue();

            if(cx > 0 && tiles[cx+1][cy] != '#'  && cx+1 != bobX && cy != bobY){
                //R
                neigh.add(new Pair<>(cx+1,cy));
            }

            if(cx < 2*n && tiles[cx-1][cy] != '#'  && cx-1 != bobX && cy != bobY){
                //L
                neigh.add(new Pair<>(cx-1,cy));
            }

            if(cy > 0 && tiles[cx][cy+1] != '#' && cx != bobX && cy+1 != bobY){
                //D
                neigh.add(new Pair<>(cx,cy + 1));
            }

            if(cx < 2*m && tiles[cx][cy-1] != '#' && cx != bobX && cy-1 != bobY){
                //L
                neigh.add(new Pair<>(cx,cy - 1));
            }

            for(Pair<Integer, Integer> p : neigh.stream().filter(x -> bobDist.get(x) != null).collect(Collectors.toSet())){
                bobDist.put(p, dist);

                if(tiles[p.getKey()][p.getValue()] == '@'){
                    bobCherryDist.put(p, dist);
                }

                tileStack.push(p);
            }

            tileStack.pop();
        }

        boolean solution = false;

        for(Pair<Integer, Integer> myCherry : myCherryDist.keySet()){
            int bd = bobCherryDist.get(myCherry);
            int md = myCherryDist.get(myCherry);

            if(md < bd){
                //move toward this cherry
                char move = 'W';

                int cx = myX;
                int cy = myY;

                List<Pair<Integer, Integer>> neigh = new ArrayList<>();
                int[] scores = new int[4];

                if(cx > 0 && tiles[cx+1][cy] != '#'  && cx+1 != bobX && cy != bobY){
                    //R
                    neigh.add(new Pair<>(cx+1,cy));
                }

                if(cx < 2*n && tiles[cx-1][cy] != '#'  && cx-1 != bobX && cy != bobY){
                    //L
                    neigh.add(new Pair<>(cx-1,cy));
                }

                if(cy > 0 && tiles[cx][cy+1] != '#' && cx != bobX && cy+1 != bobY){
                    //D
                    neigh.add(new Pair<>(cx,cy + 1));
                }

                if(cx < 2*m && tiles[cx][cy-1] != '#' && cx != bobX && cy-1 != bobY){
                    //L
                    neigh.add(new Pair<>(cx,cy - 1));
                }

                for(int i = 0; i < neigh.size(); i++){
                    Stack<Pair<Integer, Integer>> stack = new Stack<>();
                    HashMap<Pair<Integer, Integer>, Integer> dist = new HashMap<>();

                    Pair<Integer, Integer> p = neigh.get(i);

                    stack.push(p);
                    dist.put(p, 0);

                    while(!stack.isEmpty()){
                        int d = dist.get(p);

                        d++;

                        List<Pair<Integer, Integer>> ns = new ArrayList<>();

                        int ccx = p.getKey();
                        int ccy = p.getValue();

                        if(ccx > 0 && tiles[ccx+1][ccy] != '#'  && ccx+1 != bobX && ccy != bobY){
                            //R
                            neigh.add(new Pair<>(ccx+1,ccy));
                        }

                        if(ccx < 2*n && tiles[ccx-1][ccy] != '#'  && ccx-1 != bobX && cy != bobY){
                            //L
                            neigh.add(new Pair<>(ccx-1,cy));
                        }

                        if(ccy > 0 && tiles[ccx][ccy+1] != '#' && ccx != bobX && ccy+1 != bobY){
                            //D
                            neigh.add(new Pair<>(ccx,cy + 1));
                        }

                        if(ccx < 2*m && tiles[ccx][ccy-1] != '#' && ccx != bobX && ccy-1 != bobY){
                            //L
                            neigh.add(new Pair<>(ccx,cy - 1));
                        }

                        boolean reached = false;

                        for(Pair<Integer, Integer> np : ns.stream().filter(x -> dist.get(x) != null).collect(Collectors.toSet())){
                            dist.put(np, d);

                            if(tiles[p.getKey()][p.getValue()] == '@'){
                                bobCherryDist.put(np, d);
                            }

                            if (p.getKey().equals(myCherry.getKey()) && p.getValue().equals(myCherry.getValue())) {
                                scores[i] = d;
                                reached = true;
                                break;
                            }

                            tileStack.push(p);
                        }

                        if (reached) {
                            break;
                        }

                        stack.pop();
                    }

                }

                //compare scores
                for (int i = 0; i < scores.length; i++) {
                    if(scores[i] == myCherryDist.get(myCherry)){
                        Pair<Integer, Integer> target = neigh.get(i);
                        int newX = target.getKey();
                        int newY = target.getValue();

                        if (newX - myX == 1) {
                            move = 'R';
                        } else if (myX - newX == -1) {
                            move = 'L';
                        }else if(newY - myY == 1){
                            move = 'D';
                        }else if(newY - myY == -1){
                            move = 'U';
                        }
                    }
                }

                System.out.println(move);
                System.out.flush();

                tiles[myX][myY] = '.';

                solution = true;
                break;
            }
        }

        if(!solution){
            //wait
            System.out.println('W');
            System.out.flush();
        }

        String bobMove = in.next();

        if(bobMove.length() == 2){
            System.exit(0);
        }

        char move = bobMove.charAt(0);

        if (move == 'U') {
            bobY++;
        }else if(move == 'D'){
            bobY--;
        }else if(move == 'L'){
            bobX--;
        }else if(move == 'R'){
            bobX++;
        }

        tiles[bobX][bobY] = '.';

    }
}
