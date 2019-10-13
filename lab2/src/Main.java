import solver.Solver;
import state.State;
import strategies.BKTStrategy;
import strategies.IDDFSStrategy;
import strategies.IStrategy;
import strategies.RandomStrategy;

public class Main
{
    public static void main(String[] args)
    {
        System.out.println("Hello World!");
        IStrategy randomStrat = new RandomStrategy();
        IStrategy bktStrat = new BKTStrategy();
        IStrategy iddfsStrat = new IDDFSStrategy();
        State state = new State(2, 3, 3, 1, 0, 0);
        Solver solver = new Solver(state, randomStrat);
        //solver.solve();
        //solver.solve(state, bktStrat);
        solver.solve(state, iddfsStrat);
    }
}
