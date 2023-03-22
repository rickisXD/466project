import KNNLib.KNNModel;
import KNNLib.Matrix;
import KNNLib.StandardScaler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Matrix data = new Matrix("./files/kaggle_bot_accounts.csv");
//        data.fillNull(2, 0);
//        data.fillNull(3, 0);
//        data.fillNull(4, 0);
//        data.fillNull(5, 0);
//        data.fillNull(6, 0);
//        data.dropNull(data.getCategoryAttribute());
//        ArrayList<Matrix> splits = data.splitMatrix(0.8);
//        Matrix training = splits.get(0);
//        Matrix testing = splits.get(1);
//        KNNModel model = new KNNModel(5, training, new StandardScaler(), new ArrayList<>(List.of(2,3,4,5,6)));
//        System.out.println(model.predictAndFindAccuracy(testing));
//        Matrix data = new Matrix("./files/x.csv");
//        Matrix data = new Matrix("466project/src/kaggle_bot_accounts.csv");
        data.fillNull(2, 0);
        data.fillNull(3, 0);
        data.fillNull(4, 0);
        data.fillNull(5, 0);
        data.fillNull(6, 0);
        data.dropNull(data.getCategoryAttribute());

//        KNNModel model = new KNNModel(data, new StandardScaler(), [attr]);
//        System.out.println(model.predictAndFindAccuracy(Matrix testing));
//        System.out.println("Hello world!");
        ArrayList<Double> finalScore = crossValidate(10, data, 1, new ArrayList<>(List.of(2)));
        System.out.println("CVPrecision: " + finalScore.get(0));
        System.out.println("CVRecall: " + finalScore.get(1));
        System.out.println("CVF-Score: " + finalScore.get(2));

    }


    //create a function that does 10-fold cross-validation
    //pseudo-code: 1. shuffle all rows of matrix
    //             2. divide matrix into n different matrices
    //             2.5 create the test-train split on each
    //             3. do PredictAndEvaluate on each
    //             4. return weighted average precision/recall/f-score
    //             5.
    public static ArrayList<Double> crossValidate(int cv, Matrix matrix, int classifier, ArrayList<Integer> features){
        ArrayList<ArrayList<Integer>> matrixCopy = (ArrayList<ArrayList<Integer>>) matrix.getMatrix().clone();
        //need to shuffle around rows so that cv is truly random
        Collections.shuffle(matrixCopy);

        int cvRowSize = (int) Math.floor((double) matrixCopy.size() / cv);
        ArrayList<Matrix> cvMatrices = new ArrayList<>();
        int cvIndex = 0;

        //need to get all rows up to the last
        for(int i = 0; i < cv; i++){
            cvIndex = i * cvRowSize;
            ArrayList<ArrayList<Integer>> cvRows = new ArrayList<>(matrixCopy.subList(cvIndex, cvIndex + cvRowSize));
            cvMatrices.add(new Matrix(cvRows));
        }

        //now we need to get train-test splits for all cvs (80-20)
        ArrayList<ArrayList<Matrix>> trainTestCv = new ArrayList<>();

        for (Matrix cvMatrix : cvMatrices){
            ArrayList<Matrix> trainTest = cvMatrix.splitMatrix(0.8);
            trainTestCv.add(trainTest);
        }

        //now we do a prediction of each
        // first fitting on training data
        // then predicting and validating on test data


        double precisionSum = 0;
        double recallSum = 0;
        double fSum = 0;

        for (ArrayList<Matrix> testTrain : trainTestCv){
            Matrix train = testTrain.get(0);
            Matrix test = testTrain.get(1);
            //fit to model
            KNNModel model = new KNNModel(1000, train, new StandardScaler(), features);
            //predict
            ArrayList<Double> eval = model.predictAndEvaluate(test, classifier);
            //add to all accuracies
            precisionSum += eval.get(0);
            recallSum += eval.get(1);
            fSum += eval.get(2);
        }

        //now precision/recall/f-score to get avg. precision/recall/f-score
        ArrayList<Double> finalAccur = new ArrayList<>();
        finalAccur.add(precisionSum / (float)cv);
        finalAccur.add(recallSum / (float) cv);
        finalAccur.add(fSum / (float) cv);

        return finalAccur;

    }



}