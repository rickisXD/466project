import KNNLib.KNNModel;
import KNNLib.Matrix;
import KNNLib.StandardScaler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Matrix data = new Matrix("./files/kaggle_bot_accounts.csv");
        data.fillNull(2, 0);
        data.fillNull(3, 0);
        data.fillNull(4, 0);
        data.fillNull(5, 0);
        data.fillNull(6, 0);
        data.dropNull(data.getCategoryAttribute());
        tuneHyper(data, 1, new ArrayList<>(List.of(2,3,4,5,6)), 10);
    }

    //create a function that does 10-fold cross-validation
    //pseudo-code: 1. shuffle all rows of matrix
    //             2. divide matrix into n different matrices
    //             2.5 create the test-train split on each
    //             3. do PredictAndEvaluate on each
    //             4. return weighted average precision/recall/f-score
    //             5.
    public static ArrayList<Double> crossValidate(int cv, int neighbors, Matrix matrix, int classifier, ArrayList<Integer> features){
        ArrayList<ArrayList<Double>> matrixCopy = (ArrayList<ArrayList<Double>>) matrix.getMatrix().clone();
        //need to shuffle around rows so that cv is truly random
        Collections.shuffle(matrixCopy);

        int cvRowSize = (int) Math.floor((double) matrixCopy.size() / cv);
        ArrayList<Matrix> cvMatrices = new ArrayList<>();
        int cvIndex = 0;

        //need to get all rows up to the last
        for(int i = 0; i < cv; i++){
            cvIndex = i * cvRowSize;
            ArrayList<ArrayList<Double>> cvRows = new ArrayList<>(matrixCopy.subList(cvIndex, cvIndex + cvRowSize));
            cvMatrices.add(new Matrix(cvRows));
        }

        //now we do a prediction of each
        // first fitting on cv - 1 chunks
        // then predicting and validating on test data (1 chunk)

        double precisionSum = 0;
        double recallSum = 0;
        double fSum = 0;

        for (int i = 0; i < cv; i++){
            //I am treating cvMatrices like a queue, so that we can do cross-val accurately
            Matrix test = cvMatrices.remove(0);
            Matrix train = new Matrix();
            for(int j = 0; j < cvMatrices.size(); j ++){
                train = Matrix.combine(train, cvMatrices.get(j));
            }
            //fit to model
            KNNModel model = new KNNModel(neighbors, train, new StandardScaler(), features);
            //predict
            ArrayList<Double> eval = model.predictAndEvaluate(test, classifier);
            //add to all accuracies
            precisionSum += eval.get(0);
            recallSum += eval.get(1);
            fSum += eval.get(2);
            cvMatrices.add(test);
        }

        //now precision/recall/f-score to get avg. precision/recall/f-score
        ArrayList<Double> finalAccur = new ArrayList<>();
        finalAccur.add(precisionSum / (float)cv);
        finalAccur.add(recallSum / (float) cv);
        finalAccur.add(fSum / (float) cv);

        return finalAccur;
    }

    //hyper-parameter tuning, will return the n value with the highest f-score
    //will loop through n-values from  1 to range, getting the avg cross validated f-score each time
    //returns the n-value with the highest f-score
    //to test, just follow my initial call in main. It is checking between 1-10. but remember this is
    //very slow
    public static void tuneHyper(Matrix data, int classifier, ArrayList<Integer> features, int range){
        ArrayList<ArrayList<Double>> parameterScores = new ArrayList<>();
        for(int i = 1; i < range; i++){
            ArrayList<Double> parameterScore = new ArrayList<>();
            ArrayList<Double> scores = crossValidate(10, i, data, classifier, features);
            parameterScore.add((double) i);
            parameterScore.add(scores.get(2));
            parameterScores.add(parameterScore);
        }

        parameterScores.sort(((o1, o2) -> {return (int) (o1.get(1) - o2.get(1));}));
        ArrayList<Double> bestF1 = parameterScores.get(parameterScores.size() - 1);
        System.out.println("best n_neighbors parameter: " + bestF1.get(0) + "\nbest f-1 Score: "  + bestF1.get(1));
    }
}