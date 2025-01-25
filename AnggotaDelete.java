import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;


public class AnggotaDelete extends JFrame implements ActionListener {
     Database db;
    JLabel judul = new JLabel("Silakan Masukkan NIM di bawah ini:");
    JLabel labelNIM = new JLabel("NIM");
    JTextField nimTextField = new JTextField();
    JButton deleteButton = new JButton("Delete");
    Container container = getContentPane();
    JButton backButton = new JButton("Back");
    String username;
    
    public AnggotaDelete(Database db,String username) {
        this.db = db;
        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        addActionEvent();

        this.setTitle("Delete Page");
        this.setBounds(10, 10, 600, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.username = username;
    }
    public void addActionEvent() {
        deleteButton.addActionListener(this);
        backButton.addActionListener(this);

    }
    public void setLayoutManager() {
        container.setLayout(null);
    }

    public void setLocationAndSize() {
        labelNIM.setBounds(120, 120, 100, 30);
        nimTextField.setBounds(220, 120, 270, 30);
        judul.setBounds(120, 60, 300, 30);
        deleteButton.setBounds(410, 190, 80, 30);
        backButton.setBounds(220, 190, 80, 30);


    }

    public void addComponentsToContainer() {
        container.add(labelNIM);
        container.add(nimTextField);
        container.add(judul);
        container.add(deleteButton);
        container.add(backButton);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Connection conn = null;

                if (e.getSource() == deleteButton) {
                   String nim = nimTextField.getText();
                   
                   try {
                        conn = db.conn;

                        String query = "{ CALL delAnggota(?)}";
                        CallableStatement cstmt = conn.prepareCall(query);

                        cstmt.setString(1, nim);
                        
                        
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

                if (e.getSource() == backButton) {
                        Anggota anggota = new Anggota(db,username);
                        anggota.setVisible(true);
                        setVisible(false);
                        dispose();
                }
       
        }    
    

}