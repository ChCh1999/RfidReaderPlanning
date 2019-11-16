//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package GA_multi;

import base.Tag;
import io.jenetics.*;
import io.jenetics.engine.Codecs;
import io.jenetics.engine.Codec;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionStatistics;
import io.jenetics.engine.Limits;
import io.jenetics.util.DoubleRange;

import org.apache.log4j.Logger;
import util.Parameter;
import util.PathFactory;

import java.util.List;

import static io.jenetics.engine.EvolutionResult.toBestPhenotype;

public class Main_multi_GA {

    private static Logger logger = Logger.getLogger(Main_multi_GA.class);

    public static void main(String[] args) {

        for(Parameter.readerNum=5;Parameter.readerNum<=20;Parameter.readerNum++) {
            System.out.printf("Readernum:%d\n GA-AOCM",Parameter.readerNum);
            for(int i=0;i<10;i++) {
                solve_AOCM(PathFactory.createPath("normal"));
            }
        }
        for(Parameter.readerNum=5;Parameter.readerNum<=20;Parameter.readerNum++) {
            System.out.printf("Readernum:%d\n GA-RS",Parameter.readerNum);
            for (int i = 0; i < 10; i++) {
                solve_RS(PathFactory.createPath("normal"));
            }
        }
        for(Parameter.readerNum=5;Parameter.readerNum<=20;Parameter.readerNum++) {
            System.out.printf("Readernum:%d\n GA-CM",Parameter.readerNum);
            for (int i = 0; i < 10; i++) {
                solve_CM(PathFactory.createPath("normal"));
            }
        }
        for(Parameter.readerNum=5;Parameter.readerNum<=20;Parameter.readerNum++) {
            System.out.printf("Readernum:%d\n GA-BPSO\n",Parameter.readerNum);
            for (int i = 0; i < 10; i++) {
                solve_BPSO(PathFactory.createPath("normal"));
            }
        }
        //solve_BPSO(PathFactory.createPath("normal"));
        //solve_CM(PathFactory.createPath("normal"));
    }

    public static double[] solve_CM(List<Tag> tagList) {

        logger.info("-----start GA_Multi_CM-----");
        logger.info("-----readerNum: " + Parameter.readerNum + "-----");
        // Pass the tag in
        CM_multi_GA.globalTagList = tagList;

        final Engine<DoubleGene, Double> engine = Engine
                .builder(
                        CM_multi_GA::evaluate,
                        Codecs.ofVector(DoubleRange.of(Parameter.minPosition, Parameter.maxPosition),
                                Parameter.readerNum * 2))
                .optimize(Optimize.MAXIMUM)
                .build();

        final EvolutionStatistics<Double, ?>
                statistics = EvolutionStatistics.ofNumber();

        final Phenotype<DoubleGene, Double> best = engine.stream()
                .limit(Parameter.cycleNum)
                .peek(statistics)
                .collect(toBestPhenotype());
        //System.out.println(best);
        Object[] bestPosition = best.getGenotype().getChromosome().toSeq().asList().toArray();
        //System.out.println(bestPosition[0]);
        double[] dbestPosition = new double[bestPosition.length];
        for (int i = 0; i < bestPosition.length; i++) {
            DoubleGene dg = (DoubleGene) bestPosition[i];
            dbestPosition[i] = dg.getAllele();
        }

        // 根据最好的结果计算每个任务的完成度
        AOCM_multi_GA.globalTagList = tagList;
        AOCM_multi_GA.evaluate(dbestPosition);

        double totalaccuracy=0.05*AOCM_multi_GA.task1CompletionDegree+0.1*AOCM_multi_GA.task2CompletionDegree
                +0.15*AOCM_multi_GA.task3CompletionDegree+0.7*AOCM_multi_GA.task4CompletionDegree;
        //System.out.println(totalaccuracy);
        System.out.printf("%.4f %.4f %.4f %.4f %.4f\n",
                AOCM_multi_GA.task1CompletionDegree,
                AOCM_multi_GA.task2CompletionDegree,
                AOCM_multi_GA.task3CompletionDegree,
                AOCM_multi_GA.task4CompletionDegree,
                totalaccuracy);


        logger.info("Task 1 completion:" + AOCM_multi_GA.task1CompletionDegree);
        logger.info("Task 2 completion:" + AOCM_multi_GA.task2CompletionDegree);
        logger.info("Task 3 completion:" + AOCM_multi_GA.task3CompletionDegree);
        logger.info("Task 4 completion:" + AOCM_multi_GA.task4CompletionDegree);
        logger.info("Average task completion:" +
                (AOCM_multi_GA.task1CompletionDegree + AOCM_multi_GA.task2CompletionDegree
                        + AOCM_multi_GA.task3CompletionDegree + AOCM_multi_GA.task4CompletionDegree) / 4
        );
        logger.info("-----end GA_Multi_CM-----");
        return new double[]{AOCM_multi_GA.averageScore, AOCM_multi_GA.task1CompletionDegree, AOCM_multi_GA.task2CompletionDegree
                , AOCM_multi_GA.task3CompletionDegree, AOCM_multi_GA.task4CompletionDegree};
    }

