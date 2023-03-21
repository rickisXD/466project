package KNNLib;

import java.util.ArrayList;

public class Matrix {
    // reads in file and makes 2d matrix out of it
    ArrayList<ArrayList<Integer>> matrix;
    public Matrix(String filePath) {

    }

    public Matrix(ArrayList<ArrayList<Integer>> matrix) {
        this.matrix = matrix;
    }

    public ArrayList<ArrayList<Integer>> getMatrix() {
        return matrix;
    }

    public void setMatrix(ArrayList<ArrayList<Integer>> matrix) {
        this.matrix = matrix;
    }

    public int getCategoryAttribute(){
        return matrix.get(0).size()-1;
    }

    // splits the matrix into a training and testing set
    // i.e. return [training, testing]
    public static ArrayList<Matrix> splitMatrix(double percent) {
        return null;
    }
}
