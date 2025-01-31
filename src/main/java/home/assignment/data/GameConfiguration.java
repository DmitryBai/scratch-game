package home.assignment.data;

import java.util.Map;

public record GameConfiguration (
        int columns,
        int rows,
        Map<String, SymbolConfiguration> symbols,
        Probabilities probabilities,
        Map<String, WinCombination> winCombinations
)
{}
