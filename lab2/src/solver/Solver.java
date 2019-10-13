package solver;
import org.jetbrains.annotations.Contract;
import state.State;
import state.Utils;
import strategies.BKTStrategy;
import strategies.IDDFSStrategy;
import strategies.IStrategy;
import strategies.RandomStrategy;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Toma-Florin Ungureanu
 */
public class Solver
{
    private State state;
    private IStrategy strategy;

    @Contract(pure = true)
    public Solver(State startingState, IStrategy strategy)
    {
        this.state = startingState;
        this.strategy = strategy;
    }

    public void solve()
    {
        Utils utils = new Utils();
        boolean flag;
        int counter = 0;
        ArrayList<State> states = new ArrayList<>();
        states.add(this.state);
        if(this.strategy instanceof RandomStrategy)
        {
            while(!utils.isFinal(this.state) || counter < 1000)
            {
                counter++;
                flag = false;
                ArrayList<Integer> strategyResult = this.strategy.applyStrategy(this.state, null);
                int missionaries = strategyResult.get(0);
                int cannibals = strategyResult.get(1);

                if(utils.validation(state, missionaries, cannibals))
                {
                    State newState = this.state.transition(missionaries, cannibals);
//                if (Collections.frequency(states, newState) > 10)
//                {
//                    flag = true;
//                }
                    if (!flag)
                    {
                        states.add(newState);
                        this.state = newState;
                        System.out.println(this.state);
                    }
                }
            }
            System.out.println("Number of tries: " + counter);
            System.out.println("Number of valid transitions: " + states.size());
        }
        else if(this.strategy instanceof BKTStrategy)
        {
            states.clear();
            this.strategy.applyStrategy(this.state, states);
            if(!states.isEmpty())
            {
                Collections.reverse(states);
                System.out.println("Solution is:");
                for (State state: states)
                {
                    System.out.println(state);
                }
            }
        }
        else if(this.strategy instanceof IDDFSStrategy)
        {
            this.strategy.applyStrategy(this.state, null);
        }
    }

    public void solve(State state, IStrategy strategy)
    {
        this.state = state;
        this.strategy = strategy;
        solve();
    }
}
