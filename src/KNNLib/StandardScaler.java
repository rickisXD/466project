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
        for (int i : fitAttributes) {
            double mean = 0;
            double stdDev = 0;
            for (ArrayList<Integer> row : data.getMatrix()) {
                mean += row.get(i);
            }
            mean /= data.getMatrix().size();
            for (ArrayList<Integer> row : data.getMatrix()) {
                stdDev += Math.pow(row.get(i) - mean, 2);
            }
            stdDev = Math.sqrt(stdDev / data.getMatrix().size());
            means.add(mean);
            stdDevs.add(stdDev);
        }
        for (ArrayList<Integer> row : data.getMatrix()) {
            ArrayList<Integer> scaledRow = new ArrayList<>();
            for (int i = 0; i < row.size(); i++) {
                if (fitAttributes.contains(i)) {
                    scaledRow.add((int) ((row.get(i) - means.get(fitAttributes.indexOf(i))) / stdDevs.get(fitAttributes.indexOf(i))));
                } else {
                    scaledRow.add(row.get(i));
                }
            }
            scaledMatrix.add(scaledRow);
        }
        return new Matrix(scaledMatrix);
    }

    public ArrayList<ArrayList<Integer>> getScaledMatrix() {
        return scaledMatrix;
    }
}
