package util;

import base.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 生成一个标签运动轨迹
 */
public class GenerateLocation {
    /**
     * 标签以随机速度完成直线运动
     *
     * @param start 标签开始位置
     * @param end   标签结束为止
     * @param n     时隙数
     * @return 标签运动轨迹
     */
    public static List<Location> generateRandomLocationList(Location start, Location end, int n) {
        List<Location> list = new ArrayList<>();
        double dx = end.x - start.x;
        double dy = end.y - start.y;
        double[] a = new double[n];
        double sumA = 0;
        for (int i = 0; i < n; i++) {
            a[i] = new Random().nextDouble();
            sumA += a[i];
        }
        list.add(start);
        for (int i = 1; i < n; i++) {
            a[i] += a[i - 1];
            Location l = new Location(start.x + dx * a[i] / sumA, start.y + dy * a[i] / sumA);
            list.add(l);
        }
        return list;
    }

    /**
     * 传送带场景，该场景下标签随机从（0，X）运动到（A，X），然后运动到（A，Y），最后运动到（25，Y）
     *
     * @return 标签运动轨迹
     */
    public static List<Location> generateConveyorLocationList() {
        List<Location> list = new ArrayList<>();

        int interval = 5;   // 传送带之间的间隔
        int conveyorNum = 5;    // 传送带数量
        int start = new Random().nextInt(conveyorNum + 1) * interval;
        int end = new Random().nextInt(conveyorNum + 1) * interval;
        int change = new Random().nextInt(conveyorNum - 1) * interval + interval;

        // 生成轨迹
        for (int i = 0; i < change; i++) {
            list.add(new Location(i, start));
        }

        if (start < end) {
            for (int i = start; i < end; i++) {
                list.add(new Location(change, i));
            }
        } else {
            for (int i = start; i > end; i--) {
                list.add(new Location(change, i));
            }
        }

        for (int i = change; i <= 25; i++) {
            list.add(new Location(i, end));
        }

        // 补全所有时隙数
        while (list.size() < Parameter.slotNum) {
            list.add(new Location(interval * conveyorNum, end));
        }
        // 删除多余时隙数
        if (list.size() == Parameter.slotNum + 1) {
            list.remove(list.size() - 1);
        }
        return list;
    }

    /**
     * @return
     */
    public static List<Location> generateRobotLocationList() {
        List<Location> list = new ArrayList<>();

        int y = new Random().nextInt(6) * 5;
        if (new Random().nextDouble() < 0.5) {
            // 从左往右
            for (double d = 0; d < 25; d += 0.5) {
                list.add(new Location(d, y));
            }
        } else {
            // 从右往左
            for (double d = 25; d > 0; d -= 0.5) {
                list.add(new Location(d, y));
            }
        }
        return list;
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
//        List<Location> l = generateRandomLocationList(new Location(0, 0), new Location(10, 10), 5);
        int tagNum=50;
        int slots=100;
        for(int i=0;i<tagNum;i++){
            Location start=new Location(Math.random()*10,Math.random()*10);
            Location end=new Location(Math.random()*10,Math.random()*10);
            List<Location> res=generateRandomLocationList(start,end,slots);
            for(int j=0;j<res.size();j++){
                System.out.print(" "+res.get(j).x+" "+res.get(j).y);
            }
            System.out.print("\n");
        }

    }
}
