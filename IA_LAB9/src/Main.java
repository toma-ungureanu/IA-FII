import maze.Maze;
import qlearning.QLearning;

import java.util.Random;

public class Main
{

	public static void main(String[] args)
	{
		int width = 20;
		int height = 20;
		Maze maze = new Maze(width, height);

		Random random = new Random();
		for(int i = 0; i < (maze.getWidth() * maze.getHeight()) / 4; i++)
		{
			maze.addObstacle(random.nextInt(maze.getHeight()), random.nextInt(maze.getWidth()));
		}
		maze.setStart(random.nextInt(maze.getHeight()), random.nextInt(maze.getWidth()));
		maze.setExit(random.nextInt(maze.getHeight()), random.nextInt(maze.getWidth()));
		maze.printMaze();

		QLearning ql = new QLearning(maze);

		for(int exec = 0; exec < 10; exec++)
		{
			ql.init();
			ql.calculateQ();
//		    ql.printQ();
			ql.printPolicy();
		}
	}
}
