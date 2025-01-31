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

class WinningCombinationCalculatorTest {

    private static final ObjectMapper OBJECT_MAPPER = ScratchGame.getObjectMapper();

    @Test
    void testCalculateWinningCombinations_easyCase() throws IOException {
        GameConfiguration config = OBJECT_MAPPER.readValue(new File("config.json"), GameConfiguration.class);

        String[][] matrix = {
                {"A", "A", "A"},
                {"B", "B", "B"},
                {"C", "C", "C"}
        };

        Map<String, List<String>> winningCombinations = WinningCombinationCalculator.calculateWinningCombinations(config, matrix);

        Map<String, List<String>> result = Map.of(
                "A", List.of("same_symbol_3_times", "same_symbols_horizontally"),
                "B", List.of("same_symbol_3_times", "same_symbols_horizontally"),
                "C", List.of("same_symbol_3_times", "same_symbols_horizontally")
        );
        assertEquals(result, winningCombinations);
    }

    @Test
    void testCalculateWinningCombinations_noWin() throws IOException {
        GameConfiguration config = OBJECT_MAPPER.readValue(new File("config.json"), GameConfiguration.class);

        String[][] matrix = {
                {"A", "E", "F"},
                {"B", "B", "C"},
                {"E", "D", "C"}
        };

        Map<String, List<String>> winningCombinations = WinningCombinationCalculator.calculateWinningCombinations(config, matrix);

        Map<String, List<String>> result = Map.of();
        assertEquals(result, winningCombinations);
    }

    @Test
    void testCalculateWinningCombinations_bigSameSymbols() throws IOException {
        GameConfiguration config = OBJECT_MAPPER.readValue(new File("config.json"), GameConfiguration.class);

        String[][] matrix = {
                {"A", "A", "C"},
                {"A", "B", "A"},
                {"E", "A", "A"}
        };

        Map<String, List<String>> winningCombinations = WinningCombinationCalculator.calculateWinningCombinations(config, matrix);

        Map<String, List<String>> result = Map.of("A", List.of("same_symbol_6_times"));
        assertEquals(result, winningCombinations);
    }

}
