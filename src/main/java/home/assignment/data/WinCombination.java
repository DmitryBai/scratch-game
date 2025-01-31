package home.assignment.data;

import java.util.List;

public record WinCombination(
        double rewardMultiplier,
        String when,
        Integer count,
        String group,
        List<List<String>> coveredAreas
)
{ }
