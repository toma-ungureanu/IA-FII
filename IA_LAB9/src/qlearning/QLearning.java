package qlearning;

import maze.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QLearning
{
	public static final int OBSTACLE_VALUE = -10;
	public static final int EXIT_VALUE = 100;
	public static final int START_VALUE = 1;
	private static final int TRAINING_CYCLES = 6500;

	private static final double ALPHA = 0.1; // Learning rate
	private static final double GAMMA = 0.9; // Eagerness - 0 looks in the near future, 1 looks in the distant future

	private int startState;
	private int statesCount;
	private Maze maze;
	private List<List<Integer>> rewards;
	private List<List<Double>> q;

	public QLearning(Maze maze)
	{
		this.maze = maze;
	}

	public void initRewards()
	{
		rewards = new ArrayList<>(this.statesCount);
		for (int i = 0; i < this.statesCount; i++)
		{
			rewards.add(new ArrayList<>());
		}

		for (int i = 0; i < this.statesCount; i++)
		{
			for (int j = 0; j < this.statesCount; j++)
			{
				rewards.get(i).add(0);
			}
		}
	}

	public void moveTo(int goLeftRight, int goUpDown, int k)
	{
		if (goLeftRight >= 0 && goLeftRight < this.maze.getWidth()
				&& goUpDown >= 0 && goUpDown < this.maze.getHeight())
		{
			int target = goUpDown * this.maze.getWidth() + goLeftRight;
			if (maze.getMatrix().get(goUpDown).get(goLeftRight) == 0 ||
					maze.getMatrix().get(goUpDown).get(goLeftRight) == 1)
			{
				rewards.get(k).set(target, 0);
			}
			else if (maze.getMatrix().get(goUpDown).get(goLeftRight) == EXIT_VALUE)
			{
				rewards.get(k).set(target, EXIT_VALUE);
			}
			else
			{
				rewards.get(k).set(target, OBSTACLE_VALUE);
			}
		}
	}

	public void init()
	{
		this.statesCount = this.maze.getHeight() * this.maze.getWidth();
		initRewards();

		q = new ArrayList<>(this.statesCount);

		int row = 0, col = 0;
		for (int k = 0; k < this.statesCount; k++)
		{
			row = k / this.maze.getWidth();
			col = k - row * this.maze.getWidth();

			for (int state = 0; state < this.statesCount; state++)
			{
				rewards.get(k).set(state, -1);
			}

			if (this.maze.getMatrix().get(row).get(col) != EXIT_VALUE &&
					this.maze.getMatrix().get(row).get(col) != OBSTACLE_VALUE)
			{
				// Try to move left in the maze
				int goLeft = col - 1;
				moveTo(goLeft, row, k);

				// Try to move right in the maze
				int goRight = col + 1;
				moveTo(goRight, row, k);

				// Try to move up in the maze
				int goUp = row - 1;
				moveTo(col, goUp, k);

				// Try to move down in the maze
				int goDown = row + 1;
				moveTo(col, goDown, k);
			}
		}
		initializeQ();
//		printR(this.rewards);
	}

	//Set Q values to R values
	void initializeQ()
	{
		for (int i = 0; i < statesCount; i++)
		{
			this.q.add(new ArrayList<>());
		}
		for (int i = 0; i < statesCount; i++)
		{
			for (int j = 0; j < statesCount; j++)
			{
				this.q.get(i).add(j, rewards.get(i).get(j).doubleValue());
			}
		}
	}

	// Used for debug
	void printR(List<List<Integer>> matrix)
	{
		System.out.printf("%25s", "States: ");
		for (int i = 0; i < statesCount; i++)
		{
			System.out.printf("%4s", i);
		}
		System.out.println();

		for (int i = 0; i < statesCount; i++)
		{
			System.out.print("Possible states from " + i + " :[");
			for (int j = 0; j < statesCount; j++)
			{
				System.out.printf("%4s", matrix.get(i).get(j));
			}
			System.out.println("]");
		}
	}

	public void calculateQ()
	{
		Random rand = new Random();

		for (int i = 0; i < TRAINING_CYCLES; i++)
		{
			int crtState = rand.nextInt(statesCount);
			while (!isFinalState(crtState))
			{
				List<Integer> actionsFromCurrentState = possibleActionsFromState(crtState);
				// Pick a random action from the ones possible
				if (actionsFromCurrentState.size() == 0)
				{
					break;
				}
				int index = rand.nextInt(actionsFromCurrentState.size());
				int nextState = actionsFromCurrentState.get(index);

				// Q(state,action)= Q(state,action) + alpha * (R(state,action) + gamma * Max(next state, all actions) - Q(state,action))
				double q = this.q.get(crtState).get(nextState);
				double maxQ = maxQ(nextState);
				int r = rewards.get(crtState).get(nextState);

				double value = q + ALPHA * (r + GAMMA * maxQ - q);
				this.q.get(crtState).set(nextState, value);

				crtState = nextState;
			}
		}
	}

	boolean isFinalState(int state)
	{
		int i = state / this.maze.getWidth();
		int j = state - i * this.maze.getWidth();

		return maze.getMatrix().get(i).get(j) == EXIT_VALUE;
	}

	List<Integer> possibleActionsFromState(int state)
	{
		ArrayList<Integer> result = new ArrayList<>();
		for (int i = 0; i < statesCount; i++)
		{
			if (rewards.get(state).get(i) != -1)
			{
				result.add(i);
			}
		}
		return result;
	}

	double maxQ(int nextState)
	{
		List<Integer> actionsFromState = possibleActionsFromState(nextState);
		double maxValue = -10;
		for (int nextAction : actionsFromState)
		{
			double value = this.q.get(nextState).get(nextAction);

			if (value > maxValue)
			{
				maxValue = value;
			}
		}
		return maxValue;
	}

	private void getStartState()
	{
		int i = this.maze.getStart().getKey();
		int j = this.maze.getStart().getValue();
		startState = i * this.maze.getHeight() + j;
	}
	public void printPolicy()
	{
		System.out.println("\nPrint policy");
		getStartState();
		int currentState = startState, nextState;
		while (!isFinalState(currentState))
		{
			nextState = getPolicyFromState(currentState);
			System.out.println("Go from: " + currentState + " to " + nextState);
			if(nextState == currentState)
			{
				System.out.println("Not found!");
				break;
			}
			currentState = getPolicyFromState(currentState);
		}
	}

	int getPolicyFromState(int state)
	{
		List<Integer> actionsFromState = possibleActionsFromState(state);

		double maxValue = Double.MIN_VALUE;
		int policyGotoState = state;

		// Pick to move to the state that has the maximum Q value
		for (int nextState : actionsFromState)
		{
			double value = this.q.get(state).get(nextState);

			if (value > maxValue)
			{
				maxValue = value;
				policyGotoState = nextState;
			}
		}
		return policyGotoState;
	}

	public void printQ()
	{
		System.out.println("Q matrix");
		for (int i = 0; i < this.q.size(); i++)
		{
			System.out.print("From state " + i + ":  ");
			for (int j = 0; j < this.q.get(i).size(); j++)
			{
				System.out.printf("%6.2f ", (this.q.get(i).get(j)));
			}
			System.out.println();
		}
	}
}