package strategies;

import state.State;
import state.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Toma-Florin Ungureanu
 */
public class BKTStrategy implements IStrategy
{
    private List<State> allStates = new ArrayList<State>();
    private List<StateNode> allNodes = new ArrayList<>();
    private State finalState;

    public ArrayList<Integer> applyStrategy(State state,  ArrayList<State> states)
    {
        this.finalState = new State(state.getBoatCapacity(), 0, 0, 2, state.getCannibals(),
                state.getMissionaries());

        allStates.add(state);
        StateNode parentNode = new StateNode();
        allNodes.add(parentNode);
        parentNode.setCurrentState(state);
        recurse(parentNode);

        StateNode lastNode = new StateNode();
        for(StateNode stateNode: allNodes)
        {
            if(stateNode.getCurrentState().equals(this.finalState))
            {
                lastNode = stateNode;
                break;
            }
        }

        if(lastNode.getCurrentState() == null || lastNode.getParentNode() == null)
        {
            System.out.println("Solution not found!");
        }
        else
        {

            while (lastNode.getParentNode() != null)
            {
                states.add(lastNode.getCurrentState());
                lastNode = lastNode.getParentNode();
            }
            states.add(parentNode.getCurrentState());
        }

        return null;
    }

    public ArrayList<State> getNextPossibleStates(State state)
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
        if (missionaries != 0)
        {
            for (int missionary = 0; missionary <= missionaries; missionary++)
            {
                helper(missionary, cannibals, boatCapacity, shore, state, possibilities);
            }
        }
        else
        {
            helper(0, cannibals, boatCapacity, shore, state, possibilities);
        }
        return possibilities;
    }

    private boolean recurse(StateNode parentNode)
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
                StateNode newNode = new StateNode(possibility, parentNode);
                parentNode.getChildNodes().add(newNode);
                allStates.add(possibility);
                allNodes.add(newNode);
                recurse(newNode);
            }
        }
        return true;
    }

    private void helper(int missionary, int cannibals, int boatCapacity, int shore,
                                    State state, ArrayList<State> possibilities)
    {
        Utils utils = new Utils();
        State newState;

        for (int cannibal = 0; cannibal <= cannibals; cannibal++)
        {
            if(cannibal + missionary <= boatCapacity)
            {
                {
                    if (shore == 1)
                    {
                        missionary = -missionary;
                        cannibal = -cannibal;
                    }
                    if (utils.validation(state, missionary, cannibal))
                    {
                        newState = state.transition(missionary, cannibal);
                        possibilities.add(newState);
                    }
                    if (shore == 1)
                    {
                        missionary = -missionary;
                        cannibal = -cannibal;
                    }
                }
            }
        }
    }
}