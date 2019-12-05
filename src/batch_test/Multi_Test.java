package batch_test;

import GA_multi.Main_multi_GA;
import PSO_multi.AOCM_multi_PSO;
import PSO_multi.Main_multi_PSO;
import util.FileUtil;
import util.Parameter;
import util.PathFactory;

public class Multi_Test {
    public static void main(String[] args) {
        while (true) {
            String filename = String.valueOf(System.currentTimeMillis());
            run_PSO(filename);
            run_GA(filename);
        }
//        run_PSO();
//        run_GA();
    }

    public static void run_PSO(String filename) {
        double[] res;
        FileUtil filewriter = new FileUtil("res/" + filename);
        for (Parameter.readerNum = 5; Parameter.readerNum <= 20; Parameter.readerNum++) {
            System.out.printf("Readernum:%d \nPSO_AOCM\n", Parameter.readerNum);
            filewriter.writemsg(String.format("Readernum:%d \nPSO_AOCM\n", Parameter.readerNum));
            for (int i = 0; i <= 10; i++) {
                res = Main_multi_PSO.run_AOCM(PathFactory.createPath("normal"));
                filewriter.writemsg(String.format("%.4f %.4f %.4f %.4f %.4f\n",
                        res[1], res[2], res[3], res[4], res[0]));

            }
        }

        for (Parameter.readerNum = 5; Parameter.readerNum <= 20; Parameter.readerNum++) {
            System.out.printf("Readernum:%d \nPSO_RS\n", Parameter.readerNum);
            filewriter.writemsg(String.format("Readernum:%d \nPSO_RS\n", Parameter.readerNum));
            for (int i = 0; i < 10; i++) {
                res = Main_multi_PSO.run_RS(PathFactory.createPath("normal"));
                filewriter.writemsg(String.format("%.4f %.4f %.4f %.4f %.4f\n",
                        res[1], res[2], res[3], res[4], res[0]));
            }
        }
        for (Parameter.readerNum = 5; Parameter.readerNum <= 20; Parameter.readerNum++) {
            System.out.printf("Readernum:%d\n PSO-CM\n", Parameter.readerNum);
            filewriter.writemsg(String.format("Readernum:%d \nPSO_CM\n", Parameter.readerNum));
            for (int i = 0; i < 10; i++) {
                res = Main_multi_PSO.run_CM(PathFactory.createPath("normal"));
                filewriter.writemsg(String.format("%.4f %.4f %.4f %.4f %.4f\n",
                        res[1], res[2], res[3], res[4], res[0]));
            }
        }

    }

    public static void run_GA(String filename) {
        FileUtil filewriter = new FileUtil("res/" + filename);
        double[] res;
        for (Parameter.readerNum = 5; Parameter.readerNum <= 20; Parameter.readerNum++) {
            System.out.printf("Readernum:%d\n GA_AOCM\n", Parameter.readerNum);
            filewriter.writemsg(String.format("Readernum:%d \nGA_AOCM\n", Parameter.readerNum));
            for (int i = 0; i < 10; i++) {
                res = Main_multi_GA.solve_AOCM(PathFactory.createPath("normal"));
                filewriter.writemsg(String.format("%.4f %.4f %.4f %.4f %.4f\n",
                        res[1], res[2], res[3], res[4], res[0]));
            }
        }
        for (Parameter.readerNum = 5; Parameter.readerNum <= 20; Parameter.readerNum++) {
            System.out.printf("Readernum:%d\n GA_RS\n", Parameter.readerNum);
            filewriter.writemsg(String.format("Readernum:%d \nGA_RS\n", Parameter.readerNum));
            for (int i = 0; i < 10; i++) {
                res=Main_multi_GA.solve_RS(PathFactory.createPath("normal"));
                filewriter.writemsg(String.format("%.4f %.4f %.4f %.4f %.4f\n",
                        res[1], res[2], res[3], res[4], res[0]));
            }
        }
        for (Parameter.readerNum = 5; Parameter.readerNum <= 20; Parameter.readerNum++) {
            System.out.printf("Readernum:%d\n GA_CM", Parameter.readerNum);
            filewriter.writemsg(String.format("Readernum:%d \nGA_CM\n", Parameter.readerNum));
            for (int i = 0; i < 10; i++) {
                res=Main_multi_GA.solve_CM(PathFactory.createPath("normal"));
                filewriter.writemsg(String.format("%.4f %.4f %.4f %.4f %.4f\n",
                        res[1], res[2], res[3], res[4], res[0]));
            }
        }
        for (Parameter.readerNum = 5; Parameter.readerNum <= 20; Parameter.readerNum++) {
            System.out.printf("Readernum:%d\n GA_BPSO\n", Parameter.readerNum);
            filewriter.writemsg(String.format("Readernum:%d\n GA_BPSO\n", Parameter.readerNum));
            for (int i = 0; i < 10; i++) {
                res=Main_multi_GA.solve_BPSO(PathFactory.createPath("normal"));
                filewriter.writemsg(String.format("%.4f %.4f %.4f %.4f %.4f\n",
                        res[1], res[2], res[3], res[4], res[0]));
            }
        }
    }
}
