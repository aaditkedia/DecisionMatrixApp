# Decision Matrix Application

A Java Swing GUI application that helps users make rational, data-driven decisions using a weighted decision matrix approach. This tool implements a "System 2" decision-making framework, allowing users to evaluate multiple options against weighted criteria to identify the best choice.

## Features

- **Weighted Decision Matrix**: Evaluate multiple options against weighted criteria
- **User System**: Track decisions by user name
- **History Tracking**: Maintain a history of the last 5 decisions
- **File Persistence**: Save decision results to text files with timestamps
- **Interactive GUI**: Easy-to-use Swing interface for input and visualization

## How It Works

1. **Add Criteria**: Define the factors that matter to your decision, each with a weight (1-10)
2. **Add Options**: Enter the alternatives you're considering
3. **Score Options**: Rate each option against each criterion
4. **Calculate**: The application calculates weighted scores and identifies the winner
5. **Save & Review**: Save results and view decision history

## Requirements

- Java JDK 8 or higher
- No external dependencies (uses only Java Standard Library)

## How to Run

1. **Compile the project:**
   ```bash
   javac *.java
   ```

2. **Run the application:**
   ```bash
   java DecisionMatrixApp
   ```

The GUI window will open, and you're ready to make decisions!

## Usage

### Step 1: Set Your User Name (Optional)
- Enter your name in the "User Name" field
- Click "Set User Name"

### Step 2: Add Criteria
- Enter a criterion name (e.g., "Price", "Quality", "Location")
- Enter a weight from 1-10 (higher = more important)
- Click "Add Criterion"
- Repeat for all criteria you want to consider

### Step 3: Add Options
- Enter an option name (e.g., "Option A", "Apartment 1")
- Click "Add Option"
- Repeat for all alternatives

### Step 4: Score Options
- In the table, enter scores for each option under each criterion column
- Use any numeric scale you prefer (e.g., 1-10, or any numbers)

### Step 5: Calculate Decision
- Click "CALCULATE DECISION" button
- The application will:
  - Calculate weighted scores: `(Score × Criterion Weight)` for each option
  - Sum the weighted scores to get a total for each option
  - Identify and display the winner (highest total score)

### Step 6: Save Result
- Click "Save Result" to save the decision to a file
- Results are saved in the `results/` folder with a timestamp

### View History
- Click "View History" to see the last 5 decisions made
- History shows user, winner, score, and timestamp

## Project Structure

```
DecisionMatrixApp/
├── DecisionMatrixApp.java      # Main entry point
├── DecisionMatrixGUI.java      # GUI implementation
├── Criterion.java              # Criterion data class
├── DecisionResult.java         # Result storage class
├── OptionScore.java            # Option score helper class
├── HistoryManager.java         # History and file management
└── results/                    # Saved decision results (auto-created)
```

## File Output

When you save a result, a text file is created in the `results/` folder with:
- User name
- Timestamp
- All criteria and their weights
- All options and their scores
- The winning option and its score

## Example Use Case

**Decision: Choosing an apartment**

**Criteria:**
- Location (Weight: 8)
- Price (Weight: 9)
- Size (Weight: 6)

**Options:**
- Apartment A
- Apartment B
- Apartment C

**Scoring:**
- Score each apartment 1-10 on each criterion
- The application multiplies scores by weights and finds the best option

## License

This project is available for educational and personal use.

