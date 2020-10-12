package tictactoe;

import java.util.Scanner;

public class Main {

    /* Table of the game where each cell is represented by its index. */
    static int[] table;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String playerX = "", playerO = "";
        boolean correctInput = false;
        do {
            System.out.print("Input command: ");
            String[] input = sc.nextLine().split(" ");

            if (input[0].equals("exit")) {
                return;
            }

            if (input.length != 3 || !input[0].equals("start") ||
                    !validPlayerType(input[1]) || !validPlayerType(input[2])) {
                System.out.println("Bad parameters!");
            } else {
                playerX = input[1];
                playerO = input[2];
                correctInput = true;
            }

        } while (!correctInput);

        /* Start by printing an empty field. */
        table = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
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
         * - Game not finished
         * - Draw
         * - X wins
         * - O wins
         * */
        String gameState;
        do {
            player1.makeMove();
            /* After each move, print the table and decrease empty cells by one. */
            printTable(table);
            if (checkGameState().equals("Game not finished")) {
                player2.makeMove();
                printTable(table);
            }

            gameState = checkGameState();

        } while (gameState.equals("Game not finished"));

        System.out.println(gameState);

    }

    private static boolean validPlayerType(String playerType) {
        return playerType.equals("user") ||
                playerType.equals("hard") ||
                playerType.equals("medium") ||
                playerType.equals("easy");
    }

    private static String checkGameState() {
        int emptyCellsCount = 0;
        for (int i = 0; i < table.length; i++) {
            if (isCellEmpty(i)) {
                emptyCellsCount++;
            }
        }

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

        return "Game not finished";
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


    /* Utility method for printing the table. */
    private static void printTable(int[] table) {
        System.out.print("---------\n");
        System.out.print("| ");

        for (int i = 0; i < table.length; i++) {
            if (table[i] == 88) {
                System.out.print('X' + " ");
            } else if (table[i] == 79) {
                System.out.print('O' + " ");
            } else {
                System.out.print('_' + " ");
            }
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















