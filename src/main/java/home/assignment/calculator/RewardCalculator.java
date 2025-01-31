package home.assignment.calculator;

import home.assignment.data.GameConfiguration;
import home.assignment.data.SymbolConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RewardCalculator {

    private final GameConfiguration gameConfiguration;

    public RewardCalculator(GameConfiguration gameConfiguration) {
        this.gameConfiguration = gameConfiguration;
    }

    public double calculateReward(double bet, Map<String, List<String>> winningCombinations) {
        double reward = 0;

        for (Map.Entry<String, List<String>> entry : winningCombinations.entrySet()) {
            String symbol = entry.getKey();
            List<String> combinations = entry.getValue();

            double symbolReward = bet * gameConfiguration.symbols().get(symbol).rewardMultiplier();

            for (String comb : combinations) {
                symbolReward *= gameConfiguration.winCombinations().get(comb).rewardMultiplier();
            }

            reward += symbolReward;
        }
        return reward;
    }

    public List<String> getBonusSymbols(String[][] matrix) {
        List<String> bonusSymbols = new ArrayList<>();
        for (String[] strings : matrix) {
            for (String symbol : strings) {
                if (symbol != null && !symbol.isEmpty() && gameConfiguration.symbols().get(symbol).type().equals("bonus")
                        && !symbol.equals("MISS")) {
                        bonusSymbols.add(symbol);
                    }
            }
        }
        return bonusSymbols;
    }

    public double applyBonuses(List<String> bonusSymbols, double reward) {
        for (String symbol : bonusSymbols) {
            if (symbol == null) {
                continue;
            }
            SymbolConfiguration symbolConfiguration = gameConfiguration.symbols().get(symbol);

            switch (symbolConfiguration.impact()) {
                case "extra_bonus" -> reward += symbolConfiguration.extra();
                case "multiply_reward" -> reward *= symbolConfiguration.rewardMultiplier();
            }
        }
        return reward;
    }
}
