import javax.swing.*;
import java.awt.*;

public class CalculatriceResultat {
    static boolean start = true; // pour savoir si on doit effacer le champ au début

    public static void main(String[] args) {
        JFrame frame = new JFrame("Calculatrice Résultat");
        frame.setSize(350, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        JTextField text = new JTextField();
        text.setEditable(false);
        text.setFont(new Font("Arial", Font.BOLD, 24));
        text.setHorizontalAlignment(JTextField.RIGHT);
        text.setBackground(Color.WHITE);
        text.setForeground(Color.BLACK);
        frame.add(text, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 4, 5, 5));
        panel.setBackground(Color.DARK_GRAY);

        String[] buttons = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "=", "+",
                "C"
        };

        for (String b : buttons) {
            JButton button = new JButton(b);
            button.setFont(new Font("Arial", Font.BOLD, 20));
            button.setFocusPainted(false);

            // Couleur des boutons
            if ("0123456789.".contains(b)) {
                button.setBackground(Color.LIGHT_GRAY);
            } else if ("+-*/".contains(b)) {
                button.setBackground(new Color(255, 165, 0));
                button.setForeground(Color.WHITE);
            } else if (b.equals("=")) {
                button.setBackground(new Color(50, 205, 50));
                button.setForeground(Color.WHITE);
            } else if (b.equals("C")) {
                button.setBackground(Color.RED);
                button.setForeground(Color.WHITE);
            }

            panel.add(button);

            button.addActionListener(e -> {
                String cmd = e.getActionCommand();

                if ("0123456789.".contains(cmd)) {
                    text.setText(text.getText() + cmd);
                    start = false;
                } else if ("+-*/".contains(cmd)) {
                    if (!text.getText().isEmpty() && !endsWithOperator(text.getText())) {
                        text.setText(text.getText() + cmd);
                    }
                } else if (cmd.equals("=")) {
                    try {
                        double result = evaluateExpression(text.getText());
                        text.setText("" + result);
                        start = true;
                    } catch (Exception ex) {
                        text.setText("Erreur");
                    }
                } else if (cmd.equals("C")) {
                    text.setText("");
                    start = true;
                }
            });
        }

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private static boolean endsWithOperator(String s) {
        return s.endsWith("+") || s.endsWith("-") || s.endsWith("*") || s.endsWith("/");
    }

    private static double evaluateExpression(String expr) throws Exception {
        char operator = 0;
        int opIndex = -1;

        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if ("+-*/".indexOf(c) != -1) {
                operator = c;
                opIndex = i;
                break;
            }
        }

        if (opIndex == -1) throw new Exception("Pas d'opérateur");

        double num1 = Double.parseDouble(expr.substring(0, opIndex));
        double num2 = Double.parseDouble(expr.substring(opIndex + 1));

        switch (operator) {
            case '+': return num1 + num2;
            case '-': return num1 - num2;
            case '*': return num1 * num2;
            case '/':
                if (num2 == 0) throw new Exception("Division par zéro");
                return num1 / num2;
        }

        throw new Exception("Erreur de calcul");
    }
}