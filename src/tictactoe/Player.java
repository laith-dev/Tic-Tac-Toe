package tictactoe;

public abstract class Player {

    /* X or O */
    char symbol;

    /* Make a move by placing 'symbol' on the field. */
    abstract void makeMove();

}
