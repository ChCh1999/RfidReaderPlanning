package GA_simple;

import base.Reader;
import base.Tag;
import fitness.SingleFitness;
import util.Method;

import java.util.List;

public class AOCM {
    public static List<Tag> tagList;

    public static double evaluate(double[] position) {
        List<Reader> readerList = Method.Position2ReaderList(position);
        return SingleFitness.AOCM(readerList, tagList);
    }
}
