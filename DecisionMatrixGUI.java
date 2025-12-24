import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class DecisionMatrixGUI extends JFrame {

    // Data Structures
    private final ArrayList<Criterion> criteriaList = new ArrayList<>();
    private DefaultTableModel tableModel;
    private JTable decisionTable;
    
    // UI Components
    private JTextField critNameField, critWeightField, optionNameField, userNameField;
    private JLabel statusLabel;
    private String currentUserName = "Guest";

    public DecisionMatrixGUI() {
        System.out.println("[DEBUG] DecisionMatrixGUI constructor started.");
        
        setTitle("System 2 Decision Helper");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setLayout(new BorderLayout(10, 10));

        initializeComponents();
        
        System.out.println("[DEBUG] DecisionMatrixGUI components initialized. Window visible.");
    }

    private void initializeComponents() {
        System.out.println("[DEBUG] initializeComponents() called.");

        // --- TOP PANEL ---
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        
        JLabel titleLabel = new JLabel("Rational Decision Matrix");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(titleLabel);

        // --- USER PANEL ---
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        userPanel.setBorder(BorderFactory.createTitledBorder("User"));
        userNameField = new JTextField(15);
        userNameField.setText(currentUserName);
        JButton setUserBtn = new JButton("Set User Name");
        JButton viewHistoryBtn = new JButton("View History");
        userPanel.add(new JLabel("User Name:"));
        userPanel.add(userNameField);
        userPanel.add(setUserBtn);
        userPanel.add(viewHistoryBtn);

        // --- INPUT PANEL ---
        JPanel inputPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        
        // Row 1: Criteria
        JPanel critPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        critPanel.setBorder(BorderFactory.createTitledBorder("Step 1: Add Criteria"));
        critNameField = new JTextField(10);
        critWeightField = new JTextField(3);
        JButton addCritBtn = new JButton("Add Criterion");
        
        critPanel.add(new JLabel("Criterion:"));
        critPanel.add(critNameField);
        critPanel.add(new JLabel("Weight (1-10):"));
        critPanel.add(critWeightField);
        critPanel.add(addCritBtn);

        // Row 2: Options
        JPanel optPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        optPanel.setBorder(BorderFactory.createTitledBorder("Step 2: Add Options"));
        optionNameField = new JTextField(15);
        JButton addOptBtn = new JButton("Add Option");
        
        optPanel.add(new JLabel("Option Name:"));
        optPanel.add(optionNameField);
        optPanel.add(addOptBtn);

        inputPanel.add(critPanel);
        inputPanel.add(optPanel);
        topPanel.add(userPanel);
        topPanel.add(inputPanel);
        add(topPanel, BorderLayout.NORTH);

        // --- CENTER PANEL (Table) ---
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Option Name");
        tableModel.addColumn("Total Score"); 

        decisionTable = new JTable(tableModel);
        decisionTable.setRowHeight(30);
        add(new JScrollPane(decisionTable), BorderLayout.CENTER);

        // --- BOTTOM PANEL ---
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton calculateBtn = new JButton("CALCULATE DECISION");
        calculateBtn.setFont(new Font("Arial", Font.BOLD, 16));
        calculateBtn.setBackground(new Color(70, 130, 180));
        calculateBtn.setForeground(Color.WHITE);
        JButton saveBtn = new JButton("Save Result");
        saveBtn.setBackground(new Color(34, 139, 34));
        saveBtn.setForeground(Color.WHITE);
        buttonPanel.add(calculateBtn);
        buttonPanel.add(saveBtn);
        statusLabel = new JLabel("Ready.");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);

        // --- EVENT LISTENERS ---
        
        // Listener 1: Add Criterion
        addCritBtn.addActionListener(e -> {
            System.out.println("\n[DEBUG] 'Add Criterion' button clicked.");
            String name = critNameField.getText().trim();
            String weightStr = critWeightField.getText().trim();
            System.out.println("[DEBUG] Raw Inputs - Name: '" + name + "', Weight: '" + weightStr + "'");

            if (name.isEmpty() || weightStr.isEmpty()) {
                System.out.println("[DEBUG] Error: Empty fields detected.");
                JOptionPane.showMessageDialog(this, "Please enter name and weight.");
                return;
            }

            try {
                int weight = Integer.parseInt(weightStr);
                System.out.println("[DEBUG] Parsed weight integer: " + weight);

                if (weight < 1 || weight > 10) {
                    System.out.println("[DEBUG] Error: Weight out of range (1-10).");
                    throw new NumberFormatException();
                }

                criteriaList.add(new Criterion(name, weight));
                tableModel.addColumn(name + " (Wt:" + weight + ")");
                System.out.println("[DEBUG] Column added to table model. Total columns: " + tableModel.getColumnCount());
                
                critNameField.setText("");
                critWeightField.setText("");
                statusLabel.setText("Added criterion: " + name);

            } catch (NumberFormatException ex) {
                System.out.println("[DEBUG] Exception caught: Invalid number format for weight.");
                JOptionPane.showMessageDialog(this, "Weight must be an integer (1-10).");
            }
        });

        // Listener 2: Add Option
        addOptBtn.addActionListener(e -> {
            System.out.println("\n[DEBUG] 'Add Option' button clicked.");
            String name = optionNameField.getText().trim();
            System.out.println("[DEBUG] Input Name: '" + name + "'");

            if (name.isEmpty()) {
                System.out.println("[DEBUG] Error: Empty option name.");
                JOptionPane.showMessageDialog(this, "Please enter an option name.");
                return;
            }

            ArrayList<Object> rowList = new ArrayList<>();
            rowList.add(name); 
            rowList.add(0);    
            
            // Pad the row with 0s for every existing criterion
            for (int i = 0; i < criteriaList.size(); i++) {
                rowList.add(0);
            }
            System.out.println("[DEBUG] Constructed row with size: " + rowList.size());

            // Convert ArrayList to Object array for addRow
            Object[] row = rowList.toArray();
            tableModel.addRow(row);
            System.out.println("[DEBUG] Row added to table. Total rows: " + tableModel.getRowCount());
            
            optionNameField.setText("");
            statusLabel.setText("Added option: " + name);
        });

        // Listener 3: Calculate Logic
        calculateBtn.addActionListener(e -> {
            System.out.println("\n[DEBUG] --- CALCULATION STARTED ---");
            int rowCount = tableModel.getRowCount();
            int critCount = criteriaList.size();
            System.out.println("[DEBUG] Processing " + rowCount + " rows against " + critCount + " criteria.");

            if (rowCount == 0 || critCount == 0) {
                System.out.println("[DEBUG] Aborting: Not enough data.");
                JOptionPane.showMessageDialog(this, "Please add criteria and options first.");
                return;
            }

            String winnerName = "";
            int maxScore = -1;

            for (int row = 0; row < rowCount; row++) {
                int currentTotal = 0;
                String optName = (String) tableModel.getValueAt(row, 0);
                System.out.println("[DEBUG] >>> Analyzing Row " + row + ": " + optName);

                for (int i = 0; i < critCount; i++) {
                    int colIndex = i + 2; // +2 because Col 0 is Name, Col 1 is Total
                    Criterion c = criteriaList.get(i);
                    
                    try {
                        Object cellValue = tableModel.getValueAt(row, colIndex);
                        if (cellValue == null || cellValue.toString().trim().isEmpty()) {
                            System.out.println("[DEBUG]    ! Empty cell at Col " + colIndex + ". Treating as 0.");
                            continue;
                        }
                        int score = Integer.parseInt(cellValue.toString().trim());
                        int weightedScore = score * c.weight;
                        
                        System.out.println("[DEBUG]    Criterion '" + c.name + "' (Wt: " + c.weight + ")");
                        System.out.println("[DEBUG]    User Score: " + score + " -> Weighted: " + weightedScore);
                        
                        currentTotal += weightedScore;
                        
                    } catch (NumberFormatException ex) {
                        System.out.println("[DEBUG]    ! Invalid number format at Col " + colIndex + ". Treating as 0.");
                    } catch (Exception ex) {
                        System.out.println("[DEBUG]    ! Error at Col " + colIndex + ": " + ex.getMessage());
                    }
                }
                
                System.out.println("[DEBUG] === Total Utility for " + optName + ": " + currentTotal);
                tableModel.setValueAt(currentTotal, row, 1); // Update the "Total" column

                if (currentTotal > maxScore) {
                    System.out.println("[DEBUG] New Winner found! (Old Max: " + maxScore + " -> New Max: " + currentTotal + ")");
                    maxScore = currentTotal;
                    winnerName = optName;
                }
            }
            
            System.out.println("[DEBUG] --- CALCULATION FINISHED. Winner: " + winnerName + " ---");
            statusLabel.setText("Winner: " + winnerName + " (Score: " + maxScore + ")");
            JOptionPane.showMessageDialog(this, "Logical Choice: " + winnerName + "\nScore: " + maxScore);
        });

        // Listener 4: Set User Name
        setUserBtn.addActionListener(e -> {
            String name = userNameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a user name.");
                return;
            }
            currentUserName = name;
            statusLabel.setText("User set to: " + currentUserName);
            System.out.println("[DEBUG] User name set to: " + currentUserName);
        });

        // Listener 5: View History
        viewHistoryBtn.addActionListener(e -> {
            ArrayList<String> history = HistoryManager.getHistorySummary();
            if (history.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No history available yet.");
                return;
            }
            
            StringBuilder historyText = new StringBuilder("Recent Decision History (Last 5):\n\n");
            for (int i = 0; i < history.size(); i++) {
                historyText.append((i + 1)).append(". ").append(history.get(i)).append("\n");
            }
            
            JTextArea textArea = new JTextArea(historyText.toString());
            textArea.setEditable(false);
            textArea.setWrapStyleWord(true);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(600, 300));
            JOptionPane.showMessageDialog(this, scrollPane, "Decision History", JOptionPane.INFORMATION_MESSAGE);
        });

        // Listener 6: Save Result
        saveBtn.addActionListener(e -> {
            if (tableModel.getRowCount() == 0 || criteriaList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please calculate a decision first before saving.");
                return;
            }
            
            // Collect option scores
            ArrayList<OptionScore> optionScores = new ArrayList<>();
            String winner = "";
            int winnerScore = -1;
            
            for (int row = 0; row < tableModel.getRowCount(); row++) {
                String optionName = (String) tableModel.getValueAt(row, 0);
                Object totalObj = tableModel.getValueAt(row, 1);
                int totalScore = 0;
                
                if (totalObj != null) {
                    try {
                        totalScore = Integer.parseInt(totalObj.toString());
                    } catch (NumberFormatException ex) {
                        // Use 0 if parsing fails
                    }
                }
                
                optionScores.add(new OptionScore(optionName, totalScore));
                
                if (totalScore > winnerScore) {
                    winnerScore = totalScore;
                    winner = optionName;
                }
            }
            
            // Create and save result
            DecisionResult result = new DecisionResult(currentUserName, criteriaList, optionScores, winner, winnerScore);
            HistoryManager.saveResult(result);
            
            JOptionPane.showMessageDialog(this, "Decision result saved successfully!\nSaved to 'results' folder.");
            statusLabel.setText("Result saved for user: " + currentUserName);
        });
    }
}