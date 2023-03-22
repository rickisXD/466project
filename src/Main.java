import KNNLib.KNNModel;
import KNNLib.Matrix;
import KNNLib.StandardScaler;

import java.util.ArrayList;
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
        ArrayList<Matrix> splits = data.splitMatrix(0.8);
        Matrix training = splits.get(0);
        Matrix testing = splits.get(1);
        KNNModel model = new KNNModel(5, training, new StandardScaler(), new ArrayList<>(List.of(2,3,4,5,6)));
        System.out.println(model.predictAndFindAccuracy(testing));
    }
}