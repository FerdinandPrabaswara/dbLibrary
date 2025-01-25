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




public class Transaksi extends JFrame implements ActionListener {

    TransaksiInsert tinsert;

    Database db;
    String username;
    Container container = getContentPane();
    JLabel IDLabel = new JLabel("ID Anggota: ");
    JTextField IDTextField = new JTextField();
    JLabel NamaLabel = new JLabel("Nama: ");
    JTextField NamaTextField = new JTextField();
    JButton searchButton = new JButton("Search");
    JButton insertButton = new JButton("Add");
    JButton deleteButton = new JButton("Delete");
    JButton saveButton = new JButton("Save");
    JButton backButton = new JButton("Back");
    JButton bookListButton = new JButton("Show all books");  
    JTable table;
    JScrollPane scrollPane;
    String ID;


    public Transaksi(Database db,String username) {
        this.db = db;

        setLayoutManager();
		setLocationAndSize();
		addComponentsToContainer();
		addActionEvent();

        this.setTitle("Transaction Page");
		this.setBounds(10, 10, 700, 700);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
        this.username = username;
    }

    private void addActionEvent() {
        searchButton.addActionListener(this);
        insertButton.addActionListener(this);
        deleteButton.addActionListener(this);
        saveButton.addActionListener(this);
        backButton.addActionListener(this);
        bookListButton.addActionListener(this);
    }

    private void addComponentsToContainer() {
        container.add(IDLabel);
        container.add(IDTextField);
        container.add(searchButton);
        container.add(deleteButton);
        container.add(insertButton);
        container.add(backButton);
        container.add(saveButton);
        container.add(bookListButton);
        container.add(NamaLabel);
        container.add(NamaTextField);
        showTable();
    }

    private void setLocationAndSize() {
        backButton.setBounds(50, 40, 80, 30);
        IDLabel.setBounds(50, 80, 100, 30);
        IDTextField.setBounds(50, 110, 250, 30);
        NamaLabel.setBounds(50, 150, 100, 30);
        NamaTextField.setBounds(50, 180, 350,30);
        searchButton.setBounds(320, 110, 80, 30);
        bookListButton.setBounds(500, 110, 130, 30 );
        insertButton.setBounds(50, 580, 80, 30);
        deleteButton.setBounds(160, 580, 80, 30);
        saveButton.setBounds(550, 580, 80, 30);
        

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

            String query = "{ CALL ShowBukuDetails1() }";
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
            scrollPane.setBounds(50, 240, 580, 300);
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

    private String getNameByIDAnggota(String ID) {
        String name = "";
        try {
            Connection conn = db.conn;
            String query = "SELECT Nama FROM ANGGOTA WHERE ID_Anggota = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, ID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                name = rs.getString("Nama");
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }

    public void saveBooksJsonTooDb(String booksJson){

    }
    

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            HomePage home = new HomePage (db,username);
            home.setVisible(true);
            setVisible(false);
            dispose();
		}

        if (e.getSource() == bookListButton) {
            showTable();
        }

        if (e.getSource() == searchButton) {
            ID = IDTextField.getText().trim();
            try {
                Connection conn = null;
                conn = db.conn;

                String query = "{ CALL findTransactionbyAnggota(?)}";
                CallableStatement cstmt = conn.prepareCall(query);

                cstmt.setString(1, ID);
                    
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
                scrollPane.setBounds(50, 240, 580, 300);
                container.add(scrollPane);

                rs.close();
                cstmt.close();

                String name = getNameByIDAnggota(ID);
                NamaTextField.setText(name);

                    
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }

        if (e.getSource() == insertButton) {
            TransaksiInsert tinsert = new TransaksiInsert(db,username,ID);
            tinsert.setVisible(true);
            setVisible(false);
            dispose();

		}

        if (e.getSource() == deleteButton) {
           TransaksiDelete tdelete = new TransaksiDelete(db,username,ID);
            tdelete.setVisible(true);
            setVisible(false);
            dispose();
		}

        if (e.getSource() == saveButton) {
    
        }
       
    }
    
}
