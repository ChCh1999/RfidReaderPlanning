package base;

import util.Method;
import util.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * 标签类
 */
public class Tag {
    /**
     * 标签ID
     */
    public int id;

    /**
     * 标签在所有时隙的位置
     */
    public List<Location> locList;

    /**
     * 构造函数
     */
    public Tag(int id, List<Location> loc) {
        this.id = id;
        this.locList = loc;
    }

    /**
     * 给定读写器位置，且默认读写器在每个时隙都处于打开状态，计算所有时隙中读写器识别的标签数
     */
    public int calIdentifiedNum(List<Reader> readerList) {
        int identifiedNum = 0;

        for (Location location : locList) {
            double t_x = location.x;     // 标签X坐标
            double t_y = location.y;     // 标签Y坐标
            List<Reader> underReaderList = new ArrayList<>();   // 覆盖该标签的读写器
            for (int j = 0; j < readerList.size(); j++) {
                double r_x = readerList.get(j).loc.x;
                double r_y = readerList.get(j).loc.y;
                if (Method.locWithinReader(r_x, r_y, t_x, t_y, Parameter.ri)) {
                    underReaderList.add(readerList.get(j));
                }
            }

            boolean isIdentified = true;
            if (underReaderList.size() == 1) {      // 标签只位于一个读写器的覆盖范围内
                Reader r = underReaderList.get(0);
                for (Reader rOther : readerList) {
                    if (rOther.id == r.id) continue;
                    else {
                        // 如果覆盖标签的读写器不位于其他读写器的干扰范围内，则该标签被识别
                        if (Method.locWithinReader(r.loc.x, r.loc.y, rOther.loc.x, rOther.loc.y, Parameter.rf)) {
                            isIdentified = false;
                            break;
                        }
                    }
                }
                if (isIdentified) identifiedNum++;
            }
        }

        return identifiedNum;
    }

    /**
     * 给定读写器位置，且默认读写器在每个时隙都处于打开状态，计算所有时隙中读写器识别的标签数
     * 且不考虑任何碰撞
     */
    public int calIdentifiedNumWithoutConsiderCollision(List<Reader> readerList) {
        int identifiedNum = 0;

        for (Location location : locList) {
            double t_x = location.x;     // 标签X坐标
            double t_y = location.y;     // 标签Y坐标
            List<Reader> underReaderList = new ArrayList<>();   // 覆盖该标签的读写器
            for (int j = 0; j < readerList.size(); j++) {
                double r_x = readerList.get(j).loc.x;
                double r_y = readerList.get(j).loc.y;
                if (Method.locWithinReader(r_x, r_y, t_x, t_y, Parameter.ri)) {
                    underReaderList.add(readerList.get(j));
                }
            }

            // 只要覆盖该标签的读写器数量大于0，该标签被识别
            if (underReaderList.size() >= 1) {
                identifiedNum++;
            }
        }

        return identifiedNum;
    }

    /**
     * 给定读写器位置和开关，计算所有时隙被识别的标签总数
     * state表示每个读写器在每个时隙的状态，第一维是时隙，第二维是读写器
     */
    public int calIdentifiedNumWithReaderState(List<Reader> readerList, boolean[][] state) {
        int identifiedNum = 0;
        for (int i = 0; i < locList.size(); i++) {
            double t_x = this.locList.get(i).x;     // 标签X坐标
            double t_y = this.locList.get(i).y;     // 标签Y坐标
            List<Reader> underReaderList = new ArrayList<>();
            for (int j = 0; j < readerList.size(); j++) {
                double r_x = readerList.get(j).loc.x;
                double r_y = readerList.get(j).loc.y;
                if (Method.locWithinReader(r_x, r_y, t_x, t_y, Parameter.ri) && state[i][j]) {
                    underReaderList.add(readerList.get(j));
                }
            }

            if (underReaderList.size() == 1) {
                boolean isIdentified = true;
                Reader r = underReaderList.get(0);
                for (int j = 0; j < readerList.size(); j++) {
                    Reader rOther = readerList.get(j);
                    if (rOther.id == r.id || !state[i][j]) continue;
                    else {
                        if (Method.locWithinReader(r.loc.x, r.loc.y, rOther.loc.x, rOther.loc.y, Parameter.rf)) {
                            isIdentified = false;
                            break;
                        }
                    }
                }
                if (isIdentified) identifiedNum++;
            }
        }

        return identifiedNum;
    }

}
