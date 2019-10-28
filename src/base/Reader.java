package base;

import sun.awt.geom.AreaOp;
import util.Method;
import util.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * 读写器类
 */
public class Reader {
    /**
     * 读写器位置，在所有时隙读写器位置不变
     */
    public Location loc;

    /**
     * 读写器ID，每个读写器的ID是唯一的
     */
    public int id;

    /**
     * 构造函数
     * @param id ID
     * @param loc 位置
     */
    public Reader(int id, Location loc) {
        this.id = id;
        this.loc = loc;
    }

    /**
     * 计算在某个时隙中读写器覆盖的标签数
     */
    public int tagNumInASlot(List<Tag> tagList, int slotIndex) {
        int tagNumInASlot = 0;
        for (Tag t : tagList) {
            double tag_x = t.locList.get(slotIndex).x;
            double tag_y = t.locList.get(slotIndex).y;
            if (Method.locWithinReader(tag_x, tag_y, this.loc.x, this.loc.y, Parameter.ri)) {
                tagNumInASlot++;
            }
        }
        return tagNumInASlot;
    }

    /**
     * 读写器执行任务1在某个时隙的得分
     */
    public int scoreInTask1(List<Tag> tagList, int slotIndex) {
        return tagNumInASlot(tagList, slotIndex) * Parameter.scoreTask1;
    }

    /**
     * 读写器执行任务2在某个时隙的得分
     */
    public int scoreInTask2(List<Tag> tagList, int slotIndex) {
        // 如果当前时隙不需要监控标签，直接返回0
        if (slotIndex % Parameter.interval != 0)
            return 0;

        // 记录这个时隙内、在任务2监控区域中的标签
        List<Tag> matchTagList = new ArrayList<>();
        for (Tag t : tagList) {
            if (Method.pointLocateInArea(t.locList.get(slotIndex).x, t.locList.get(slotIndex).y,
                    Parameter.task2MinPosition, Parameter.task2MaxPosition))
                matchTagList.add(t);
        }
        return tagNumInASlot(matchTagList, slotIndex) * Parameter.scoreTask2;
    }

    /**
     * 读写器执行任务3在某个时隙的得分
     */
    public int scoreInTask3(List<Tag> tagList, int slotIndex) {
        return tagNumInASlot(tagList, slotIndex) * Parameter.scoreTask3;
    }

    /**
     * 读写器执行任务4在某个时隙的得分
     */
    public int scoreInTask4(List<Tag> tagList, int slotIndex) {
        List<Tag> tagListInReader = tagInASlot(tagList, slotIndex);
        for (Tag t : tagListInReader) {
            if (t.id == Parameter.task4ID) {
                return Parameter.scoreTask4;
            }
        }
        return 0;
    }

    /**
     * 某个时隙内读写器覆盖的标签集合
     */
    public List<Tag> tagInASlot(List<Tag> tagList, int slotIndex) {
        List<Tag> res = new ArrayList<>();
        for (Tag t : tagList) {
            double tag_x = t.locList.get(slotIndex).x;
            double tag_y = t.locList.get(slotIndex).y;
            if (Method.locWithinReader(tag_x, tag_y, this.loc.x, this.loc.y, Parameter.ri)) {
                res.add(t);
            }
        }
        return res;
    }
}
