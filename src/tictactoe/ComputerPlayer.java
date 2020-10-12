package tictactoe;

import java.util.Arrays;
import java.util.Random;

import static tictactoe.Main.*;

public class ComputerPlayer extends Player {

    /* Difficulty of the AI.
     * It can be:
     * - Easy
     * - Medium
     * - Hard
     * */
    String difficulty;

    /**
     * Create a computer player that knows how to play the game.
     *
     * @param difficulty of the AI.
     * @param symbol     of the AI player, X or O.
     */
    public ComputerPlayer(String difficulty, char symbol) {
        this.difficulty = difficulty;
        super.symbol = symbol;
    }

    /**
     * Make a move by filling an empty cell in the Tic-Tac-Toe table.
     * The move depends on the AI difficulty level.
     */
    @Override
    protected void makeMove() {
        System.out.println("Making move level " + difficulty + " (as " + this.symbol + ").");

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

    /**
     * The easy level always makes random moves regardless of the state of the game.
     */
    private void makeMoveEasy() {
        makeRandomMove();
    }

    /**
     * The "medium" level difficulty makes a move using the following process:
     * - If it can win in one move (if it has two in a row), it places a third to get three in a row and win.
     * - If the opponent can win in one move, it plays the third itself to block the opponent to win.
     * - Otherwise, it makes a random move.
     */
    private void makeMoveMedium() {

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

    /**
     * Compared to the "medium" level difficulty, this level not just go one move ahead to see
     * an immediate win or prevent an immediate loss. This level can see two moves ahead,
     * three moves ahead and so on. Basically, it can see all possible outcomes till the end
     * of the game and choose the best of them considering his opponent also would play perfectly.
     * So, it doesn't rely on the blunders of the opponent, it plays perfectly regardless of the opponent's skill.
     */
    private void makeMoveHard() {
        int bestNextMoveIndex = minimax(Main.table, this.symbol);
        Main.table[bestNextMoveIndex] = this.symbol;
    }


    /**
     * The minimax algorithm. It can all the outcomes of the current state of the game and decides the best
     * next move.
     *
     * @param newTable: this algorithm makes a virtual game by itself depending on the current state of the
     *                  table and continues playing until it reaches a terminal state (i.e. a win, lose or draw).
     *                  So, this table is where this algorithm makes its virtual game.
     * @param player    : the player symbol (X or O). This algorithm will play its symbol at first
     *                  then the opponent symbol and so on to reach a terminal state.
     * @return the index with the highest score (i.e. the best possible index to play).
     */
    private int minimax(int[] newTable, char player) {
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

            /* Collect the score resulted from calling minimax on the opponent. */
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

    /**
     * Return the empty cells indexes in this table.
     *
     * @param table to check empty cells in.
     * @return an array of empty cells indexes.
     */
    private int[] emptyIndexes(int[] table) {
        int emptyCellsCount = 0;
        for (int i = 0; i < table.length; i++) {
            if (Main.isCellEmpty(i)) {
                emptyCellsCount++;
            }
        }

        int[] result = new int[emptyCellsCount];
        for (int i = 0, index = 0; i < table.length; i++) {
            if (Main.isCellEmpty(i)) {
                result[index] = i;
                index++;
            }
        }

        return result;
    }

    /**
     * Check if the player with the provided symbol wins (i.e. has 3 symbols in a row) in this specific state of the table.
     *
     * @param table:  the current state of the game.
     * @param symbol: the player symbol to check if he's winning.
     * @return true if this player wins, otherwise false.
     */
    private boolean winning(int[] table, char symbol) {
        return (table[0] == symbol && table[1] == symbol && table[2] == symbol) ||
                (table[3] == symbol && table[4] == symbol && table[5] == symbol) ||
                (table[6] == symbol && table[7] == symbol && table[8] == symbol) ||
                (table[0] == symbol && table[3] == symbol && table[6] == symbol) ||
                (table[1] == symbol && table[4] == symbol && table[7] == symbol) ||
                (table[2] == symbol && table[5] == symbol && table[8] == symbol) ||
                (table[0] == symbol && table[4] == symbol && table[8] == symbol) ||
                (table[2] == symbol && table[4] == symbol && table[6] == symbol);
    }

    /**
     * Make a random move on the table.
     */
    private void makeRandomMove() {
        Random random = new Random();

        /* Keep generating random indexes in range [0,8] until you find an empty cell.  */
        boolean correctMove = false;
        do {
            int index = random.nextInt(9);
            if (isCellEmpty(index)) {
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
     *
     * @param moveMode: winning or blocking move.
     * @return the index of a crucial move if exists, otherwise -1.
     */
    private int getCrucialMoveIndex(String moveMode) {
        /* If the mode is winning, then the symbol to search for (two in a row) should be this symbol,
         * otherwise, it's the opponent symbol. */
        char charToSearchFor;

        if (moveMode.equals("winning")) {
            charToSearchFor = this.symbol;
        } else if (moveMode.equals("blocking")) {
            charToSearchFor = this.symbol == 'X' ? 'O' : 'X';
        } else {
            System.out.println("Unknown move type!");
            return -1;
        }

        /* Check every empty cell and see if filling it would make the AI win or the opponent lose
         * his chance of winning. For example, cell 0 is a crucial cell if any of the following is true:
         * - index 1 and 2 have two of the same symbol.
         * - index 3 and 6 have two of the same symbol.
         * - index 4 and 8 have two of the same symbol.
         */

        // Cell with index 0.
        if (Main.table[1] == charToSearchFor && Main.table[2] == charToSearchFor ||
                Main.table[3] == charToSearchFor && Main.table[6] == charToSearchFor ||
                Main.table[4] == charToSearchFor && Main.table[8] == charToSearchFor) {
            if (isCellEmpty(0)) {
                return 0;
            }
        }

        // Cell with index 1.
        if (Main.table[0] == charToSearchFor && Main.table[2] == charToSearchFor ||
                Main.table[4] == charToSearchFor && Main.table[7] == charToSearchFor) {
            if (isCellEmpty(1)) {
                return 1;
            }
        }

        // Cell with index 2.
        if (Main.table[0] == charToSearchFor && Main.table[1] == charToSearchFor ||
                Main.table[5] == charToSearchFor && Main.table[8] == charToSearchFor ||
                Main.table[4] == charToSearchFor && Main.table[6] == charToSearchFor) {
            if (isCellEmpty(2)) {
                return 2;
            }
        }

        // Cell with index 3.
        if (Main.table[0] == charToSearchFor && Main.table[6] == charToSearchFor ||
                Main.table[4] == charToSearchFor && Main.table[5] == charToSearchFor) {
            if (isCellEmpty(3)) {
                return 3;
            }
        }

        // Cell with index 4.
        if (Main.table[1] == charToSearchFor && Main.table[7] == charToSearchFor ||
                Main.table[3] == charToSearchFor && Main.table[5] == charToSearchFor) {
            if (isCellEmpty(4)) {
                return 4;
            }
        }

        // Cell with index 5.
        if (Main.table[3] == charToSearchFor && Main.table[4] == charToSearchFor ||
                Main.table[2] == charToSearchFor && Main.table[8] == charToSearchFor) {
            if (isCellEmpty(5)) {
                return 5;
            }
        }

        // Cell with index 6.
        if (Main.table[0] == charToSearchFor && Main.table[3] == charToSearchFor ||
                Main.table[7] == charToSearchFor && Main.table[8] == charToSearchFor ||
                Main.table[2] == charToSearchFor && Main.table[4] == charToSearchFor) {
            if (isCellEmpty(6)) {
                return 6;
            }
        }

        // Cell with index 7.
        if (Main.table[1] == charToSearchFor && Main.table[4] == charToSearchFor ||
                Main.table[6] == charToSearchFor && Main.table[8] == charToSearchFor) {
            if (isCellEmpty(7)) {
                return 7;
            }
        }

        // Cell with index 8.
        if (Main.table[2] == charToSearchFor && Main.table[5] == charToSearchFor ||
                Main.table[6] == charToSearchFor && Main.table[7] == charToSearchFor ||
                Main.table[0] == charToSearchFor && Main.table[4] == charToSearchFor) {
            if (isCellEmpty(8)) {
                return 8;
            }
        }

        return -1;
    }

}

/**
 * Make a virtual moves on the table (as described in the minimax method) and store each move's
 * index (in the table) and its score(1 for winning, -1 for loosing and 0 for draw).
 */
class Move {
    int index;
    int score;

}














