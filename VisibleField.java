import java.util.Arrays;



/**
 * VisibleField class This is the data that's being displayed at any one point
 * in the game (i.e., visible field, because it's what the user can see about
 * the minefield), Client can call getStatus(row, col) for any square. It
 * actually has data about the whole current state of the game, including the
 * underlying mine field (getMineField()). Other accessors related to game
 * status: numMinesLeft(), isGameOver(). It also has mutators related to actions
 * the player could do (resetGameDisplay(), cycleGuess(), uncover()), and
 * changes the game state accordingly.
 * 
 * It, along with the MineField (accessible in mineField instance variable),
 * forms the Model for the game application, whereas GameBoardPanel is the View
 * and Controller, in the MVC design pattern. It contains the MineField that
 * it's partially displaying. That MineField can be accessed (or modified) from
 * outside this class via the getMineField accessor.
 */
public class VisibleField {
	/**
	 * Jingcheng Wen, 23 hrs ago
	 * (Style -1): VF Representation invariant missing
	 */
	/**
	 * <rep invar>: all data in current is between -3 to 11, and location must inside the board
	 */
	// ----------------------------------------------------------
	// The following public constants (plus numbers mentioned in comments below) are
	// the possible states of one
	// location (a "square") in the visible field (all are values that can be
	// returned by public method
	// getStatus(row, col)).

	// Covered states (all negative values):
	public static final int COVERED = -1; // initial value of all squares
	public static final int MINE_GUESS = -2;
	public static final int QUESTION = -3;

	// Uncovered states (all non-negative values):

	// values in the range [0,8] corresponds to number of mines adjacent to this
	// square

	public static final int MINE = 9; // this loc is a mine that hasn't been guessed already (end of losing game)
	public static final int INCORRECT_GUESS = 10; // is displayed a specific way at the end of losing game
	public static final int EXPLODED_MINE = 11; // the one you uncovered by mistake (that caused you to lose)
	// ----------------------------------------------------------

	// <put instance variables here>
	private MineField mineField;
	private int[][] current; // record the current minefield player can see
	private int numGuesses; // num of mines guessed
	private int gameStates; // 0 means to be continued; 1 means win; 2 means lose
	private static final int CONTINUED = 0;
	private static final int WIN = 1;
	private static final int LOSE = 2;
	private int numOpened; // number of currently known open squares
	/**
	 * Jingcheng Wen, 23 hrs ago
	 * (Style -1): VF Representation invariant missing
	 */

	/**
	 * Create a visible field that has the given underlying mineField. The initial
	 * state will have all the mines covered up, no mines guessed, and the game not
	 * over.
	 * 
	 * @param mineField the minefield to use for for this VisibleField
	 */
	public VisibleField(MineField mineField) {
		this.mineField = mineField;
		int rows = this.mineField.numRows();
		int cols = this.mineField.numCols();

		current = new int[rows][cols];
		for (int[] curentRows : current) {
			Arrays.fill(curentRows, COVERED);
		}
	}

	/**
	 * Reset the object to its initial state (see constructor comments), using the
	 * same underlying MineField.
	 */
	public void resetGameDisplay() {

		for (int[] curentRows : current) {
			Arrays.fill(curentRows, COVERED);
		}
		numGuesses = 0;
		gameStates = 0;
		numOpened = 0;
	}

	/**
	 * Returns a reference to the mineField that this VisibleField "covers"
	 * 
	 * @return the minefield
	 */
	public MineField getMineField() {
		return mineField;
	}

	/**
	 * Returns the visible status of the square indicated.
	 * 
	 * @param row row of the square
	 * @param col col of the square
	 * @return the status of the square at location (row, col). See the public
	 *         constants at the beginning of the class for the possible values that
	 *         may be returned, and their meanings. PRE: getMineField().inRange(row,
	 *         col)
	 */
	public int getStatus(int row, int col) {
		assert mineField.inRange(row, col);
		return current[row][col];
	}

	/**
	 * Returns the the number of mines left to guess. This has nothing to do with
	 * whether the mines guessed are correct or not. Just gives the user an
	 * indication of how many more mines the user might want to guess. This value
	 * can be negative, if they have guessed more than the number of mines in the
	 * minefield.
	 * 
	 * @return the number of mines left to guess.
	 */
	public int numMinesLeft() {
		return mineField.numMines() - numGuesses; // DUMMY CODE so skeleton compiles

	}

	/**
	 * Cycles through covered states for a square, updating number of guesses as
	 * necessary. Call on a COVERED square changes its status to MINE_GUESS; call on
	 * a MINE_GUESS square changes it to QUESTION; call on a QUESTION square changes
	 * it to COVERED again; call on an uncovered square has no effect.
	 * 
	 * @param row row of the square
	 * @param col col of the square PRE: getMineField().inRange(row, col)
	 */
	public void cycleGuess(int row, int col) {
		assert getMineField().inRange(row, col); // && !isUncovered(row, col);
		if (current[row][col] == COVERED) {
			current[row][col] = MINE_GUESS;
			numGuesses++;
		} else if (current[row][col] == MINE_GUESS) {
			current[row][col] = QUESTION;
			numGuesses--;
		} else { // current[row][col] == QUESTION
			current[row][col] = COVERED;
		}

	}

