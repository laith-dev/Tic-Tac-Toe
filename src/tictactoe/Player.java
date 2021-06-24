package tictactoe;

/**
 * An abstract Tic-Tac-Toe player that has a specific symbol and can make a move.
 */
public abstract class Player {

    /* X or O */
    char symbol;

    /* Make a move by placing 'symbol' on the field. */
    abstract void makeMove();

}