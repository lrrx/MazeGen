package core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MazeChunkGenerator {
	
	static int[][] blocks = {
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
	};
	static List<int[]> posStack = new ArrayList<int[]>();
	static int[] startPos = {1,1};
	static Random random;
	static Integer nextMove;
	static int stepSize = 2;
	static Integer[][] move = {{0,-1},{1,0},{0,1},{-1,0}};
	static int[] currentPos = startPos;
	
	public static int[][] generateMaze(double chunkNoise) {
		random = new Random((long) (chunkNoise * 2147483647D));
		
		currentPos = startPos;
		
		blocks = new int[][] {
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
		};
		//posStack.add(new int[] {22, 22});
		//posStack.add(new int[] {3, 4});
		
		while(true) {
			ArrayList<Integer> moveDirectionOptions = new ArrayList<Integer>();
			
			moveDirectionOptions = getAllDirectionOptions();
			
			//debug(moveDirectionOptions.toString());
			if(moveDirectionOptions.size() > 0) {
				int moveChoice = random.nextInt(moveDirectionOptions.size());
				nextMove = moveDirectionOptions.get(moveChoice);
				//System.out.print(moveChoice + " - (" + currentPos[0] + "|" + currentPos[1] + ") -> " + move[nextMove][0] + "," + move[nextMove][1] + " -> ");
				blocks[currentPos[0] + move[nextMove][0]][currentPos[1] + move[nextMove][1]] = 1;
				currentPos[0] += move[nextMove][0] * stepSize;
				currentPos[1] += move[nextMove][1] * stepSize;
				blocks[currentPos[0]][currentPos[1]] = 1;
				posStack.add(new int[] {currentPos[0], currentPos[1]});
				//displayList(posStack);
			}
			else {
				//System.out.print("(" + currentPos[0] + "|" + currentPos[1] + ") ----> ");
				
				if(posStack.size() >= 1) {
					posStack.remove(posStack.size() - 1);
				}
				if(posStack.size() == 0) {
					break;
				}
				currentPos = posStack.get(posStack.size() - 1);
				//System.out.println("(" + currentPos[0] + "|" + currentPos[1] + ")");
			}
		}
		//display();
		//System.out.println("done");
		return blocks;
	}
	
	public static ArrayList<Integer> getAllDirectionOptions(){
		ArrayList<Integer> directionOptionList = new ArrayList<Integer>();
		for(int i = 0; i <= 3; i++) {
			if(check(i)) {
				directionOptionList.add(i);
			}
		}
		return directionOptionList;
	}
	
	public static void display() {
		System.out.println("Maze:");
		for(int y = 0; y <= 15; y++) {
			for(int x = 0; x <= 15; x++) {
				System.out.print(String.valueOf(blocks[x][y]).replace("1", " ").replace("0", "█"));
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public static void displayList(List<int[]> list) {
		for(int[] n : list) {
			System.out.println("[" + n[0] + "|" + n[1] + "]");
		}
	}
	
	public static void debug(Object object) {
		System.out.println("DEBUG: " + object.toString());
	}
	
	public static boolean check(int direction) {
		//debug("check");
		//System.out.println(direction);
		if(direction == 0) {
				if(currentPos[1] > 1) {
					if(blocks[currentPos[0] + 0][currentPos[1] - stepSize] == 0) {
						return true;
					}
				}
		}else if(direction == 1) {
				if(currentPos[0] < 15) {
					if(blocks[currentPos[0] + stepSize][currentPos[1] + 0] == 0) {
						return true;
					}
				}
		}else if(direction == 2) {
				if(currentPos[1] < 15) {
					if(blocks[currentPos[0] + 0][currentPos[1] + stepSize] == 0) {
						return true;
					}
				}
		}else if(direction == 3) {
				if(currentPos[0] > 1) {
					if(blocks[currentPos[0] - stepSize][currentPos[1] + 0] == 0) {
						return true;
					}
				}
		}
		
		return false;
	}
}
