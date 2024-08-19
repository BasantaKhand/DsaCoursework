import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class BasicCalculator extends JFrame {
    private JTextField inputField;
    private JButton calculateButton;
    private JLabel resultLabel;

    public BasicCalculator() {
        setTitle("Basic Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLayout(new GridLayout(3, 1));

        inputField = new JTextField();
        calculateButton = new JButton("Calculate");
        resultLabel = new JLabel("Result: ");

        add(inputField);
        add(calculateButton);
        add(resultLabel);

        calculateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String expression = inputField.getText();
                try {
                    double result = evaluateExpression(expression);
                    resultLabel.setText("Result: " + result);
                } catch (Exception ex) {
                    resultLabel.setText("Error: Invalid expression");
                }
            }
        });
    }

    private double evaluateExpression(String expression) {
        return new ExpressionEvaluator().evaluate(expression);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            
                new BasicCalculator().setVisible(true);
            }
        });
    }
}

class ExpressionEvaluator {
    public double evaluate(String expression) {
        return evaluateExpression(expression);
    }

    private double evaluateExpression(String expression) {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();
        Stack<Character> brackets = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);
            if (ch == ' ') continue;

            if (Character.isDigit(ch) || ch == '.') {
                StringBuilder sb = new StringBuilder();
                while (i < expression.length() && 
(Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) 
{
                    sb.append(expression.charAt(i++));
                }
                i--;
                numbers.push(Double.parseDouble(sb.toString()));
            } else if (ch == '(' || ch == '[' || ch == '{') {
                operators.push(ch);
                brackets.push(ch);
            } else if (ch == ')' || ch == ']' || ch == '}') {
                while (!operators.isEmpty() && 
!isMatchingPair(operators.peek(), ch)) {
                    numbers.push(applyOperation(operators.pop(), 
numbers.pop(), numbers.pop()));
                }
                if (!operators.isEmpty() && 
isMatchingPair(operators.peek(), ch)) {
                    operators.pop();
                    brackets.pop();
                }
            } else if (ch == '+' || ch == '-' || ch == '*' || ch == '/') {
                while (!operators.isEmpty() && precedence(ch) <= 
precedence(operators.peek()) && !isBracket(operators.peek())) {
                    numbers.push(applyOperation(operators.pop(), 
numbers.pop(), numbers.pop()));
                }
                operators.push(ch);
            }
        }

        while (!operators.isEmpty()) {
            numbers.push(applyOperation(operators.pop(), numbers.pop(), 
numbers.pop()));
        }

        return numbers.pop();
    }

    private boolean isMatchingPair(char open, char close) {
        return (open == '(' && close == ')') ||
               (open == '[' && close == ']') ||
               (open == '{' && close == '}');
    }

    private boolean isBracket(char ch) {
        return ch == '(' || ch == ')' || ch == '[' || ch == ']' || ch == 
'{' || ch == '}';
    }

    private int precedence(char op) {
        if (op == '+' || op == '-') return 1;
        if (op == '*' || op == '/') return 2;
        return 0;
    }

    private double applyOperation(char op, double b, double a) {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/':
                if (b == 0) throw new ArithmeticException("Division by zero");
                return a / b;
        }
        return 0;
    }
}
