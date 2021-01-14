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
     * Ask the human player about his move (the cell index in of which he wants to place his symbol),
     * and check its correctness, i.e. the cell is not empty.
     */
    @Override
    void makeMove() {
        Scanner sc = new Scanner(System.in);

        int moveIndex;
        while (true) {

            /* Only numbers are allowed. */
            try {
                System.out.print("Make a move (You are " + this.symbol + "): ");
                moveIndex = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("You should enter numbers!");
                sc.nextLine(); // Clear the buffer or the program will enter an infinite loop.
                continue;
            }

            /* The index should be in range [0,8]. */
            if (moveIndex < 0 || moveIndex > 8) {
                System.out.println("Index should be from 0 to 8 (inclusive).");
                continue;
            }

            if (Main.isCellEmpty(moveIndex)) {
                // Make the user's move
                Main.table[moveIndex] = symbol;
                break;
            } else {
                System.out.println("This cell is occupied! Choose another one!");
            }

        }
    }

}
