package batch_test;

import PSO_simple.Main;
import base.Tag;
import util.Parameter;
import util.PathFactory;

import java.text.DecimalFormat;
import java.util.List;

public class SO_Single_Main {
    public static void main(String[] args) {
        SO_Single_Main so = new SO_Single_Main();
//        so.print(so.readerNumScenario1());
//        so.print(so.readerNumScenario2());
        so.print(so.riScenario1());
        so.print(so.rfScenario1());
        so.print(so.riScenario2());
        so.print(so.rfScenario2());
    }

    /**
     * 以读写器数量为变量的监控准确率结果
     * @return 读写器数和监控准确率的二维数组
     */
    private double[] readerNumScenario1() {
        System.out.println("scenario1_readerNum");
        List<Tag> tagList = PathFactory.createPath("normal");

        double[] res = new double[16];

        int readerNum = Parameter.readerNum;
        for (int i = 5; i <= 20; i++) {
            Parameter.readerNum = i;
            res[i - 5] = run(tagList);
        }
        Parameter.readerNum = readerNum;
        return res;
    }

    /**
     * 以读写器数量为变量的监控准确率结果
     * @return 读写器数和监控准确率的二维数组
     */
    private double[] readerNumScenario2() {
        System.out.println("scenario2_readerNum");
        List<Tag> tagList = PathFactory.createPath("random");

        double[] res = new double[16];

        int readerNum = Parameter.readerNum;
        int tagNum = Parameter.tagNum;

        for (int i = 10; i <= 25; i++) {
            Parameter.readerNum = i;
            Parameter.tagNum = 50;
            res[i - 10] = run(tagList);
        }
        Parameter.readerNum = readerNum;
        Parameter.tagNum = tagNum;
        return res;
    }

    private double[] riScenario1() {
        System.out.println("scenario1_ri");

        List<Tag> tagList = PathFactory.createPath("normal");

        double[] res = new double[4];

        double ri = Parameter.ri;
        int index = 0;
        for (double d = 1.0; d <= 1.35; d += 0.1) {
            Parameter.ri = d;
            res[index] = run(tagList);
            index++;
        }
        Parameter.ri = ri;
        return res;
    }

    private double[] rfScenario1() {
        System.out.println("scenario1_rf");

        List<Tag> tagList = PathFactory.createPath("normal");

        double[] res = new double[4];

        double rf = Parameter.rf;
        int index = 0;
        for (double d = 1.5; d <= 1.85; d += 0.1) {
            Parameter.rf = d;
            res[index] = run(tagList);
            index++;
        }
        Parameter.rf = rf;
        return res;
    }

    private double[] riScenario2() {
        System.out.println("scenario2_ri");

        List<Tag> tagList = PathFactory.createPath("random");

        double[] res = new double[4];

        int tagNum = Parameter.tagNum;
        double ri = Parameter.ri;
        int index = 0;
        for (double d = 1.0; d <= 1.35; d += 0.1) {
            Parameter.ri = d;
            Parameter.tagNum = 50;
            res[index] = run(tagList);
            index++;
        }
        Parameter.ri = ri;
        Parameter.tagNum = tagNum;
        return res;
    }

    private double[] rfScenario2() {
        System.out.println("scenario2_rf");

        List<Tag> tagList = PathFactory.createPath("random");

        double[] res = new double[4];

        int tagNum = Parameter.tagNum;
        double rf = Parameter.rf;
        int index = 0;
        for (double d = 1.5; d <= 1.85; d += 0.1) {
            Parameter.rf = d;
            Parameter.tagNum = 50;
            res[index] = run(tagList);
            index++;
        }
        Parameter.rf = rf;
        Parameter.tagNum = tagNum;
        return res;
    }


    /*
    运行SO一次
     */
    private double run(List<Tag> tagList) {
        double max = 0;
        for (int j = 0; j < Parameter.runCycle; j++) {
            double accuracy = Main.run_BPSO(tagList);
            if (accuracy > max) {
                max = accuracy;
            }
        }
        return max;
    }

    private void print(double[] res) {
        DecimalFormat df = new DecimalFormat("#.0000");
        System.out.print("PSO_BPSO = [");
        for (double re : res) {
            System.out.print(df.format(re) + " ");
        }
        System.out.print("];");
        System.out.print("\n");
    }
}
