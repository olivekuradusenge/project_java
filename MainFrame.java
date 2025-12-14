package ui;

import model.student;
import service.StudentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.regex.Pattern;

public class MainFrame extends JFrame {

    private JTextField txtFirstName, txtLastName, txtEmail, txtPhone;
    private JTable table;
    private DefaultTableModel tableModel;
    private StudentService service = new StudentService();
    private int selectedId = -1;

    public MainFrame() {
        setTitle("Student Management App");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(5,2,5,5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Student Info"));

        inputPanel.add(new JLabel("First Name:"));
        txtFirstName = new JTextField();
        inputPanel.add(txtFirstName);

        inputPanel.add(new JLabel("Last Name:"));
        txtLastName = new JTextField();
        inputPanel.add(txtLastName);

        inputPanel.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        inputPanel.add(txtEmail);

        inputPanel.add(new JLabel("Phone:"));
        txtPhone = new JTextField();
        inputPanel.add(txtPhone);

        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);

        inputPanel.add(btnPanel);

        add(inputPanel, BorderLayout.NORTH);

        // Table Panel
        tableModel = new DefaultTableModel(new Object[]{"ID","First Name","Last Name","Email","Phone"},0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Load initial data
        loadTable();

        // Button actions
        btnAdd.addActionListener(e -> addStudent());
        btnUpdate.addActionListener(e -> updateStudent());
        btnDelete.addActionListener(e -> deleteStudent());
        btnClear.addActionListener(e -> clearFields());

        // Table row click
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                selectedId = Integer.parseInt(tableModel.getValueAt(row,0).toString());
                txtFirstName.setText(tableModel.getValueAt(row,1).toString());
                txtLastName.setText(tableModel.getValueAt(row,2).toString());
                txtEmail.setText(tableModel.getValueAt(row,3).toString());
                txtPhone.setText(tableModel.getValueAt(row,4).toString());
            }
        });
    }

    // Load students into table
    private void loadTable() {
        tableModel.setRowCount(0);
        List<student> list = service.getAllStudents();
        for(student s : list) {
            tableModel.addRow(new Object[]{s.getId(), s.getFirstName(), s.getLastName(), s.getEmail(), s.getPhone()});
        }
    }

    // Validation
    private boolean validateFields() {
        if(txtFirstName.getText().isEmpty() || txtLastName.getText().isEmpty()
                || txtEmail.getText().isEmpty() || txtPhone.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this,"Please fill all fields!");
            return false;
        }
        if(!Pattern.matches("^[A-Za-z]{2,30}$", txtFirstName.getText())) {
            JOptionPane.showMessageDialog(this,"Invalid First Name!");
            return false;
        }
        if(!Pattern.matches("^[A-Za-z]{2,30}$", txtLastName.getText())) {
            JOptionPane.showMessageDialog(this,"Invalid Last Name!");
            return false;
        }
        if(!Pattern.matches("^(.+)@(.+)$", txtEmail.getText())) {
            JOptionPane.showMessageDialog(this,"Invalid Email!");
            return false;
        }
        if(!Pattern.matches("^\\d{10,15}$", txtPhone.getText())) {
            JOptionPane.showMessageDialog(this,"Invalid Phone!");
            return false;
        }
        return true;
    }

    // CRUD Methods
    private void addStudent() {
        if(!validateFields()) return;
        student s = new student(0, txtFirstName.getText(), txtLastName.getText(), txtEmail.getText(), txtPhone.getText());
        service.addStudent(s);
        loadTable();
        clearFields();
    }

    private void updateStudent() {
        if(selectedId == -1) { JOptionPane.showMessageDialog(this,"Select a student first!"); return; }
        if(!validateFields()) return;
        student s = new student(selectedId, txtFirstName.getText(), txtLastName.getText(), txtEmail.getText(), txtPhone.getText());
        service.updateStudent(s);
        loadTable();
        clearFields();
    }

    private void deleteStudent() {
        if(selectedId == -1) { JOptionPane.showMessageDialog(this,"Select a student first!"); return; }
        service.deleteStudent(selectedId);
        loadTable();
        clearFields();
    }

    private void clearFields() {
        txtFirstName.setText("");
        txtLastName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        selectedId = -1;
    }

    // Main method to run GUI
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
