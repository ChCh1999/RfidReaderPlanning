package PSO_multi;

import base.Location;
import base.Reader;
import base.Tag;
import net.sourceforge.jswarm_pso.FitnessFunction;
import util.Method;
import util.Parameter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AOCM_multi_PSO extends FitnessFunction {
    /**
     * Record all tags, do not change with iteration
     */
    static List<Tag> globalTagList;
    /**
     * Tags for each iteration, which can be changed
     */
    private List<Tag> localTagList;

    /**
     * Record task 1 completion
     */
    static double task1CompletionDegree;
    /**
     * Record task 2 completion
     */
    static double task2CompletionDegree;
    /**
     * Record task 3 completion
     */
    static double task3CompletionDegree;
    /**
     * Record task 4 completion
     */
    static double task4CompletionDegree;
    /**
     * Record each reader chooses which task in each slot
     */
    static double[][] chooseTask;
    static int[][][] scoreGlobal;

    @Override
    public double evaluate(double[] position) {
        // Generate a reader set
        List<Reader> readerList = new ArrayList<>();
        for (int i = 0; i < Parameter.readerNum; i++) {
            Reader r = new Reader(i, new Location(position[2 * i], position[2 * i + 1]));
            readerList.add(r);
        }

        // Generate a local tag collection, initial value all tags
        localTagList = new ArrayList<>();
        localTagList.addAll(globalTagList);

        double[][] chooseTaskLocal = new double[readerList.size()][Parameter.slotNum];
        int[][][] scoreLocal = new int[Parameter.readerNum][Parameter.slotNum][Parameter.taskNum];

        // Record the score and total score of each task
        int totalScore = 0;
        int task1Score = 0;
        int task2Score = 0;
        int task3Score = 0;
        int task4Score = 0;

        // Auxiliary array, recording whether a reader is located within the interference range of other readers
        boolean[] state = newState(readerList);

        for (int i = 0; i < Parameter.slotNum; i++) {
            for (int j = 0; j < Parameter.readerNum; j++) {

                // Get the tag set under this reader in the time slot
                List<Tag> tagList = readerList.get(j).tagInASlot(globalTagList, i);
                // Traverse the tag to determine if it is within the identification radius of other readers, and if so, remove it
                Iterator<Tag> it = tagList.iterator();
                while (it.hasNext()) {
                    Tag t = it.next();
                    if (locateWithinOtherReader(t, j, i, readerList)) {
                        it.remove();
                    }
                }
                // Record a single list for task3, need to do an intersection with the unidentified localList
                List<Tag> tagList3 = new ArrayList<>();
                for (Tag t : tagList) {
                    if (localTagList.contains(t))
                        tagList3.add(t);
                }
                // Get all tasks' scores
                int[] score = new int[]{
                        readerList.get(j).scoreInTask1(tagList, i),
                        readerList.get(j).scoreInTask2(tagList, i),
                        readerList.get(j).scoreInTask3(tagList3, i),
                        readerList.get(j).scoreInTask4(tagList, i),
                };
                // Get the maximum value and its subscript
                int maxIndex = 0;
                int maxScore = score[0];
                for (int k = 1; k < score.length; k++) {
                    if (score[k] > maxScore) {
                        maxIndex = k;
                        maxScore = score[k];
                    }
                }
                chooseTaskLocal[j][i] = maxIndex + 1;
                scoreLocal[j][i] = score;

                // First, determine if the reader is under the interference radius of another reader. If yes, skip it directly.
                if (state[j] == true) continue;

                // Add points
                totalScore += maxScore;
                if (maxIndex == 0) task1Score += maxScore;
                else if (maxIndex == 1) task2Score += maxScore;
                else if (maxIndex == 2) task3Score += maxScore;
                else if (maxIndex == 3) task4Score += maxScore;

                // If you select the third task, need to update the localTag
                if (maxIndex == 2)
                    localTagList.removeAll(tagList3);
            }
        }

        setTaskCompletionDegree(task1Score, task2Score, task3Score, task4Score);

        chooseTask = new double[readerList.size()][Parameter.slotNum];
        for (int i = 0; i < chooseTaskLocal.length; i++) {
            for (int j = 0; j < chooseTaskLocal[0].length; j++) {
                chooseTask[i][j] = chooseTaskLocal[i][j];
            }
        }

        scoreGlobal = new int[Parameter.readerNum][Parameter.slotNum][Parameter.taskNum];
        for (int i = 0; i < scoreLocal.length; i++) {
            for (int j = 0; j < scoreLocal[0].length; j++) {
                for (int k = 0; k < scoreLocal[0][0].length; k++) {
                    scoreGlobal[i][j][k] = scoreLocal[i][j][k];
                }
            }
        }

        return (task1CompletionDegree * Parameter.scoreTask1 +
                task2CompletionDegree * Parameter.scoreTask2 +
                task3CompletionDegree * Parameter.scoreTask3 +
                task4CompletionDegree * Parameter.scoreTask4)
                /
                (Parameter.scoreTask1 + Parameter.scoreTask2 + Parameter.scoreTask3
                        +Parameter.scoreTask4);
    }

    private boolean locateWithinOtherReader(Tag t, int readerIndex, int slotIndex, List<Reader> readerList) {
        for (int i = 0; i < readerList.size(); i++) {
            if (i != readerIndex &&
                    Method.locWithinReader(t.locList.get(slotIndex).x, t.locList.get(slotIndex).y,
                            readerList.get(i).loc.x, readerList.get(i).loc.y, Parameter.ri)) {
                return true;
            }
        }
        return false;
    }

    private boolean[] newState(List<Reader> readerList) {
        boolean[] state = new boolean[Parameter.readerNum];
        for (int i = 0; i < Parameter.readerNum; i++) {
            for (int j = 0; j < Parameter.readerNum; j++) {
                if (i != j &&
                        Method.locWithinReader(
                                readerList.get(i).loc.x, readerList.get(i).loc.y,
                                readerList.get(j).loc.x, readerList.get(j).loc.y,
                                Parameter.rf)
                        ) {
                    state[i] = true;
                }
            }
        }
        return state;
    }

    /**
     * Set global variables - how complete each task is
     */
    private void setTaskCompletionDegree(int task1Score, int task2Score, int task3Score, int task4Score) {
        task1CompletionDegree = (double) task1Score / (Parameter.scoreTask1 * Parameter.tagNum * Parameter.slotNum);
        // Task 2 needs to be monitored in all timeslots
        int task2TagNumInAllSlots = 0;
        for (Tag t : globalTagList) {
            for (int i = 0; i < Parameter.slotNum; i += Parameter.interval) {
                if (Method.pointLocateInArea(t.locList.get(i).x, t.locList.get(i).y,
                        Parameter.task2MinPosition, Parameter.task2MaxPosition))
                    task2TagNumInAllSlots++;
            }
        }
        task2CompletionDegree = (double) task2Score / (Parameter.scoreTask2 * task2TagNumInAllSlots);
        task3CompletionDegree = (double) task3Score / (Parameter.scoreTask3 * Parameter.tagNum);
        task4CompletionDegree = (double) task4Score / (Parameter.scoreTask4 * Parameter.slotNum);
    }
}
