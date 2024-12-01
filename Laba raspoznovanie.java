import java.util.Arrays;
import java.util.Random;

public class Perceptron2 {
    private double[][] weights;
    private double[] bias;     
    private double learningRate;

  
    public Perceptron2(int inputSize, int outputSize, double learningRate) {
        this.weights = new double[outputSize][inputSize];
        this.bias = new double[outputSize];
        this.learningRate = learningRate;


        Random rand = new Random();
        for (int i = 0; i < outputSize; i++) {
            for (int j = 0; j < inputSize; j++) {
                weights[i][j] = rand.nextDouble() - 0.5;
            }
            bias[i] = rand.nextDouble() - 0.5;
        }
    }

  
    public int activation(double sum) {
        return sum >= 0 ? 1 : 0;
    }

  
    public int[] predict(int[] inputs) {
        int[] output = new int[bias.length];
        for (int i = 0; i < bias.length; i++) {
            double sum = bias[i];
            for (int j = 0; j < inputs.length; j++) {
                sum += weights[i][j] * inputs[j];
            }
            output[i] = activation(sum); 
        }
        return output;
    }


    public void train(int[][] trainingInputs, int[][] labels, int epochs) {
        for (int epoch = 0; epoch < epochs; epoch++) {
            for (int i = 0; i < trainingInputs.length; i++) {
                int[] prediction = predict(trainingInputs[i]);
                for (int j = 0; j < labels[i].length; j++) {
                    int error = labels[i][j] - prediction[j];
                    // Коррекция весов
                    for (int k = 0; k < trainingInputs[i].length; k++) {
                        weights[j][k] += learningRate * error * trainingInputs[i][k];
                    }
                    bias[j] += learningRate * error;
                }
            }
        }
    }

    public static void main(String[] args) {
  
        int inputSize = 100;
 
        int outputSize = 10;
        double learningRate = 0.1;

        Perceptron2 perceptron = new Perceptron2(inputSize, outputSize, learningRate);


        int[][] trainingInputs = generateFakeData(inputSize, outputSize);
        int[][] labels = generateLabels(outputSize);


        perceptron.train(trainingInputs, labels, 1000);


        for (int i = 0; i < trainingInputs.length; i++) {
            int[] prediction = perceptron.predict(trainingInputs[i]);
            System.out.println("Цифра: " + i + ", Предсказание: " + Arrays.toString(prediction));
        }
    }


    public static int[][] generateFakeData(int inputSize, int outputSize) {
        Random rand = new Random();
        int[][] data = new int[outputSize][inputSize];
        for (int i = 0; i < outputSize; i++) {
            for (int j = 0; j < inputSize; j++) {
                data[i][j] = rand.nextInt(2); 
            }
        }
        return data;
    }


    public static int[][] generateLabels(int outputSize) {
        int[][] labels = new int[outputSize][outputSize];
        for (int i = 0; i < outputSize; i++) {
            labels[i][i] = 1; 
        }
        return labels;
    }
}
