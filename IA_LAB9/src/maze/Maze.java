package maze;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.util.Pair;

public class Maze
{
	public static final int OBSTACLE_VALUE = -10;
	public static final int START_VALUE = 1;
	public static final int EXIT_VALUE = 100;
	private int width;
	private int height;
	private Pair<Integer, Integer> start;
	private Pair<Integer, Integer> exit;
	private List<List<Integer>> matrix;

	/**
	 * Constructor for n * n matrix
	 *
	 * @param size : the size of the matrix
	 */
	public Maze(int size)
	{
		setHeight(size + 2);
		setWidth(size + 2);
		initMatrix();
	}

	/**
	 * Constructor used to initialized with a certain given matrix
	 *
	 * @param matrix : the given matrix which IS ALREADY bordered
	 */
	public Maze(List<List<Integer>> matrix)
	{
		setHeight(matrix.size());
		setWidth(matrix.get(0).size());
		setMatrix(matrix);
	}

	/**
	 * Default constructor for when we read a maze from file
	 */
	public Maze() { }

	/**
	 * Constructor for n * m matrix
	 *
	 * @param width  : the width of the matrix
	 * @param height : height of the matrix
	 */
	public Maze(int width, int height)
	{
		setWidth(width);
		setHeight(height);
		initMatrix();
	}

	/**
	 * Function after the size of the matrix is known in order to initialize it
	 * Add 2 lines and columns as borders to the matirix
	 */
	public void initMatrix()
	{
		this.matrix = new ArrayList<>(height);
		for (int j = 0; j < this.height; j++)
		{
			this.matrix.add(j, new ArrayList<>(width));
		}

		for (int i = 0; i < this.height; i++)
		{
			for (int j = 0; j < this.width; j++)
			{
				matrix.get(i).add(j, 0);
			}
		}
	}

	public void printMaze()
	{
		for (int i = 0; i < this.height; i++)
		{
			for (int j = 0; j < this.width; j++)
			{
				System.out.print(this.matrix.get(i).get(j) + "\t");
			}
			System.out.println();
		}
		System.out.println("EXIT:"  + this.exit.toString().replace("=",","));
		System.out.println("START: " + this.start.toString().replace("=",","));
		System.out.println();
	}

	public void ensureAllValues(int value)
	{
		int newRow = new Random().nextInt(this.height);
		int newCol = new Random().nextInt(this.width);
		int cellVal = matrix.get(newRow).get(newCol);
		if(cellVal != 0)
		{
			while (cellVal != 0)
			{
				newRow = new Random().nextInt(this.height);
				newCol = new Random().nextInt(this.width);
				cellVal = matrix.get(newRow).get(newCol);
			}
		}

		if(value == START_VALUE)
		{
			setStart(new Pair<>(newRow,newCol));
		}
		else if(value == EXIT_VALUE)
		{
			setExit(new Pair<>(newRow,newCol));
		}

		matrix.get(newRow).set(newCol, value);
	}

	public void addObstacle(int row, int column)
	{
		int cellVal = matrix.get(row).get(column);
		int newRow = 0, newCol = 0;
		if(cellVal == 0)
		{
			matrix.get(row).set(column, OBSTACLE_VALUE);
		}
		else
		{
			ensureAllValues(OBSTACLE_VALUE);
		}
	}

	public void setStart(int startRow, int startColumn)
	{
		int cellVal = matrix.get(startRow).get(startColumn);
		if(cellVal == 0)
		{
			setStart(new Pair<>(startRow,startColumn));
			this.matrix.get(startRow).set(startColumn, START_VALUE);
		}

		else
		{
			ensureAllValues(START_VALUE);
		}
	}

	public void setExit(int exitRow, int exitColumn)
	{
		int cellVal = matrix.get(exitRow).get(exitColumn);
		if(cellVal == 0)
		{
			this.exit = new Pair<>(exitRow, exitColumn);
			this.matrix.get(exitRow).set(exitColumn, EXIT_VALUE);
		}
		else
		{
			ensureAllValues(EXIT_VALUE);
		}
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	public void setMatrix(List<List<Integer>> matrix)
	{
		this.matrix = matrix;
	}

	public List<List<Integer>> getMatrix()
	{
		return this.matrix;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public void saveMaze(String filename)
	{
		try
		{
			FileOutputStream file = new FileOutputStream(filename);
			ObjectOutputStream out = new ObjectOutputStream(file);
			out.writeObject(this.matrix);

			out.close();
			file.close();

		}
		catch (IOException error)
		{
			error.printStackTrace();
		}
	}

	public void loadMaze(String filename)
	{
		try
		{
			// Reading the object from a file
			FileInputStream file = new FileInputStream(filename);
			ObjectInputStream in = new ObjectInputStream(file);

			// Method for deserialization of object
			setMatrix((List<List<Integer>>) in.readObject());

			setHeight(this.matrix.size());
			setWidth(this.matrix.get(0).size());
			for(int i = 0; i < matrix.size(); i++)
			{
				for(int j = 0; j < matrix.get(i).size(); j++)
				{
					if(matrix.get(i).get(j) == START_VALUE)
					{
						setStart(new Pair<>(i,j));
					}

					else if(matrix.get(i).get(j) == EXIT_VALUE)
					{
						setExit(new Pair<>(i,j));
					}
				}
			}

			in.close();
			file.close();
		}

		catch(IOException ex)
		{
			System.out.println("IOException is caught");
		}

		catch(ClassNotFoundException ex)
		{
			System.out.println("ClassNotFoundException is caught");
		}
	}

	public Pair<Integer, Integer> getStart()
	{
		return this.start;
	}

	public Pair<Integer, Integer> getExit()
	{
		return this.exit;
	}

	public void setStart(Pair<Integer, Integer> start)
	{
		this.start = start;
	}

	public void setExit(Pair<Integer, Integer> exit)
	{
		this.exit = exit;
	}
}
