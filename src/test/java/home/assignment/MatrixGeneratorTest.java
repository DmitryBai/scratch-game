package home.assignment;

import com.fasterxml.jackson.databind.ObjectMapper;
import home.assignment.data.GameConfiguration;
import org.junit.jupiter.api.RepeatedTest;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MatrixGeneratorTest {

    private static final String CONFIG_DIRECTORY = "src/test/resources/config/";
    private static final ObjectMapper OBJECT_MAPPER = ScratchGame.getObjectMapper();

    @RepeatedTest(20)
    void testGenerateMatrix() throws IOException {
        var config = OBJECT_MAPPER.readValue(new File(CONFIG_DIRECTORY + "easy_config.json"), GameConfiguration.class);

        MatrixGenerator matrixGenerator = new MatrixGenerator(config);

        String[][] matrix = matrixGenerator.generateMatrix();

        assertNotNull(matrix);
        assertEquals(3, matrix.length);
        assertEquals(3, matrix[0].length);

        for (String[] row : matrix) {
            for (String symbol : row) {
                assertTrue(config.symbols().containsKey(symbol));
            }
        }
    }

    // Can also calculate and test probabilities
}
