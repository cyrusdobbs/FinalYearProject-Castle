package controller.players.ismcts;

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
        // Test
        INDArray nd = Nd4j.create(new float[]{1,2,3,4},new int[]{2,2});
        System.out.print(nd.toString());
    }

    public Evaluator(String modelFile) {
        try {
            String simpleMlp = new ClassPathResource(modelFile).getFile().getPath();
            model = KerasModelImport.importKerasSequentialModelAndWeights(simpleMlp, false);
        } catch (UnsupportedKerasConfigurationException | IOException | InvalidKerasConfigurationException e) {
            System.out.println("Couldn't import model.");
            System.exit(0);
        }
    }

    public boolean evaluate(INDArray input) {
        input = Nd4j.expandDims(input, 0);
        input = Nd4j.expandDims(input, 0);
        INDArray output = model.output(input);
        return output.getDouble(0) < 0.5;
    }
}
