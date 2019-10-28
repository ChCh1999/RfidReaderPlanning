package PSO_simple;

import base.Reader;
import base.Tag;
import fitness.SingleFitness;
import net.sourceforge.jswarm_pso.FitnessFunction;
import util.Method;

import java.util.*;

/**
 * Reader Scheduling strategy,
 * Use a greedy algorithm to open the reader with the most coverage tags at each time slot,
 * Close all other readers that may overlap it
 */
public class RS extends FitnessFunction{

    public static List<Tag> tagList;

    @Override
    public double evaluate(double[] position) {
        List<Reader> readerList = Method.Position2ReaderList(position);
        return SingleFitness.RS(readerList, tagList);
    }
}
