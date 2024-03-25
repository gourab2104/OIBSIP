import java.util.Random;
import javax.swing.JOptionPane;

public class NumberGame {
    public static void main(String[] args) {
        Random random = new Random();

        int minRange = 1;
        int maxRange = 100;
        int attemptsLimit = 5;
        int score = 0;

        JOptionPane.showMessageDialog(null, "Welcome to the Number Game!");

        String playerName = JOptionPane.showInputDialog("Enter your name: ");
        String playAgain;

        do {
            int randomNumber = random.nextInt(maxRange - minRange + 1) + minRange;
            int attempts = 0;
            boolean guessedCorrectly = false;

            JOptionPane.showMessageDialog(null, "New round, " + playerName + "! Guess the number between " + minRange + " and " + maxRange);

            while (attempts < attemptsLimit && !guessedCorrectly) {
                String userInput = JOptionPane.showInputDialog("Enter your guess: ");
                int userGuess = Integer.parseInt(userInput);
                attempts++;

                if (userGuess == randomNumber) {
                    JOptionPane.showMessageDialog(null, "Congratulations, " + playerName + "! You guessed the correct number in " + attempts + " attempts.");
                    guessedCorrectly = true;
                    score += attempts;
                } else if (userGuess < randomNumber) {
                    JOptionPane.showMessageDialog(null, "Too low! Try again.");
                } else {
                    JOptionPane.showMessageDialog(null, "Too high! Try again.");
                }
            }

            if (!guessedCorrectly) {
                JOptionPane.showMessageDialog(null, "Sorry, " + playerName + ", you've reached the maximum attempts. The correct number was: " + randomNumber);
            }

            playAgain = JOptionPane.showInputDialog("Do you want to play again? (yes/no): ");
        } while (playAgain.equalsIgnoreCase("yes"));

        JOptionPane.showMessageDialog(null, "Your total score, " + playerName + ", is: " + score);
        JOptionPane.showMessageDialog(null, "Thanks for playing, " + playerName + "!");
    }
}
