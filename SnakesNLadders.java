package com.company;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class SnakesNLadders {
    public static void main(String[] args) {
        new SnakesNLadders().run();
    }

    public void run() {
        Scanner in = new Scanner(System.in);
        int bsize = in.nextInt();

        bsize+=2;

        in.nextLine();
        int players = in.nextInt();
        in.nextLine();
        int snakes = in.nextInt();
        in.nextLine();

        HashMap<Tile, Tile> snakeMap = new HashMap<>(snakes);

        for (int i = 0; i < snakes; i+=2) {
            int x = in.nextInt();
            int y = in.nextInt();

            Tile start = new Tile(x, y);

            x = in.nextInt();
            y = in.nextInt();

            Tile end = new Tile(x, y);

            snakeMap.put(start, end);
        }

        in.nextLine();
        int ladders = in.nextInt();
        in.nextLine();

        HashMap<Tile, Tile> ladderMap = new HashMap<>(snakes);


        for (int i = 0; i < ladders; i+=2) {
            int x = in.nextInt();
            int y = in.nextInt();

            Tile start = new Tile(x, y);

            x = in.nextInt();
            y = in.nextInt();

            Tile end = new Tile(x, y);

            ladderMap.put(start, end);
        }

        in.nextLine();
        int rolls = in.nextInt();
        in.nextLine();

        Integer[] pos = new Integer[players];

        for (int k = 0; k < pos.length; k++) {
            pos[k] = 0;
        }

        HashSet<Integer> winners = new HashSet<>();

        for (int i = 0; i < rolls; i++) {
            int a = in.nextInt();
            int b = in.nextInt();

            int p = i % players;

            if (winners.contains(p)) {
                continue;
            }

            int sum = a + b;

            int newPos = pos[p] + sum;


            if(newPos >= bsize + 2){
                winners.add(p);
                continue;
            }

            for(;;){
                int y = (newPos / bsize) + 1;

                int x = y % 2 == 0 ? bsize - newPos % 5 : newPos % 5;

                Tile curTile = new Tile(x,y);

                Tile snakeEnd = snakeMap.get(curTile);
                if (snakeEnd != null) {

                    newPos = snakeEnd.y % 2 == 0 ?
                            (snakeEnd.y - 1) * bsize + (bsize - snakeEnd.x + 1) :
                            (snakeEnd.y - 1) * bsize + (snakeEnd.x);

                    continue;
                }

                Tile ladderEnd = ladderMap.get(curTile);

                if (ladderEnd == null) {
                    break;
                }


                newPos = ladderEnd.y % 2 == 0 ?
                        (ladderEnd.y - 1) * bsize + (bsize - ladderEnd.x + 1) :
                        (ladderEnd.y - 1) * bsize + (ladderEnd.x);            }

            pos[p] = newPos;

            if(pos[p] >= bsize + 2){
                winners.add(p);
            }
        }

        for (int i = 0; i < players; i++) {
            if (winners.contains(i)) {
                System.out.println((i+1) + " winner");
            }else {

                int pp = pos[i];


                int y = (pp / bsize) + 1;

                int x = y % 2 == 0 ? bsize - pp % 5 : pp % 5;

                System.out.println((i+1) + " " + x + " " + y);
            }
        }
    }

    class Tile{
        int x,y;

        public Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
