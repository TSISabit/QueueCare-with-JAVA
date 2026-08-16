package ui;

import service.DoctorApprovalService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DoctorApprovalFrame extends JFrame {
    private DoctorApprovalService approvalService;
    private JTable doctorTable;
    private DefaultTableModel tableModel;

    public DoctorApprovalFrame() {
        approvalService = new DoctorApprovalService();

        setTitle("QueueCare - Doctor Approval");
        setSize(750, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        createUI();
        loadRequests();

        setVisible(true);
    }

    private void createUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        JLabel titleLabel = new JLabel("Doctor Approval Requests");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        String[] columns = {
                "Doctor ID",
                "Name",
                "Email",
                "Status"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        doctorTable = new JTable(tableModel);
        doctorTable.setRowHeight(25);

        mainPanel.add(
                new JScrollPane(doctorTable),
                BorderLayout.CENTER
        );

        JPanel buttonPanel = new JPanel();

        JButton approveButton = new JButton("Approve");
        JButton rejectButton = new JButton("Reject");
        JButton refreshButton = new JButton("Refresh");

        buttonPanel.add(approveButton);
        buttonPanel.add(rejectButton);
        buttonPanel.add(refreshButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        approveButton.addActionListener(
                e -> updateStatus("APPROVED")
        );

        rejectButton.addActionListener(
                e -> updateStatus("REJECTED")
        );

        refreshButton.addActionListener(
                e -> loadRequests()
        );

        add(mainPanel);
    }

    private void loadRequests() {
        tableModel.setRowCount(0);

        List<String[]> requests =
                approvalService.getAllRequests();

        for (String[] request : requests) {
            tableModel.addRow(request);
        }
    }

    private void updateStatus(String status) {
        int selectedRow = doctorTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a doctor first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String doctorId =
                tableModel.getValueAt(selectedRow, 0).toString();

        String currentStatus =
                tableModel.getValueAt(selectedRow, 3).toString();

        if (!currentStatus.equalsIgnoreCase("PENDING")) {
            JOptionPane.showMessageDialog(
                    this,
                    "This request has already been processed.",
                    "Already Processed",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        boolean updated =
                approvalService.updateStatus(
                        doctorId,
                        status
                );

        if (updated) {
            JOptionPane.showMessageDialog(
                    this,
                    "Doctor request " + status.toLowerCase() + "."
            );

            loadRequests();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not update doctor request.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}