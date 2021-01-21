package tictactoe;

import java.util.Scanner;

public class Main {

    private static final String GAME_NOT_FINISHED = "Game not finished";

    /* Table of the game where each cell is represented by its index. */
    static int[] table;

    private static int emptyCellsCount;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String playerX, playerO;
        while (true) {
            printIntro();
            String[] startGameParams = sc.nextLine().split(" ");

            if (startGameParams[0].equals("exit")) {
                return;
            }

            /* A valid input format would be: start <playerX> <playerO> */
            if (startGameParams.length != 3 || !startGameParams[0].equals("start") ||
                    !validPlayerType(startGameParams[1]) || !validPlayerType(startGameParams[2])) {
                System.out.println("Bad parameters!");
            } else {
                // The input is valid.
                playerX = startGameParams[1];
                playerO = startGameParams[2];
                break;
            }
        }

        /* Start by printing an empty field. */
        table = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
        emptyCellsCount = 9;
        printTable(table);

        Player player1, player2;

        /* Determine who will be the X player and the O player. */
        if (playerX.equals("user")) {
            player1 = new HumanPlayer('X');
        } else {
            player1 = new ComputerPlayer(playerX, 'X');
        }

        if (playerO.equals("user")) {
            player2 = new HumanPlayer('O');
        } else {
            player2 = new ComputerPlayer(playerO, 'O');
        }

        /* Game state can be one of four possible values:
         * - Game not finished.
         * - Draw.
         * - X wins.
         * - O wins.
         * */
        while (true) {

            player1.makeMove();
            doAfterEachMove();

            if (getGameState().equals(GAME_NOT_FINISHED)) {
                player2.makeMove();
                doAfterEachMove();
            } else {
                // GAME OVER
                break;
            }
        }

        System.out.println(getGameState());
    }

    private static void printIntro() {
        System.out.println("\\************************************************\\" + "\n" +
                "> To start the game, type:" + "\n" +
                "start <playerX> <playerO>" + "\n" +
                "> Substitute <playerX>/<playerO> with any of the following:" + "\n" +
                "user -> for a human player." + "\n" +
                "hard/medium/easy -> for an AI player." + "\n\n" +
                "> For example: start user hard" + "\n" +
                "\\************************************************\\" + "\n" +
                "Input command: ");
    }

    private static void doAfterEachMove() {
        emptyCellsCount--;
        printTable(table);
    }

    private static boolean validPlayerType(String playerType) {
        return playerType.equals("user") || playerType.equals("hard") ||
                playerType.equals("medium") || playerType.equals("easy");
    }

    private static String getGameState() {
        /* Check whether there is at least one row or column or diagonal of Xs. */
        boolean atLeastOneRowOfX =
                (table[0] == 'X' && table[1] == 'X' && table[2] == 'X') ||
                        (table[3] == 'X' && table[4] == 'X' && table[5] == 'X') ||
                        (table[6] == 'X' && table[7] == 'X' && table[8] == 'X') ||
                        (table[0] == 'X' && table[3] == 'X' && table[6] == 'X') ||
                        (table[1] == 'X' && table[4] == 'X' && table[7] == 'X') ||
                        (table[2] == 'X' && table[5] == 'X' && table[8] == 'X') ||
                        (table[0] == 'X' && table[4] == 'X' && table[8] == 'X') ||
                        (table[2] == 'X' && table[4] == 'X' && table[6] == 'X');

        /* Check whether there is at least one row or column or diagonal of Os. */
        boolean atLeastOneRowOfO =
                (table[0] == 'O' && table[1] == 'O' && table[2] == 'O') ||
                        (table[3] == 'O' && table[4] == 'O' && table[5] == 'O') ||
                        (table[6] == 'O' && table[7] == 'O' && table[8] == 'O') ||
                        (table[0] == 'O' && table[3] == 'O' && table[6] == 'O') ||
                        (table[1] == 'O' && table[4] == 'O' && table[7] == 'O') ||
                        (table[2] == 'O' && table[5] == 'O' && table[8] == 'O') ||
                        (table[0] == 'O' && table[4] == 'O' && table[8] == 'O') ||
                        (table[2] == 'O' && table[4] == 'O' && table[6] == 'O');

        if (atLeastOneRowOfX) {
            return "X wins";
        }

        if (atLeastOneRowOfO) {
            return "O wins";
        }

        if (emptyCellsCount == 0) {
            return "Draw";
        }

        return GAME_NOT_FINISHED;
    }

    /**
     * Returns true if this cell is empty or not.
     *
     * @param index of the cell to check.
     * @return true if the cell is empty, otherwise false.
     */
    public static boolean isCellEmpty(int index) {
        return table[index] != 'X' && table[index] != 'O';
    }

    /**
     * Print the passed table.
     *
     * @param table the table to be printed.
     */
    private static void printTable(int[] table) {
        System.out.print("---------\n");
        System.out.print("| ");

        /* Print X and O as characters, otherwise, print the index for each cell. */
        for (int i = 0; i < table.length; i++) {
            if (table[i] == 88) {
                System.out.print('X' + " ");
            } else if (table[i] == 79) {
                System.out.print('O' + " ");
            } else {
                System.out.print('_' + " ");
            }

            // For proper formatting.
            if (i == 2 || i == 5) {
                System.out.print("| \n| ");
            }
            if (i == 8) {
                System.out.println("|");
            }
        }

        System.out.print("---------\n");

    }


}















