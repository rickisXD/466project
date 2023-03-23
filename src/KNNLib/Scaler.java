package KNNLib;

import java.util.ArrayList;

public interface Scaler {
    public Matrix initTransform(Matrix data, ArrayList<Integer> fitAttributes);
    public Matrix fitTransform(Matrix data, ArrayList<Integer> fitAttributes);
}
