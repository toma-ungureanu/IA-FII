package state;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * @author Toma-Florin Ungureanu
 */
public class Utils
{
    public boolean isFinal(@NotNull State s)
    {
        return (s.getMissionaries() == 0 && s.getCannibals() == 0 && s.getShore() == 2);
    }

    public boolean validation(@NotNull State state, int missionaries, int cannibals)
    {
        State newState = new State(state.getBoatCapacity(),
                state.getMissionaries() + missionaries,
                state.getCannibals() + cannibals,
                3 - state.getShore(),
                state.getCannibalsAcross() - cannibals,
                state.getMissionariesAcross() - missionaries);

        return checker(newState, missionaries, cannibals);
    }

    private boolean checker(State state, int missionaries, int cannibals)
    {
        if ((state.getMissionaries() > 0) && (state.getMissionaries() < state.getCannibals()))
        {
            return false;
        }
        else if ((state.getMissionariesAcross() > 0) && (state.getMissionariesAcross() < state.getCannibalsAcross()))
        {
            return false;
        }
        else if (missionaries + cannibals > state.getBoatCapacity())
        {
            return false;
        }
        else if ((missionaries + cannibals > 0) && (missionaries >= 0) && (cannibals >= 0) && (state.getShore() == 2))
        {
            return false;
        }
        else if ((missionaries + cannibals < 0) && (missionaries <= 0) && (cannibals <= 0) && (state.getShore() == 1))
        {
            return false;
        }
        else if(missionaries == 0 && cannibals == 0)
        {
            return false;
        }
        return true;

    }

    @Contract(pure = true)
    public Utils()
    {
    }
}
