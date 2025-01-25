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

public class TransaksiInsert extends JFrame implements ActionListener {
    Database db;

    Container container = getContentPane();
    JLabel labelID = new JLabel("ID Anggota");
    JLabel labelKode = new JLabel("Kode Eksemplar");
    JLabel labelJudul = new JLabel("Cari judul buku: ");
    JTextField textKode = new JTextField();
    JTextField textJudul = new JTextField();
    JTextField textID = new JTextField();
    JButton searchButton = new JButton("Search");
    JButton backButton = new JButton("Back");
    JButton addButton = new JButton("Add");
    JButton bookListButton = new JButton("Show all books");
    JLabel notes = new JLabel("*Tambahkan buku berdasarkan kode eksemplar");
    JButton saveButton = new JButton("Save");
    String username;
    JTable table;
    JScrollPane scrollPane;
    String ID;

    
    public TransaksiInsert(Database db,String username, String ID) {
        this.db = db;

        setLayoutManager();
		setLocationAndSize();
		addComponentsToContainer();
		addActionEvent();

        this.setTitle("Transaction Insert Page");
		this.setBounds(10, 10, 710, 700);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
        this.username = username;
        this.ID = ID;
    
    }

    private void addActionEvent() {
        backButton.addActionListener(this);
        addButton.addActionListener(this);
        searchButton.addActionListener(this);
        bookListButton.addActionListener(this);
        saveButton.addActionListener(this);
    }

    private void addComponentsToContainer() {
        container.add(labelKode);
        container.add(textKode);
        container.add(backButton);
        container.add(addButton);
        container.add(searchButton);
        container.add(labelJudul);
        container.add(textJudul);
        container.add(bookListButton);
        container.add(notes);
        container.add(saveButton);
        showTable();
    }

    private void setLocationAndSize() {
        backButton.setBounds(60, 60, 80, 30);
        labelKode.setBounds(60, 120, 130, 30);
        textKode.setBounds(60, 150, 250, 30);
        notes.setBounds(60, 170, 200, 30);
        labelJudul.setBounds(60, 220, 130, 30);
        textJudul.setBounds(60, 250, 250, 30);
        searchButton.setBounds(350, 250, 80, 30);
        saveButton.setBounds(560, 570, 80, 30);
        bookListButton.setBounds(510, 250, 130, 30);
        addButton.setBounds(350, 150, 80, 30);
        notes.setBounds(60, 180, 300, 30);
        notes.setFont(new Font("Arial", Font.PLAIN, 12));


    }

    private void setLayoutManager() {
        container.setLayout(null);
    }

    private void showTable() {
        Connection conn = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;

        try {
            conn = db.conn;

            String query = "{ CALL ShowBukuDetails() }";
            cstmt = conn.prepareCall(query);

            rs = cstmt.executeQuery();

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            String[] columnNames = new String[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    columnNames[i - 1] = rsmd.getColumnName(i);
                }

            DefaultTableModel model = new DefaultTableModel(columnNames, 0);
                while (rs.next()) {
                    Object[] rowData = new Object[columnCount];
                        for (int i = 1; i <= columnCount; i++) {
                            rowData[i - 1] = rs.getObject(i);
                        }
                    model.addRow(rowData);
                }

            table = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBounds(60, 300, 580, 250);
            container.add(scrollPane);

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (cstmt != null)
                    cstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Connection conn = null;
        if (e.getSource() == backButton) {
            Transaksi transaksi = new Transaksi(db,username);
            transaksi.setVisible(true);
            setVisible(false);
            dispose();
		}

        if (e.getSource() == searchButton) {
            String judul = textJudul.getText().trim();
            try {
                conn = db.conn;
                String query = "{ CALL findBook(?)}";
                CallableStatement cstmt = conn.prepareCall(query);

                cstmt.setString(1, judul);
                    
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
                scrollPane.setBounds(60, 300, 580, 250);
                container.add(scrollPane);

                rs.close();
                cstmt.close();

                    
            } catch (Exception ee) {
                ee.printStackTrace();
            }
		}

        if(e.getSource() == bookListButton){
            showTable();
        }

        if (e.getSource() == addButton) {
            String temp_kode = textKode.getText().trim();
            String admin = username;
            int index = 0;
            
            String kode = "{ \"operation\": \"insert\", \"id_anggota\": \"" + ID + "\", \"kode_eksemplar\": \"" + temp_kode + "\" }";
            // kode = kode.replace("225150700111025", ID);
            // kode = kode.replace("b3.15.002121", temp_kode);
            // System.out.println(kode);

            try{
                conn = db.conn;
                String query = "{ CALL ManageTransaksi(?,?,?)}";
                CallableStatement cstmt = conn.prepareCall(query);
                cstmt.setString(1,kode);
                cstmt.setInt(2,index);
                cstmt.setString(3,username);

                cstmt.executeUpdate();

                cstmt.close();

            } catch (Exception ee){
                ee.printStackTrace();

            }
        }

        
    }
    
}

// '[
//   {
//     "operation": "insert",
//     "id_anggota": "225150700111025",
//     "kode_eksemplar": "b3.15.002121"
//   }