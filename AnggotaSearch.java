import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class AnggotaSearch extends JFrame implements ActionListener {
    Database db;
    JLabel judul = new JLabel("Silakan Masukkan Nama di bawah ini:");
    JLabel labelNama = new JLabel("Nama");
    JTextField namaTextField = new JTextField();
    JButton searchButton = new JButton("Search");
    Container container = getContentPane();
    JButton backButton = new JButton("Back");
    JTable table;
    JScrollPane scrollPane;
    String username;

    public AnggotaSearch(Database db,String username) {
        this.db = db;
        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        addActionEvent();

        this.setTitle("Search Page");
        this.setBounds(10, 10, 600, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.username = username;
    }
    public void addActionEvent() {
        searchButton.addActionListener(this);
        backButton.addActionListener(this);

    }
    public void setLayoutManager() {
        container.setLayout(null);
    }

    public void setLocationAndSize() {
        labelNama.setBounds(70, 120, 100, 30);
        namaTextField.setBounds(150, 120, 250, 30);
        judul.setBounds(70, 60, 300, 30);
        searchButton.setBounds(420, 120, 80, 30);
        backButton.setBounds(70, 450, 80, 30);


    }

    public void addComponentsToContainer() {
        container.add(labelNama);
        container.add(namaTextField);
        container.add(judul);
        container.add(searchButton);
        container.add(backButton);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Connection conn = null;

                if (e.getSource() == searchButton) {
                   String nama = namaTextField.getText();
                   
                   try {
                        conn = db.conn;

                        String query = "{ CALL findName(?)}";
                        CallableStatement cstmt = conn.prepareCall(query);

                        cstmt.setString(1, nama);
                        
                        ResultSet rs = cstmt.executeQuery();

                        ResultSetMetaData rsmd = rs.getMetaData();
                        int columnCount = rsmd.getColumnCount();
                        DefaultTableModel model = new DefaultTableModel();
                      
                        for (int i = 1; i <= columnCount; i++) {
                            model.addColumn(rsmd.getColumnName(i));
                        }

                        while (rs.next()) {
                            Object[] row = new Object[columnCount];
                            for (int i = 1; i <= columnCount; i++) {
                                row[i - 1] = rs.getObject(i);
                            }
                            model.addRow(row);
                        }
                        table = new JTable(model);
                        scrollPane = new JScrollPane(table);
                        scrollPane.setPreferredSize(new Dimension(500, 200));
                        scrollPane.setBounds(50, 180, 500, 230);
                        container.add(scrollPane);

                        rs.close();
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