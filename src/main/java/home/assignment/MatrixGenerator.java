package home.assignment;

import home.assignment.data.GameConfiguration;
import home.assignment.data.SymbolProbability;

import java.util.Map;
import java.util.Random;

public class MatrixGenerator {

    private final GameConfiguration gameConfiguration;
    private final Random random = new Random();

    public MatrixGenerator(GameConfiguration gameConfiguration) {
        this.gameConfiguration = gameConfiguration;
    }

    public String[][] generateMatrix() {
        int rows = gameConfiguration.rows();
        int columns = gameConfiguration.columns();
        String[][] matrix = new String[rows][columns];

        int standardProbSum = getAllStandardSymbolsProbabilitiesSum();
        int bonusProbSum = getSumForProbability(gameConfiguration.probabilities().bonusSymbols());

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = generateCell(standardProbSum, bonusProbSum, i, j);
            }
        }
        return matrix;
    }

    private String generateCell(int standardProbSum, int bonusProbSum, int i, int j) {
        int randomValue = random.nextInt(standardProbSum + bonusProbSum) + 1;

        if (randomValue <= standardProbSum) {
            return getStandardSymbol(i, j, randomValue);
        } else {
            return getBonusSymbol(standardProbSum, randomValue);
        }
    }

    private String getBonusSymbol(int standardProbSum, int randomValue) {
        int probSum = standardProbSum;
        for (Map.Entry<String, Integer> entry : gameConfiguration.probabilities().bonusSymbols().symbols().entrySet()) {
            probSum += entry.getValue();

            if (randomValue <= probSum) {
                return entry.getKey();
            }
        }
        return null;
    }

    private String getStandardSymbol(int row, int column, int randomValue) {
        int probSum = 0;
        var prob = getProbability(row, column);
        int sumOfProbForCell = getSumForProbability(prob);
        int remainder = randomValue % sumOfProbForCell;

        for (Map.Entry<String, Integer> entry : prob.symbols().entrySet()) {
            probSum += entry.getValue();

            if (remainder <= probSum) {
                return entry.getKey();
            }
        }
        return null;
    }

    private int getSumForProbability(SymbolProbability symbolProbability) {
        return symbolProbability.symbols().values()
                .stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    private SymbolProbability getProbability(final int row, final int column) {
        return gameConfiguration.probabilities().standardSymbols()
                .stream()
                .filter(prob -> prob.row() == row && prob.column() == column)
                .findFirst()
                .orElseGet(() -> gameConfiguration.probabilities().standardSymbols().get(0));
    }

    private int getAllStandardSymbolsProbabilitiesSum() {
        return gameConfiguration.probabilities().standardSymbols()
                .stream()
                .flatMapToInt(prob -> prob.symbols().values().stream().mapToInt(Integer::intValue))
                .sum();
    }

}
