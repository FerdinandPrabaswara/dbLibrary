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

public class AnggotaUpdate extends JFrame implements ActionListener {
   Database db;
   String username;
        JLabel labelName = new JLabel("Nama");
        JLabel judul = new JLabel("Silakan Masukkan data di bawah ini:");
        JLabel labelNIM = new JLabel("NIM");
        JLabel labelSM = new JLabel("Seleksi Masuk");
        JLabel labelPS = new JLabel("Program Studi");
        String[] prodi = { "Teknik Komputer", "Teknologi Informasi", "Pendidikan Teknologi Informasi",
                        "Teknik Komputer", "Sistem Informasi", "Ilmu Komputer" };
        String[] seleksi = { "Seleksi Nasional Berdasarkan Tes", "Seleksi Nasional Berdasarkan Prestasi",
                        "Seleksi Mandiri", "Seleksi Kelas Regular I", };
        JComboBox prodiBox = new JComboBox(prodi);
        JComboBox seleksiBox = new JComboBox(seleksi);
        
        JTextField nameTextField = new JTextField();
        JTextField nimTextField = new JTextField();
        JButton updateButton = new JButton("Update");
        Container container = getContentPane();
        JButton backButton = new JButton("Back");
        JLabel notes = new JLabel("*Masukkan NIM lama");

        public AnggotaUpdate (Database db,String username) {
        this.db = db;
        this.username = username;
                setLayoutManager();
                setLocationAndSize();
                addComponentsToContainer();
                addActionEvent();

                this.setTitle("Update Page");
                this.setBounds(10, 10, 600, 600);
                this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                this.setResizable(false);
                this.setLocationRelativeTo(null);
        }

        public void setLayoutManager() {
                container.setLayout(null);
        }

        public void setLocationAndSize() {
                labelName.setBounds(120, 110, 100, 30);
                labelNIM.setBounds(120, 180, 100, 30);
                nameTextField.setBounds(220, 110, 270, 30);
                nimTextField.setBounds(220, 180, 270, 30);
                prodiBox.setBounds(220, 320, 270, 30);
                labelSM.setBounds(120, 250, 100, 30);
                labelPS.setBounds(120, 320, 100, 30);
                seleksiBox.setBounds(220, 250, 270, 30);
                updateButton.setBounds(350,400,100,30);
                judul.setBounds(120, 60,300,30);
                backButton.setBounds(160,400,100,30);
                notes.setBounds(220, 210, 270, 30);
                notes.setFont(new Font("Arial", Font.PLAIN, 12));

        }

        public void addComponentsToContainer() {
                container.add(labelName);
                container.add(labelNIM);
                container.add(nameTextField);
                container.add(nimTextField);
                container.add(prodiBox);
                container.add(labelSM);
                container.add(labelPS);
                container.add(seleksiBox);
                container.add(updateButton);
                container.add(judul);
                container.add(backButton);
                container.add(notes);
        }
        public void addActionEvent() {
	        updateButton.addActionListener(this);
                backButton.addActionListener(this);
	}

        @Override
        public void actionPerformed(ActionEvent e) {
                Connection conn = null;

                if (e.getSource() == updateButton) {
                   String nama = nameTextField.getText();
                   String nim = nimTextField.getText();
                   String seleksiMasuk = (String) seleksiBox.getSelectedItem();
                   seleksiMasuk = seleksiMasuk.trim();
                   String programStudi = (String) prodiBox.getSelectedItem();
                   programStudi = programStudi.trim();
                   String Admin = username;
                   try {
                        conn = db.conn;

                        String query = "{ CALL updateAnggota(?, ?, ?, ?,?)}";
                        CallableStatement cstmt = conn.prepareCall(query);

                        cstmt.setString(1, nama);
                        cstmt.setString(2, nim);
                        cstmt.setString(3, programStudi);
                        cstmt.setString(4, seleksiMasuk);
                        cstmt.setString(5, Admin);
                   
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
