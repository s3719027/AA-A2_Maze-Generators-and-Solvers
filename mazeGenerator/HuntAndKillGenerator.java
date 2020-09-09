package mazeGenerator;

import maze.Maze;
import maze.Cell;
import maze.Wall;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import java.util.Random;

/*
@author Daniel Bound
*/

public class HuntAndKillGenerator implements MazeGenerator {

	@Override
	public void generateMaze(Maze maze) {
		
		/*
		RANDOMLY choose start target cell
		WHILE there are still unvisited cells
			REMOVE target cell from unvisited cells
			RANDOMLY order cell directions in list
			IF target cell is tunnel
				CHANGE target cell to tunnels location
			ELSE
				ITERATE through cell directions list 
				IF any direction can be traversed 
					CHANGE target cell to neighbour
					BREAK the wall
				ELSE
					RESCAN map for unvisited cell adjacent to visited cell, 
						break the wall and make target cell
		END WHILE		
		*/

		List<Cell> unvisitedCells = new ArrayList<Cell>();
		//contains the integer values for NORTH, EAST, SOUTH and WEST
		List<Integer> cellDirections = new ArrayList<Integer>();
		Random random = new Random(System.currentTimeMillis());
		Cell targetCell; //the cell that is currently hunting
		
		//add all cells to unvisited ArrayList
		for (int i = 0; i != maze.sizeR; i++) {
			for (int j = 0; j != maze.sizeC; j++) {
				unvisitedCells.add(maze.map[i][j]);
			}
		}
		
		//initializes targetCell to random cell in unvisitedCells ArrayList
		targetCell = unvisitedCells.get(random.nextInt(unvisitedCells.size()));
		
		while (!unvisitedCells.isEmpty()) {
			
			unvisitedCells.remove(targetCell);
			
			//resets and adds directions to cellDirections
			cellDirections.clear();
			for (int i = 0; i != 6; i++) {
				if (i != 1 && i != 4) {
					cellDirections.add(new Integer(i));
				}
			}
			//randomise cellDirections so hunt is performed in a random order
			Collections.shuffle(cellDirections);
			
			//check if cell is an unvisited tunnel first
			if (unvisitedCells.contains(targetCell.tunnelTo)) {
				targetCell = targetCell.tunnelTo;
			}
			//otherwise, proceed to hunt
			else {
				
				boolean deadEnd = true;
				int direction;
				for (int i = 0; i != 4; i++) {
					direction = cellDirections.get(i).intValue();
					//check each cell direction one by one that has already previously been randomised
					if (unvisitedCells.contains(targetCell.neigh[direction])) {
						
						deadEnd = false;
						
						if (direction > 2) {
							targetCell.neigh[direction].wall[direction - 3].present = false;
						}
						else {
							targetCell.neigh[direction].wall[direction + 3].present = false;
						}
						targetCell.wall[direction].present = false;
						targetCell = targetCell.neigh[direction];
						break;
					}
				}			
				//if deadend, rescan maze for cell next to visited one and break the wall
				boolean newCellFound = false;
				if (deadEnd) {
					for (int i = 0; i != maze.sizeR; i++) {
						for (int j = 0; j != maze.sizeC; j++) {
							//if cell is unvisited and cell above is visited then break wall
							if (i != maze.sizeR - 1 && !newCellFound) {
								if (unvisitedCells.contains(maze.map[i][j]) && !unvisitedCells.contains(maze.map[i + 1][j])) {
									targetCell = maze.map[i][j];
									targetCell.wall[2].present = false;
									targetCell.neigh[2].wall[5].present = false;
									newCellFound = true;
								}
							}
							//if cell is unvisited and cell on the right is visited then break wall
							if (j != maze.sizeC - 1 && !newCellFound) {
								if (unvisitedCells.contains(maze.map[i][j]) && !unvisitedCells.contains(maze.map[i][j + 1])) {
									targetCell = maze.map[i][j];
									targetCell.wall[0].present = false;
									targetCell.neigh[0].wall[3].present = false;
									newCellFound = true;
								}
							}
							//if cell is unvisited and cell below is visited then break wall
							if (i != 0 && !newCellFound) {
								if (unvisitedCells.contains(maze.map[i][j]) && !unvisitedCells.contains(maze.map[i - 1][j])) {
									targetCell = maze.map[i][j];
									targetCell.wall[5].present = false;
									targetCell.neigh[5].wall[2].present = false;
									newCellFound = true;
								}
							}
							//if cell is unvisited and cell on the left is visited then break wall
							if (j != 0 && !newCellFound) {
								if (unvisitedCells.contains(maze.map[i][j]) && !unvisitedCells.contains(maze.map[i][j - 1])) {
									targetCell = maze.map[i][j];
									targetCell.wall[3].present = false;
									targetCell.neigh[3].wall[0].present = false;
									newCellFound = true;
								}
							}
						}
					}
				}
			}
		}
	} // end of generateMaze()

} // end of class HuntAndKillGenerator
