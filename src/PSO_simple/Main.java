package PSO_simple;


import PSO_simple_new.PSO;
import base.Tag;
import net.sourceforge.jswarm_pso.Swarm;

import org.apache.log4j.Logger;
import util.Method;
import util.Parameter;
import util.Particle;
import util.PathFactory;

import java.util.List;

/**
 * http://jswarm-pso.sourceforge.net/
 */
public class Main {
    private static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        for(Parameter.rf=1.5;Parameter.rf<=1.81;Parameter.rf+=0.1) {
            System.out.println("rf is :"+Parameter.rf);
            System.out.println("PSO_RS------------------------------------");
            for (int i = 1; i <=30; i++) {
                run_RS(PathFactory.createPath("normal"));
            }
            System.out.println("PSO_AOCM----------------------------------");
            for (int i = 1; i <=30; i++) {
                run_AOCM(PathFactory.createPath("normal"));
            }
            System.out.println("PSO_CM------------------------------------");
            for (int i = 1; i <=30; i++) {
                run_CM(PathFactory.createPath("normal"));
            }
        }
    }

    public static double run_RS(List<Tag> tagList) {
        logger.info("-----start PSO_Single_RS-----");
        logger.info("-----readerNum: " + Parameter.readerNum + "-----");

        Particle.particleNum = Parameter.readerNum * 2;
        // Pass the tag in
        RS.tagList = tagList;
        Swarm swarm = new Swarm(PSO.particle_num, new Particle(), new RS());
        // Set the monitoring area
        swarm.setMaxPosition(Parameter.maxPosition);
        swarm.setMinPosition(Parameter.minPosition);
        // Optimize a few times
        for (int i = 0; i < Parameter.cycleNum; i++)
            swarm.evolve();
        // Print en results
//        System.out.println(Method.printSwarmInfo(swarm));
        logger.info(Method.printSwarmInfo(swarm));

        RS r = new RS();
        RS.tagList=tagList;
        double result = r.evaluate(swarm.getBestPosition());
        System.out.printf("%.4f\n",result);

        return result;
    }

    public static double run_AOCM(List<Tag> tagList) {
        logger.info("-----start PSO_Single_AOCM-----");
        logger.info("-----readerNum: " + Parameter.readerNum + "-----");
        Particle.particleNum = Parameter.readerNum * 2;
        // Pass the tag in
        AOCM.tagList = tagList;
        Swarm swarm = new Swarm(PSO.particle_num, new Particle(), new AOCM());
        // Set the monitoring area
        swarm.setMaxPosition(Parameter.maxPosition);
        swarm.setMinPosition(Parameter.minPosition);
        // Optimize a few times
        for (int i = 0; i < Parameter.cycleNum; i++)
            swarm.evolve();
        // Print en results
//        System.out.println(Method.printSwarmInfo(swarm));
        logger.info(Method.printSwarmInfo(swarm));
        AOCM r = new AOCM();
        AOCM.tagList=tagList;
        double result = r.evaluate(swarm.getBestPosition());
        System.out.printf("%.4f\n",result);
        return result;
    }

    public static double run_CM(List<Tag> tagList) {
        logger.info("-----start PSO_Single_CM-----");
        logger.info("-----readerNum: " + Parameter.readerNum + "-----");
        Particle.particleNum = Parameter.readerNum * 2;
        // Pass the tag in
        CM.tagList = tagList;
        Swarm swarm = new Swarm(PSO.particle_num, new Particle(), new CM());
        // Set the monitoring area
        swarm.setMaxPosition(Parameter.maxPosition);
        swarm.setMinPosition(Parameter.minPosition);
        // Optimize a few times
        for (int i = 0; i < Parameter.cycleNum; i++)
            swarm.evolve();
        // Print en results
//        System.out.println(Method.printSwarmInfo(swarm));
        CM r = new CM();
        CM.tagList = tagList;
        double result = r.evaluate(swarm.getBestPosition());
        System.out.printf("%.4f\n",result);
        return result;
    }
    public static double run_BPSO(List<Tag> tagList) {
        PSO.process(tagList, "single");
//        System.out.println(PSO.getBestFitness());
        System.out.println(PSO.getBestFitness());
        return PSO.getBestFitness();
    }
}
