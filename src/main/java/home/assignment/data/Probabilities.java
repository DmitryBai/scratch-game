package home.assignment.data;

import java.util.List;

public record Probabilities(
        List<SymbolProbability> standardSymbols,
        SymbolProbability bonusSymbols

) {
}
