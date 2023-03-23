package KNNLib;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class StandardScaler implements Scaler {

    ArrayList<ArrayList<Double>> scaledMatrix;
    ArrayList<Double> means;
    ArrayList<Double> stdDevs;

    public StandardScaler() {
        this.scaledMatrix = new ArrayList<>();
        this.means = new ArrayList<>(); // the mean for every scaled column
        this.stdDevs = new ArrayList<>(); // the standard deviation for every scaled column
    }

    // returns the matrix with each element scaled normally (also stores the new matrix in scaledMatrix)
    public Matrix initTransform(Matrix data, ArrayList<Integer> fitAttributes) {
        for (int i : fitAttributes) {
            double mean = 0;
            double stdDev = 0;
            double nullCount = 0;
            for (ArrayList<Double> row : data.getMatrix()) {
                if (row.get(i) == null) {
                    nullCount++;
                } else {
                    mean += row.get(i);
                }
            }
            mean /= (data.getMatrix().size() - nullCount);
            for (ArrayList<Double> row : data.getMatrix()) {
                if (row.get(i) != null) {
                    stdDev += Math.pow(row.get(i) - mean, 2);
                }
            }
            stdDev = Math.sqrt(stdDev / data.getMatrix().size());
            means.add(mean);
            stdDevs.add(stdDev);
        }
        for (ArrayList<Double> row : data.getMatrix()) {
            ArrayList<Double> scaledRow = new ArrayList<>();
            for (int i = 0; i < row.size(); i++) {
                if (fitAttributes.contains(i) && row.get(i) != null) {
                    scaledRow.add(((row.get(i) - means.get(fitAttributes.indexOf(i))) / stdDevs.get(fitAttributes.indexOf(i))));
                } else {
                    scaledRow.add(row.get(i));
                }
            }
            scaledMatrix.add(scaledRow);
        }
        return new Matrix(scaledMatrix);
    }

    public Matrix fitTransform(Matrix data, ArrayList<Integer> fitAttributes) {
        ArrayList<ArrayList<Double>> newScaledMatrix = new ArrayList<>();
        for (int row = 0; row < data.getMatrix().size(); row++) {
            ArrayList<Double> newScaledRow = new ArrayList<>();
            for (int col = 0; col < data.getMatrix().get(row).size(); col++) {
                if (fitAttributes.contains(col) && data.getMatrix().get(row).get(col) != null) {
                    newScaledRow.add((data.getMatrix().get(row).get(col) - means.get(fitAttributes.indexOf(col))) / stdDevs.get(fitAttributes.indexOf(col)));
                } else {
                    newScaledRow.add(data.getMatrix().get(row).get(col));
                }
            }
            newScaledMatrix.add(newScaledRow);
        }
        return new Matrix(newScaledMatrix);
    }

    public ArrayList<ArrayList<Double>> getScaledMatrix() {
        return scaledMatrix;
    }
}
