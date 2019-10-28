package fitness;

import base.Reader;
import base.Tag;
import util.Method;
import util.Parameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 计算单任务的适应值
 */
public class SingleFitness {

    // 最大覆盖策略，阅读器全部打开，不考虑阅读器碰撞
    public static double CM(List<Reader> readerList, List<Tag> tagList) {
        int res = 0;
        for (Tag t : tagList) {
            res += t.calIdentifiedNumWithoutConsiderCollision(readerList);
        }
        return (double) res / (Parameter.tagNum * Parameter.slotNum);
    }

    // 常开策略，阅读器全部打开，考虑碰撞
    public static double AOCM(List<Reader> readerList, List<Tag> tagList) {
        int res = 0;
        for (Tag t : tagList) {
            res += t.calIdentifiedNum(readerList);
        }

        return (double) res / (Parameter.tagNum * Parameter.slotNum);
    }

    // 调度策略
    public static double RS(List<Reader> readerList, List<Tag> tagList) {
        // Total number of tags identified by all time slots
        int res = 0;

        // Traverse every time slot
        for (int i = 0; i < Parameter.slotNum; i++) {
            // The number of tags covered by each reader in the time slot
            Map<Reader, Integer> map = new HashMap<>();
            // The number of tags covered by the reader in this time slot
            for (Reader r : readerList) {
                map.put(r, r.tagNumInASlot(tagList, i));
            }

            // Each time you choose to cover the reader with the most tags, think of the tag it covers as an identifiable tag and remove all readers that overlap it.
            while (!map.isEmpty()) {
                // Sort by value field in descending order, result is stored in list
                List<Map.Entry<Reader, Integer>> list = new ArrayList<>(map.entrySet());
                list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));

                if (list.get(0).getValue() == 0)    break;

                double maxReader_x = list.get(0).getKey().loc.x;
                double maxReader_y = list.get(0).getKey().loc.y;
                // Traverse other readers and delete them if conflicts occur
                for (int j = 1; j < list.size(); j++) {
                    double currentReader_x = list.get(j).getKey().loc.x;
                    double currentReader_y = list.get(j).getKey().loc.y;
                    if (Method.locWithinReader(maxReader_x, maxReader_y, currentReader_x, currentReader_y,
                            Math.max(2 * Parameter.ri, Parameter.rf))) {
                        map.remove(list.get(j).getKey());
                    }
                }
                // Plus the identified tag
                res += list.get(0).getValue();
                map.remove(list.get(0).getKey());
            }
        }
        return (double) res / (Parameter.tagNum * Parameter.slotNum);
    }
    // 同步优化策略，需要考虑读写器的位置以及开关状态
    public static double BPSO(List<Reader> readerList, List<Tag> tagList, boolean[][] state) {
        int res = 0;
        for (Tag t : tagList) {
            res += t.calIdentifiedNumWithReaderState(readerList, state);
        }

        return (double) res / (Parameter.tagNum * Parameter.slotNum);
    }

    /**
     * 读写器距离权重因子
     * @param readerList
     * @return
     */
    public static double readerDistance(List<Reader> readerList){
        double res=0;
        //double totalDistance=0;
        for (int i = 0; i < readerList.size(); i++) {
            for (int j = i + 1; j < readerList.size(); j++) {
                Reader r1 = readerList.get(i);
                Reader r2 = readerList.get(j);
                double distance = Math.sqrt(Math.pow(r1.loc.x - r2.loc.x, 2) + Math.pow(r1.loc.y - r2.loc.y, 2));
                res += distance;
            }
        }
        return res*Parameter.distancefactor;
    }

}
