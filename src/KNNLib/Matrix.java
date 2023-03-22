package KNNLib;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Matrix {
    // reads in file and makes 2d matrix out of it
    ArrayList<ArrayList<Double>> matrix;

    public Matrix(ArrayList<ArrayList<Double>> matrix) {
        this.matrix = matrix;
    }

    public Matrix() {
        this.matrix = new ArrayList<>();
    }

    public Matrix(String filePath) {
        this.matrix = new ArrayList<>();

        String line = "";
        String delimiter = ","; // specify the delimiter used in your CSV file

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String header = br.readLine(); // clear header line
            while ((line = br.readLine()) != null) {
                ArrayList<String> row = new ArrayList<>(Arrays.asList(line.split(delimiter)));
                matrix.add(parseRow(row));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public ArrayList<ArrayList<Double>> getMatrix() {
        return matrix;
    }

    public void setMatrix(ArrayList<ArrayList<Double>> matrix) {
        this.matrix = matrix;
    }

    public int getCategoryAttribute(){
        return matrix.get(0).size()-1;
    }

    //returns number of rows to calculate recall
    public int getRelevantDocumentCount(int classifier){
        int relevantCount = 0;
        int categoryAtt = getCategoryAttribute();
        for(ArrayList<Double> row: this.matrix){
            if(row.get(categoryAtt) == classifier){
                relevantCount++;
            }
        }
        return relevantCount;

    }

    //splits the matrix into a training and testing set
    // i.e. return [training, testing]
    public ArrayList<Matrix> splitMatrix(double percent) {
        ArrayList<Matrix> splitMatrices = new ArrayList<>();

        ArrayList<ArrayList<Double>> matrixCopy = new ArrayList<>();
        for (ArrayList<Double> row : this.matrix) {
            ArrayList<Double> rowCopy = new ArrayList<>(row);
            matrixCopy.add(rowCopy);
        }
        Collections.shuffle(matrixCopy);

        // Calculate split
        int numTrainingRows = (int) Math.round(matrixCopy.size() * percent);

        // Split into training and testing sets
        ArrayList<ArrayList<Double>> trainingMatrix = new ArrayList<>(matrixCopy.subList(0, numTrainingRows));
        ArrayList<ArrayList<Double>> testingMatrix = new ArrayList<>(matrixCopy.subList(numTrainingRows, matrixCopy.size()));

        // Create new Matrix objects for the training and testing sets
        Matrix training = new Matrix();
        Matrix testing = new Matrix();

        training.setMatrix(trainingMatrix);
        testing.setMatrix(testingMatrix);

        splitMatrices.add(training);
        splitMatrices.add(testing);

        return splitMatrices;
    }

    //function to combine two matrices
    public static Matrix combine(Matrix m1, Matrix m2){
        ArrayList<ArrayList<Double>> copyM1 = (ArrayList<ArrayList<Double>>) m1.getMatrix().clone();
        ArrayList<ArrayList<Double>> copyM2 = (ArrayList<ArrayList<Double>>) m2.getMatrix().clone();
        for(ArrayList<Double> row : copyM2){
            copyM1.add(row);
        }

        return new Matrix(copyM1);

    }

    public void fillNull(int attribute, double value) {
        for (ArrayList<Double> row : matrix) {
            if (row.get(attribute) == null) {
                row.set(attribute, value);
            }
        }
    }

    public void dropNull(int attribute) {
        int i = 0;
        while (i < matrix.size()) {
            if (matrix.get(i).get(attribute) == null) {
                matrix.remove(i);
            } else {
                i++;
            }
        }
    }

    // parses a row from kaggle_bot_accounts.csv
    // still needs categoric one-hot encoding for REGISTRATION_IPV4 and REGISTRATION_LOCATION
    private ArrayList<Double> parseRow(ArrayList<String> row) {
        ArrayList<Double> parsedRow = new ArrayList<>();

        parsedRow.add(parseGender(row.get(2))); // GENDER (1 = Male, 0 = Female)
        parsedRow.add(parseBoolean(row.get(4))); // IS_GLOGIN
        parsedRow.add(parseNumeric(row.get(5))); // FOLLOWER_COUNT
        parsedRow.add(parseNumeric(row.get(6))); // FOLLOWING_COUNT
        parsedRow.add(parseNumeric(row.get(7))); // DATASET_COUNT
        parsedRow.add(parseNumeric(row.get(8))); // CODE_COUNT
        parsedRow.add(parseNumeric(row.get(9))); // DISCUSSION_COUNT
        parsedRow.add(parseNumeric(row.get(10))); // AVG_NB_READ_TIME_MIN
        // REGISTRATION_IPV4 (not implemented)
        // REGISTRATION_LOCATION (not implemented)

        for (int i=13; i<=15; i++) {
            if (row.size() <= i) { // Missing data, csv row is cut short
                parsedRow.add(null);
            } else {
                parsedRow.add(parseNumeric(row.get(i))); // TOTAL_VOTES_GAVE_NB, TOTAL_VOTES_GAVE_DS, TOTAL_VOTES_GAVE_DC
            }
        }

        if (row.size() > 16) {
            parsedRow.add(parseBoolean(row.get(16))); // ISBOT
        } else {
            parsedRow.add(null); // Missing ISBOT data
        }

        return parsedRow;
    }

    private Double parseNumeric(String str) {
        if (str.isEmpty()) { // empty data
            return null;
        } else {
            return Double.parseDouble(str);
        }
    }

    private Double parseBoolean(String str) {
        if (str.isEmpty()) { // empty data
            return null;
        } else {
            return (str.equals("True")) ? 1.0 : 0.0;
        }
    }

    private Double parseGender(String str) {
        if (str.isEmpty()) { // empty data
            return null;
        } else {
            return (str.equals("Male")) ? 1.0 : 0.0;
        }
    }
}
