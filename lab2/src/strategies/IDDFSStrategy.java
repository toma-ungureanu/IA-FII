package strategies;

import state.State;

import java.util.ArrayList;

/**
 * @author Toma-Florin Ungureanu
 */
public class IDDFSStrategy implements IStrategy
{
    public ArrayList<Integer> applyStrategy(State state,  ArrayList<State> states)
    {
        int missionaries = 0, cannibals = 0;
        //apply strategy
        ArrayList<Integer> returnArray = new ArrayList<>();
        returnArray.add(missionaries);
        returnArray.add(cannibals);

        return returnArray;
    }
}