	/**
	 * Uncovers this square and returns false iff you uncover a mine here. If the
	 * square wasn't a mine or adjacent to a mine it also uncovers all the squares
	 * in the neighboring area that are also not next to any mines, possibly
	 * uncovering a large region. Any mine-adjacent squares you reach will also be
	 * uncovered, and form (possibly along with parts of the edge of the whole
	 * field) the boundary of this region. Does not uncover, or keep searching
	 * through, squares that have the status MINE_GUESS. Note: this action may cause
	 * the game to end: either in a win (opened all the non-mine squares) or a loss
	 * (opened a mine).
	 * 
	 * @param row of the square
	 * @param col of the square
	 * @return false iff you uncover a mine at (row, col) PRE:
	 *         getMineField().inRange(row, col)
	 */
	public boolean uncover(int row, int col) {
		assert mineField.inRange(row, col);
		if (mineField.hasMine(row, col)) { // lose the game
			current[row][col] = EXPLODED_MINE;
			gameStates = LOSE;
			loseGameStatus();
			return false;
		}
		else { // not mine in this place, to continue
			current[row][col] = COVERED; // it will be opend in the doDFSSearch(row, col);
			doDFSSearch(row, col);
			int rows = mineField.numRows();
			int cols = mineField.numCols();
			if (numOpened == rows * cols - mineField.numMines()) { // the win-condition is to open all the non-mine locations,
				gameStates = WIN;
				winGameStatus();
			}
			return true;
		}
		
	}

	/**
	 * Returns whether the game is over. (Note: This is not a mutator.)
	 * @return whether game over
	 */
	public boolean isGameOver() {
		return gameStates > 0; // DUMMY CODE so skeleton compiles
	}

	/**
	 * Returns whether this square has been uncovered. (i.e., is in any one of the
	 * uncovered states, vs. any one of the covered states).
	 * 
	 * @param row of the square
	 * @param col of the square
	 * @return whether the square is uncovered PRE: getMineField().inRange(row, col)
	 */
	public boolean isUncovered(int row, int col) {
		assert mineField.inRange(row, col);
		return current[row][col] >= 0; // which means not covered, question, mine guess
	}

	// <put private methods here>

	/**
	 * when we win the game, get the end state of the result of the mine field that
	 * player can see PRE: gameOver > 0
	 */
	private void winGameStatus() {
		assert isGameOver();
		for (int i = 0; i < mineField.numRows(); i++) {
			for (int j = 0; j < mineField.numCols(); j++) {
				if (current[i][j] == QUESTION) {
					current[i][j] = MINE;
				}
			}
		}
	}

	/**
	 * when we win the game, get the end state of the result of the mine field that
	 * player can see PRE: gameOver > 0
	 */
	private void loseGameStatus() {
		assert isGameOver();
		for (int i = 0; i < mineField.numRows(); i++) {
			for (int j = 0; j < mineField.numCols(); j++) {
				if ((current[i][j] == COVERED || current[i][j] == QUESTION) && mineField.hasMine(i, j)) {
					current[i][j] = MINE;
				} else if (current[i][j] == MINE_GUESS && !mineField.hasMine(i, j)) {
					current[i][j] = INCORRECT_GUESS; // mark it as the incorrect guess.
				}
			}
		}
	}

	/**
	 * use DFS recursion to explore the adjacent squares that does not have mine,
	 * and for every square of these, find out how many mines are surrounded
	 * 
	 * @param row
	 * @param col 
	 * PRE: getMineField().inRange(row, col) is true
	 */
	private void doDFSSearch(int row, int col) {
		// corner case
		if (!mineField.inRange(row, col) || mineField.hasMine(row, col) || isUncovered(row, col)) {
			return;
		}
		int numSurroundedMines = mineField.numAdjacentMines(row, col); // means the number of surrounded mines
		current[row][col] = numSurroundedMines;
		numOpened++;
		if (numSurroundedMines == 0) { // step into neighbor squares
			int[][] direction = new int[][] { { 1, 0 }, { 1, 1 }, { 0, 1 }, { -1, 1 }, 
				{ -1, 0 }, { -1, -1 }, { 0, -1 }, { 1, -1 } };
			for (int i = 0; i < direction.length; i++) {
				int r = row + direction[i][0];
				int c = col + direction[i][1];
				if (mineField.inRange(r, c)) {
					doDFSSearch(r, c);
				}
			}
		}
	}
}
