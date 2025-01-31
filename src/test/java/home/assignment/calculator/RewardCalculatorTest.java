package home.assignment.calculator;

import com.fasterxml.jackson.databind.ObjectMapper;
import home.assignment.ScratchGame;
import home.assignment.data.GameConfiguration;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RewardCalculatorTest {

    private static final ObjectMapper OBJECT_MAPPER = ScratchGame.getObjectMapper();

    @Test
    void testCalculateReward_NoWinningCombinations() throws IOException {
        GameConfiguration config = OBJECT_MAPPER.readValue(new File("config.json"), GameConfiguration.class);
        RewardCalculator rewardCalculator = new RewardCalculator(config);

        double bet = 100.0;

        double reward = rewardCalculator.calculateReward(bet, Map.of());

        assertEquals(0.0, reward);
    }

    @Test
    void testCalculateReward_WithWinningCombinations() throws IOException {
        GameConfiguration config = OBJECT_MAPPER.readValue(new File("config.json"), GameConfiguration.class);
        RewardCalculator rewardCalculator = new RewardCalculator(config);

        double bet = 100.0;

        Map<String, List<String>> winningCombinations = Map.of(
                "A", List.of("same_symbol_5_times"),
                "B", List.of( "same_symbol_3_times", "same_symbols_horizontally")
        );

        double reward = rewardCalculator.calculateReward(bet, winningCombinations);

        assertEquals(1600.0, reward);
    }
}
