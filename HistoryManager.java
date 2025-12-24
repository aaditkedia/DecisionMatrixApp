import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class HistoryManager {
    private static final String HISTORY_FILE = "decision_history.txt";
    private static final String RESULTS_DIR = "results";
    private static final int MAX_HISTORY = 5;
    private static final ArrayList<DecisionResult> historyCache = new ArrayList<>();

    // Save a decision result to file
    public static void saveResult(DecisionResult result) {
        try {
            // Create results directory if it doesn't exist
            Path resultsPath = Paths.get(RESULTS_DIR);
            if (!Files.exists(resultsPath)) {
                Files.createDirectories(resultsPath);
            }

            // Create filename with timestamp and user name
            String sanitizedUser = result.userName.replaceAll("[^a-zA-Z0-9]", "_");
            String filename = sanitizedUser + "_" + 
                            result.timestamp.replaceAll("[:.]", "-").replaceAll("T", "_") + ".txt";
            Path filePath = resultsPath.resolve(filename);

            // Write result to file
            try (PrintWriter writer = new PrintWriter(new FileWriter(filePath.toFile()))) {
                writer.write(result.toFileFormat());
            }

            // Update history cache (keep only last 5 entries)
            historyCache.add(result);
            if (historyCache.size() > MAX_HISTORY) {
                historyCache.remove(0); // Remove oldest entry
            }

            // Save history to file
            saveHistoryToFile();

            System.out.println("[DEBUG] Result saved to: " + filePath.toString());
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to save result: " + e.getMessage());
        }
    }

    // Save history cache to file
    private static void saveHistoryToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(HISTORY_FILE))) {
            for (DecisionResult r : historyCache) {
                writer.write(r.toFileFormat());
            }
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to save history: " + e.getMessage());
        }
    }

    // Load history from file (called on startup)
    public static void loadHistory() {
        File historyFile = new File(HISTORY_FILE);
        if (!historyFile.exists()) {
            return;
        }

        historyCache.clear();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(historyFile))) {
            String line;
            String currentUser = "";
            String currentTimestamp = "";
            String currentWinner = "";
            int currentWinnerScore = 0;
            ArrayList<Criterion> currentCriteria = new ArrayList<>();
            ArrayList<OptionScore> currentOptions = new ArrayList<>();
            boolean inEntry = false;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("=== Decision Matrix Result ===")) {
                    inEntry = true;
                    currentCriteria.clear();
                    currentOptions.clear();
                } else if (line.startsWith("User: ")) {
                    currentUser = line.substring(6).trim();
                } else if (line.startsWith("Date: ")) {
                    currentTimestamp = line.substring(6).trim();
                } else if (line.trim().startsWith("- ") && line.contains("(Weight:")) {
                    // Parse criterion: "  - Name (Weight: X)"
                    String critLine = line.trim().substring(2);
                    int weightIndex = critLine.indexOf("(Weight:");
                    if (weightIndex > 0) {
                        String name = critLine.substring(0, weightIndex).trim();
                        String weightStr = critLine.substring(weightIndex + 8, critLine.indexOf(")"));
                        try {
                            int weight = Integer.parseInt(weightStr.trim());
                            currentCriteria.add(new Criterion(name, weight));
                        } catch (NumberFormatException e) {
                            // Skip invalid criterion
                        }
                    }
                } else if (line.trim().startsWith("WINNER: ")) {
                    // Parse winner: "WINNER: Name (Score: X)"
                    String winnerLine = line.substring(8);
                    int scoreIndex = winnerLine.indexOf("(Score:");
                    if (scoreIndex > 0) {
                        currentWinner = winnerLine.substring(0, scoreIndex).trim();
                        String scoreStr = winnerLine.substring(scoreIndex + 7, winnerLine.indexOf(")"));
                        try {
                            currentWinnerScore = Integer.parseInt(scoreStr.trim());
                        } catch (NumberFormatException e) {
                            // Use 0 if parsing fails
                        }
                    }
                } else if (line.equals("================================") && inEntry) {
                    // End of entry - create result object
                    if (!currentUser.isEmpty() && !currentWinner.isEmpty()) {
                        DecisionResult result = new DecisionResult(currentUser, currentCriteria, 
                                                                  currentOptions, currentWinner, currentWinnerScore);
                        result.timestamp = currentTimestamp;
                        historyCache.add(result);
                    }
                    inEntry = false;
                } else if (line.trim().matches("^[^:]+: \\d+( <-- WINNER)?$")) {
                    // Parse option score: "  OptionName: Score" or "  OptionName: Score <-- WINNER"
                    String optionLine = line.trim();
                    int colonIndex = optionLine.indexOf(":");
                    if (colonIndex > 0) {
                        String optionName = optionLine.substring(0, colonIndex).trim();
                        String scorePart = optionLine.substring(colonIndex + 1).trim();
                        if (scorePart.contains("<--")) {
                            scorePart = scorePart.substring(0, scorePart.indexOf("<--")).trim();
                        }
                        try {
                            int score = Integer.parseInt(scorePart);
                            currentOptions.add(new OptionScore(optionName, score));
                        } catch (NumberFormatException e) {
                            // Skip invalid option
                        }
                    }
                }
            }
            
            // Keep only last MAX_HISTORY entries
            if (historyCache.size() > MAX_HISTORY) {
                int removeCount = historyCache.size() - MAX_HISTORY;
                for (int i = 0; i < removeCount; i++) {
                    historyCache.remove(0);
                }
            }
            
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to load history: " + e.getMessage());
        }
    }

    // Get history summary for display
    public static ArrayList<String> getHistorySummary() {
        ArrayList<String> summary = new ArrayList<>();
        
        // Reverse to show most recent first
        for (int i = historyCache.size() - 1; i >= 0; i--) {
            DecisionResult result = historyCache.get(i);
            String summaryLine = String.format("%s: %s won with score %d (%s)", 
                result.userName, result.winner, result.winnerScore, 
                result.timestamp.length() > 19 ? result.timestamp.substring(0, 19) : result.timestamp);
            summary.add(summaryLine);
        }
        
        return summary;
    }
}

