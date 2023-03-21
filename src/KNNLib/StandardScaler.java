package KNNLib;

import java.util.ArrayList;

public class StandardScaler implements Scaler {

    ArrayList<ArrayList<Integer>> scaledMatrix;
    ArrayList<Double> means;
    ArrayList<Double> stdDevs;

    public StandardScaler() {
        this.scaledMatrix = new ArrayList<>();
        this.means = new ArrayList<>(); // the mean for every scaled column
        this.stdDevs = new ArrayList<>(); // the standard deviation for every scaled column
    }

    // returns the matrix with each element scaled normally (also stores the new matrix in scaledMatrix)
    public Matrix fitTransform(Matrix data, ArrayList<Integer> fitAttributes) {
        for (int i = 0; i < data.getMatrix().size(); i++) {
            if (!fitAttributes.contains(i)) {
                scaledMatrix.add(data.getMatrix().get(i));
            } else {
                ArrayList<Integer> row = data.getMatrix().get(i);
                double mean = 0;
                double stdDev = 0;
                for (int j = 0; j < row.size(); j++) {
                    mean += row.get(j);
                }
                mean /= row.size();
                for (int j = 0; j < row.size(); j++) {
                    stdDev += Math.pow(row.get(j) - mean, 2);
                }
                stdDev = Math.sqrt(stdDev / row.size());
                means.add(mean);
                stdDevs.add(stdDev);
                ArrayList<Integer> scaledRow = new ArrayList<>();
                for (int j = 0; j < row.size(); j++) {
                    scaledRow.add((int) ((row.get(j) - mean) / stdDev));
                }
                scaledMatrix.add(scaledRow);
            }
        }
        return new Matrix(scaledMatrix);
    }

    public ArrayList<ArrayList<Integer>> getScaledMatrix() {
        return scaledMatrix;
    }
}
