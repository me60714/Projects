import java.util.Scanner;

public class Gomoku {
    private static char white = '●';
    private static char black = '○';

    private char[][] board = new char[16][16];  // set the chessboard to 15*15
    private static Gomoku go = new Gomoku();    // add one chessboard

    // use ASCII to build the chessboard
    private Gomoku() {
        for(int i = 0; i < 16; i++) {        // rows
            for(int j = 0; j < 16; j++) {    // columns
                if (i == 0 && j == 0) {
                    board[i][j] = ' ';
                } else if (i == 0 && j <= 10) {           // first row, number part
                    board[i][j] = (char)(j + 47);
                } else if (i == 0 && j > 10 && j <= 15) { // first row, alphabet part
                    board[i][j] = (char)(j + 86);
                } else if (j == 0 && i <= 10) {           // first column, number part
                    board[i][j] = (char)(i + 47) ;
                } else if (j == 0 && i > 10 && i <= 15) { // first column, alphabet part
                    board[i][j] = (char)(i + 86);
                } else {
                    board[i][j] = '+';
                }
            }
        }
        drawBoard(true);
    }


    public static Gomoku getGomoku() {
        return go;
    }

    public void drawBoard(boolean blackOrWhite) {
        System.out.println("\n\n--------------------- Gomoku/(Five in a Row) --------------------");
        System.out.println("---------------------- Author: Wei-Chih Kao ---------------------");
        System.out.println("---------------------- Date: June 29 2022  ----------------------");
        System.out.println();

        for(int i = 0; i < 16; i++){
                for(int j = 0; j < 16; j++) {
                    System.out.print(board[i][j]);
                    System.out.print("  ");
            }
                System.out.println();
        }

        if(blackOrWhite) {
            System.out.println();
            System.out.println("---------------------  Black chess's turn  ---------------------");
        } else {
            System.out.println();
            System.out.println("---------------------  White Chess's turn  ---------------------");
        }
    }

    // determine if the stone is out of the board or not
    public boolean outOfTheBoard(String n) {
        boolean outOrNot = true;
        String[] range = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e"};
        for(String s : range) {
            if(s.equals(n)) {
                outOrNot = false;
                break;
            }
        }
        return outOrNot;
    }

    public void playChess() {
        boolean blackFirst = true;
        Scanner sc = new Scanner(System.in);
        while(true) {

            System.out.print("PLease input the coordinate (row column, ex: 6 b): ");
            int row, column;
            while(true) {
                String r = sc.next();
                String c = sc.next();

                if (outOfTheBoard(r) || outOfTheBoard(c)) {
                    System.out.print("Out of the board, please place again: ");
                }else {
                    row = (int) r.charAt(0) - 47;
                    column = (int) c.charAt(0) - 47;

                    if (row >= 11) {
                        row = (int) r.charAt(0) - 86;
                    }
                    if (column >= 11) {
                        column = (int) c.charAt(0) - 86;
                    }

                    // determine that if there is already occupied by a stone
                    if (board[row][column] == white || board[row][column] == black) {
                        System.out.print("Already has a chess here, please place again: ");
                    }else {
                        break;
                    }
                }
            }
            // place the stone
            if(blackFirst) {
                board[row][column] = black;
            } else {
                board[row][column] = white;
            }

            boolean win = isWin(row, column);
            if(win && blackFirst){
                System.err.println("Black Win!");
                drawBoard(true);
                break;
            }else if(win && !blackFirst) {
                System.err.println("White Win!");
                drawBoard(false);
                break;
            }

            // redraw the board and change the player
            blackFirst = !blackFirst;
            drawBoard(blackFirst);
        }
    }

    public boolean isWin(int row, int column) {
        int highBound = 15;
        int lowBound = 1;

        /**
         * There four different possibilities that cause win for each stone
         * horizontal, vertical, right bevel and left bevel
         *
         *    *       *       *
         *      *     *     *
         *        *   *   *
         *          * * *
         *    * * * * * * * * *
         *          * * *
         *        *   *   *
         *      *     *     *
         *    *       *       *
         *
         */

        char[] horizontalWay = {'*', '*', '*', '*', '*', '*', '*', '*', '*'};
        char[] verticalWay = {'*', '*', '*', '*', '*', '*', '*', '*', '*'};
        char[] leftBevelWay = {'*', '*', '*', '*', '*', '*', '*', '*', '*'};
        char[] rightBevelWay = {'*', '*', '*', '*', '*', '*', '*', '*', '*'};

        // let the current stone be in the middle of these arrays
        rightBevelWay[4] = leftBevelWay[4] = verticalWay[4] = horizontalWay[4] = board[row][column];

        // to place the surrounding stones to the arrays
        for (int i = 1; i <= 4; i++) {
            // horizontal way
            if (column - i >= lowBound) {
                horizontalWay[4 - i] = board[row][column - i];
            }
            if (column + i <= highBound) {
                horizontalWay[4 + i] = board[row][column + i];
            }
            // vertical way
            if (row - i >= lowBound) {
                verticalWay[4 - i] = board[row - i][column];
            }
            if (row + i <= highBound) {
                verticalWay[4 + i] = board[row + i][column];
            }
            // left bevel way
            if (row - i >= lowBound && column - i >= lowBound) {
                leftBevelWay[4 + i] = board[row - i][column - i];
            }
            if (row + i <= highBound && column + i <= highBound) {
                leftBevelWay[4 - i] = board[row + i][column + i];
            }
            // right bevel way
            if (row - i >= lowBound && column + i <= highBound) {
                rightBevelWay[4 + i] = board[row - i][column + i];
            }
            if (column - i >= lowBound && row + i <= highBound) {
                rightBevelWay[4 - i] = board[row + i][column - i];
            }
        }

        boolean fiveInHor = fivePieces(horizontalWay, row, column);
        boolean fiveInVer = fivePieces(verticalWay, row, column);
        boolean fiveInLBW = fivePieces(leftBevelWay, row, column);
        boolean fiveInRBW = fivePieces(rightBevelWay, row, column);

        return(fiveInHor || fiveInVer || fiveInLBW || fiveInRBW);
    }

    // to see if there are five same continuous stones
    private boolean fivePieces(char[] array, int row, int column) {
        int count = 1;
        for(int i = 0; i < 5;) {
            if (array[i] == board[row][column]) {
                for (int j = i + 1; j < 9; j++) {
                    if(array[j] == array[j - 1]) {
                        count += 1;
                        if(count == 5) return true;
                    }else {
                        count = 1;
                        i = j;
                        break;
                    }
                }
            }
            i++;
        }
        return false;
    }
}

