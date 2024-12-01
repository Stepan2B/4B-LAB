public class Perceptron {
    private double[] weights;
    private double learningRate; 

  
    public Perceptron(int inputSize, double learningRate) {
        this.weights = new double[inputSize + 1];  
        this.learningRate = learningRate;
        for (int i = 0; i < weights.length; i++) {
            weights[i] = Math.random() - 0.5;  
        }
    }

 
    public int activation(double sum) {
        return sum >= 0 ? 1 : 0;
    }


    public int predict(int[] inputs) {
        double sum = weights[0]; 
        for (int i = 0; i < inputs.length; i++) {
            sum += inputs[i] * weights[i + 1];  
        }
        return activation(sum);
    }

    
    public void train(int[][] trainingInputs, int[] labels, int epochs) {
        for (int epoch = 0; epoch < epochs; epoch++) {
            for (int i = 0; i < trainingInputs.length; i++) {
                int prediction = predict(trainingInputs[i]);
                int error = labels[i] - prediction;

                weights[0] += learningRate * error; 
                for (int j = 0; j < trainingInputs[i].length; j++) {
                    weights[j + 1] += learningRate * error * trainingInputs[i][j];
                }
            }
        }
    }


    public void printWeights() {
        System.out.println("Weights: " + Arrays.toString(weights));
    }

    public static void main(String[] args) {
        // Логическая функция "И" (AND)
        int[][] inputsAnd = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
        int[] labelsAnd = {0, 0, 0, 1};  
        
        // Логическая функция "ИЛИ" (OR)
        int[][] inputsOr = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
        int[] labelsOr = {0, 1, 1, 1}; 
        
        // Логическая функция "НЕ" (NOT)
        int[][] inputsNot = {{0}, {1}};
        int[] labelsNot = {1, 0};  

        // Перцептрон для "И" (2 входа)
        System.out.println("Обучение перцептрона для логической функции И (AND):");
        Perceptron perceptronAnd = new Perceptron(2, 0.1);
        perceptronAnd.train(inputsAnd, labelsAnd, 100);
        for (int[] input : inputsAnd) {
            System.out.println("Вход: " + Arrays.toString(input) + " -> Выход: " + perceptronAnd.predict(input));
        }

        // Перцептрон для "ИЛИ" (2 входа)
        System.out.println("\nОбучение перцептрона для логической функции ИЛИ (OR):");
        Perceptron perceptronOr = new Perceptron(2, 0.1);
        perceptronOr.train(inputsOr, labelsOr, 100);
        for (int[] input : inputsOr) {
            System.out.println("Вход: " + Arrays.toString(input) + " -> Выход: " + perceptronOr.predict(input));
        }

        // Перцептрон для "НЕ" (1 вход)
        System.out.println("\nОбучение перцептрона для логической функции НЕ (NOT):");
        Perceptron perceptronNot = new Perceptron(1, 0.1);
        perceptronNot.train(inputsNot, labelsNot, 100);
        for (int[] input : inputsNot) {
            System.out.println("Вход: " + Arrays.toString(input) + " -> Выход: " + perceptronNot.predict(input));
        }
    }
}
