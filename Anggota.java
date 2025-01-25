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

public class Anggota extends JFrame implements ActionListener{
     Database db;
     String username;
	Container container = getContentPane();
    JLabel judulLabel = new JLabel("Daftar Anggota Ruang Baca FILKOM:");
    JButton searchButton = new JButton("Search");
	JButton insertButton = new JButton("Insert");
    JButton deleteButton = new JButton("Delete");
    JButton updateButton = new JButton("Update");
	JButton backButton = new JButton("Back");
    JTable table;
    

    Anggota(Database db , String username) {
		this.db = db;

		setLayoutManager();
		setLocationAndSize();
		addComponentsToContainer();
		addActionEvent();

		this.setTitle("Anggota Form");
		this.setBounds(10, 10, 700, 700);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
        this.username = username;
	}

    public void addActionEvent() {
        searchButton.addActionListener(this);
        insertButton.addActionListener(this);
        deleteButton.addActionListener(this);
        updateButton.addActionListener(this);
        backButton.addActionListener(this);
    }

    public void addComponentsToContainer() {
        container.add(searchButton);
        container.add(insertButton);
        container.add(deleteButton);
        container.add(updateButton); 
        container.add(judulLabel); 
        container.add(backButton); 
        showTable();     
    }

    public void setLocationAndSize() {
        backButton.setBounds(50, 40, 80, 30);
        searchButton.setBounds(70, 550, 100, 30);
		insertButton.setBounds(220, 550, 100, 30);
		deleteButton.setBounds(370, 550, 100, 30);
		updateButton.setBounds(520, 550, 100, 30);
        judulLabel.setBounds(50, 80, 300, 30);
    }

    public void setLayoutManager() {
        container.setLayout(null);
    }

    private void showTable() {
        Connection conn = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;

        try {
            // Connect to the database
            conn = db.conn;

            // Prepare the stored procedure call
            String query = "{ CALL listAnggota() }";
            cstmt = conn.prepareCall(query);

            // Execute the stored procedure
            rs = cstmt.executeQuery();

            // Create a table model with column names and data
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
            scrollPane.setBounds(50, 120, 600, 400);
            container.add(scrollPane);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the resources
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
      if (e.getSource() == insertButton) {
            AnggotaInsert insert = new AnggotaInsert (db,username);
            insert.setVisible(true);
            setVisible(false);
            dispose();
    }
     if (e.getSource() == updateButton) {
            AnggotaUpdate update = new AnggotaUpdate(db,username);
            update.setVisible(true);
            setVisible(false);
            dispose();
     }

     if (e.getSource() == deleteButton) {
        AnggotaDelete delete = new AnggotaDelete(db,username);
        delete.setVisible(true);
        setVisible(false);
        dispose();
     }
      if (e.getSource() == searchButton) {
        AnggotaSearch search = new AnggotaSearch(db,username);
        search.setVisible(true);
        setVisible(false);
        dispose();
     }
      if (e.getSource() == backButton) {
        HomePage home = new HomePage(db,username);
        home.setVisible(true);
        setVisible(false);
        dispose();
     }


    }


  

}