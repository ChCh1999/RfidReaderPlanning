package util;

import base.Location;
import base.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PathFactory {
    /**
     * Generate tags' path
     *
     * @param mode include normal and random,
     *             normal means each 10 tags move along the diagonal,
     *             random means each tag chooses their own path randomly in the area.
     * @return tags
     */
    public static List<Tag> createPath(String mode) {
        List<Tag> tagList = new ArrayList<>();
        if (mode.equals("normal")) {
            for (int i = 0; i < Parameter.tagNum / 4; i++) {
                List<Location> locList1 = GenerateLocation.
                        generateRandomLocationList(new Location(Parameter.minPosition, Parameter.minPosition), new Location(Parameter.maxPosition, Parameter.maxPosition), Parameter.slotNum);
                List<Location> locList2 = GenerateLocation.
                        generateRandomLocationList(new Location(Parameter.minPosition, Parameter.maxPosition), new Location(Parameter.maxPosition, Parameter.minPosition), Parameter.slotNum);
                List<Location> locList3 = GenerateLocation.
                        generateRandomLocationList(new Location(Parameter.maxPosition, Parameter.minPosition), new Location(Parameter.minPosition, Parameter.maxPosition), Parameter.slotNum);
                List<Location> locList4 = GenerateLocation.
                        generateRandomLocationList(new Location(Parameter.maxPosition, Parameter.maxPosition), new Location(Parameter.minPosition, Parameter.minPosition), Parameter.slotNum);
                tagList.add(new Tag(i * 4, locList1));
                tagList.add(new Tag(i * 4 + 1, locList2));
                tagList.add(new Tag(i * 4 + 2, locList3));
                tagList.add(new Tag(i * 4 + 3, locList4));
            }
        } else if (mode.equals("random")) {
            for (int i = 0; i < Parameter.tagNum; i++) {
                List<Location> locList =
                        GenerateLocation.generateRandomLocationList(
                                new Location(new Random().nextDouble() * (Parameter.maxPosition - Parameter.minPosition) + Parameter.minPosition, new Random().nextDouble() * (Parameter.maxPosition - Parameter.minPosition) + Parameter.minPosition),
                                new Location(new Random().nextDouble() * (Parameter.maxPosition - Parameter.minPosition) + Parameter.minPosition, new Random().nextDouble() * (Parameter.maxPosition - Parameter.minPosition) + Parameter.minPosition),
                                Parameter.slotNum);
                tagList.add(new Tag(i, locList));
            }
        } else if (mode.equals("conveyor")) {
            for (int i = 0; i < Parameter.tagNum; i++) {
                List<Location> locList = GenerateLocation.generateConveyorLocationList();
                tagList.add(new Tag(i, locList));
            }
        } else  if (mode.equals("robot")) {
            for (int i = 0; i < Parameter.tagNum; i++) {
                List<Location> locList = GenerateLocation.generateRobotLocationList();
                tagList.add(new Tag(i, locList));
            }
        }

        return tagList;
    }
}
