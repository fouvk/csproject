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
        setLayout(new GridBagLayout()); //uses gridbaglayout for centering and aligning different components in a dynamic grid

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8); //padding around components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        //wide title label
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("Void Invasion", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, Game.width/10));
        add(titleLabel, gbc);

        //left name label
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Monospaced", Font.BOLD, Game.width/20));
        add(nameLabel, gbc);

        //right input field
        gbc.gridx = 1;
        nameField = new JTextField(6);
        nameField.setFont(new Font("Monospaced", Font.BOLD, Game.width/20));
        add(nameField, gbc);

        //right start button
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
        nameField.addActionListener(startAction); //so that pressing enter starts the game too

        setVisible(true);
    }

    private void startGame() {
        String name = nameField.getText().trim(); //removes leading/trailing spaces

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "<html><body style='font-size:30px;'>Please enter a name.</body></html>", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (name.contains(":")) { //cant have colon because i use it to seperate the name and score
            JOptionPane.showMessageDialog(this, "<html><body style='font-size:30px;'>Invalid name, try again.</body></html>", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        //close the login window and starts the game
        dispose();
        new Game(name);
    }
}