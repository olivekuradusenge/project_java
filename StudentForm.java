package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentForm extends JFrame implements ActionListener {

    JLabel lblId, lblName, lblEmail;
    JTextField txtId, txtName, txtEmail;
    JButton btnSave, btnClear;

    Connection con;

    public StudentForm() {
        setTitle("Student Form");
        setSize(400, 250);
        setLayout(new GridLayout(4, 2, 10, 10));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        lblId = new JLabel("Student ID:");
        lblName = new JLabel("Name:");
        lblEmail = new JLabel("Email:");

        txtId = new JTextField();
        txtName = new JTextField();
        txtEmail = new JTextField();

        btnSave = new JButton("Save");
        btnClear = new JButton("Clear");

        btnSave.addActionListener(this);
        btnClear.addActionListener(this);

        add(lblId); add(txtId);
        add(lblName); add(txtName);
        add(lblEmail); add(txtEmail);
        add(btnSave); add(btnClear);

        connectDB();   // 🔥 CONNECT TO DATABASE

        setVisible(true);
    }

    private void connectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/studentdb",
                "root",
                ""   // change if your MySQL has password
            );

            JOptionPane.showMessageDialog(this, "Database Connected");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Database Connection Failed:\n" + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSave) {
            saveStudents();
        } else {
            txtId.setText("");
            txtName.setText("");
            txtEmail.setText("");
        }
    }

    private void saveStudents() {

        if (con == null) {
            JOptionPane.showMessageDialog(this, "No database connection");
            return;
        }

        String id = txtId.getText().trim();
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();

        if (id.isEmpty() || name.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fill all fields");
            return;
        }

        try {
            String sql =
                "INSERT INTO student_form (student_id, name, email) VALUES (?, ?, ?)";

            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, name);
            pst.setString(3, email);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Student Saved Successfully");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentForm::new);
    }
}
