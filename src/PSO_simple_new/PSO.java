package PSO_simple_new;

import PSO_multi.BPSO_multi_PSO;
import base.Tag;
import util.Parameter;
import util.PathFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * PSO算法实现
 *
 * @author wangqiang
 */
public class PSO {

    private static double[] gbest;//全局最优位置

    private static double gbest_fitness = Double.MIN_VALUE;//全局最优位置对应的fitness

    private static double task1=0;

    private static double task2=0;

    private static double task3=0;

    private static double task4=0;

    public static int particle_num = 500;//粒子数

    private static int N = Parameter.cycleNum;//迭代次数

    //读写器位置更新的权重
    private static double c1= 1.494;

    private static double c2=1.494;

    private static double w = 0.7;//惯性因子

    //读写器状态更新的权重
    private static double w_state=0.7;

    private static double c1_state=2;

    private static double c2_state=2;

    private static List<Particle> particles = new ArrayList<>();//粒子群



    public static double getBestFitness() {
        return gbest_fitness;
    }

    /**
     * 初始化所有粒子
     */
    public static void initialParticles(List<Tag> tagList, String choose) {
        for (int i = 0; i < particle_num; i++) {
            Particle particle = new Particle();
            particle.tagList = tagList;
            particle.initialX();
            particle.initialV();
            particle.fitness = particle.calculateFitness(choose);
            particles.add(particle);
        }
    }

    /**
     * update gbest
     */
    public static void updateGbest() {
        double fitness = Double.MIN_VALUE;
        int index = 0;
        for (int i = 0; i < particle_num; i++) {
            if (particles.get(i).fitness > fitness) {
                index = i;
                fitness = particles.get(i).fitness;
            }
        }
        if (fitness > gbest_fitness) {
            gbest = particles.get(index).pbest.clone();
            gbest_fitness = fitness;
            task1=particles.get(index).task1;
            task2=particles.get(index).task2;
            task3=particles.get(index).task3;
            task4=particles.get(index).task4;
        }
    }


    /**
     * 跟新每个粒子的速度
     */
    public static void updateV() {

        //由于速度的两个部分的取值范围不一样，需要不同的更新方式
        for(Particle particle:particles) {
            for (int i = 0; i < Parameter.readerNum * 2; i++) {
                double v = w * particle.V[i] + c1 * rand() * (particle.pbest[i] - particle.X[i]) + c2 * rand() * (gbest[i] - particle.X[i]);
                if (v > particle.Vmax)
                    v = particle.Vmax;
                else if (v < -particle.Vmax)
                    v = -particle.Vmax;
                particle.V[i] = v;

            }
            for (int i = Parameter.readerNum * 2; i < particle.dimension; i++) {
                double v = w_state * particle.V[i] + c1_state * rand() * (particle.pbest[i] - particle.X[i]) + c2_state * rand() * (gbest[i] - particle.X[i]);
                if (v > particle.Vstate)
                    v = particle.Vstate;
                else if (v < -particle.Vstate)
                    v = -particle.Vstate;
                particle.V[i] = v;
            }

        }
    }


    /**
     * 更新每个粒子的位置和pbest
     */
    public static void updateX(String choose) {
        for (Particle particle : particles) {
            for (int i = 0; i < Parameter.readerNum * 2; i++) {
                particle.X[i] = particle.X[i] + particle.V[i];
                if (particle.X[i] > Parameter.maxPosition) {
                    particle.X[i] = Parameter.maxPosition;
                }
                if (particle.X[i] < Parameter.minPosition) {
                    particle.X[i] = Parameter.minPosition;
                }
            }

            //粒子位置中的开关状态更新
            for(int i=2*Parameter.readerNum;i<particle.X.length;i++){

                particle.X[i]=((int)Math.abs(particle.X[i]+particle.V[i]));
                if(particle.X[i]>Particle.stateMax){
                    particle.X[i]=Particle.stateMax;
                }
                if(particle.X[i]<0){
                    particle.X[i]=0;
                }


            }

            double newFitness = particle.calculateFitness(choose); //新的适应值
            //如果新的适应值比原来的大则更新fitness和pbest
            if (newFitness > particle.fitness) {
                particle.pbest = particle.X.clone();
                particle.fitness = newFitness;
            }
        }
    }


    /**
     * 算法主要流程
     */
    public static void process(List<Tag> tagList, String choose) {
        int n = 0;
        particles.clear();
        gbest_fitness = 0;
        gbest = new double[Particle.dimension];
        initialParticles(tagList, choose);
        updateGbest();
        while (n++ < N) {
            updateV();
            updateX(choose);
            double gbest_fitness_temp=gbest_fitness;
            updateGbest();
            //打印PSO过程
//            System.out.printf("Epoch %d, fitness is %f",n,gbest_fitness);
////            System.out.println(n + ".当前gbest:(" + gbest[0] + "," + gbest[1] + ")  fitness=" + gbest_fitness);
//            System.out.printf("  %.4f %.4f %.4f %.4f\n",task1,task2,task3,task4);
            //打印迭代主要数据
//            if(gbest_fitness>gbest_fitness_temp)
//                System.out.printf("Epoch %d, fitness is %f\n",n,gbest_fitness);
        }
        //打印位置相关记录
//        System.out.print("[");
//        for (int i = 0; i < gbest.length; i++) {
//            if(i!=gbest.length-1)
//                System.out.print(gbest[i] + ", ");
//            else
//                System.out.print(gbest[i]);
//        }
//        System.out.println("]");
//
//        System.out.println(task1);
//        System.out.println(task2);
//        System.out.println(task3);
//        System.out.println(task4);
    }

    /**
     * 返回一个0~1的随机数
     *
     * @return
     */
    public static double rand() {
        return new Random().nextDouble();
    }

    public static double[] getBestLocation(){
        return gbest;
    }

    public static void main(String[] args) {
        process(PathFactory.createPath("normal"), "multi");
    }

}
