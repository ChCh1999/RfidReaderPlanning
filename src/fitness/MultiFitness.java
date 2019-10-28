package fitness;

import base.Location;
import base.Reader;
import base.Tag;
import util.Method;
import util.Parameter;

import java.util.ArrayList;
import java.util.List;

public class MultiFitness {

    /**
     * Record all tags, do not change with iteration
     */
    public static List<Tag> globalTagList;
    /**
     * Record task 1 completion
     */
    public static double task1CompletionDegree;
    /**
     * Record task 2 completion
     */
    public static double task2CompletionDegree;
    /**
     * Record task 3 completion
     */
    public static double task3CompletionDegree;
    /**
     * Record task 4 completion
     */
    public static double task4CompletionDegree;
    /**
     * Record each reader chooses which task in each slot
     */
    public static double[][] chooseTask;
    /**
     * Tags for each iteration, which can be changed
     */
    private static List<Tag> localTagList;

    /**
     * main function
     */
    public static double RS(double[] position) {
        // Generate a reader set
        List<Reader> readerList = new ArrayList<>();
        for (int i = 0; i < Parameter.readerNum; i++) {
            Reader r = new Reader(i, new Location(position[2 * i], position[2 * i + 1]));
            readerList.add(r);
        }

        // Generate a local tag collection, initial value all tags
        localTagList = new ArrayList<>();
        localTagList.addAll(globalTagList);

        // Scoring array, recording each reader's score for each task per slot
        int[][][] score = newScore(readerList);
        double[][] chooseTaskLocal = new double[readerList.size()][Parameter.slotNum];

        // Switch status table, recording the switch of each time slot of each reader
        // Java boolean will automatically evaluate to false, false means the time slot of this reader is not processed
        boolean[][] state = new boolean[Parameter.readerNum][Parameter.slotNum];

        // Record the score and total score of each task
        int totalScore = 0;
        int task1Score = 0;
        int task2Score = 0;
        int task3Score = 0;
        int task4Score = 0;

        // If the array has non-zero elements, it should continue to loop
        while (!allZero(score, state)) {
            // Get the position and score of the largest element, the array member is the reader, slot, task, score
            int[] maxPosition = findMax(score, state);
            totalScore += maxPosition[3];
            if (maxPosition[2] == 0)
                task1Score += maxPosition[3];
            else if (maxPosition[2] == 1)
                task2Score += maxPosition[3];
            else if (maxPosition[2] == 2)
                task3Score += maxPosition[3];
            else if (maxPosition[2] == 3)
                task4Score += maxPosition[3];
            chooseTaskLocal[maxPosition[0]][maxPosition[1]] = maxPosition[2] + 1;
            update(score, state, maxPosition, readerList);
        }

        chooseTask = new double[readerList.size()][Parameter.slotNum];
        for (int i = 0; i < chooseTaskLocal.length; i++) {
            for (int j = 0; j < chooseTaskLocal[0].length; j++) {
                chooseTask[i][j] = chooseTaskLocal[i][j];
            }
        }
        setTaskCompletionDegree(task1Score, task2Score, task3Score, task4Score);
        return (task1CompletionDegree * Parameter.scoreTask1 +
                task2CompletionDegree * Parameter.scoreTask2 +
                task3CompletionDegree * Parameter.scoreTask3 +
                task4CompletionDegree * Parameter.scoreTask4)
                /
                (Parameter.scoreTask1 + Parameter.scoreTask2 + Parameter.scoreTask3
                        + Parameter.scoreTask4);
    }

    /**
     * Set global variables - how complete each task is
     */
    private static void setTaskCompletionDegree(int task1Score, int task2Score, int task3Score, int task4Score) {
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

    /**
     * Generate an array of ratings
     */
    private static int[][][] newScore(List<Reader> readerList) {
        // Array subscripts start from 0
        int[][][] score = new int[Parameter.readerNum][Parameter.slotNum][Parameter.taskNum];
        for (int i = 0; i < Parameter.readerNum; i++) {
            for (int j = 0; j < Parameter.slotNum; j++) {
                score[i][j] = new int[]{
                        readerList.get(i).scoreInTask1(localTagList, j),
                        readerList.get(i).scoreInTask2(localTagList, j),
                        readerList.get(i).scoreInTask3(localTagList, j),
                        readerList.get(i).scoreInTask4(localTagList, j)
                };
            }
        }
        return score;
    }

    /**
     * To determine if there is a non-zero element in the score array
     */
    private static boolean allZero(int[][][] score, boolean[][] state) {
        for (int i = 0; i < score.length; i++) {
            for (int j = 0; j < score[i].length; j++) {
                for (int k = 0; k < score[i][j].length; k++) {
                    if (score[i][j][k] > 0 && !state[i][j])
                        return false;
                }
            }
        }
        return true;
    }

    /**
     * Finding the largest unprocessed element in the score array
     */
    private static int[] findMax(int[][][] score, boolean[][] state) {
        int maxReader = -1;
        int maxSlot = -1;
        int maxTask = -1;
        int maxScore = -1;

        for (int i = 0; i < score.length; i++) {
            for (int j = 0; j < score[i].length; j++) {
                for (int k = 0; k < score[i][j].length; k++) {
                    if (score[i][j][k] > maxScore && !state[i][j]) {
                        maxReader = i;
                        maxSlot = j;
                        maxTask = k;
                        maxScore = score[i][j][k];
                    }
                }
            }
        }

        return new int[]{maxReader, maxSlot, maxTask, maxScore};
    }

    /**
     * Update score and status tables
     */
    private static void update(int[][][] score, boolean[][] state, int[] maxPosition, List<Reader> readerList) {
        // Reader and time slot to get the largest element
        int readerID = maxPosition[0];
        int slotID = maxPosition[1];
        // Get the reader's coordinates
        double maxReader_x = readerList.get(readerID).loc.x;
        double maxReader_y = readerList.get(readerID).loc.y;
        // Close all other readers that collided with the reader/writer and set the status to 1
        // Note that the maximum reader itself is also set to 1 because it has already been scanned.
        for (int i = 0; i < Parameter.readerNum; i++) {
            double currentReader_x = readerList.get(i).loc.x;
            double currentReader_y = readerList.get(i).loc.y;
            if (Method.locWithinReader(maxReader_x, maxReader_y, currentReader_x, currentReader_y,
                    Math.max(2 * Parameter.ri, Parameter.rf)))
                state[i][slotID] = true;
        }

        // If task 3 is executed, the score table needs to be updated because task 3 is globally related
        if (maxPosition[2] == 2) {
            // Get the identified tags and remove them from the list of local tags
            List<Tag> hasIdentified = readerList.get(readerID).tagInASlot(localTagList, slotID);
            localTagList.removeAll(hasIdentified);
            // Calculate the score using the updated tag list, update task 3 score items for all tables
            for (int i = 0; i < score.length; i++) {
                for (int j = 0; j < score[i].length; j++) {
                    score[i][j][2] = readerList.get(i).scoreInTask3(localTagList, j);
                }
            }
        }
    }

    /**
     *  读写器距离权重因子
     */
    public static double readerDistance(List<Reader> readerList){
        double res=0;
        //double totalDistance=0;
        for(int i=0;i<readerList.size();i++){
            for(int j=i+1;j<readerList.size();j++){
                Reader r1=readerList.get(i);
                Reader r2=readerList.get(j);
                double distance=Math.sqrt(Math.pow(r1.loc.x-r2.loc.x,2)+Math.pow(r1.loc.y-r2.loc.y,2));
                res+=distance;
            }
        }
        return res*Parameter.distancefactor;
    }
}

