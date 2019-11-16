package PSO_multi;


import PSO_simple_new.PSO;
import base.Tag;
import fitness.MultiFitness;
import net.sourceforge.jswarm_pso.Swarm;
import org.apache.log4j.Logger;
import util.Method;
import util.Parameter;
import util.Particle;
import util.PathFactory;

import java.nio.file.Path;
import java.util.List;

public class Main_multi_PSO {
    private static Logger logger = Logger.getLogger(Main_multi_PSO.class);

    public static void main(String[] args) {

        /*for(Parameter.readerNum=5;Parameter.readerNum<=20;Parameter.readerNum++) {
            System.out.printf("Readernum:%d PSO-AOCM\n",Parameter.readerNum);
            for(int i=0;i<10;i++) {
                run_AOCM(PathFactory.createPath("normal"));
            }
        }

        for(Parameter.readerNum=5;Parameter.readerNum<=20;Parameter.readerNum++) {
            System.out.printf("Readernum:%d PSO-RS\n",Parameter.readerNum);
            for(int i=0;i<10;i++) {
                run_RS(PathFactory.createPath("normal"));
            }
        }

        for(Parameter.readerNum=5;Parameter.readerNum<=20;Parameter.readerNum++) {
            System.out.printf("Readernum:%d PSO-CM\n",Parameter.readerNum);
            for(int i=0;i<10;i++) {
                run_CM(PathFactory.createPath("normal"));
            }
        }
*/
        //run_CM(PathFactory.createPath("normal"));
        //run_AOCM(PathFactory.createPath("normal"));
        //run_RS(PathFactory.createPath("normal"));
        run_BPSO(PathFactory.createPath("normal"));
    }

    public static double[] run_RS(List<Tag> tagList) {
        logger.info("-----start PSO_Multi_RS-----");
        logger.info("-----readerNum: " + Parameter.readerNum + "-----");
        Particle.particleNum = Parameter.readerNum * 2;

        // Pass the tag in
        MultiFitness.globalTagList = tagList;
        Swarm swarm = new Swarm(Swarm.DEFAULT_NUMBER_OF_PARTICLES, new Particle(), new RS_multi_PSO());

        // Set the monitoring area
        swarm.setMaxPosition(Parameter.maxPosition);
        swarm.setMinPosition(Parameter.minPosition);
        // Optimize a few times
        for (int i = 0; i < Parameter.cycleNum; i++)
            swarm.evolve();
        // Print en results
//        System.out.println(Method.printSwarmInfo(swarm));
        logger.info(Method.printSwarmInfo(swarm));

        // Then run the final result again to update the task completion degree
        RS_multi_PSO r = new RS_multi_PSO();
        double average = r.evaluate(swarm.getBestPosition());
        System.out.printf("%.4f %.4f %.4f %.4f %.4f\n",
                MultiFitness.task1CompletionDegree,
                MultiFitness.task2CompletionDegree,
                MultiFitness.task3CompletionDegree,
                MultiFitness.task4CompletionDegree,
                average);


        logger.info("Task 1 completion:" + MultiFitness.task1CompletionDegree);
        logger.info("Task 2 completion:" + MultiFitness.task2CompletionDegree);
        logger.info("Task 3 completion:" + MultiFitness.task3CompletionDegree);
        logger.info("Task 4 completion:" + MultiFitness.task4CompletionDegree);
//        logger.info("Average task completion:" +
//                (MultiFitness.task1CompletionDegree + MultiFitness.task2CompletionDegree
//                        + MultiFitness.task3CompletionDegree + MultiFitness.task4CompletionDegree) / 4
//        );

        logger.info("-----end PSO_Multi_RS-----");
        return new double[]{average, MultiFitness.task1CompletionDegree, MultiFitness.task2CompletionDegree
                , MultiFitness.task3CompletionDegree, MultiFitness.task4CompletionDegree};
    }

