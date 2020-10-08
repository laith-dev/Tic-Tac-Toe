package tictactoe;

import java.util.Arrays;
import java.util.Random;

public class ComputerPlayer extends Player {

    String difficulty;

    public ComputerPlayer(String difficulty, char symbol) {
        this.difficulty = difficulty;
        super.symbol = symbol;
    }

    @Override
    void makeMove() {
        System.out.println("Making move level \"" + difficulty + "\"");

        try {
            Thread.sleep(1500L);
        } catch (InterruptedException e) {
            System.out.println("Interrupted Exception -> " + Arrays.toString(e.getStackTrace()));
        }

        switch (difficulty) {
            case "easy":
                makeMoveEasy();
                break;

            case "medium":
                makeMoveMedium();
                break;

            case "hard":
                makeMoveHard();
        }

    }

    /* The easy level always makes random moves regardless of the state of the game. */
    void makeMoveEasy() {
        makeRandomMove();
    }

    /**
     * The "medium" level difficulty makes a move using the following process:
     * <p>
     * - If it can win in one move (if it has two in a row), it places a third to get three in a row and win.
     * - If the opponent can win in one move, it plays the third itself to block the opponent to win.
     * - Otherwise, it makes a random move.
     */
    void makeMoveMedium() {

        /* Get the index of the winning move, if exists. */
        int winningMoveIndex = getCrucialMoveIndex("winning");
        /* Is there a winning move? If so, play it and win. */
        if (winningMoveIndex > -1) {
            Main.table[winningMoveIndex] = this.symbol;
            return;
        }

        /* Get the index of a blocking move, if exists. */
        int blockingMoveIndex = getCrucialMoveIndex("blocking");
        /* Is there a blocking move? If so, play it and block the opponent from winning. */
        if (blockingMoveIndex > -1) {
            Main.table[blockingMoveIndex] = this.symbol;
            return;
        }

        /* If there is no winning move or a blocking move, just make a random move. */
        makeRandomMove();

    }

    void makeMoveHard() {
        int bestNextMoveIndex = minimax(Main.table, this.symbol);
        Main.table[bestNextMoveIndex] = this.symbol;
    }


