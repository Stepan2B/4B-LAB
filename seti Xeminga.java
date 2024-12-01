import java.util.Arrays;

public class HopfieldWithRecognition {
    private int size;
    private double[][] weights;

    public HopfieldWithRecognition(int size) {
        this.size = size;
        this.weights = new double[size][size];
    }

    public void train(int[][] patterns) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                weights[i][j] = 0;
            }
        }

        for (int[] pattern : patterns) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (i != j) {
                        weights[i][j] += pattern[i] * pattern[j];
                    }
                }
            }
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                weights[i][j] /= size;
            }
        }
    }

    public int[] recall(int[] pattern, int iterations) {
        int[] currentPattern = Arrays.copyOf(pattern, pattern.length);

        for (int iter = 0; iter < iterations; iter++) {
            for (int i = 0; i < size; i++) {
                double sum = 0;
                for (int j = 0; j < size; j++) {
                    sum += weights[i][j] * currentPattern[j];
                }
                currentPattern[i] = sum >= 0 ? 1 : -1;
            }
        }

        return currentPattern;
    }

    public void printPattern(int[] pattern) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                System.out.print(pattern[i * 10 + j] == 1 ? "X" : ".");
            }
            System.out.println();
        }
    }

    public int recognize(int[][] patterns, int[] restoredPattern) {
        int closestPattern = -1;
        double maxSimilarity = -Double.MAX_VALUE;

        for (int i = 0; i < patterns.length; i++) {
            double similarity = 0;
            for (int j = 0; j < restoredPattern.length; j++) {
                similarity += patterns[i][j] * restoredPattern[j];
            }

            if (similarity > maxSimilarity) {
                maxSimilarity = similarity;
                closestPattern = i;
            }
        }

        return closestPattern;
    }

    public static void main(String[] args) {
        int size = 100;

        HopfieldWithRecognition hopfield = new HopfieldWithRecognition(size);

        int[][] patterns = {
            {
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                1, -1, -1, -1, -1, -1, -1, -1, 1, 1,
                1, -1, -1, -1, -1, -1, -1, -1, 1, 1,
                1, -1, -1, -1, -1, -1, -1, -1, 1, 1,
                1, -1, -1, -1, -1, -1, -1, -1, 1, 1,
                1, -1, -1, -1, -1, -1, -1, -1, 1, 1,
                1, -1, -1, -1, -1, -1, -1, -1, 1, 1,
                1, -1, -1, -1, -1, -1, -1, -1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1
            },
            {
                -1, -1, -1, 1, 1, -1, -1, -1, -1, -1,
                -1, -1, -1, 1, 1, -1, -1, -1, -1, -1,
                -1, -1, -1, 1, 1, -1, -1, -1, -1, -1,
                -1, -1, -1, 1, 1, -1, -1, -1, -1, -1,
                -1, -1, -1, 1, 1, -1, -1, -1, -1, -1,
                -1, -1, -1, 1, 1, -1, -1, -1, -1, -1,
                -1, -1, -1, 1, 1, -1, -1, -1, -1, -1,
                -1, -1, -1, 1, 1, -1, -1, -1, -1, -1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1
            },
            {
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                -1, -1, -1, -1, -1, -1, -1, -1, 1, 1,
                -1, -1, -1, -1, -1, -1, -1, -1, 1, 1,
                -1, -1, -1, -1, -1, -1, -1, -1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1
            }
        };

        hopfield.train(patterns);

        int[] damagedPattern = {
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, -1, -1, -1, -1, -1, -1, -1, 1, 1,
            1, -1, -1, -1, -1, -1, -1, -1, 1, 1,
            1, -1, -1, -1, 1, -1, -1, -1, 1, 1,
            1, -1, -1, -1, 1, -1, -1, -1, 1, 1,
            1, -1, -1, -1, 1, -1, -1, -1, 1, 1,
            1, -1, -1, -1, -1, -1, -1, -1, 1, 1,
            1, -1, -1, -1, -1, -1, -1, -1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1
        };

        System.out.println("Поврежденный образец цифры '0':");
        hopfield.printPattern(damagedPattern);

        int[] recoveredPattern = hopfield.recall(damagedPattern, 10);

        System.out.println("\nВосстановленный образец цифры '0':");
        hopfield.printPattern(recoveredPattern);

        int recognizedDigit = hopfield.recognize(patterns, recoveredPattern);
        System.out.println("\nРаспознанная цифра: " + recognizedDigit);
    }
}
