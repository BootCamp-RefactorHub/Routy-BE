package com.c4.routy.domain.direction.optimization.strategy;

import com.c4.routy.domain.direction.dto.KakaoMobility.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 유전 알고리즘 (Genetic Algorithm)
 * 무작위 초기화 + 선택 + 교차 + 돌연변이
 */
public class GeneticStrategy extends RouteStrategy {
    public GeneticStrategy(List<Location> locations, List<Integer> fixed, int[][] weight) {
        super(locations, fixed, weight);
    }

    @Override
    protected List<Integer> findOptimalOrder() {
        int n = weights.length;
        int populationSize = 50;
        int generations = 200;

        List<List<Integer>> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            List<Integer> indiv = new ArrayList<>();
            for (int j = 0; j < n; j++) indiv.add(j);
            Collections.shuffle(indiv);
            population.add(indiv);
        }

        for (int g = 0; g < generations; g++) {
            population.sort((a, b) -> totalWeight(a) - totalWeight(b));
            List<List<Integer>> nextGen = new ArrayList<>(population.subList(0, populationSize/2));

            while (nextGen.size() < populationSize) {
                List<Integer> parent1 = nextGen.get((int)(Math.random() * (populationSize/2)));
                List<Integer> parent2 = nextGen.get((int)(Math.random() * (populationSize/2)));
                List<Integer> child = crossover(parent1, parent2);
                mutate(child);
                nextGen.add(child);
            }
            population = nextGen;
        }

        population.sort((a, b) -> totalWeight(a) - totalWeight(b));
        return population.get(0);
    }

    private int totalWeight(List<Integer> order) {
        int total = 0;
        for (int i = 0; i < order.size()-1; i++)
            total += weights[order.get(i)][order.get(i+1)];
        return total;
    }

    private List<Integer> crossover(List<Integer> p1, List<Integer> p2) {
        int n = p1.size();
        int start = (int)(Math.random() * n);
        int end = (int)(Math.random() * n);
        if (start > end) { int tmp = start; start = end; end = tmp; }

        List<Integer> child = new ArrayList<>(Collections.nCopies(n, -1));

        // fixed는 그대로 유지
        for (int f : fixed) child.set(f, f);

        for (int i = start; i <= end; i++) {
            if (!fixed.contains(i)) child.set(i, p1.get(i));
        }

        int idx = 0;
        for (int i = 0; i < n; i++) {
            if (!child.contains(p2.get(i))) {
                while (idx < n && (child.get(idx) != -1 || fixed.contains(idx))) idx++;
                if (idx < n) child.set(idx, p2.get(i));
            }
        }
        return child;
    }

    private void mutate(List<Integer> indiv) {
        if (Math.random() < 0.1) {
            int i = (int)(Math.random() * indiv.size());
            int j = (int)(Math.random() * indiv.size());
            if (!fixed.contains(i) && !fixed.contains(j)) Collections.swap(indiv, i, j);
        }
    }
}
