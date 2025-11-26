import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CalculatriceResultat {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Calculatrice Moderne");
        frame.setSize(400, 550);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        JTextField screen = new JTextField();
        screen.setEditable(false);
        screen.setFont(new Font("Arial", Font.BOLD, 28));
        screen.setHorizontalAlignment(JTextField.RIGHT);
        screen.setBackground(Color.WHITE);
        screen.setForeground(Color.BLACK);
        screen.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        frame.add(screen, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 4, 10, 10));
        panel.setBackground(Color.WHITE);

        String[] keys = {
                "(", ")", "%", "AC",
                "7", "8", "9", "÷",
                "4", "5", "6", "×",
                "1", "2", "3", "−",
                "0", ".", "=", "+"
        };

        // Action de chaque bouton
        ActionListener listener = e -> {
            String cmd = e.getActionCommand();

            switch (cmd) {
                case "AC":
                    screen.setText("");
                    break;
                case "=":
                    try {
                        String expr = screen.getText();
                        expr = expr.replace("×", "*").replace("÷", "/").replace("−", "-");
                        double result = eval(expr);
                        screen.setText("" + result);
                    } catch (Exception ex) {
                        screen.setText("Erreur");
                    }
                    break;
                default:
                    screen.setText(screen.getText() + cmd);
            }
        };

        // Ajouter les boutons
        for (String key : keys) {
            JButton btn = new JButton(key);
            btn.setFont(new Font("Arial", Font.BOLD, 22));
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Style des boutons
            if ("0123456789.".contains(key)) {
                btn.setBackground(new Color(230, 235, 245)); // gris clair
            } else if (key.equals("=")) {
                btn.setBackground(new Color(40, 100, 255)); // bleu
                btn.setForeground(Color.WHITE);
            } else if (key.equals("AC")) {
                btn.setBackground(new Color(180, 200, 255)); // bleu clair
            } else {
                btn.setBackground(new Color(200, 210, 230)); // gris bleuté
            }

            btn.addActionListener(listener);
            panel.add(btn);
        }

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    // Fonction de calcul : gère + - * / et parenthèses
    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Caractère inattendu");
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if (eat('*')) x *= parseFactor();
                    else if (eat('/')) x /= parseFactor();
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return +parseFactor();
                if (eat('-')) return -parseFactor();

                double x;
                int startPos = this.pos;

                if (eat('(')) {
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else {
                    throw new RuntimeException("Erreur");
                }

                return x;
            }
        }.parse();
    }
}