    public static double[] run_AOCM(List<Tag> tagList) {

        logger.info("-----start PSO_Multi_AOCM-----");
        logger.info("-----readerNum: " + Parameter.readerNum + "-----");

        Particle.particleNum = Parameter.readerNum * 2;

        // Pass the tag in
        AOCM_multi_PSO.globalTagList = tagList;
        Swarm swarm = new Swarm(Swarm.DEFAULT_NUMBER_OF_PARTICLES, new Particle(), new AOCM_multi_PSO());

        // Set the monitoring area
        swarm.setMaxPosition(Parameter.maxPosition);
        swarm.setMinPosition(Parameter.minPosition);
        // Optimize a few times
        for (int i = 0; i < Parameter.cycleNum; i++)
            swarm.evolve();
        // Print en results
//        System.out.println(Method.printSwarmInfo(swarm));
        logger.info(Method.printSwarmInfo(swarm));

        // Then run the final result again to update the task completion degree
        AOCM_multi_PSO r = new AOCM_multi_PSO();
        double average = r.evaluate(swarm.getBestPosition());

        System.out.printf("%.4f %.4f %.4f %.4f %.4f\n",
                AOCM_multi_PSO.task1CompletionDegree,
                AOCM_multi_PSO.task2CompletionDegree,
                AOCM_multi_PSO.task3CompletionDegree,
                AOCM_multi_PSO.task4CompletionDegree,
                average);


        logger.info("Task 1 completion:" + AOCM_multi_PSO.task1CompletionDegree);
        logger.info("Task 2 completion:" + AOCM_multi_PSO.task2CompletionDegree);
        logger.info("Task 3 completion:" + AOCM_multi_PSO.task3CompletionDegree);
        logger.info("Task 4 completion:" + AOCM_multi_PSO.task4CompletionDegree);
        logger.info("Average task completion:" +
                (AOCM_multi_PSO.task1CompletionDegree + AOCM_multi_PSO.task2CompletionDegree
                        + AOCM_multi_PSO.task3CompletionDegree + AOCM_multi_PSO.task4CompletionDegree) / 4
        );

        logger.info("-----end PSO_Multi_AOCM-----");
        return new double[]{average, AOCM_multi_PSO.task1CompletionDegree, AOCM_multi_PSO.task2CompletionDegree
                , AOCM_multi_PSO.task3CompletionDegree, AOCM_multi_PSO.task4CompletionDegree};

    }

    public static double[] run_CM(List<Tag> tagList) {
        logger.info("-----start PSO_Multi_CM-----");
        logger.info("-----readerNum: " + Parameter.readerNum + "-----");

        Particle.particleNum = Parameter.readerNum * 2;

        // Pass the tag in
        CM_multi_PSO.globalTagList = tagList;
        Swarm swarm = new Swarm(Swarm.DEFAULT_NUMBER_OF_PARTICLES, new Particle(), new CM_multi_PSO());

        // Set the monitoring area
        swarm.setMaxPosition(Parameter.maxPosition);
        swarm.setMinPosition(Parameter.minPosition);
        // Optimize a few times
        for (int i = 0; i < Parameter.cycleNum; i++)
            swarm.evolve();
        // Print en results
//        System.out.println(Method.printSwarmInfo(swarm));
        logger.info(Method.printSwarmInfo(swarm));
        // Then run the final result again to update the task completion degree
        AOCM_multi_PSO.globalTagList = tagList;
        AOCM_multi_PSO r = new AOCM_multi_PSO();

        double average = r.evaluate(swarm.getBestPosition());
        System.out.printf("%.4f %.4f %.4f %.4f %.4f\n",
                AOCM_multi_PSO.task1CompletionDegree,
                AOCM_multi_PSO.task2CompletionDegree,
                AOCM_multi_PSO.task3CompletionDegree,
                AOCM_multi_PSO.task4CompletionDegree,
                average);


        logger.info("Task 1 completion:" + AOCM_multi_PSO.task1CompletionDegree);
        logger.info("Task 2 completion:" + AOCM_multi_PSO.task2CompletionDegree);
        logger.info("Task 3 completion:" + AOCM_multi_PSO.task3CompletionDegree);
        logger.info("Task 4 completion:" + AOCM_multi_PSO.task4CompletionDegree);
        logger.info("Average task completion:" +
                (AOCM_multi_PSO.task1CompletionDegree + AOCM_multi_PSO.task2CompletionDegree
                        + AOCM_multi_PSO.task3CompletionDegree + AOCM_multi_PSO.task4CompletionDegree) / 4
        );

        logger.info("-----end PSO_Multi_CM-----");
        return new double[]{average, AOCM_multi_PSO.task1CompletionDegree, AOCM_multi_PSO.task2CompletionDegree
                , AOCM_multi_PSO.task3CompletionDegree, AOCM_multi_PSO.task4CompletionDegree};
    }

    public static double[] run_BPSO(List<Tag> tagList) {
        PSO.process(tagList, "multi");
//        System.out.println(PSO.getBestFitness());
        //System.out.println(PSO.getBestLocation());
        return new double[]{
                PSO.getBestFitness(), BPSO_multi_PSO.task1CompletionDegree,
                BPSO_multi_PSO.task2CompletionDegree, BPSO_multi_PSO.task3CompletionDegree,
                BPSO_multi_PSO.task4CompletionDegree
        };
    }


}
