package KNNLib;

import java.util.*;

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
    //compare our predictions to the actual result and returns the accuracy of our model (precision/recall/f-score)
    public ArrayList<Double> predictAndEvaluate(Matrix newData, int classifier) {
        ArrayList<Integer> predictions = predict(newData);
        ArrayList<Double> accuracyMetrics = new ArrayList<>();
        int recallDenominator = newData.getRelevantDocumentCount(classifier);
        int precisionDenominator = 0;
        int accurate = 0;

        for (int i = 0; i < newData.getMatrix().size(); i++) {
            if ((newData.getMatrix().get(i).get(newData.getCategoryAttribute()).intValue() == predictions.get(i)) &&
                    (predictions.get(i) == classifier)){
                accurate++;
            }
        }

        //to calculate precision, we need TP + FP from prediction
        for(int prediction : predictions){
            if(prediction == classifier){
                precisionDenominator++;
            }
        }


        double precision = 0;
        double recall = 0;
        double f1Score = 0;
        if(accurate != 0){
            precision = (double) accurate / precisionDenominator;
            recall = (double) accurate / recallDenominator;
            f1Score = (2 * precision * recall) / (precision + recall);
        }

        accuracyMetrics.add(precision);
        accuracyMetrics.add(recall);
        accuracyMetrics.add(f1Score);

        return accuracyMetrics;
    }



    // takes in a set of data we want to predict with our model,
    // needs to have same dimensions as the matrix we fitted the model with
    // returns a list of predicted classifications
    public ArrayList<Integer> predict(Matrix newData) {
        ArrayList<Integer> predictions = new ArrayList<>();
        for (int i = 0; i < newData.getMatrix().size(); i++) {
            predictions.add(findCategory(i));
        }
        return predictions;
    }

    // takes in 2 rows, then computes the euclidean distance between them
    public double findDistance(int row1, int row2) {
        ArrayList<Double> data1 = matrix.getMatrix().get(row1);
        ArrayList<Double> data2 = matrix.getMatrix().get(row2);
        int dist = 0;
        for (int i : this.fitAttributes){
            double a1 = data1.get(i) == null ? 0 : data1.get(i);
            double a2 = data2.get(i) == null ? 0 : data2.get(i);
            dist += Math.pow(a1 - a2, 2);
        }
        return Math.sqrt(dist);
    }

    // based on row, finds the k nearest neighbors and
    // returns the most common category
    public int findCategory(int row) {
        HashMap<Integer, Double> dists = new HashMap<>();
        HashMap<Integer, Integer> categories = new HashMap<>();
        for (int i = 0; i < matrix.getMatrix().size(); i++) {
            if(i == row){
                continue;
            }
            dists.put(i,findDistance(row,i));
        }
        List<Map.Entry<Integer, Double>> sortList
                = new ArrayList<Map.Entry<Integer, Double>>(
                dists.entrySet());

        sortList.sort(new Comparator<Map.Entry<Integer, Double>>() {
            public int compare(
                    Map.Entry<Integer, Double> entry1,
                    Map.Entry<Integer, Double> entry2) {

                return Double.compare(entry1.getValue(), entry2.getValue());
            }
        });

        for (int i = 0; i < n; i++) {
            int key = matrix.getMatrix().get(sortList.get(i).getKey()).get(matrix.getCategoryAttribute()).intValue();
            if(!categories.containsKey(key)){
                categories.put(key, 1);
            } else {
                categories.replace(key,categories.get(key)+1);
            }

        }
        return Collections.max(categories.entrySet(), Map.Entry.comparingByValue()).getKey();
    }
}