    /* The main minimax function. */
    int minimax(int[] newTable, char player) {
        int[] availSpots = emptyIndexes(newTable);

        char opponentSymbol = this.symbol == 'X' ? 'O' : 'X';

        /* Check for a terminal state such as win, lose or tie. And return a value accordingly. */
        if (winning(newTable, this.symbol)) {
            return 10;
        } else if (winning(newTable, opponentSymbol)) {
            return -10;
        } else if (availSpots.length == 0) {
            return 0;
        }

        Move[] moves = new Move[availSpots.length];
        // Loop through available spots.
        for (int i = 0; i < availSpots.length; i++) {
            /* Create an object for each empty spot and store the index of that spot. */
            moves[i] = new Move();
            moves[i].index = newTable[availSpots[i]];

            // Set the empty spot to the current player (virtual move by the current player).
            newTable[availSpots[i]] = player;

            /* Collect the score resulted from calling minimax on the opponent of the current player. */
            int result;
            if (player == this.symbol) {
                result = minimax(newTable, opponentSymbol);
            } else {
                result = minimax(newTable, player);
            }
            moves[i].score = result;

            // Reset the spot to empty.
            newTable[availSpots[i]] = (char) moves[i].index;

        }

        /* Choose the best move according to the current player:
         * - If it's aiPlayer, choose the move with the highest score.
         * - If it's huPlayer, choose the move with the lowest score. */
        int bestMoveIndex = -1;
        int bestScore;
        if (player == this.symbol) {
            bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < moves.length; i++) {
                if (moves[i].score > bestScore) {
                    bestScore = moves[i].score;
                    bestMoveIndex = i;
                }
            }
        } else {
            bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < moves.length; i++) {
                if (moves[i].score < bestScore) {
                    bestScore = moves[i].score;
                    bestMoveIndex = i;
                }
            }
        }

        return moves[bestMoveIndex].index;
    }

    /* Returns an array of empty cells in the table. */
    int[] emptyIndexes(int[] table) {
        int emptyCellsCount = 0;
        for (int i : table) {
            if (i != 'X' && i != 'O') {
                emptyCellsCount++;
            }
        }

        int[] result = new int[emptyCellsCount];
        for (int i = 0, index = 0; i < table.length; i++) {
            if (table[i] != 'X' && table[i] != 'O') {
                result[index] = i;
                index++;
            }
        }

        return result;
    }

    /* Winning combinations using the passed board indexes. */
    boolean winning(int[] board, char player) {
        return (board[0] == player && board[1] == player && board[2] == player) ||
                (board[3] == player && board[4] == player && board[5] == player) ||
                (board[6] == player && board[7] == player && board[8] == player) ||
                (board[0] == player && board[3] == player && board[6] == player) ||
                (board[1] == player && board[4] == player && board[7] == player) ||
                (board[2] == player && board[5] == player && board[8] == player) ||
                (board[0] == player && board[4] == player && board[8] == player) ||
                (board[2] == player && board[4] == player && board[6] == player);

    }

    private void makeRandomMove() {
        Random random = new Random();

        /* A random move is done when a random empty cell is found. */
        boolean correctMove = false;
        do {
            // Generate a random index in the table to fill it.
            int index = random.nextInt(9);
            // Check if the cell is empty.
            if (Main.table[index] != 'X' && Main.table[index] != 'O') {
                // Make the move.
                Main.table[index] = symbol;
                correctMove = true;
            }

        } while (!correctMove);
    }

    /**
     * Returns the index of a crucial move, if exists. Otherwise, returns -1.
     * The crucial move might be:
     * - a winning move: two of the symbols in a row so this method returns the index
     * of the third cell to win the game.
     * - a blocking move: two of the opponent symbol in a row so this method returns the index
     * of the third cell to stop the win.
     */
    private int getCrucialMoveIndex(String moveMode) {
        char charToSearchFor;

        if (moveMode.equals("winning")) {
            charToSearchFor = this.symbol;
        } else if (moveMode.equals("blocking")) {
            // Find the opponent symbol.
            charToSearchFor = this.symbol == 'X' ? 'O' : 'X';
        } else {
            System.out.println("Unknown move type!");
            return -1;
        }

        /* Check every row if there is two in a row of charToSearchFor. */
        for (int i = 1; i < Main.table.length; i++) {
            if (Main.table[i] == charToSearchFor && Main.table[i - 1] == charToSearchFor) {
                if (i == 2 || i == 5 || i == 8) {
                    // Make sure the cell is empty before returning the index of it.
                    if (Main.table[i - 2] != 'X' && Main.table[i - 2] != 'O')
                        return i - 2;
                } else {
                    // Make sure the cell is empty before returning the index of it.
                    if (Main.table[i + 1] != 'X' && Main.table[i + 1] != 'O')
                        return i + 1;
                }
            }
            // When reaching the boundaries of the table, move i to the middle of the next row.
            if (i == 2 || i == 5) {
                i++;
            }
        }

        /* Check every column if there is two in a row of charToSearchFor. */
        for (int i = 3; i < Main.table.length; i += 3) {
            if (Main.table[i] == charToSearchFor && Main.table[i - 3] == charToSearchFor) {
                if (i == 6 || i == 7 || i == 8) {
                    if (Main.table[i - 6] != 'X' && Main.table[i - 6] != 'O')
                        return i - 6;
                } else {
                    if (Main.table[i + 3] != 'X' && Main.table[i + 3] != 'O')
                        return i + 3;
                }

            }

            // If the bottom of the column is reached, assign i to the middle of the next column by subtracting
            // 2 from i.
            // i -= 5 --> consider the increment of i before each iteration, i += 3.
            if (i == 6 || i == 7) {
                i -= 5;
            }
        }

        /* Other cases for the winning move to occur:
         * - Two in a row of charToSearchFor diagonally.
         * - The winning move cell is located at the middle of row/column/diagonal.
         * */

        /*
         * Both diagonals.
         * */
        if (Main.table[0] == charToSearchFor && Main.table[4] == charToSearchFor)
            if (Main.table[8] != 'X' && Main.table[8] != 'O')
                return 8;

        if (Main.table[0] == charToSearchFor && Main.table[8] == charToSearchFor)
            if (Main.table[4] != 'X' && Main.table[4] != 'O')
                return 4;

        if (Main.table[4] == charToSearchFor && Main.table[8] == charToSearchFor)
            if (Main.table[0] != 'X' && Main.table[0] != 'O')
                return 0;

        if (Main.table[2] == charToSearchFor && Main.table[4] == charToSearchFor)
            if (Main.table[6] != 'X' && Main.table[6] != 'O')
                return 6;

        if (Main.table[2] == charToSearchFor && Main.table[6] == charToSearchFor)
            if (Main.table[4] != 'X' && Main.table[4] != 'O')
                return 4;

        if (Main.table[4] == charToSearchFor && Main.table[6] == charToSearchFor)
            if (Main.table[2] != 'X' && Main.table[2] != 'O')
                return 2;

        /*
         * Odd cases in rows.
         * */
        if (Main.table[0] == charToSearchFor && Main.table[2] == charToSearchFor)
            if (Main.table[1] != 'X' && Main.table[1] != 'O')
                return 1;

        if (Main.table[3] == charToSearchFor && Main.table[5] == charToSearchFor)
            if (Main.table[4] != 'X' && Main.table[4] != 'O')
                return 4;

        if (Main.table[6] == charToSearchFor && Main.table[8] == charToSearchFor)
            if (Main.table[7] != 'X' && Main.table[7] != 'O')
                return 7;

        /*
         * Odd cases in columns.
         * */
        if (Main.table[0] == charToSearchFor && Main.table[6] == charToSearchFor)
            if (Main.table[3] != 'X' && Main.table[3] != 'O')
                return 3;

        if (Main.table[1] == charToSearchFor && Main.table[7] == charToSearchFor)
            if (Main.table[4] != 'X' && Main.table[4] != 'O')
                return 4;

        if (Main.table[2] == charToSearchFor && Main.table[8] == charToSearchFor)
            if (Main.table[5] != 'X' && Main.table[5] != 'O')
                return 5;


        return -1;
    }

}


class Move {
    int index;
    int score;

}














