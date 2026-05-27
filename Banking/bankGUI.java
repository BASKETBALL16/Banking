package Banking;

import javax.swing.*;
import java.awt.*;

public class bankGUI extends JFrame {

    private Manager manager;
    private JTextField nameField;
    private JPasswordField pinField;

    public bankGUI(Manager manager) {
        this.manager = manager;

        setTitle("First National Bank");
        setSize(380, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Outer panel centers everything vertically
        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(Color.WHITE);
        setContentPane(root);

        // Inner panel holds all content at a fixed width
        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBackground(Color.WHITE);
        inner.setPreferredSize(new Dimension(280, 340));

        // --- Header ---
        JLabel bankIcon = new JLabel("", SwingConstants.CENTER);
        bankIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        bankIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("First National Bank", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Sign in to your account", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Form fields (labels + inputs share the same fixed width) ---
        JLabel nameLabel = new JLabel("Full Name");
        nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel pinLabel = new JLabel("PIN");
        pinLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        pinLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        pinField = new JPasswordField();
        pinField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        pinField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Buttons ---
        JButton loginBtn = new JButton("Log In");
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.addActionListener(e -> handleLogin());

        JButton createBtn = new JButton("Create Account");
        createBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        createBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        createBtn.addActionListener(e -> handleCreateAccount());

        JLabel footer = new JLabel("© 2025 First National Bank  •  FDIC Insured", SwingConstants.CENTER);
        footer.setFont(new Font("SansSerif", Font.PLAIN, 10));
        footer.setForeground(Color.LIGHT_GRAY);
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Assemble inner panel ---
        inner.add(bankIcon);
        inner.add(Box.createVerticalStrut(8));
        inner.add(titleLabel);
        inner.add(Box.createVerticalStrut(4));
        inner.add(subtitleLabel);
        inner.add(Box.createVerticalStrut(24));
        inner.add(nameLabel);
        inner.add(Box.createVerticalStrut(4));
        inner.add(nameField);
        inner.add(Box.createVerticalStrut(12));
        inner.add(pinLabel);
        inner.add(Box.createVerticalStrut(4));
        inner.add(pinField);
        inner.add(Box.createVerticalStrut(20));
        inner.add(loginBtn);
        inner.add(Box.createVerticalStrut(8));
        inner.add(createBtn);
        inner.add(Box.createVerticalStrut(20));
        inner.add(footer);

        root.add(inner);
        setVisible(true);
    }

    private void handleLogin() {
        String fullName = nameField.getText().trim();
        String pin      = new String(pinField.getPassword()).trim();

        if (fullName.isEmpty() || pin.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your name and PIN.", "Missing Info", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] parts = fullName.split(" ", 2);
        if (parts.length < 2) {
            JOptionPane.showMessageDialog(this, "Please enter your full name (first and last).", "Invalid Name", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] types = {"CHECKING", "SAVINGS", "ROTHIRA"};
        JComboBox<String> typeBox = new JComboBox<>(types);
        JOptionPane.showMessageDialog(this, typeBox, "Select Account Type", JOptionPane.PLAIN_MESSAGE);

        String type = (String) typeBox.getSelectedItem();
        User user   = new User(parts[0], parts[1], pin);
        Account acc = manager.getAccount(user, type);

        if (acc == null) {
            JOptionPane.showMessageDialog(this, "Account not found. Check your details.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            dispose();
            new Dashboardgui(acc, manager);
        }
    }

    private void handleCreateAccount() {
        String first = JOptionPane.showInputDialog(this, "Enter First Name:");
        if (first == null || first.trim().isEmpty()) return;

        String last = JOptionPane.showInputDialog(this, "Enter Last Name:");
        if (last == null || last.trim().isEmpty()) return;

        String pin = JOptionPane.showInputDialog(this, "Create a PIN:");
        if (pin == null || pin.trim().isEmpty()) return;

        String type = JOptionPane.showInputDialog(this, "Account Type (CHECKING / SAVINGS / ROTHIRA):");
        if (type == null || type.trim().isEmpty()) return;

        String depositInput = JOptionPane.showInputDialog(this, "Initial Deposit Amount:");
        if (depositInput == null || depositInput.trim().isEmpty()) return;

        double deposit;
        try {
            deposit = Double.parseDouble(depositInput);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid deposit amount.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = new User(first.trim(), last.trim(), pin.trim());
        boolean created = manager.createAccount(user, type.trim(), deposit);

        if (created) {
            Account acc = manager.getAccount(user, type.trim().toUpperCase());
            JOptionPane.showMessageDialog(this,
                "Account created!\nAccount ID: " + acc.getAccountID() +
                "\nBalance: $" + String.format("%.2f", acc.getBalance()),
                "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "An account of that type already exists for this user.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}