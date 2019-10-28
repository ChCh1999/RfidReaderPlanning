package batch_test;

import GA_multi.Main_multi_GA;
import PSO_multi.Main_multi_PSO;
import base.Tag;
import util.Parameter;
import util.PathFactory;

import java.text.DecimalFormat;
import java.util.List;

public class Multi_Main {


    public static void main(String[] args) {
        Multi_Main m = new Multi_Main();

        m.print(m.readerNum());
		m.print(m.ri());
    }

    /**
     * 运行所有算法一次
     */
    private double[][] run(List<Tag> tagList) {
        double[][] max = new double[7][5];
        for (int j = 0; j < Parameter.runCycle; j++) {
            double accuracy[];
            accuracy = Main_multi_PSO.run_RS(tagList);
            if (max[0][0] < accuracy[0])
                max[0] = accuracy.clone();
            accuracy = Main_multi_PSO.run_AOCM(tagList);
            if (max[1][0] < accuracy[0])
                max[1] = accuracy.clone();
            accuracy = Main_multi_PSO.run_CM(tagList);
            if (max[2][0] < accuracy[0])
                max[2] = accuracy.clone();
			accuracy = Main_multi_GA.solve_RS(tagList);
			if (max[3][0] < accuracy[0])
				max[3] = accuracy.clone();
			accuracy = Main_multi_GA.solve_AOCM(tagList);
			if (max[4][0] < accuracy[0])
				max[4] = accuracy.clone();
			accuracy = Main_multi_GA.solve_CM(tagList);
			if (max[5][0] < accuracy[0])
				max[5] = accuracy.clone();
            accuracy = Main_multi_PSO.run_BPSO(tagList);
            if (max[6][0] < accuracy[0])
                max[6] = accuracy.clone();
        }
        System.out.println("finish one");
        return max;
    }

    private void print(double[][][] res) {
        DecimalFormat df = new DecimalFormat("#.0000");
        for (int j = 0; j < 5; j++) {
            System.out.print("PSO_RS = [");
            for (int i = 0; i < res.length; i++) {
                System.out.print(df.format(res[i][0][j]) + " ");
            }
            System.out.print("];");
            System.out.print("\n");

            System.out.print("PSO_AOCM = [");
            for (int i = 0; i < res.length; i++) {
                System.out.print(df.format(res[i][1][j]) + " ");
            }
            System.out.print("];");
            System.out.print("\n");

            System.out.print("PSO_CM = [");
            for (int i = 0; i < res.length; i++) {
                System.out.print(df.format(res[i][2][j]) + " ");
            }
            System.out.print("];");
            System.out.print("\n");

            System.out.print("GA_RS = [");
            for (int i = 0; i < res.length; i++) {
                System.out.print(df.format(res[i][3][j]) + " ");
            }
            System.out.print("];");
            System.out.print("\n");

            System.out.print("GA_AOCM = [");
            for (int i = 0; i < res.length; i++) {
                System.out.print(df.format(res[i][4][j]) + " ");
            }
            System.out.print("];");
            System.out.print("\n");

            System.out.print("GA_CM = [");
            for (int i = 0; i < res.length; i++) {
                System.out.print(df.format(res[i][5][j]) + " ");
            }
            System.out.print("];");
            System.out.print("\n");

            System.out.print("PSO_BPSO = [");
            for (int i = 0; i < res.length; i++) {
                System.out.print(df.format(res[i][6][j]) + " ");
            }
            System.out.print("];");
            System.out.print("\n");
            System.out.println();
        }
    }

    /**
     * 以读写器数量为变量的监控准确率结果
     * @return 读写器数和监控准确率的二维数组
     */
    private double[][][] readerNum() {
        System.out.println("scenario1_readerNum");
        // Generate labels and label paths
        List<Tag> tagList = PathFactory.createPath("normal");
//        List<Tag> tagList = PathFactory.createPath("robot");

        double[][][] res = new double[16][7][5];

        int readerNum = Parameter.readerNum;
        for (int i = 5; i <= 20; i++) {
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
    private double[][][] ri() {
        System.out.println("scenario1_ri");

//        List<Tag> tagList = PathFactory.createPath("complexConveyor");
        List<Tag> tagList = PathFactory.createPath("normal");
        double[][][] res = new double[11][7][5];

        double ri = Parameter.ri;
        double rf = Parameter.rf;
        int index = 0;
        for (double d = 1.0; d <= 2.05; d += 0.1) {
            Parameter.ri = d;
            Parameter.rf = d * 1.5;
            res[index] = run(tagList);
            index++;
        }
        Parameter.ri = ri;
        Parameter.rf = rf;
        return res;
    }

}
