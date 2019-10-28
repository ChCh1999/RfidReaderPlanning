package PSO_multi;

import base.Location;
import base.Reader;
import base.Tag;
import fitness.MultiFitness;
import net.sourceforge.jswarm_pso.FitnessFunction;
import util.Method;
import util.Parameter;

import java.util.ArrayList;

import java.util.List;


public class RS_multi_PSO extends FitnessFunction {

    /**
     * main function
     */
    @Override
    public double evaluate(double[] position) {
        return MultiFitness.RS(position);
    }


}
