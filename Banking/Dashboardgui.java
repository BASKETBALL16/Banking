package Banking;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class Dashboardgui extends JFrame {

    private static final Color NAVY      = new Color(10, 25, 60);
    private static final Color NAVY_DARK = new Color(5, 12, 35);
    private static final Color NAVY_MID  = new Color(20, 45, 100);
    private static final Color GOLD      = new Color(197, 160, 80);
    private static final Color OFF_WHITE = new Color(245, 243, 238);
    private static final Color GRAY_SOFT = new Color(160, 158, 152);
    private static final Color GREEN     = new Color(80, 200, 120);
    private static final Color RED_SOFT  = new Color(200, 80, 80);

    private Account myAccount;
    private Manager manager;
    private JLabel balanceLabel;

    public Dashboardgui(Account myAccount, Manager manager) {
        this.myAccount = myAccount;
        this.manager   = manager;

        setTitle("First National Bank — Dashboard");
        setSize(460, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        JPanel root = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, NAVY, 0, getHeight(), NAVY_DARK));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(BorderFactory.createEmptyBorder(40, 50, 30, 50));
        setContentPane(root);

        // Header
        JLabel greetLabel = new JLabel("Welcome back,");
        greetLabel.setFont(new Font("Georgia", Font.ITALIC, 16));
        greetLabel.setForeground(GRAY_SOFT);
        greetLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel(myAccount.getUser().getFirstName() + " " + myAccount.getUser().getLastName());
        nameLabel.setFont(new Font("Georgia", Font.BOLD, 24));
        nameLabel.setForeground(GOLD);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel typeLabel = new JLabel(myAccount.getAccountType() + "  •  " + myAccount.getAccountID());
        typeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        typeLabel.setForeground(GRAY_SOFT);
        typeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator line = new JSeparator();
        line.setForeground(GOLD);
        line.setMaximumSize(new Dimension(300, 1));
        line.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Balance card
        JPanel balanceCard = new JPanel();
        balanceCard.setLayout(new BoxLayout(balanceCard, BoxLayout.Y_AXIS));
        balanceCard.setBackground(NAVY_MID);
        balanceCard.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(10, GOLD),
                BorderFactory.createEmptyBorder(20, 30, 20, 30)));
        balanceCard.setMaximumSize(new Dimension(340, 100));
        balanceCard.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel balanceTitle = new JLabel("Current Balance");
        balanceTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        balanceTitle.setForeground(GRAY_SOFT);
        balanceTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        balanceLabel = new JLabel("$" + String.format("%.2f", myAccount.getBalance()));
        balanceLabel.setFont(new Font("Georgia", Font.BOLD, 32));
        balanceLabel.setForeground(OFF_WHITE);
        balanceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        balanceCard.add(balanceTitle);
        balanceCard.add(Box.createVerticalStrut(6));
        balanceCard.add(balanceLabel);

        // Buttons
        JButton depositBtn  = makeButton("DEPOSIT",           true,  GREEN);
        JButton withdrawBtn = makeButton("WITHDRAW",          true,  RED_SOFT);
        JButton statementBtn= makeButton("PRINT STATEMENT",   false, GOLD);
        JButton logoutBtn   = makeButton("LOG OUT",           false, GRAY_SOFT);

        depositBtn.addActionListener(e -> handleDeposit());
        withdrawBtn.addActionListener(e -> handleWithdraw());
        statementBtn.addActionListener(e -> {
            myAccount.printStatement();
            JOptionPane.showMessageDialog(this, "Statement printed to console.", "Statement", JOptionPane.INFORMATION_MESSAGE);
        });
        logoutBtn.addActionListener(e -> handleLogout());

        JLabel footer = new JLabel("© 2025 First National Bank  •  FDIC Insured");
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        footer.setForeground(GRAY_SOFT);
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);

        root.add(greetLabel);
        root.add(Box.createVerticalStrut(4));
        root.add(nameLabel);
        root.add(Box.createVerticalStrut(4));
        root.add(typeLabel);
        root.add(Box.createVerticalStrut(16));
        root.add(line);
        root.add(Box.createVerticalStrut(24));
        root.add(balanceCard);
        root.add(Box.createVerticalStrut(24));
        root.add(depositBtn);
        root.add(Box.createVerticalStrut(10));
        root.add(withdrawBtn);
        root.add(Box.createVerticalStrut(10));
        root.add(statementBtn);
        root.add(Box.createVerticalStrut(10));
        root.add(logoutBtn);
        root.add(Box.createVerticalStrut(20));
        root.add(footer);

        setVisible(true);
    }

    private void handleDeposit() {
        String input = JOptionPane.showInputDialog(this, "How much to deposit?");
        if (input == null || input.trim().isEmpty()) return;
        try {
            double amount = Double.parseDouble(input.trim());
            myAccount.deposit(amount);
            refreshBalance();
            JOptionPane.showMessageDialog(this,
                "Success! New Balance: $" + String.format("%.2f", myAccount.getBalance()),
                "Deposit", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleWithdraw() {
        String input = JOptionPane.showInputDialog(this, "How much to withdraw?");
        if (input == null || input.trim().isEmpty()) return;
        try {
            double amount = Double.parseDouble(input.trim());
            boolean success = myAccount.withdraw(amount);
            refreshBalance();
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Success! New Balance: $" + String.format("%.2f", myAccount.getBalance()),
                    "Withdrawal", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Withdrawal failed. Check your balance.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleLogout() {
        manager.saveDatabase();  // Persist all balance changes to file
        dispose();
        new bankGUI(manager);    // Return to login screen
    }

    private void refreshBalance() {
        balanceLabel.setText("$" + String.format("%.2f", myAccount.getBalance()));
    }

    private JButton makeButton(String text, boolean filled, Color color) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (filled) {
                    g2.setColor(getModel().isPressed() ? color.darker() : color);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                } else {
                    g2.setColor(new Color(255, 255, 255, 15));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(filled ? NAVY : OFF_WHITE);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(8, color),
                BorderFactory.createEmptyBorder(12, 0, 12, 0)));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(300, 48));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.repaint(); }
            @Override public void mouseExited(MouseEvent e)  { btn.repaint(); }
        });
        return btn;
    }

    private static class RoundedBorder extends AbstractBorder {
        private final int   radius;
        private final Color color;

        RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color  = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(x, y, w - 1, h - 1, radius, radius);
            g2.dispose();
        }
    }
}