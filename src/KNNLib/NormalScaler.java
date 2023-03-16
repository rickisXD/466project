package KNNLib;

import java.util.ArrayList;

public class NormalScaler implements Scaler {

    ArrayList<ArrayList<Integer>> scaledMatrix;
    ArrayList<Double> means;
    ArrayList<Double> stdDevs;

    public NormalScaler() {
        this.scaledMatrix = new ArrayList<>();
        this.means = new ArrayList<>();
        this.stdDevs = new ArrayList<>();
    }

    // returns the matrix with each element scaled normally (also stores the new matrix in scaledMatrix)
    public Matrix fitTransform(Matrix data, ArrayList<Integer> fitAttributes) {
        return null;
    }
}
