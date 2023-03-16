package KNNLib;

import java.util.ArrayList;

public class KNNModel {

    int n;
    Matrix matrix;
    Scaler scaler;
    Matrix scaledData;
    ArrayList<Integer> fitAttributes;

    public KNNModel(int numNeighbors, Matrix matrix, Scaler scaler, ArrayList<Integer> fitAttributes) {
        this.n = numNeighbors;
        this.matrix = matrix;
        this.scaler = scaler;
        this.fitAttributes = fitAttributes;
        this.scaledData = scaler.fitTransform(matrix, fitAttributes);
    }

    // takes in a set of data we predict with in our data, needs to have same
    // dimensions as the matrix we fitted the model with (assume all columns are numeric)
    public double predictAndFindAccuracy(Matrix newData) {
        return 0.0;
    }

    // takes in a set of data we want to predict with our model,
    // needs to have same dimensions as the matrix we fitted the model with
    // returns a list of predicted classifications
    public ArrayList<String> predict(Matrix newData) {
        return null;
    }

    // takes in 2 rows, then computes the euclidean distance between them
    public double findDistance(int row1, int row2) {
        return 0.0;
    }

    // based on row, finds the k nearest neighbors and
    // returns the most common category
    public int findCategory(int row) {
        return 0;
    }
}
