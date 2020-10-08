package tictactoe;

import java.util.InputMismatchException;
import java.util.Scanner;

public class HumanPlayer extends Player {

    public HumanPlayer(char symbol) {
        super.symbol = symbol;
    }

    @Override
    void makeMove() {
        Scanner sc = new Scanner(System.in);

        int col, row;
        boolean correctMove = false;
        do {

            /* Only numbers are allowed. */
            try {
                System.out.print("Enter the coordinates: ");
                col = sc.nextInt();
                row = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("You should enter numbers!");
                sc.nextLine(); // Clear the buffer or the program will enter an infinite loop.
                continue;
            }

            /* The coordinates should be in the range [1,3]. */
            if (outOfRange(col, row)) {
                System.out.println("Coordinates should be from 1 to 3!");
                continue;
            }

            /* The index (in the table) corresponding to the entered coordinates. */
            int index = getIndexIfEmpty(col, row);
            if (index > -1) {
                // Make the user's move
                Main.table[index] = symbol;

                correctMove = true;
            } else {
                System.out.println("This cell is occupied! Choose another one!");
                continue;
            }

        } while (!correctMove);

    }

    /* Checks the entered coordinates if they are in the allowed range [1,3]. */
    private boolean outOfRange(int col, int row) {
        return col > 3 || col < 1 || row > 3 || row < 1;
    }

    /* If the cell [col,row] is empty, return the corresponding index of it in the table,
     * otherwise return -1. */
    private static int getIndexIfEmpty(int col, int row) {
        // Index of the cell that has coordinates [col,row].
        int index = 0;

        // Find the cell that has the coordinates [col,row].
        for (int i = 3; i >= 1; i--) {
            for (int j = 1; j <= 3; j++, index++) {

                // Found the cell
                if (i == row && j == col) {
                    if (Main.table[index] != 'X' && Main.table[index] != 'O') {
                        return index;
                    }
                }
            }
        }

        return -1;
    }

}
