package tictactoe;

import java.util.InputMismatchException;
import java.util.Scanner;

public class HumanPlayer extends Player {

    /**
     * Create a human player by providing the symbol that he will play with.
     */
    public HumanPlayer(char symbol) {
        super.symbol = symbol;
    }

    /**
     * Let the human player play by asking him about the index of the cell that he would like to play it
     * and check its correctness (i.e. the index is in range and the cell is not empty).
     */
    @Override
    void makeMove() {
        Scanner sc = new Scanner(System.in);

        int cellIndex;
        boolean correctMove = false;
        do {

            /* Only numbers are allowed. */
            try {
                System.out.print("Enter the index (You are " + this.symbol + "): ");
                cellIndex = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("You should enter numbers!");
                sc.nextLine(); // Clear the buffer or the program will enter an infinite loop.
                continue;
            }

            /* The index should be in range [0,8]. */
            if (cellIndex < 0 || cellIndex > 8) {
                System.out.println("Index should be from 0 to 8 (inclusive).");
                continue;
            }

            if (Main.isCellEmpty(cellIndex)) {
                // Make the user's move
                Main.table[cellIndex] = symbol;
                correctMove = true;
            } else {
                System.out.println("This cell is occupied! Choose another one!");
                continue;
            }

        } while (!correctMove);

    }

}
