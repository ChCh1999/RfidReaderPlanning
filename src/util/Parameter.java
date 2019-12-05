package util;

/**
 * 算法运行参数
 */
public class Parameter {
    /**
     * 时隙数
     */
    public static int slotNum = 100;
    public static int particleNum=50;
    /**
     * 读写器部署区域的最大值
     */
    public static double maxPosition = 3;

    /**
     * 读写器部署区域的最小值
     */
    public static double minPosition = 0;

    /**
     * 标签数
     */
    public static int tagNum = 40;

    /**
     * 读写器数量
     */
    public static int readerNum = 10;

    /**
     * 迭代数量
     */
    public static int cycleNum = 1000;

    /**
     * 运行得到最大值的次数
     */
    public static int runCycle = 1;
    /**
     * 读写器识别半径
     */
    public static double ri = 1;

    /**
     * 读写器干扰半径
     */
    public static double rf = 1.5;

    /**
     * 任务数量
     */
    public static int taskNum = 4;

    /**
     * 任务1的得分
     */
    public static int scoreTask1 = 5;

    /**
     * 任务2的得分
     */
    public static int scoreTask2 = 10;

    /**
     * 任务3的得分
     */
    public static int scoreTask3 = 15;

    /**
     * 任务4的得分
     */
    public static int scoreTask4 = 70;

    /**
     * 任务4监控的标签ID
     */
    public static int task4ID = 9;

    /**
     * 任务2监控区域的最大值
     */
    public static double task2MaxPosition = 2.25;

    /**
     * 任务2监控区域的最小值
     */
    public static double task2MinPosition = 0.75;

    /**
     * 任务2监控的时间间隔
     */
    public static int interval = 1;

    /**
     * 比较两个Double是否相等
     */
    public static double THRESHOLD = .0001;

    /**
     *  阅读器之间距离的约束权重
     */
    public static double distancefactor=0.0001;

}
