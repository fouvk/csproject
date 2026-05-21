import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginWindow extends JFrame {
    private JTextField nameField;
    private JButton startButton;

    public LoginWindow() {
        setTitle("VOID INVASION");
        setSize(Game.width, Game.height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("Void Invasion", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, Game.width/10));
        add(titleLabel, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Monospaced", Font.BOLD, Game.width/20));
        add(nameLabel, gbc);

        gbc.gridx = 1;
        nameField = new JTextField(6);
        nameField.setFont(new Font("Monospaced", Font.BOLD, Game.width/20));
        add(nameField, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        gbc.gridwidth = 1;
        startButton = new JButton("Start");
        startButton.setFont(new Font("Monospaced", Font.BOLD, Game.width/20));
        add(startButton, gbc);

        ActionListener startAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        };

        startButton.addActionListener(startAction);
        nameField.addActionListener(startAction);

        setVisible(true);
    }

    private void startGame() {
        String name = nameField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "<html><body style='font-size:30px;'>Please enter a name.</body></html>", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (name.contains(":")) {
            JOptionPane.showMessageDialog(this, "<html><body style='font-size:30px;'>Invalid name, try again.</body></html>", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        dispose();
        new Game(name);
    }
}