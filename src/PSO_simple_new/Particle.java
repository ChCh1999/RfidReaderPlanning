package PSO_simple_new;

import PSO_multi.BPSO_multi_PSO;
import base.Reader;
import base.Tag;
import fitness.SingleFitness;
import util.Method;
import util.Parameter;

import java.util.List;
import java.util.Random;

/**
 * 粒子类
 *
 * @author wangqiang
 */

public class Particle {

    //维数
    //public int dimension = Parameter.readerNum * 2 + Parameter.readerNum * Parameter.slotNum;
    //将粒子的开关状态用二进制数来表示，这样可以降低粒子的维度

    public static int dimension=Parameter.readerNum*2+Parameter.slotNum;

    //粒子开关状态的最大值
    public static int stateMax=1<<Parameter.readerNum-1;

    //粒子的位置
    public double[] X = new double[Parameter.readerNum*2+Parameter.slotNum];

    //局部最好位置
    public double[] pbest = new double[Parameter.readerNum*2+Parameter.slotNum];

    //粒子的速度
    public double[] V = new double[Parameter.readerNum*2+Parameter.slotNum];

    //读写器位置更新的最大速度
    public static double Vmax = 0.5;

    //读写器状态更新的最大速度
    public static double Vstate=30;

    //适应值
    public double fitness;

    public List<Tag> tagList;

    public double task1=0;

    public double task2=0;

    public double task3=0;

    public double task4=0;

    /**
     * 根据当前位置计算适应值
     *
     * @return newFitness
     */
    public double calculateFitness(String choose) {

        List<Reader> readerList = Method.Position2ReaderList(X);

        // 得到读写器开关状态数组
        /**
        boolean[][] state = new boolean[Parameter.slotNum][Parameter.readerNum];
        for (int i = 0; i < Parameter.slotNum; i++) {
            for (int j = 0; j < Parameter.readerNum; j++) {
                if (Math.abs(X[Parameter.readerNum * 2 + i * Parameter.readerNum+ j] - 0) < Parameter.THRESHOLD) {
                    state[i][j] = false;
                } else {
                    state[i][j] = true;
                }
            }
        }
        */

        //与之前的方法不同，由于现在粒子的状态用二进制数来表示，需要额外的转换
        boolean[][] state=new boolean[Parameter.slotNum][Parameter.readerNum];
        for(int i=0;i<Parameter.slotNum;i++){
            int stateNumber=(int)X[Parameter.readerNum*2+i];
//            System.out.printf("%d ",stateNumber);
            for(int j=0;j<Parameter.readerNum;j++){
                state[i][j]=((stateNumber&1)==1);
                stateNumber=stateNumber>>>1;
            }
        }
//        System.out.println();

        double res = Integer.MAX_VALUE;
        if (choose.equals("single"))
            res = SingleFitness.BPSO(readerList, tagList, state);
        else if (choose.equals("multi")) {
            res = BPSO_multi_PSO.evaluate(readerList, tagList, state);
            task1=BPSO_multi_PSO.task1CompletionDegree;
            task2=BPSO_multi_PSO.task2CompletionDegree;
            task3=BPSO_multi_PSO.task3CompletionDegree;
            task4=BPSO_multi_PSO.task4CompletionDegree;
        }
        return res;
    }


    /**
     * 初始化自己的位置和pbest
     */
    public void initialX() {
        //读写器位置的初始化
        for (int i = 0; i < Parameter.readerNum * 2; i++) {
            X[i] = Parameter.minPosition + new Random().nextDouble() * (Parameter.maxPosition - Parameter.minPosition);
        }

        /**
        for (int i = 0; i < Parameter.slotNum; i++) {
            if (new Random().nextDouble() < 0.5)
                X[i + 2 * Parameter.readerNum] = 0;
            else
                X[i + 2 * Parameter.readerNum] = 1;
        }
        */
        //读写器开关状态的初始化
        for(int i=2*Parameter.readerNum*2;i<X.length;i++){
            X[i]=new Random().nextInt((stateMax+1)/2)+(stateMax)/2;
        }

        for (int i = 0; i < dimension; i++) {
            pbest[i] = X[i];
        }
    }

    /**
     * 初始化自己的速度
     */
    public void initialV() {
        /**
        for (int i = 0; i < dimension; i++) {
            double tmp = new Random().nextDouble();//随机产生一个0~1的随机小数
            V[i] = tmp * 4 + (-2);
        }
        */

        //针对读写器的状态和位置进行不同的初始化

        //对于读写器的位置，速度初始化为 [-Vmax , Vmax]
        for(int i=0;i<2*Parameter.readerNum*2;i++){
            V[i]=new Random().nextDouble()*2*Vmax-Vmax;
        }

        //对于读写器的状态，速度初始化为 [-Vstate, Vstate]
        for(int i=2*Parameter.readerNum;i<dimension;i++){
            V[i]=new Random().nextDouble()*2*Vstate-Vstate;
        }

    }

}
