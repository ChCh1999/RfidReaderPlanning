package util;

import base.Location;
import base.Reader;
import net.sourceforge.jswarm_pso.Swarm;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 工具方法
 */
public class Method {
    /**
     * 确定某个位置是否位于读写器的范围内
     *
     * @param loc_x    位置的X坐标
     * @param loc_y    位置的Y坐标
     * @param reader_x 读写器的X坐标
     * @param reader_y 读写器的Y坐标
     * @param r        读写器半径
     * @return true false
     */
    public static boolean locWithinReader(double loc_x, double loc_y, double reader_x, double reader_y, double r) {
        return Math.pow(loc_x - reader_x, 2) + Math.pow(loc_y - reader_y, 2) < r * r;
    }

    /**
     * 输出最优粒子的位置和适应值
     *
     * @param swarm 粒子的信息
     * @return
     */
    public static String printSwarmInfo(Swarm swarm) {
        DecimalFormat df = new DecimalFormat("######0.00");
        StringBuilder stats = new StringBuilder();
        if (!Double.isNaN(swarm.getBestFitness())) {
            stats.append("Best fitness: ").append(swarm.getBestFitness()).append("\nBest position: \t[");
            for (int i = 0; i < swarm.getBestPosition().length; i++)
                stats.append(df.format(swarm.getBestPosition()[i])).append(i < (swarm.getBestPosition().length - 1) ? ", " : "");
            stats.append("]");
        }
        return stats.toString();
    }


    /**
     * 确定一个点是否位于一个矩形区域内
     *
     * @param point_x  点的X坐标
     * @param point_y  点的Y坐标
     * @param area_min 区域的最小值
     * @param area_max 区域的最大值
     * @return
     */
    public static boolean pointLocateInArea(double point_x, double point_y, double area_min, double area_max) {
        return point_x > area_min && point_x < area_max && point_y > area_min && point_y < area_max;
    }

    /**
     * 将double数组的位置转化为读写器集合
     * @param position 读写器位置
     * @return
     */
    public static List<Reader> Position2ReaderList(double[] position) {
        List<Reader> readerList = new ArrayList<>();
        for (int i = 0; i < Parameter.readerNum; i++) {
            Reader r = new Reader(i, new Location(position[2 * i], position[2 * i + 1]));
            readerList.add(r);
        }

        return readerList;
    }

    public static boolean[][] Position2ReaderState(double[] position,int slotNum,int readerNum){
        boolean[][] state=new boolean[slotNum][readerNum];
        //System.out.println("Position dimentionality is:"+position.length);
        for(int i=0;i<Parameter.slotNum;i++){
            int stateNumber=(int)position[i];
            for(int j=0;j<Parameter.readerNum;j++){
                state[i][j]=((stateNumber&1)==1);
                stateNumber=stateNumber>>>1;
            }
        }
        return state;
    }

}
