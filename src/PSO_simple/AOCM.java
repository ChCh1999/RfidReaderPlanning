package PSO_simple;

import base.Reader;
import base.Tag;
import fitness.SingleFitness;
import net.sourceforge.jswarm_pso.FitnessFunction;
import util.Method;

import java.util.List;

/**
 * Always On Coverage Maximum strategy,
 * always open all readers in all slots
 */
public class AOCM extends FitnessFunction {
    public static List<Tag> tagList;

    @Override
    public double evaluate(double[] position) {
        List<Reader> readerList = Method.Position2ReaderList(position);
        return SingleFitness.AOCM(readerList, tagList);
    }
}
