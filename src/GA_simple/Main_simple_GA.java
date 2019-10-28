package GA_simple;

import base.Tag;
import io.jenetics.*;
import io.jenetics.engine.Codecs;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionStatistics;
import io.jenetics.util.DoubleRange;
import util.Parameter;
import util.PathFactory;

import java.nio.file.Path;
import java.util.List;

import static io.jenetics.engine.EvolutionResult.toBestPhenotype;

public class Main_simple_GA {
    public static void main(String[] args) {
        for(Parameter.rf=1.5;Parameter.rf<=1.81;Parameter.rf+=0.1) {
            System.out.println("rf is:"+Parameter.rf);
            System.out.println("GA_CM----------------------------------");
            for (int i = 1; i <= 30; i++) {
                solve_CM(PathFactory.createPath("normal"));
            }
            System.out.println("GA_AOCM--------------------------------");
            for (int i = 1; i <= 30; i++) {
                solve_AOCM(PathFactory.createPath("normal"));
            }
            System.out.println("GA_RS----------------------------------");
            for (int i = 1; i <= 30; i++) {
                solve_RS(PathFactory.createPath("normal"));
            }
        }
    }

    public static double solve_CM(List<Tag> tagList) {
        // Pass the tag in
        CM.tagList = tagList;

        final Engine<DoubleGene, Double> engine = Engine
                .builder(
                        CM::evaluate,
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

        //System.out.println(statistics);
        //System.out.println(best.getFitness());
        //System.out.println(best.getGenotype());
        Object[] bestPosition = best.getGenotype().getChromosome().toSeq().asList().toArray();
        double[] dbestPosition = new double[bestPosition.length];
        for (int i = 0; i < bestPosition.length; i++) {
            DoubleGene dg = (DoubleGene) bestPosition[i];
            dbestPosition[i] = dg.getAllele();
        }

        CM.tagList = tagList;
        double fitness=CM.evaluate(dbestPosition);
        System.out.printf("%.4f\n",fitness);
        return fitness;
    }

    public static double solve_RS(List<Tag> tagList) {
        // Pass the tag in
        RS.tagList = tagList;

        final Engine<DoubleGene, Double> engine = Engine
                .builder(
                        RS::evaluate,
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

//        System.out.println(statistics);
//        System.out.println(best.getFitness());
//        System.out.println(best.getGenotype());
        Object[] bestPosition = best.getGenotype().getChromosome().toSeq().asList().toArray();
        double[] dbestPosition = new double[bestPosition.length];
        for (int i = 0; i < bestPosition.length; i++) {
            DoubleGene dg = (DoubleGene) bestPosition[i];
            dbestPosition[i] = dg.getAllele();
        }

        RS.tagList = tagList;
        double fitness=RS.evaluate(dbestPosition);
        System.out.printf("%.4f\n",fitness);
        return fitness;
    }

    public static double solve_AOCM(List<Tag> tagList) {
        // Pass the tag in
        AOCM.tagList = tagList;

        final Engine<DoubleGene, Double> engine = Engine
                .builder(
                        AOCM::evaluate,
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

//        System.out.println(statistics);
//        System.out.println(best.getFitness());
//        System.out.println(best.getGenotype());
        Object[] bestPosition = best.getGenotype().getChromosome().toSeq().asList().toArray();
        double[] dbestPosition = new double[bestPosition.length];
        for (int i = 0; i < bestPosition.length; i++) {
            DoubleGene dg = (DoubleGene) bestPosition[i];
            dbestPosition[i] = dg.getAllele();
        }

        AOCM.tagList = tagList;
        double fitness=AOCM.evaluate(dbestPosition);
        System.out.printf("%.4f\n",fitness);

        return fitness;
    }
}
