package home.assignment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import home.assignment.calculator.RewardCalculator;
import home.assignment.calculator.WinningCombinationCalculator;
import home.assignment.data.GameConfiguration;
import home.assignment.data.Result;
import home.assignment.exception.ConfigurationFileException;
import home.assignment.exception.InvalidCommandArgumentsException;
import home.assignment.exception.ResultProcessException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScratchGame {

    private static final ObjectMapper objectMapper = getObjectMapper();

    public static void main(String[] args) {
        int configFileIndex = Integer.MIN_VALUE;
        int betAmountIndex = Integer.MIN_VALUE;

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if ("--config".equals(arg)) {
                configFileIndex = i + 1;
            } else if ("--betting-amount".equals(arg)) {
                betAmountIndex = i + 1;
            }
        }
        if (configFileIndex < 1 || configFileIndex >= args.length || betAmountIndex < 1 || betAmountIndex >= args.length) {
            throw new InvalidCommandArgumentsException("Command should be: java -jar <game-jar-file> --config <game-config-file> --betting-amount <amount>");
        }

        String configFile = args[configFileIndex];
        double bet = Double.parseDouble(args[betAmountIndex]);

        Result result = runGame(configFile, bet);
        try {
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result));
        } catch (JsonProcessingException e) {
            throw new ResultProcessException("Error trying to print results of the game", e);
        }
    }

    public static Result runGame(String configFile, double bet) {
        GameConfiguration gameConfiguration = null;
        try {
             gameConfiguration = objectMapper.readValue(new File(configFile), GameConfiguration.class);
        } catch (IOException e) {
            throw new ConfigurationFileException("Error trying to read a configuration file on path: " + configFile, e);
        }

        var matrixGenerator = new MatrixGenerator(gameConfiguration);
        String[][] matrix = matrixGenerator.generateMatrix();

        Map<String, List<String>> winningCombinations = WinningCombinationCalculator.calculateWinningCombinations(gameConfiguration, matrix);
        var rewardCalculator = new RewardCalculator(gameConfiguration);

        double reward = rewardCalculator.calculateReward(bet, winningCombinations);

        List<String> bonusSymbols = new ArrayList<>();
        if (!winningCombinations.isEmpty()) {
            bonusSymbols = rewardCalculator.getBonusSymbols(matrix);
            reward = rewardCalculator.applyBonuses(bonusSymbols, reward);
        }

        String bonusSymbolsJoined = bonusSymbols.isEmpty() ? null : String.join(" ", bonusSymbols);

        return new Result(matrix, reward, winningCombinations, bonusSymbolsJoined);
    }

    public static ObjectMapper getObjectMapper() {
        var objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        return objectMapper;
    }
}