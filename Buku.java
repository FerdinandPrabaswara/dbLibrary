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

public class Buku extends JFrame implements ActionListener {
     Database db;

    Container container = getContentPane();
    JLabel JudulLabel = new JLabel("Judul: ");
    JTextField JudulTextField = new JTextField();
    JButton searchButton = new JButton("Search");
    JButton insertButton = new JButton("Add");
    JButton deleteButton = new JButton("Delete");
    JButton updateButton = new JButton("Update");
    JButton saveButton = new JButton("Save");
    JButton backButton = new JButton("Back");
    JButton bookListButton = new JButton("Show all books");  
    JTable table;
    JScrollPane scrollPane;
    String username;    


    public Buku(Database db, String username) {
        this.db = db;

        setLayoutManager();
		setLocationAndSize();
		addComponentsToContainer();
		addActionEvent();

        this.setTitle("Buku Page");
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
        updateButton.addActionListener(this);
        saveButton.addActionListener(this);
        backButton.addActionListener(this);
        bookListButton.addActionListener(this);
    }

    private void addComponentsToContainer() {
        container.add(JudulLabel);
        container.add(JudulTextField);
        container.add(searchButton);
        container.add(deleteButton);
        container.add(insertButton);
        container.add(updateButton);
        container.add(backButton);
        container.add(saveButton);
        container.add(bookListButton);
        showTable();
    }

    private void setLocationAndSize() {
        backButton.setBounds(50, 80, 80, 30);
        JudulLabel.setBounds(50, 150, 40, 30);
        JudulTextField.setBounds(100, 150, 250, 30);
        searchButton.setBounds(370, 150, 80, 30);
        bookListButton.setBounds(500, 150, 130, 30 );
        insertButton.setBounds(50, 580, 80, 30);
        deleteButton.setBounds(160, 580, 80, 30);
        updateButton.setBounds(270, 580, 80, 30);
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
            scrollPane.setBounds(50, 210, 580, 340);
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
            String judul = JudulTextField.getText().trim();
            try {
                Connection conn = null;
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
                scrollPane.setBounds(50, 210, 580, 340);
                container.add(scrollPane);

                rs.close();
                cstmt.close();

                    
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }

        if (e.getSource() == insertButton) {
            BukuInsert insert = new BukuInsert (db,username);
            insert.setVisible(true);
            setVisible(false);
            dispose();
		}

        if (e.getSource() == deleteButton) {
            BukuDelete delete = new BukuDelete (db,username);
            delete.setVisible(true);
            setVisible(false);
            dispose();
		}

        if (e.getSource() == updateButton) {
            BukuUpdate update = new BukuUpdate (db,username);
            update.setVisible(true);
            setVisible(false);
            dispose();
		}
    }
    
}
