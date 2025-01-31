package home.assignment;

import home.assignment.data.Result;
import home.assignment.exception.ConfigurationFileException;
import home.assignment.exception.InvalidCommandArgumentsException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ScratchGameIntegrationTest {

    private static final String CONFIG_DIRECTORY = "src/test/resources/config/";

    @Test
    void testMain_InvalidArguments_NoBet() {
        String[] args = {"--config", "config.json"};

        assertThrows(InvalidCommandArgumentsException.class, () -> ScratchGame.main(args));
    }

    @Test
    void testMain_InvalidArguments_NoConfig() {
        String[] args = { "--betting-amount", "100"};

        assertThrows(InvalidCommandArgumentsException.class, () -> ScratchGame.main(args));
    }

    @Test
    void testMain_InvalidArguments_IncorrectConfigPath() {
        String[] args = {"--config", "no.json", "--betting-amount", "100"};

        assertThrows(ConfigurationFileException.class, () -> ScratchGame.main(args));
    }

    @Test
    void testMain_InvalidArguments_IncorrectConfigFile() {
        String[] args = {"--config", CONFIG_DIRECTORY + "incorrect_config.json", "--betting-amount", "100"};

        assertThrows(ConfigurationFileException.class, () -> ScratchGame.main(args));
    }

    @Test
    void testMain_ValidArguments() {
        String[] args = {"--config", "config.json", "--betting-amount", "100"};

        assertDoesNotThrow(() -> ScratchGame.main(args));
    }

    @Test
    void testRunGame_NotPossibleToWin() {
        Result result = ScratchGame.runGame(CONFIG_DIRECTORY + "loss.json", 100);

        assertEquals(0, result.reward());
        assertEquals(Map.of(), result.appliedWinningCombinations());
        assertNull(result.appliedBonusSymbol());
    }
}
