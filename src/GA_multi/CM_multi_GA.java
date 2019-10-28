//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package GA_multi;

import base.Reader;
import base.Tag;

import util.Method;
import util.Parameter;

import java.util.ArrayList;
import java.util.List;

public class CM_multi_GA {

    /**
     * Record all tags, do not change with iteration
     */
    static List<Tag> globalTagList;

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
     * Weighted average
     */
    static double averageScore;

    public static double evaluate(double[] position) {
        List<Reader> readerList = Method.Position2ReaderList(position);

        // Generate a local tag collection, initial value all tags
        List<Tag> localTagList = new ArrayList<>();
        localTagList.addAll(globalTagList);


        // Record the score and total score of each task
        int totalScore = 0;
        int task1Score = 0;
        int task2Score = 0;
        int task3Score = 0;
        int task4Score = 0;


        for (int i = 0; i < Parameter.slotNum; i++) {
            for (int j = 0; j < Parameter.readerNum; j++) {

                // get tag list in this slot
                List<Tag> tagList = readerList.get(j).tagInASlot(globalTagList, i);

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
        return averageScore;
    }

    /**
     * Set global variables - how complete each task is
     */
    private static void setTaskCompletionDegree(int task1Score, int task2Score, int task3Score, int task4Score) {
//        System.out.printf("Score:%d %d %d %d",task1Score,task2Score,task3Score,task4Score);
        task1CompletionDegree = (double) task1Score / (Parameter.scoreTask1 * Parameter.tagNum * Parameter.slotNum);
        // Task 2 needs to be monitored in all timeslot
        int task2TagNumInAllSlots = 0;

        for (int i = 0; i < Parameter.slotNum; i += Parameter.interval) {
            for (Tag t : globalTagList) {
                if (Method.pointLocateInArea(t.locList.get(i).x, t.locList.get(i).y,
                        Parameter.task2MinPosition, Parameter.task2MaxPosition))
                    task2TagNumInAllSlots++;
            }
        }
//        System.out.printf("%d %d;",task2Score/Parameter.scoreTask2,task2TagNumInAllSlots);
        task2CompletionDegree = (double) task2Score  / (Parameter.scoreTask2 * task2TagNumInAllSlots * Parameter.readerNum);
        task3CompletionDegree = (double) task3Score / (Parameter.scoreTask3 * Parameter.tagNum);
        task4CompletionDegree = (double) task4Score / (Parameter.scoreTask4 * Parameter.slotNum * Parameter.readerNum);

        averageScore = (task1CompletionDegree * Parameter.scoreTask1 +
                task2CompletionDegree * Parameter.scoreTask2 +
                task3CompletionDegree * Parameter.scoreTask3 +
                task4CompletionDegree * Parameter.scoreTask4)
                /
                (Parameter.scoreTask1 + Parameter.scoreTask2 + Parameter.scoreTask3
                        + Parameter.scoreTask4);
    }
}
