package mazeGenerator;

import maze.Maze;
import maze.Cell;
import maze.Wall;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/*
@author Daniel Bound
*/

public class KruskalGenerator implements MazeGenerator {

	@Override
	public void generateMaze(Maze maze) {
		
		/*
		Instead of using the tree method for determining connected and disconnected paths via 
		disjointed trees, I have used unique identifers given to each cell to determined 
		connectedness. If a cells neighbour has a different unique identifier, a path to that cell
		cannot be carved. Once this has been determined, the wall is broken and every value in the 
		map with the same identifer as the neighbour has its identifer set to the value of the 
		current cell. This ensures that when looking again, no loops can be formed.
		
		Private class can also be found at the bottom that defines an Edge object.
		*/
		
		int cellIdentifiers[][] = new int[maze.sizeC][maze.sizeR];
		List<Edge> edges = new ArrayList<Edge>();
		
		//initialise unique cell identiifiers
		for (int i = 0; i != maze.sizeR; i++) {
			for (int j = 0; j != maze.sizeC; j++) {
				cellIdentifiers[i][j] = (i * maze.sizeR) + j;
			}
		}

		//initialise edges
		for (int i = 0; i != maze.sizeR; i++) {
			for (int j = 0; j != maze.sizeC; j++) {
				
				//if not top row of map
				if (i != maze.sizeR - 1) {
					edges.add(new Edge(maze.map[i][j], maze.map[i + 1][j]));
				}
				//if not right edge of map
				if (j != maze.sizeC - 1) {
					edges.add(new Edge(maze.map[i][j], maze.map[i][j + 1]));
				}
				
				//if cell has tunnel, change tunnel destination identifier to entry identifier
				if (maze.map[i][j].tunnelTo != null) {
					cellIdentifiers[maze.map[i][j].tunnelTo.r][maze.map[i][j].tunnelTo.c] = cellIdentifiers[maze.map[i][j].r][maze.map[i][j].c];
				}
			}
		}

		Random random = new Random(System.currentTimeMillis());
		int randomNum = 0;
		Edge currEdge;
		
		while (!edges.isEmpty()) {

			randomNum = random.nextInt(edges.size());
			currEdge = edges.get(randomNum);
			
			int currR = currEdge.getIndexCell().r;
			int currC = currEdge.getIndexCell().c;
			int neighbourR = currEdge.getNeighbourCell().r;
			int neighbourC = currEdge.getNeighbourCell().c;
			
			int currIdentifier = cellIdentifiers[currR][currC];
			int neighbourIdentifier = cellIdentifiers[neighbourR][neighbourC];
			
			//if neighbour has a different identifier to current one then...
			if (currIdentifier != neighbourIdentifier) {
				//change all of neighbours identifiers to current one
				for (int i = 0; i != maze.sizeR; i++) {
					for (int j = 0; j != maze.sizeC; j++) {
						if (cellIdentifiers[i][j] == neighbourIdentifier) {
							cellIdentifiers[i][j] = currIdentifier;
						}
					}
				}
				//remove edge from list
				edges.remove(currEdge);
				
				//if the edge is joining two horizontal cells
				if (currR != neighbourR) {
					currEdge.getIndexCell().wall[2].present = false;
					currEdge.getNeighbourCell().wall[5].present = false;
				}
				//if the edge is joining two vertical cells
				else if (currC != neighbourC){
					currEdge.getIndexCell().wall[0].present = false;
					currEdge.getNeighbourCell().wall[3].present = false;
				}
			}
			else {
				edges.remove(currEdge);
			}
		}
	} // end of generateMaze()
	
	/*
	A private class that defines an edge by a pair of adjacent cells.
	One cell is given the name index cell and the other is the neighbour.
	*/
	private class Edge {
		
		private Cell indexCell, neighbourCell;
		
		public Edge(Cell indexCell, Cell neighbourCell) {
			this.indexCell = indexCell;
			this.neighbourCell = neighbourCell;
		}
		public Cell getIndexCell() {
			return this.indexCell;
		}
		public Cell getNeighbourCell() {
			return this.neighbourCell;
		}
	} // end of generateMaze()

} // end of class KruskalGenerator
