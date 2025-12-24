import javax.swing.SwingUtilities;

public class DecisionMatrixApp {
    public static void main(String[] args) {
        System.out.println("[DEBUG] Program launched. Starting main method.");
        
        // Load history on startup
        HistoryManager.loadHistory();
        
        // Run the GUI on the standard Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            System.out.println("[DEBUG] Invoking GUI on Event Dispatch Thread.");
            new DecisionMatrixGUI().setVisible(true);
        });
        
        System.out.println("[DEBUG] Main method finished (GUI running in background).");
    }
}