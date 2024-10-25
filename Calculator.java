import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Calculator extends JFrame implements ActionListener {
    private JTextPane display;
    private JLabel promptLabel; // Label for showing prompts
    private StyledDocument doc;
    private Style defaultStyle;
    private Style operatorStyle;
    
    private double num1 = 0, num2 = 0, result = 0;
    private char operator;

    public Calculator() {
        setTitle("Calculator");
        setSize(400, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create a panel for the welcome message and display
        JPanel topPanel = new JPanel(new BorderLayout());

        // Instruction label with welcome message in green and centered
        promptLabel = new JLabel("Welcome!", SwingConstants.CENTER);
        promptLabel.setFont(new Font("Arial", Font.BOLD, 30));
        promptLabel.setForeground(Color.BLACK);  // Set text color to green
        topPanel.add(promptLabel, BorderLayout.NORTH);

        // Display panel with JTextPane for mixed font support
        display = new JTextPane();
        display.setEditable(false);
        display.setPreferredSize(new Dimension(400, 70));
        topPanel.add(display, BorderLayout.CENTER);

        // Add the top panel to the frame
        add(topPanel, BorderLayout.NORTH);

        // StyledDocument to manage styles in the display
        doc = display.getStyledDocument();

        // Define different styles
        defaultStyle = doc.addStyle("default", null);
        StyleConstants.setFontFamily(defaultStyle, "Verdana");
        StyleConstants.setFontSize(defaultStyle, 28);

        operatorStyle = doc.addStyle("operator", null);
        StyleConstants.setFontFamily(operatorStyle, "Courier New");
        StyleConstants.setBold(operatorStyle, true);
        StyleConstants.setFontSize(operatorStyle, 28);

        // Button panel with grid layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 4, 10, 10));

        // Button labels
        String[] buttons = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", ".", "=", "+"
        };

        // Monospaced font for buttons
        Font buttonFont = new Font("Courier New", Font.BOLD, 20);

        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(buttonFont);
            button.addActionListener(this);
            buttonPanel.add(button);
        }

        add(buttonPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        calculator.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        try {
            if (command.charAt(0) >= '0' && command.charAt(0) <= '9' || command.equals(".")) {
                doc.insertString(doc.getLength(), command, defaultStyle);
                promptLabel.setText("Enter an operator (+, -, *, /):");
            } else if (command.equals("=")) {
                num2 = Double.parseDouble(display.getText());

                switch (operator) {
                    case '+':
                        result = num1 + num2;
                        break;
                    case '-':
                        result = num1 - num2;
                        break;
                    case '*':
                        result = num1 * num2;
                        break;
                    case '/':
                        if (num2 != 0) {
                            result = num1 / num2;
                        } else {
                            display.setText("Error! Division by zero.");
                            promptLabel.setText("Enter a new calculation:");
                            return;
                        }
                        break;
                    default:
                        return;
                }
                display.setText(String.valueOf(result));
                promptLabel.setText("Calculation complete! Enter a new calculation:");
                num1 = result;
            } else {
                if (!display.getText().isEmpty()) {
                    num1 = Double.parseDouble(display.getText());
                    operator = command.charAt(0);
                    doc.insertString(doc.getLength(), " " + operator + " ", operatorStyle);
                    promptLabel.setText("Enter the next number:");
                }
            }
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }
}
