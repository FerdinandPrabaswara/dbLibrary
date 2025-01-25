import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;


public class BukuInsert extends JFrame implements ActionListener {
     Database db;

    Container container = getContentPane();
    JLabel labelIntro = new JLabel("Please fill the data below: ");
    JLabel labelJudul = new JLabel("Judul");
    JTextField judulTextField = new JTextField();
    JLabel labelKode = new JLabel("Kode Eksemplar");
    JTextField textKode = new JTextField();
    JLabel labelPengarang = new JLabel("Pengarang");
    JTextField textPengarang = new JTextField();
    JLabel labelPenerbit = new JLabel("Penerbit");
    JTextField textPenerbit = new JTextField();
    JLabel labelTmptTerbit = new JLabel("Tempat Terbit");
    JTextField textTmptTerbit = new JTextField();
    JButton backButton = new JButton("Back");
    JButton addButton = new JButton("Add");
    String username;
    
    public BukuInsert(Database db,String username) {
        this.db = db;

        setLayoutManager();
	setLocationAndSize();
	addComponentsToContainer();
	addActionEvent();

        this.setTitle("Insert Page");
	this.setBounds(10, 10, 600, 600);
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	this.setResizable(false);
	this.setLocationRelativeTo(null);
        this.username = username;
        System.out.println(username);
    }

    private void addActionEvent() {
        backButton.addActionListener(this);
        addButton.addActionListener(this);
     }

    private void addComponentsToContainer() {
        container.add(labelIntro);
        container.add(labelJudul);
        container.add(judulTextField);
        container.add(labelKode);
        container.add(textKode);
        container.add(labelPengarang);
        container.add(textPengarang);
        container.add(labelPenerbit);
        container.add(textPenerbit);
        container.add(labelTmptTerbit);
        container.add(textTmptTerbit);
        container.add(backButton);
        container.add(addButton);
    }

    private void setLocationAndSize() {
        backButton.setBounds(60, 60, 80, 30);
        labelIntro.setBounds(60, 110, 300, 30);
        judulTextField.setBounds(200, 160, 300, 30);
        labelJudul.setBounds(60, 160, 80, 30);
        labelKode.setBounds(60, 210, 130, 30);
        textKode.setBounds(200, 210, 300, 30);
        labelPengarang.setBounds(60, 260, 80, 30);
        textPengarang.setBounds(200, 260, 300, 30);
        labelPenerbit.setBounds(60, 310, 80, 30);
        textPenerbit.setBounds(200, 310, 300, 30);
        labelTmptTerbit.setBounds(60, 360, 130, 30);
        textTmptTerbit.setBounds(200, 360, 300, 30);
        addButton.setBounds(420, 440, 80, 30);

    }

    private void setLayoutManager() {
        container.setLayout(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Connection conn = null;
        if (e.getSource() == backButton) {
            Buku buku = new Buku (db,username);
            buku.setVisible(true);
            setVisible(false);
            dispose();
		}

        if (e.getSource() == addButton) {
            String Judul = judulTextField.getText();
            String Kode = textKode.getText();
            String Pengarang = textPengarang.getText();
            String Penerbit = textPenerbit.getText();
            String tempatTerbit = textTmptTerbit.getText();
            String Action = "insert";
            String Admin = username;
                try {
                    conn = db.conn;
        
                    String query = "{ CALL ManageBuku(?, ?, ?, ?, ?, ?, ?)}";
                    CallableStatement cstmt = conn.prepareCall(query);
        
                    cstmt.setString(1, Kode);
                    cstmt.setString(2, Judul);
                    cstmt.setString(3, Pengarang);
                    cstmt.setString(4, Penerbit);
                    cstmt.setString(5, tempatTerbit);
                    cstmt.setString(6, Action);
                    cstmt.setString(7, Admin);
                    
                    ResultSet resultSet = cstmt.executeQuery();
                    if (resultSet.next()) {
                    int errNumber = resultSet.getInt("err_number");
                    String errText = resultSet.getString("err_text");
                    JOptionPane.showMessageDialog(this, errText);

                }
                        
                   cstmt.close();
        
                } catch (Exception ee) {
                        ee.printStackTrace();
                }
        }
    }
}
