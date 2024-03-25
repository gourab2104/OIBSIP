import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class OnlineExamSystemGUI extends JFrame {
    private User currentUser = null;
    private UserDatabase userDB;
    private QuestionDatabase questionDB;
    private Timer timer;
    private JTextArea outputTextArea;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public OnlineExamSystemGUI() {
        super("Online Exam System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null); // Center the window
        setLayout(new BorderLayout());

        userDB = new UserDatabase();
        questionDB = new QuestionDatabase();
        timer = new Timer();

        JPanel loginPanel = new JPanel(new GridLayout(3, 2));
        loginPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        loginPanel.add(passwordField);
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new LoginButtonListener());
        loginPanel.add(loginButton);
        add(loginPanel, BorderLayout.NORTH);

        outputTextArea = new JTextArea();
        outputTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputTextArea);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void updateOutput(String text) {
        outputTextArea.append(text + "\n");
    }

    private class LoginButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            currentUser = userDB.authenticate(username, password);
            if (currentUser != null) {
                updateOutput("Login successful. Welcome, " + currentUser.getUsername() + "!");
                updateProfile(currentUser);
                startExam();
            } else {
                updateOutput("Login failed. Please try again.");
            }
        }
    }

    private void updateProfile(User user) {
        String newName = JOptionPane.showInputDialog("Enter new name (press Enter to keep current name):");
        if (newName != null && !newName.isEmpty()) {
            user.setName(newName);
            userDB.updateUser(user);
            updateOutput("Profile updated successfully.");
        }
    }

    private void startExam() {
        updateOutput("Timer started. You have 1 minute for the exam.");
        timer.startTimer(60, currentUser); // 1 minute exam
        takeExam(currentUser);
        timer.stopTimer();
        logout();
        updateOutput("Logged out successfully.");
    }

    private void takeExam(User user) {
        updateOutput("Exam Started:");
        int score = 0;

        for (Question question : questionDB.getQuestions()) {
            StringBuilder message = new StringBuilder(question.getQuestionText() + "\n");
            Object[] options = new Object[question.getOptions().length];
            for (int i = 0; i < question.getOptions().length; i++) {
                options[i] = (i + 1) + ". " + question.getOptions()[i];
            }
            int choice = JOptionPane.showOptionDialog(null, message.toString(), "Question", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);

            if (choice >= 0 && choice < question.getOptions().length) {
                String selectedAnswer = question.getOptions()[choice];
                user.addAnswer(question.getQuestionText(), selectedAnswer);
                if (selectedAnswer.equals(question.getCorrectAnswer())) {
                    updateOutput("Correct!");
                    score++;
                } else {
                    updateOutput("Incorrect. The correct answer is: " + question.getCorrectAnswer());
                }
            } else {
                updateOutput("Invalid choice. Skipping question.");
            }
        }
        updateOutput("Exam Completed. Your total score is: " + score);
    }

    private void logout() {
        currentUser = null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            OnlineExamSystemGUI gui = new OnlineExamSystemGUI();
            gui.setVisible(true);
        });
    }
}
