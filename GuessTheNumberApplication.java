//package Task;
import java.awt.*;
import java.awt.event.*;

public class GuessTheNumberApplication extends Frame implements ActionListener {
    private Label instructionLabel;
    private TextField guessTextField;
    private Button submitButton;
    private int targetNumber;
    private int attemptsLeft;
    private int score;
    private int maxRounds;
    private final int MAX_ATTEMPTS = 3;
    private int currentRound;

    public GuessTheNumberApplication() {
        setTitle("Guess The Number");
        setSize(800, 500);
        setLayout(new FlowLayout());

        instructionLabel = new Label("Attempts left: " + MAX_ATTEMPTS + ". Enter your guess:");
        guessTextField = new TextField(10);
        submitButton = new Button("Submit");

        add(instructionLabel);
        add(guessTextField);
        add(submitButton);

        submitButton.addActionListener(this);

        startNewGame();

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            try {
                int guess = Integer.parseInt(guessTextField.getText());
                String message = checkGuess(guess);
                showMessageDialog(message);
            } catch (NumberFormatException ex) {
                showMessageDialog("Please enter a valid number!");
            }
        }
    }

    private void startNewGame() {
        maxRounds = 2;
        currentRound = 1;
        score = 0;
        startNewRound();
    }

    private void startNewRound() {
        attemptsLeft = MAX_ATTEMPTS;
        targetNumber = generateRandomNumber(1, 100);
        updateInstructionLabel();
    }

    private int generateRandomNumber(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    private String checkGuess(int guess) {
        if (guess == targetNumber) {
            score += attemptsLeft * 10;
            if (currentRound < maxRounds) {
                currentRound++;
                startNewRound();
                return "Congratulations! You've guessed the number! Your score is: " + score;
            } else {
                System.exit(0); // End the game after completing all rounds
            }
        } else if (guess < targetNumber) {
            attemptsLeft--;
            return "Try a higher number. Attempts left: " + attemptsLeft;
        } else {
            attemptsLeft--;
            return "Try a lower number. Attempts left: " + attemptsLeft;
        }
        return null;
    }

    private void updateInstructionLabel() {
        instructionLabel.setText("Round: " + currentRound + ", Attempts left: " + attemptsLeft + ". Enter your guess:");
    }

    private void showMessageDialog(String message) {
        Dialog dialog = new Dialog(this, "Guess The Number Result", true);
        dialog.setLayout(new FlowLayout());
        dialog.setSize(500, 300);
        Label label = new Label(message);
        Button button = new Button("OK");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        dialog.add(label);
        dialog.add(button);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        GuessTheNumberApplication game = new GuessTheNumberApplication();
        game.setVisible(true);
    }
}
