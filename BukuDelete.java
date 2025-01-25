import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BukuDelete extends JFrame implements ActionListener{
        Database db;
    JLabel judul = new JLabel("Please fill the data below:");
    JLabel labelKode = new JLabel("Kode eksemplar");
    JTextField kodeTextField = new JTextField();
    JButton deleteButton = new JButton("Delete");
    Container container = getContentPane();
    JButton backButton = new JButton("Back");
    String username;
    
    public BukuDelete(Database db,String username) {
        this.db = db;
        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        addActionEvent();
        this.username = username;
        this.setTitle("Delete Page");
        this.setBounds(10, 10, 600, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
    }

    public void addActionEvent() {
        deleteButton.addActionListener(this);
        backButton.addActionListener(this);

    }

    public void setLayoutManager() {
        container.setLayout(null);
    }

    public void setLocationAndSize() {
        labelKode.setBounds(100, 120, 100, 30);
        kodeTextField.setBounds(220, 120, 270, 30);
        judul.setBounds(100, 60, 300, 30);
        deleteButton.setBounds(410, 190, 80, 30);
        backButton.setBounds(220, 190, 80, 30);

    }

    public void addComponentsToContainer() {
        container.add(labelKode);
        container.add(kodeTextField);
        container.add(judul);
        container.add(deleteButton);
        container.add(backButton);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Connection conn = null;
        if (e.getSource() == deleteButton) {
            String kode = kodeTextField.getText();
            try{
                 conn = db.conn;
        
                    String query = "{ CALL ManageBuku(?, ?, ?, ?, ?, ?, ?)}";
                    CallableStatement cstmt = conn.prepareCall(query);
        
                    cstmt.setString(1, kode);
                    cstmt.setString(2, null);
                    cstmt.setString(3, null);
                    cstmt.setString(4, null);
                    cstmt.setString(5, null);
                    cstmt.setString(6, "delete");
                    cstmt.setString(7, null);
                    
                     ResultSet resultSet = cstmt.executeQuery();
                    if (resultSet.next()) {
                    int errNumber = resultSet.getInt("err_number");
                    String errText = resultSet.getString("err_text");
                    JOptionPane.showMessageDialog(this, errText);

                    }
            } catch (Exception ee){
                ee.printStackTrace();
            }

                    
        }

        if (e.getSource() == backButton) {
            Buku buku = new Buku(db,username);
            buku.setVisible(true);
            setVisible(false);
            dispose();
        } 
    } 



}
