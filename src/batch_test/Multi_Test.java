package batch_test;

import GA_multi.Main_multi_GA;
import PSO_multi.Main_multi_PSO;
import util.Parameter;
import util.PathFactory;

public class Multi_Test {
    public static void main(String[] args) {
//        while (true){
//            run_PSO();
//            run_GA();
//        }
        run_PSO();
        run_GA();
    }

    public static void run_PSO() {
        for(Parameter.readerNum=15; Parameter.readerNum<=20; Parameter.readerNum++) {
            System.out.printf("Readernum:%d \nPSO_AOCM\n",Parameter.readerNum);
            for(int i=0;i<10;i++) {
                Main_multi_PSO.run_AOCM(PathFactory.createPath("normal"));
            }
        }

        for(Parameter.readerNum=15;Parameter.readerNum<=20;Parameter.readerNum++) {
            System.out.printf("Readernum:%d \nPSO_RS\n",Parameter.readerNum);
            for(int i=0;i<10;i++) {
                Main_multi_PSO.run_RS(PathFactory.createPath("normal"));
            }
        }
        for(Parameter.readerNum=5;Parameter.readerNum<=20;Parameter.readerNum++) {
            System.out.printf("Readernum:%d\n PSO-CM\n",Parameter.readerNum);
            for(int i=0;i<10;i++) {
                Main_multi_PSO.run_CM(PathFactory.createPath("normal"));
            }
        }

    }

    public static void run_GA() {
        for(Parameter.readerNum=5;Parameter.readerNum<=20;Parameter.readerNum++) {
            System.out.printf("Readernum:%d\n GA_AOCM\n",Parameter.readerNum);
            for(int i=0;i<10;i++) {
                Main_multi_GA.solve_AOCM(PathFactory.createPath("normal"));
            }
        }
        for(Parameter.readerNum=5;Parameter.readerNum<=20;Parameter.readerNum++) {
            System.out.printf("Readernum:%d\n GA_RS",Parameter.readerNum);
            for (int i = 0; i < 10; i++) {
                Main_multi_GA.solve_RS(PathFactory.createPath("normal"));
            }
        }
        for(Parameter.readerNum=5;Parameter.readerNum<=20;Parameter.readerNum++) {
            System.out.printf("Readernum:%d\n GA_CM",Parameter.readerNum);
            for (int i = 0; i < 10; i++) {
                Main_multi_GA.solve_CM(PathFactory.createPath("normal"));
            }
        }
        for(Parameter.readerNum=5;Parameter.readerNum<=20;Parameter.readerNum++) {
            System.out.printf("Readernum:%d\n GA_BPSO\n",Parameter.readerNum);
            for (int i = 0; i < 10; i++) {
                Main_multi_GA.solve_BPSO(PathFactory.createPath("normal"));
            }
        }
    }
}
