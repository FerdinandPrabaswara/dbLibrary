import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class LoginFrame extends JFrame implements ActionListener {
	Database db;
	Container container = getContentPane();
	JLabel userLabel = new JLabel("USERNAME");
	JLabel passwordLabel = new JLabel("PASSWORD");
	JTextField userTextField = new JTextField();
	JPasswordField passwordField = new JPasswordField();
	JButton loginButton = new JButton("LOGIN");
	JButton resetButton = new JButton("RESET");
	JCheckBox showPassword = new JCheckBox("Show Password");
	JLabel messageLabel = new JLabel();
	JButton exitButton = new JButton("EXIT");

	LoginFrame(Database db) {
		this.db = db;

		setLayoutManager();
		setLocationAndSize();
		addComponentsToContainer();
		addActionEvent();
		this.setTitle("Login Form");
		this.setBounds(10, 10, 600, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
	}

	public void setLayoutManager() {
		container.setLayout(null);
	}

	public void setLocationAndSize() {
		userLabel.setBounds(170, 150, 100, 30);
		passwordLabel.setBounds(170, 220, 100, 30);
		userTextField.setBounds(270, 150, 150, 30);
		passwordField.setBounds(270, 220, 150, 30);
		showPassword.setBounds(270, 250, 150, 30);
		loginButton.setBounds(170, 300, 100, 30);
		resetButton.setBounds(320, 300, 100, 30);
	}

	public void addComponentsToContainer() {
		container.add(userLabel);
		container.add(passwordLabel);
		container.add(userTextField);
		container.add(passwordField);
		container.add(showPassword);
		container.add(loginButton);
		container.add(resetButton);
	}

	public void addActionEvent() {
		loginButton.addActionListener(this);
		resetButton.addActionListener(this);
		showPassword.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loginButton) {
			String username = userTextField.getText();
			String password = new String(passwordField.getPassword());

			if (!authenticate(username, password)) {
				JOptionPane.showMessageDialog(this, "Invalid Username or Password");
			} else {
				JOptionPane.showMessageDialog(this, "Login Successful");
				HomePage homePage = new HomePage(db,username);
				homePage.setVisible(true);
				setVisible(false);
				dispose();
			}

		}

		if (e.getSource() == resetButton) {
			userTextField.setText("");
			passwordField.setText("");
		}

		if (e.getSource() == showPassword) {
			if (showPassword.isSelected()) {
				passwordField.setEchoChar((char) 0);
			} else {
				passwordField.setEchoChar('*');
			}

		}
	}

	private boolean authenticate(String username, String password) {
		try {
			PreparedStatement stmnt = db.conn.prepareStatement("CALL login(?, ?)");
			stmnt.setString(1, username);
			stmnt.setString(2, password);

			ResultSet result = stmnt.executeQuery();

			if (result.next())
				return true;
			return false;
		} catch (SQLException err) {
			return false;
		}
	}
}