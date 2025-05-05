import java.util.Arrays;
import java.util.Random;

public class SimulatedAnnealing {
    private static final int N = 100; // размер доски
    private static final int ITERATIONS = 1000; // количество итераций
    private static final double COOLING_RATE = 0.99; // коэффициент охлаждения
    
    public static void main(String[] args) {
        Random rand = new Random();
        int[] permutation = new int[N];
        
        // Инициализация начальной перестановки
        for (int i = 0; i < N; i++) {
            permutation[i] = i;
        }
        shuffleArray(permutation);
        
        int currentScore = evaluate(permutation);
        int bestScore = currentScore;
        double temperature = 1.0;
        
        for (int i = 0; i < ITERATIONS; i++) {
            temperature *= COOLING_RATE;
            
            // Создание соседа путем перестановки двух элементов
            int[] neighbor = Arrays.copyOf(permutation, N);
            swap(neighbor, rand.nextInt(N), rand.nextInt(N));
            
            int neighborScore = evaluate(neighbor);
            
            // Принятие решения о переходе
            if (neighborScore > currentScore || 
                Math.random() < Math.exp((neighborScore - currentScore) / temperature)) {
                permutation = neighbor;
                currentScore = neighborScore;
                
                if (currentScore > bestScore) {
                    bestScore = currentScore;
                }
            }
        }
        
        System.out.println("Решение:");
        for (int i = 0; i < N; i++) {
            System.out.print((permutation[i] + 1) + " ");
        }
    }
    
    // Подсчет количества ферзей, которые не бьют друг друга
    private static int evaluate(int[] p) {
        int score = 0;
        for (int i = 0; i < N; i++) {
            int diag1 = 1;
            int diag2 = 1;
            for (int j = 0; j < i; j++) {
                if (Math.abs(i - j) == Math.abs(p[i] - p[j])) {
                    diag1 = 0;
                }
                if (Math.abs(i - j) == Math.abs((N - 1 - p[i]) - (N - 1 - p[j]))) {
                    diag2 = 0;
                }
            }
            score += diag1 & diag2;
        }
        return score;
    }
    
    // Перемешивание массива
    private static void shuffleArray(int[] array) {
        Random rnd = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            swap(array, i, index);
        }
    }
    
    // Обмен элементов в массиве
    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
