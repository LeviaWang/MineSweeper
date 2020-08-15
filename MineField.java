import java.util.Arrays;
import java.util.Random;



/**
 * MineField class with locations of mines for a game. This class is mutable,
 * because we sometimes need to change it once it's created. mutators:
 * populateMineField, resetEmpty includes convenience method to tell the number
 * of mines adjacent to a location.
 */
public class MineField {

	// <put instance variables here>
	/**
	 * Jingcheng Wen, 23 hrs ago
	 * (Style -1): MF Representation invariant missing
	 */
	/**<rep invar>: all locations in the array is boolean, true or false, the location must inside the board
	 */
	private int rows; // the number of row
	private int cols; // the number of column
	private int numMines; // the number of mines
	private boolean[][] mineData; // the array to store which square has mine

	private static final boolean HAS_MINE = true;
	private static final boolean NO_MINE = false;
	private Random random = new Random();

	/**
	 * Create a minefield with same dimensions as the given array, and populate it
	 * with the mines in the array such that if mineData[row][col] is true, then
	 * hasMine(row,col) will be true and vice versa. numMines() for this minefield
	 * will corresponds to the number of 'true' values in mineData.
	 * 
	 * @param mineData the data for the mines; must have at least one row and one
	 *                 col.
	 */
	public MineField(boolean[][] mineData) {
		rows = mineData.length;
		cols = mineData[0].length;

		this.mineData = new boolean[mineData.length][mineData[0].length];
		int numOfMines = 0; // to temporarily store the number of mines
		for (int i = 0; i < mineData.length; i++) {
			for (int j = 0; j < mineData[0].length; j++) {
				this.mineData[i][j] = mineData[i][j];
				if (this.mineData[i][j] == HAS_MINE) {
					numOfMines++;
				}
			}
		}
		numMines = numOfMines;
	}

	/**
	 * Create an empty minefield (i.e. no mines anywhere), that may later have
	 * numMines mines (once populateMineField is called on this object). Until
	 * populateMineField is called on such a MineField, numMines() will not
	 * correspond to the number of mines currently in the MineField.
	 * 
	 * @param numRows  number of rows this minefield will have, must be positive
	 * @param numCols  number of columns this minefield will have, must be positive
	 * @param numMines number of mines this minefield will have, once we populate
	 *                 it. PRE: numRows > 0 and numCols > 0 and 0 <= numMines < (1/3
	 *                 of total number of field locations).
	 */
	public MineField(int numRows, int numCols, int numMines) {
		assert numMines >= 0 && numRows > 0 && numCols > 0 && numMines < numRows * numCols / 3.0;

		rows = numRows;
		cols = numCols;
		this.numMines = numMines;

		mineData = new boolean[numRows][numCols];

	}

	/**
	 * Removes any current mines on the minefield, and puts numMines() mines in
	 * random locations on the minefield, ensuring that no mine is placed at (row,
	 * col).
	 * 
	 * @param row the row of the location to avoid placing a mine
	 * @param col the column of the location to avoid placing a mine PRE:
	 *            inRange(row, col)
	 */
	public void populateMineField(int row, int col) {
		assert inRange(row, col);
		resetEmpty();
		int count = 0;
		int r = 0; // to temporarily denote row
		int c = 0; // to temporarily denote column
		while(count < numMines) {
			r = random.nextInt(rows);
			c = random.nextInt(cols);
			if (!(r == row && c == col) && mineData[r][c] == NO_MINE) {
				mineData[r][c] = HAS_MINE;
				count++;
			}
		}
	}

	/**
	 * Reset the minefield to all empty squares. This does not affect numMines(),
	 * numRows() or numCols() Thus, after this call, the actual number of mines in
	 * the minefield does not match numMines(). Note: This is the state the
	 * minefield is in at the beginning of a game.
	 */
	public void resetEmpty() {
		for (boolean[] rowMines: mineData) {
			Arrays.fill(rowMines, NO_MINE);
		}
	}

	/**
	 * Returns the number of mines adjacent to the specified mine location (not
	 * counting a possible mine at (row, col) itself). Diagonals are also considered
	 * adjacent, so the return value will be in the range [0,8]
	 * 
	 * @param row row of the location to check
	 * @param col column of the location to check
	 * @return the number of mines adjacent to the square at (row, col) PRE:
	 *         inRange(row, col)
	 */
	public int numAdjacentMines(int row, int col) {
		assert inRange(row, col);
		int[][] direction = new int[][] {{1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}};
		int count = 0;
		int r, c; // to temporarily denote row and column
		for (int i = 0; i < direction.length; i++) {
			r = row + direction[i][0];
			c = col + direction[i][1];
			count += (inRange(r, c) && hasMine(r, c) ? 1 : 0); 
		}
		
		return count;
	}

	/**
	 * Returns true iff (row,col) is a valid field location. Row numbers and column
	 * numbers start from 0.
	 * 
	 * @param row row of the location to consider
	 * @param col column of the location to consider
	 * @return whether (row, col) is a valid field location
	 */
	public boolean inRange(int row, int col) {
		return row >= 0 && row < rows && col >= 0 && col < cols;
	}

	/**
	 * Returns the number of rows in the field.
	 * 
	 * @return number of rows in the field
	 */
	public int numRows() {
		return rows; // DUMMY CODE so skeleton compiles
	}

	/**
	 * Returns the number of columns in the field.
	 * 
	 * @return number of columns in the field
	 */
	public int numCols() {
		return cols; // DUMMY CODE so skeleton compiles
	}

	/**
	 * Returns whether there is a mine in this square
	 * 
	 * @param row row of the location to check
	 * @param col column of the location to check
	 * @return whether there is a mine in this square PRE: inRange(row, col)
	 */
	public boolean hasMine(int row, int col) {
		assert inRange(row, col);
		return mineData[row][col]; // DUMMY CODE so skeleton compiles
	}

	/**
	 * Returns the number of mines you can have in this minefield. For mines created
	 * with the 3-arg constructor, some of the time this value does not match the
	 * actual number of mines currently on the field. See doc for that constructor,
	 * resetEmpty, and populateMineField for more details.
	 * 
	 * @return
	 */
	public int numMines() {
		return numMines; // DUMMY CODE so skeleton compiles
	}

	// <put private methods here>

}
