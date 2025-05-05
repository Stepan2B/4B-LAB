import numpy as np

class AntSystem:
    def __init__(self, distances, num_ants=20, alpha=1, beta=2, rho=0.5, Q=100):
        self.distances = distances
        self.num_ants = num_ants
        self.alpha = alpha  # важность феромона
        self.beta = beta    # важность видимости
        self.rho = rho      # испарение феромона
        self.Q = Q          # константа феромона
        self.pheromone = np.ones(distances.shape)
        self.num_cities = distances.shape[0]
        
    def visibility(self):
        return 1 / (self.distances + np.eye(self.num_cities) * np.inf)
    
    def probability(self, city, visited):
        denominator = 0
        probabilities = np.zeros(self.num_cities)
        
        for j in range(self.num_cities):
            if j not in visited:
                numerator = (self.pheromone[city][j] ** self.alpha) * (self.visibility()[city][j] ** self.beta)
                probabilities[j] = numerator
                denominator += numerator
                
        return probabilities / denominator if denominator != 0 else probabilities
    
    def update_pheromone(self, paths, distances):
        self.pheromone *= (1 - self.rho)
        for path, dist in zip(paths, distances):
            for i in range(self.num_cities):
                j = (i + 1) % self.num_cities
                self.pheromone[path[i]][path[j]] += self.Q / dist
                
    def solve(self, iterations=100):
        best_path = None
        best_distance = float('inf')
        
        for _ in range(iterations):
            paths = []
            distances = []
            
            for _ in range(self.num_ants):
                visited = [0]  # начинаем с первого города
                current_city = 0
                
                while len(visited) < self.num_cities:
                    prob = self.probability(current_city, visited)
                    next_city = np.random.choice(np.arange(self.num_cities), p=prob)
                    visited.append(next_city)
                    current_city = next_city
                
                # Возвращаемся в начальный город
                visited.append(0)
                paths.append(visited)
                
                # Рассчитываем длину пути
                distance = sum(self.distances[visited[i]][visited[i+1]] for i in range(self.num_cities))
                distances.append(distance)
                
                # Обновляем лучший путь
                if distance < best_distance:
                    best_distance = distance
                    best_path = visited
                    
            self.update_pheromone(paths, distances)
            
        return best_path, best_distance
