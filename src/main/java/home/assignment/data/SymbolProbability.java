package home.assignment.data;

import java.util.Map;

public record SymbolProbability(
        int column,
        int row,
        Map<String, Integer> symbols
)
{ }
