package batch_test;

import GA_simple.Main_simple_GA;
import PSO_simple.Main;

import base.Tag;
import util.Parameter;
import util.PathFactory;

import java.text.DecimalFormat;
import java.util.List;

public class Single_Main {

    public static void main(String[] args) {
        Single_Main m = new Single_Main();

        m.print(m.readerNum());
        m.print(m.ri());
        m.print(m.rf());
    }

    /**
     * 运行所有算法一次
     */
//    private double[] run(List<Tag> tagList) {
//        double[] max = new double[2];
//        for (int j = 0; j < Parameter.runCycle; j++) {
//            double accuracy = Main.run_CM(tagList);
//            if (max[0] < accuracy)
//                max[0] = accuracy;
//            accuracy = Main_simple_GA.solve_CM(tagList);
//            if (max[1] < accuracy)
//                max[1] = accuracy;
//        }
//        System.out.println("finish one");
//        return max;
//    }
    private double[] run(List<Tag> tagList) {
        double[] max = new double[7];
        for (int j = 0; j < Parameter.runCycle; j++) {
            double accuracy = Main.run_RS(tagList);
            if (max[0] < accuracy)
                max[0] = accuracy;
            accuracy = Main.run_AOCM(tagList);
            if (max[1] < accuracy)
                max[1] = accuracy;
            accuracy = Main.run_CM(tagList);
            if (max[2] < accuracy)
                max[2] = accuracy;
            accuracy = Main.run_BPSO(tagList);
//            accuracy = Main.run_CM(tagList);
            if (max[3] < accuracy)
                max[3] = accuracy;
            accuracy = Main_simple_GA.solve_RS(tagList);
            if (max[4] < accuracy)
                max[4] = accuracy;
            accuracy = Main_simple_GA.solve_AOCM(tagList);
            if (max[5] < accuracy)
                max[5] = accuracy;
            accuracy = Main_simple_GA.solve_CM(tagList);
            if (max[6] < accuracy)
                max[6] = accuracy;
        }
        System.out.println("finish one");
        return max;
    }

    private void print(double[][] res) {
        DecimalFormat df = new DecimalFormat("#.0000");

        System.out.print("PSO_RS = [");
        for (int i = 0; i < res.length; i++) {
            System.out.print(df.format(res[i][0]) + " ");
        }
        System.out.print("];");
        System.out.print("\n");

        System.out.print("PSO_AOCM = [");
        for (int i = 0; i < res.length; i++) {
            System.out.print(df.format(res[i][1]) + " ");
        }
        System.out.print("];");
        System.out.print("\n");

        System.out.print("PSO_CM = [");
        for (int i = 0; i < res.length; i++) {
            System.out.print(df.format(res[i][2]) + " ");
        }
        System.out.print("];");
        System.out.print("\n");

        System.out.print("PSO_BPSO = [");
        for (int i = 0; i < res.length; i++) {
            System.out.print(df.format(res[i][3]) + " ");
        }
        System.out.print("];");
        System.out.print("\n");

        System.out.print("GA_RS = [");
        for (int i = 0; i < res.length; i++) {
            System.out.print(df.format(res[i][4]) + " ");
        }
        System.out.print("];");
        System.out.print("\n");

        System.out.print("GA_AOCM = [");
        for (int i = 0; i < res.length; i++) {
            System.out.print(df.format(res[i][5]) + " ");
        }
        System.out.print("];");
        System.out.print("\n");

        System.out.print("GA_CM = [");
        for (int i = 0; i < res.length; i++) {
            System.out.print(df.format(res[i][6]) + " ");
        }
        System.out.print("];");
        System.out.print("\n");
        System.out.println();

    }

    /**
     * 以读写器数量为变量的监控准确率结果
     * @return 读写器数和监控准确率的二维数组
     */
    private double[][] readerNum() {
        System.out.println("scenario1_readerNum");
        // Generate labels and label paths
        List<Tag> tagList = PathFactory.createPath("random");
//        List<Tag> tagList = PathFactory.createPath("robot");

        double[][] res = new double[16][7];

        int readerNum = Parameter.readerNum;
        for (int i = 5; i <= 20; i++) {
            System.out.printf("readerNum=%d\n",i);
            Parameter.readerNum = i;
            res[i - 5] = run(tagList);
        }
        Parameter.readerNum = readerNum;
        return res;
    }

    /**
     * 以读写器识别半径为变量的监控准确率结果
     * @return 读写器识别半径和监控准确率的二维数组
     */
    private double[][] ri() {
        System.out.println("scenario2_ri");

        List<Tag> tagList = PathFactory.createPath("random");

        double[][] res = new double[4][7];

        double ri = Parameter.ri;
        int index = 0;
        for (double d = 1.0; d <= 1.35; d += 0.1) {
            Parameter.ri = d;
            System.out.printf("ri=%f\n",d);
            res[index] = run(tagList);
            index++;
        }
        Parameter.ri = ri;
        return res;
    }
    
    private double[][] rf() {
        System.out.println("scenario1_rf");

        List<Tag> tagList = PathFactory.createPath("random");

        double[][] res = new double[4][7];

        double rf = Parameter.rf;
        int index = 0;
        for (double d = 1.5; d <= 1.85; d += 0.1) {
            System.out.printf("rd=%f\n",d);
            Parameter.rf = d;
            res[index] = run(tagList);
            index++;
        }
        Parameter.rf = rf;
        return res;
    }

}
