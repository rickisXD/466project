package KNNLib;

import java.util.ArrayList;

public interface Scaler {
    public Matrix fitTransform(Matrix data, ArrayList<Integer> fitAttributes);
}
