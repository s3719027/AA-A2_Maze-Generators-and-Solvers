package mazeSolver;

import maze.Maze;
import maze.Cell;
import maze.Wall;import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;


/**
 * Implements the recursive backtracking maze solving algorithm.
	@author Daniel Bound
 */
public class RecursiveBacktrackerSolver implements MazeSolver {

	private List<Cell> visitedCells = new ArrayList<Cell>();
	private List<Cell> pathToExit = new LinkedList<Cell>();
	private Maze mazeRef;
	private Cell entranceCell, exitCell; 
	boolean exitFound = false;

	@Override
	public void solveMaze(Maze maze) {

		mazeRef = maze;
		entranceCell = maze.entrance;
		exitCell = maze.exit;
		recursiveBacktrack(entranceCell);

	} // end of solveMaze()
	
	private void recursiveBacktrack(Cell cell) {
		
		visitedCells.add(cell);
		pathToExit.add(cell);
		mazeRef.drawFtPrt(cell);
		
		if (!exitFound) {
			//if current cell is the exit cell
			if (cell == exitCell) {
				exitFound = true;
				recursiveBacktrack(cell);
			}
			//if cell has tunnel proceed through tunnel
			if (cell.tunnelTo != null) {
				if (!visitedCells.contains(cell.tunnelTo)) {
					if (!exitFound) {
						recursiveBacktrack(cell.tunnelTo);
					}
				}
			}
			//if north cell accessable and unvisited
			if (cell.wall[2].present == false) {
				if (!visitedCells.contains(cell.neigh[2])) {
					if (!exitFound) {
						recursiveBacktrack(cell.neigh[2]);
					}
				}
			}
			//if east cell accessable and unvisited
			if (cell.wall[0].present == false) {
				if (!visitedCells.contains(cell.neigh[0])) {
					if (!exitFound) {
						recursiveBacktrack(cell.neigh[0]);
					}
				}
			}
			//if south cell accessable and unvisited
			if (cell.wall[5].present == false) {
				if (!visitedCells.contains(cell.neigh[5])) {
					if (!exitFound) {
						recursiveBacktrack(cell.neigh[5]);
					}
				}
			}
			//if west cell accessable and unvisited
			if (cell.wall[3].present == false) {
				if (!visitedCells.contains(cell.neigh[3])) {
					if (!exitFound) {
						recursiveBacktrack(cell.neigh[3]);
					}
				}
			}
			//if this recursive iteration does not yield an exit, it must be removed from the final exit path
			if (!exitFound) {
				pathToExit.remove(cell);		
			}
		}
		else {
			return;
		}	
	}

	@Override
	public boolean isSolved() {
		
		boolean solved = true;
				
		//if first element in the path does not match the entrance cell...
		if (pathToExit.get(0) != entranceCell) {
			solved = false;
		}
		//if last element in the path does not match the exit cell...
		if (pathToExit.get(pathToExit.size() - 1) != exitCell) {

			solved = false;
		}
		//if cells cannot form path to exit in the given order then...
		for (int i = 0; i != pathToExit.size() - 2; i++) {
			if ((pathToExit.get(i).r != pathToExit.get(i + 1).r) && (pathToExit.get(i).c != pathToExit.get(i + 1).c)) {
				//makes sure disconnected cells are not tunnels before changing to false
				if (pathToExit.get(i).tunnelTo == null) {
					solved = false;
				}
			}
		}
		return solved;
	} // end if isSolved()


	@Override
	public int cellsExplored() {
		return visitedCells.size();
	} // end of cellsExplored()

} // end of class RecursiveBackTrackerSolver
