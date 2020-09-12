package tictactoe;

import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static boolean currentTurn = true;
    static String now;
    static String next;
    static String temp;
    static int[] winningPos =
            {0, 1, 2,
             3, 4, 5,
             6, 7, 8,
             0, 3, 6,
             1, 4, 7,
             2, 5, 8,
             0, 4, 8,
             2, 4, 6};

    public static char[][] createField(String field) {
        field = field.replaceAll("_", " ");
        char[][] arr = new char[3][3];
        int k = 0;
            for (int i = 0; i < arr.length; i++) {
                for (int j = 0; j < arr[0].length; j++) {
                    arr[i][j] = field.charAt(k);
                    k++;
                }
            }
        return arr;
    }

    public static void printField(char[][] field) {
        System.out.print("---------");
        for (int i = 0; i < 3; i++) {
            System.out.print("\n| ");
            for (int j = 0; j < 3; j++) {
                System.out.print(field[i][j] + " ");
            }
            System.out.print("|");
        }
        System.out.println("\n---------");
    }

    public static boolean isMoreSpace(char[] arr) {
        boolean isMore = false;
        for (char ch : arr) {
            if (ch == ' ') {
                isMore = true;
                break;
            }
        }
        return isMore;
    }

    public static String checkWin(char[] field, int oSum, int xSum) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 24;  i += 3) {
            if (getSum(field, winningPos[i], winningPos[i + 1], winningPos[i + 2], oSum)) {
                sb.append("O wins");
            }
            if (getSum(field, winningPos[i], winningPos[i + 1], winningPos[i + 2], xSum)) {
                sb.append("X wins");
            }
        }
        return sb.length() != 0 ? sb.toString() : "nan";
    }

    public static boolean getSum(char[] inputLine, int first, int second, int third, int resultsum) {
        int sum = inputLine[first] + inputLine[second]+ inputLine[third];
        return sum == resultsum;
    }

    public static char[] twoDimIntoOne(char[][] arr) {
        char[] line = new char[9];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                line[i * 3 + j] = arr[i][j];
            }
        }
        return line;
    }

    public static void enterCell(char[][] field) {
        boolean stop = false;
        String xI;
        String yI;
        int x;
        int y;
        do {
            System.out.print("Enter the coordinates: ");
            xI = sc.next();
            if (xI.matches("\\d")) {
                x = Integer.parseInt(xI);
            } else {
                System.out.println("You should enter numbers!");
                continue;
            }
            yI = sc.next();
            if (yI.matches("\\d")) {
                y = Integer.parseInt(yI);
            } else {
                System.out.println("You should enter numbers!");
                continue;
            }
            if (x > 3 | y > 3 | x <= 0 | y <= 0) {
                System.out.println("Coordinates should be from 1 to 3!");
                continue;
            } if (field[3-y][x-1] != ' ') {
                System.out.println("This cell is occupied! Choose another one!");
            } else  {
                field[3-y][x-1] = currentTurn ? 'X' : 'O';
                currentTurn = !currentTurn;
                stop = true;
            }
        } while (!stop);
    }

    public static void makeEasyMove(char[][] field) {
        boolean stop = false;
        Random rand = new Random();
        int x;
        int y;
        while(!stop) {
            x = rand.nextInt(3) + 1;
            y = rand.nextInt(3) + 1;
            if (field[3 - y][x - 1] == ' ') {
                field[3 - y][x - 1] = currentTurn ? 'X' : 'O';
                currentTurn = !currentTurn;
                stop = true;
            }
        }
    }

    public static void copyArr(char[][] from, char[][] to) {
        for (int i = 0; i < from.length; i++) {
            System.arraycopy(from[i], 0, to[i], 0, from[0].length);
        }
    }

    public static int[] oneMoveWin(char[][] field) {
        int x;
        int y;
        int[] winDot = new int[] {-1, -1};
        char[][] copy = new char[3][3];
        copyArr(field, copy);
        String res;

        for(int i = 0 ; i < field.length * field[0].length ; i++) {
             x = i % field.length + 1;
             y = i / field.length + 1;
            if (copy[3 - x][y - 1] == ' ') {


                copy[3 - x][y - 1] = currentTurn ? 'O' : 'X';
                res = checkWin(twoDimIntoOne(copy), 237, 264);
                if (!res.equals("nan")) {
                    winDot[0] = x;
                    winDot[1] = y;
                    copy[3 - x][y - 1] = ' ';
                    break;
                }

                copy[3 - x][y - 1] = currentTurn ? 'X' : 'O';
                res = checkWin(twoDimIntoOne(copy), 237, 264);
                if (!res.equals("nan")) {
                    winDot[0] = x;
                    winDot[1] = y;
                    copy[3 - x][y - 1] = ' ';
                    break;
                }


                copy[3 - x][y - 1] = ' ';
            }
        }
        return winDot;
    }

    public static void makeMediumMove(char[][] field) {
        int x;
        int y;
        int[] cell = oneMoveWin(field);
        if (cell[0] != -1 & cell[1] != -1) {
            x = cell[1];
            y = cell[0];
            field[3 - y][x - 1] = currentTurn ? 'X' : 'O';
            currentTurn = !currentTurn;
        } else {
            makeEasyMove(field);
        }
    }

    public static void makeHardMove(char[][] field) {

    }

    public static void doAction( char[][] field, String turn) {
        switch (turn) {
            case "user":
                enterCell(field);
                break;
            case "easy":
                System.out.println("Making move level \"easy\"");
                makeEasyMove(field);
                break;
            case "medium":
                System.out.println("Making move level \"medium\"");
                makeMediumMove(field);
                break;
            case "hard":
                System.out.println("Making move level \"hard\"");
                makeMediumMove(field);
                //makeHardMove(field);
                break;
        }
    }

    public static String getNextTurn() {
        temp = now;
        now = next;
        next = temp;
        return next;
    }

    public static void main(String[] args) {
        String input;
        Pattern goodParam = Pattern.compile("start(\\s(easy|user|medium|hard)){2}");
        String[] params;
        while (true) {
            System.out.print("Input command: ");
            input = sc.nextLine();
            if (input.equals("exit")) {
                break;
            } else {
                Matcher matcher = goodParam.matcher(input);
                if (!matcher.matches()) {
                    System.out.println("Bad parameters!");
                    continue;
                }
            }
            params = input.split(" ");
            now = params[1];
            next = params[2];
            char[][] gameField = createField("_________");
            String result;
            char[] line;
            printField(gameField);
            do {
                doAction(gameField, getNextTurn());
                printField(gameField);
                line = twoDimIntoOne(gameField);
                result = checkWin(line, 237, 264);
                if (!isMoreSpace(line) & result.length() < 6) {
                    result = "Draw";
                    break;
                }
            } while (result.equals("nan"));
            System.out.println(result);
        }
    }
}