    public static double[] solve_AOCM(List<Tag> tagList) {

        logger.info("-----start GA_Multi_AOCM-----");
        logger.info("-----readerNum: " + Parameter.readerNum + "-----");
        // 传入标签
        AOCM_multi_GA.globalTagList = tagList;

        final Engine<DoubleGene, Double> engine = Engine
                .builder(
                        AOCM_multi_GA::evaluate,
                        Codecs.ofVector(DoubleRange.of(Parameter.minPosition, Parameter.maxPosition),
                                Parameter.readerNum * 2))
                .optimize(Optimize.MAXIMUM)
                .build();

        final EvolutionStatistics<Double, ?>
                statistics = EvolutionStatistics.ofNumber();

        final Phenotype<DoubleGene, Double> best = engine.stream()
                .limit(Parameter.cycleNum)
                .peek(statistics)
                .collect(toBestPhenotype());

        Object[] bestPosition = best.getGenotype().getChromosome().toSeq().asList().toArray();
        double[] dbestPosition = new double[bestPosition.length];
        for (int i = 0; i < bestPosition.length; i++) {
            DoubleGene dg = (DoubleGene) bestPosition[i];
            dbestPosition[i] = dg.getAllele();
        }

        // 根据最好的结果计算每个任务的完成度
        AOCM_multi_GA.evaluate(dbestPosition);
        double totalaccuracy=0.05*AOCM_multi_GA.task1CompletionDegree+0.1*AOCM_multi_GA.task2CompletionDegree
                +0.15*AOCM_multi_GA.task3CompletionDegree+0.7*AOCM_multi_GA.task4CompletionDegree;

        System.out.printf("%.4f %.4f %.4f %.4f %.4f\n",AOCM_multi_GA.task1CompletionDegree
                ,AOCM_multi_GA.task2CompletionDegree
                ,AOCM_multi_GA.task3CompletionDegree
                ,AOCM_multi_GA.task4CompletionDegree
                ,totalaccuracy);

        logger.info("Task 1 completion:" + AOCM_multi_GA.task1CompletionDegree);
        logger.info("Task 2 completion:" + AOCM_multi_GA.task2CompletionDegree);
        logger.info("Task 3 completion:" + AOCM_multi_GA.task3CompletionDegree);
        logger.info("Task 4 completion:" + AOCM_multi_GA.task4CompletionDegree);
        logger.info("Average task completion:" +
                (AOCM_multi_GA.task1CompletionDegree + AOCM_multi_GA.task2CompletionDegree
                        + AOCM_multi_GA.task3CompletionDegree + AOCM_multi_GA.task4CompletionDegree) / 4
        );
        logger.info("-----end GA_Multi_AOCM-----");
        return new double[]{AOCM_multi_GA.averageScore, AOCM_multi_GA.task1CompletionDegree, AOCM_multi_GA.task2CompletionDegree
                , AOCM_multi_GA.task3CompletionDegree, AOCM_multi_GA.task4CompletionDegree};
    }

