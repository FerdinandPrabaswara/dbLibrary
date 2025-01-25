import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HomePage extends JFrame implements ActionListener  {
      Database db;
      String username;
        Container container = getContentPane();
	JButton anggotaButton = new JButton("Member list");
    JButton bukuButton = new JButton("Book list");
    JButton transaksiButton = new JButton("Transaction list");
	JButton exitButton = new JButton("EXIT");

    HomePage(Database db , String username) {
		this.db = db;

		setLayoutManager();
		setLocationAndSize();
		addComponentsToContainer();
		addActionEvent();
                this.username = username;
               
		this.setTitle("Home Form");
		this.setBounds(10, 10, 600, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
	}

    private void setLayoutManager() {
        container.setLayout(null);
    }

    public void setLocationAndSize() {
		anggotaButton.setBounds(190, 150, 220, 30);
		bukuButton.setBounds(190, 220, 220, 30);
		transaksiButton.setBounds(190, 290, 220, 30);
	}

    public void addComponentsToContainer() {
        container.add(anggotaButton);
        container.add(bukuButton);
        container.add(transaksiButton);
	}

    public void addActionEvent() {
		anggotaButton.addActionListener(this);
        bukuButton.addActionListener(this);
        transaksiButton.addActionListener(this);
	}

    @Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == anggotaButton) {
            Anggota anggota = new Anggota(db,username);
            anggota.setVisible(true);
            setVisible(false);
            dispose();
		}

        if (e.getSource() == bukuButton) {
            Buku buku = new Buku(db,username);
            buku.setVisible(true);
            setVisible(false);
            dispose();
            
        }

        if (e.getSource() == transaksiButton) {
            Transaksi transaksi = new Transaksi(db,username);
            transaksi.setVisible(true);
            setVisible(false);
            dispose();
        }
	}  

  
}
