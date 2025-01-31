package home.assignment.calculator;

import home.assignment.data.GameConfiguration;
import home.assignment.data.WinCombination;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WinningCombinationCalculator {

    private static final int ROW_INDEX = 0;
    private static final int COLUMN_INDEX = 2;

    public static Map<String, List<String>> calculateWinningCombinations(GameConfiguration gameConfiguration,
                                                                         String[][] matrix) {
        Map<String, Integer> symbolOccurrences = calculateSymbolOccurrences(matrix);
        Map<String, List<String>> winningCombinations = new HashMap<>();
        // Separate "same_symbols" from other conditions for different logic and because "same_symbols" need to be sorted
        // to have only highest possible count condition instead of all of applicable conditions (f.e. 6 instead 3-6)
        Map<String, WinCombination> sameSymbolWinCombinations = gameConfiguration.winCombinations().entrySet()
                .stream()
                .filter(entry -> entry.getValue().when().equals("same_symbols"))
                .sorted(Comparator.<Map.Entry<String, WinCombination>>comparingInt(entry -> entry.getValue().count()).reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
        Map<String, WinCombination> otherWinCombinations = gameConfiguration.winCombinations().entrySet()
                .stream()
                .filter(entry -> !entry.getValue().when().equals("same_symbols"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        findSameSymbolsWinningConditions(symbolOccurrences, winningCombinations, sameSymbolWinCombinations);
        findCoveredAreasWinningConditions(gameConfiguration, matrix, winningCombinations, otherWinCombinations);

        return winningCombinations;
    }

    private static void findCoveredAreasWinningConditions(GameConfiguration gameConfiguration, String[][] matrix, Map<String, List<String>> winningCombinations, Map<String, WinCombination> otherWinCombinations) {
        for (Map.Entry<String, WinCombination> entry : otherWinCombinations.entrySet()) {
            String name = entry.getKey();
            WinCombination winCombination = entry.getValue();

            AREA: for (List<String> coveredArea : winCombination.coveredAreas()) {
                String symbol = matrix[coveredArea.get(0).charAt(ROW_INDEX) - '0'][coveredArea.get(0).charAt(COLUMN_INDEX) - '0'];

                for (String pos : coveredArea) {
                    int row = pos.charAt(ROW_INDEX) - '0';
                    int column = pos.charAt(COLUMN_INDEX) - '0';

                    if (!matrix[row][column].equals(symbol)) {
                        continue AREA;
                    }
                }

                if (gameConfiguration.symbols().get(symbol).type().equals("standard")) {
                    winningCombinations.computeIfAbsent(symbol, k -> new ArrayList<>()).add(name);
                }
            }
        }
    }

    private static void findSameSymbolsWinningConditions(Map<String, Integer> symbolOccurrences, Map<String, List<String>> winningCombinations, Map<String, WinCombination> sameSymbolWinCombinations) {
        for (Map.Entry<String, WinCombination> entry : sameSymbolWinCombinations.entrySet()) {
            String name = entry.getKey();
            WinCombination winCombination = entry.getValue();

            symbolOccurrences.entrySet()
                        .stream()
                        .filter(occurrence -> occurrence.getValue() >= winCombination.count())
                        .forEach(occurrence -> {
                            if (!winningCombinations.containsKey(occurrence.getKey())) {
                                ArrayList<String> value = new ArrayList<>();
                                value.add(name);
                                winningCombinations.put(occurrence.getKey(), value);
                            }
                        });

        }
    }

    private static Map<String, Integer> calculateSymbolOccurrences(String[][] matrix) {
        Map<String, Integer> symbolOccurrences = new HashMap<>();

        for (String[] strings : matrix) {
            for (String symbol : strings) {
                symbolOccurrences.compute(symbol, (k, v) -> v == null ? 1 : v + 1);
            }
        }

        return symbolOccurrences;
    }

}