    public static double[] solve_RS(List<Tag> tagList) {

        logger.info("-----start GA_Multi_RS-----");
        logger.info("-----readerNum: " + Parameter.readerNum + "-----");
        // Pass the tag in
        RS_multi_GA.globalTagList = tagList;

        final Engine<DoubleGene, Double> engine = Engine
                .builder(
                        RS_multi_GA::evaluate,
                        Codecs.ofVector(DoubleRange.of(Parameter.minPosition, Parameter.maxPosition),
                                Parameter.readerNum * 2))
                .optimize(Optimize.MAXIMUM)
                .build();

        final EvolutionStatistics<Double, ?>
                statistics = EvolutionStatistics.ofNumber();

        final Phenotype<DoubleGene, Double> best = engine.stream()
                .limit(Parameter.cycleNum)
                .peek(statistics)
                .collect(toBestPhenotype());

        Object[] bestPosition = best.getGenotype().getChromosome().toSeq().asList().toArray();
        double[] dbestPosition = new double[bestPosition.length];
        for (int i = 0; i < bestPosition.length; i++) {
            DoubleGene dg = (DoubleGene) bestPosition[i];
            dbestPosition[i] = dg.getAllele();
        }

        // Then run the final result again to update the task completion degree
        RS_multi_GA.evaluate(dbestPosition);
        double totalaccuracy=0.05*RS_multi_GA.task1CompletionDegree+0.1*RS_multi_GA.task2CompletionDegree
                +0.15*RS_multi_GA.task3CompletionDegree+0.7*RS_multi_GA.task4CompletionDegree;

        System.out.printf("%.4f %.4f %.4f %.4f %.4f\n",RS_multi_GA.task1CompletionDegree,RS_multi_GA.task2CompletionDegree,RS_multi_GA.task3CompletionDegree,RS_multi_GA.task4CompletionDegree,totalaccuracy);


        logger.info("Task 1 completion:" + RS_multi_GA.task1CompletionDegree);
        logger.info("Task 2 completion:" + RS_multi_GA.task2CompletionDegree);
        logger.info("Task 3 completion:" + RS_multi_GA.task3CompletionDegree);
        logger.info("Task 4 completion:" + RS_multi_GA.task4CompletionDegree);
        logger.info("Average task completion:" +
                (RS_multi_GA.task1CompletionDegree + RS_multi_GA.task2CompletionDegree
                        + RS_multi_GA.task3CompletionDegree + RS_multi_GA.task4CompletionDegree) / 4
        );

        logger.info("-----end GA_Multi_RS-----");
        return new double[]{RS_multi_GA.averageScore, RS_multi_GA.task1CompletionDegree, RS_multi_GA.task2CompletionDegree
                , RS_multi_GA.task3CompletionDegree, RS_multi_GA.task4CompletionDegree};
    }

    public static double[] solve_BPSO(List<Tag> tagList){
        BPSO_multi_GA.globalTagList=tagList;
        DoubleRange[] r=new DoubleRange[Parameter.readerNum*2+Parameter.slotNum];
        for(int i=0;i<Parameter.readerNum*2;i++){
            r[i]=DoubleRange.of(Parameter.minPosition,Parameter.maxPosition);
        }
        for(int i=Parameter.readerNum*2;i<Parameter.readerNum*2+Parameter.slotNum;i++){
            r[i]=DoubleRange.of(0,1<<Parameter.readerNum);
        }
        final Engine<DoubleGene, Double> engine = Engine
                .builder(
                        BPSO_multi_GA::evaluate,
                        Codecs.ofVector(r))
                .populationSize(400)
                .offspringFraction(0.5)
                .survivorsSelector(new RouletteWheelSelector<>())
                .offspringSelector(new TournamentSelector<>())
                .optimize(Optimize.MAXIMUM)
                .build();

        final EvolutionStatistics<Double, ?>
                statistics = EvolutionStatistics.ofNumber();

        final Phenotype<DoubleGene, Double> best = engine.stream()
                .limit(Parameter.cycleNum)
                .peek(statistics)
                .collect(toBestPhenotype());
        //System.out.println(best);
        //Object[] bestPosition = best.getGenotype().getChromosome().toSeq().asList().toArray();
        //System.out.println(bestPosition.length);

        double[] dbestPosition = new double[Parameter.readerNum*2+Parameter.slotNum];
        for (int i = 0; i < dbestPosition.length; i++) {
            dbestPosition[i]=best.getGenotype().getChromosome(i).getGene().doubleValue();
        }

        BPSO_multi_GA.evaluate(dbestPosition);
        double totalaccuracy=0.05*BPSO_multi_GA.task1CompletionDegree+0.1*BPSO_multi_GA.task2CompletionDegree
                +0.15*BPSO_multi_GA.task3CompletionDegree+0.7*BPSO_multi_GA.task4CompletionDegree;

        System.out.printf("%.4f %.4f %.4f %.4f %.4f\n",BPSO_multi_GA.task1CompletionDegree
                ,BPSO_multi_GA.task2CompletionDegree
                ,BPSO_multi_GA.task3CompletionDegree
                ,BPSO_multi_GA.task4CompletionDegree
                ,totalaccuracy);

        return new double[]{totalaccuracy, BPSO_multi_GA.task1CompletionDegree, BPSO_multi_GA.task2CompletionDegree
                , BPSO_multi_GA.task3CompletionDegree, BPSO_multi_GA.task4CompletionDegree};
    }
}
