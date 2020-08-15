
public class MineFieldTester {
	private static MineField mine;
	
	private static boolean[][] smallMineField = 
	      {{false, false, false, false}, 
	       {true, false, false, false}, 
	       {false, true, true, false},
	       {false, true, false, true}};
	
	public static void main(String[] args) {
		mine = new MineField(smallMineField);
		mine.resetEmpty();
		mine.populateMineField(3, 3);
		System.out.println(mine.numCols());
		System.out.println(mine.numRows());
		System.out.println(mine.numMines());
		System.out.println(mine.numAdjacentMines(2, 2));
		System.out.println(mine.hasMine(3, 3));
		return;
	}

}
