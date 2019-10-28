package batch_test;

import PSO_multi.Main_multi_PSO;
import base.Tag;
import util.Parameter;
import util.PathFactory;

import java.text.DecimalFormat;
import java.util.List;

public class SO_Multi_Main {
    public static void main(String[] args) {
        SO_Multi_Main smm = new SO_Multi_Main();
        smm.print(smm.readerNum());
    }

    /**
     * 以读写器数量为变量的监控准确率结果
     * @return 读写器数和监控准确率的二维数组
     */
    private double[][] readerNum() {
        System.out.println("scenario1_readerNum");
        List<Tag> tagList = PathFactory.createPath("normal");

        double[][] res = new double[16][5];

        int readerNum = Parameter.readerNum;
        for (int i = 5; i <= 20; i++) {
            Parameter.readerNum = i;
            res[i - 5] = run(tagList);
        }
        Parameter.readerNum = readerNum;
        return res;
    }

    /*
    运行SO一次
     */
    private double[] run(List<Tag> tagList) {
        double[] max = new double[5];
        for (int j = 0; j < Parameter.runCycle; j++) {
            double[] accuracy = Main_multi_PSO.run_BPSO(tagList);
            if (accuracy[0] > max[0]) {
                max = accuracy.clone();
            }
        }
        return max;
    }

    private void print(double[][] res) {
        DecimalFormat df = new DecimalFormat("#.0000");
        for (int j = 0; j < 5; j++) {
            System.out.print("PSO_BPSO = [");
            for (int i = 0; i < res.length; i++) {
                System.out.print(df.format(res[i][j]) + " ");
            }
            System.out.print("];");
            System.out.print("\n");
            System.out.println();
        }
    }
}
