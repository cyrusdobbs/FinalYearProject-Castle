package util;

import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.io.ClassPathResource;

import java.io.IOException;


public class Evaluator {

    private MultiLayerNetwork model;

    public static void main(String[] args) {
        INDArray nd = Nd4j.create(new float[]{1,2,3,4},new int[]{2,2});

        System.out.print(nd.toString());
    }

    public Evaluator(String modelFile) throws UnsupportedKerasConfigurationException, IOException, InvalidKerasConfigurationException {
//        try {
            String simpleMlp = new ClassPathResource(modelFile).getFile().getPath();
            model = KerasModelImport.importKerasSequentialModelAndWeights(simpleMlp);
//        } catch (UnsupportedKerasConfigurationException | IOException | InvalidKerasConfigurationException e) {
//            System.out.println("Couldn't import model.");
//            System.exit(0);
//        }
    }

    public int evaluate(INDArray input) {
//        String[][] gameStateArray = gameStateToArray(gameState);

//        String[][] gameStateArray = {
//            {"2","2","2","2","9","9","14"},
//            {"14","2","3","4","5","6","7","8","9","10","11","12","13"},
//            {"2","2","2","2","9","9","14"},
//            {"4"},
//            {"2"}
//        };

        input = Nd4j.expandDims(input, 0);
        input = Nd4j.expandDims(input, 0);
        return model.output(input).getInt(0);
    }

//    private INDArray getNDArray(String[][] gameStateArray, SimpleGameState gameState) {
//        INDArray input = Nd4j.zeros(new int[]{4, 41}, DataType.UINT32);
//
//        try {
//            for (int i = 0; i < 3; i++) {
//                int copies = 0;
//                String previousCard = null;
//                for (String card : gameStateArray[i]) {
//                    if (!card.isEmpty()) {
//                        if (card.equals(previousCard)) {
//                            copies++;
//                        } else {
//                            copies = 0;
//                        }
//                        int row = copies;
//                        int col = Integer.parseInt(card) - 2 + i * 13;
//                        input.putScalar(row, col, 1);
//                        previousCard = card;
//                    }
//                }
//            }
//        } catch (IllegalArgumentException e) {
//            System.out.println("EVALUATE ERROR.");
//            System.exit(0);
//        }
//
//        for (int i = 3; i < 5; i++) {
//            for (int k = 0; k < Integer.parseInt(gameStateArray[i][0]); k++) {
//                input.putScalar(k, 36 + i, 1);
//            }
//        }
//        return input;
//    }
//
//    private String[][] gameStateToArray(SimpleGameState gameState) {
//        String[] hand = cardsToString(gameState.getHand()).split("\\" + CastleConstants.LIST_DELIMITER);
//        String[] castleFU = cardsToString(gameState.getCastleFU()).split("\\" + CastleConstants.LIST_DELIMITER);
//        String[] opCastleFU = cardsToString(gameState.getOpCastleFU()).split("\\" + CastleConstants.LIST_DELIMITER);
//        String[] castleFDSize = String.valueOf(gameState.getCastleFDSize()).split("\\" + CastleConstants.LIST_DELIMITER);
//        String[] opCastleFDSize = String.valueOf(gameState.getOpCastleFDSize()).split("\\" + CastleConstants.LIST_DELIMITER);
//        return new String[][]{hand, castleFU, opCastleFU, castleFDSize, opCastleFDSize};
//    }
//
//    private String cardsToString(CardCollection cards) {
//        if (cards.isEmpty()) {
//            return CastleConstants.NO_CARDS;
//        }
//
//        StringBuilder stringBuilder = new StringBuilder();
//        for (Card card : cards.getCardCollection()) {
//            stringBuilder.append(card.getRank().getValueCode()).append(CastleConstants.LIST_DELIMITER);
//        }
//        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
//        return stringBuilder.toString();
//    }
}
