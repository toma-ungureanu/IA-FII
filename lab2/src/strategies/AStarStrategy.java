package strategies;

import org.jetbrains.annotations.NotNull;
import state.State;
import state.StateNode;
import state.StateNodeHeuristic;
import utils.Heuristic;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class AStarStrategy implements IStrategy
{
    private List<State> allStates = new ArrayList<>();
    private List<StateNodeHeuristic> allNodes = new ArrayList<>();
    private State finalState;

    @Override
    public ArrayList<Integer> applyStrategy(State state, ArrayList<State> states)
    {
        this.finalState = new State(state.getBoatCapacity(), 0, 0, 2, state.getCannibals(),
                state.getMissionaries());

        allStates.add(state);
        StateNodeHeuristic parentNode = new StateNodeHeuristic();
        allNodes.add(parentNode);
        parentNode.setCurrentState(state);
        recurse(parentNode);
        Heuristic heuristic = new Heuristic();
        heuristic.firstH(parentNode);

        return null;
    }

    ArrayList<State> getNextPossibleStates(@NotNull State state)
    {
        int boatCapacity = state.getBoatCapacity();
        int shore = state.getShore();
        int missionaries, cannibals;
        if (shore == 1)
        {
            missionaries = state.getMissionaries();
            cannibals = state.getCannibals();
        }
        else
        {
            missionaries = state.getMissionariesAcross();
            cannibals = state.getCannibalsAcross();
        }

        ArrayList<State> possibilities = new ArrayList<>();
        Utils utils = new Utils();
        if (missionaries != 0)
        {
            for (int missionary = 0; missionary <= missionaries; missionary++)
            {
                utils.transitionHelper(missionary, cannibals, boatCapacity, shore, state, possibilities);
            }
        }
        else
        {
            utils.transitionHelper(0, cannibals, boatCapacity, shore, state, possibilities);
        }
        return possibilities;
    }

    private boolean recurse(StateNodeHeuristic parentNode)
    {
        if(this.allStates.contains(this.finalState))
        {
            return true;
        }

        List<State> possibilities = getNextPossibleStates(parentNode.getCurrentState());
        for(State possibility: possibilities)
        {
            if(!allStates.contains(possibility))
            {
                StateNodeHeuristic newNode = new StateNodeHeuristic(possibility, parentNode);
                parentNode.getChildNodes().add(newNode);
                allStates.add(possibility);
                allNodes.add(newNode);
                recurse(newNode);
            }
        }
        return true;
    }

}
