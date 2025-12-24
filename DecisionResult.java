import java.util.ArrayList;

public class DecisionResult {
    public String userName;
    public String timestamp;
    public ArrayList<Criterion> criteria;
    public ArrayList<OptionScore> optionScores;
    public String winner;
    public int winnerScore;

    public DecisionResult(String userName, ArrayList<Criterion> criteria, 
                         ArrayList<OptionScore> optionScores, String winner, int winnerScore) {
        this.userName = userName;
        this.criteria = new ArrayList<>(criteria);
        this.optionScores = new ArrayList<>(optionScores);
        this.winner = winner;
        this.winnerScore = winnerScore;
        this.timestamp = java.time.LocalDateTime.now().toString();
    }

    // Convert to file format (structured text)
    public String toFileFormat() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Decision Matrix Result ===\n");
        sb.append("User: ").append(userName).append("\n");
        sb.append("Date: ").append(timestamp).append("\n\n");
        
        sb.append("Criteria:\n");
        for (Criterion c : criteria) {
            sb.append("  - ").append(c.name).append(" (Weight: ").append(c.weight).append(")\n");
        }
        
        sb.append("\nOptions and Scores:\n");
        for (OptionScore os : optionScores) {
            sb.append("  ").append(os.optionName).append(": ").append(os.totalScore);
            if (os.optionName.equals(winner)) {
                sb.append(" <-- WINNER");
            }
            sb.append("\n");
        }
        
        sb.append("\nWINNER: ").append(winner).append(" (Score: ").append(winnerScore).append(")\n");
        sb.append("================================\n\n");
        
        return sb.toString();
    }
}

