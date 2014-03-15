package com.systemincloud.ext.vip.examples.webcamclient.java;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.systemincloud.sdk.java.SicClient;
import com.systemincloud.sdk.java.SicException;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.Box;

public class NewInstanceFrame extends JFrame {
    
    private static final long serialVersionUID = 1L;
    
    private JPanel mainPanel = new JPanel();
    
    private final Box               centralBox     = Box.createVerticalBox();
    private final JLabel            lblParameters  = new JLabel("Parameters:");
    private final DefaultTableModel paramsModel    = new DefaultTableModel(new Object[][] { }, new String[] { "Key", "Value" }) { 
        private static final long serialVersionUID = 1L;
        @Override public boolean isCellEditable(int row, int column) { return column == 0 ? false : true; }
    };
    private final JTable            tblParameters  = new JTable(paramsModel);
    private final JScrollPane       jparamsPanel   = new JScrollPane(tblParameters);
    private final JPanel            paramsPanel    = new JPanel();
    
    private final JPanel  buttonsPanel  = new JPanel();
    private final JButton btnCreate     = new JButton("Create");
    private final JButton btnCancel     = new JButton("Cancel");
    
    private SicClient sicClient;
    private NewInstanceFrameListener listener;
    private String machineId;
    
    public NewInstanceFrame(SicClient sicClient, NewInstanceFrameListener listener, String machineId) {
        this.setTitle("New Instance on " + machineId);
        this.sicClient = sicClient;
        this.listener = listener;
        this.machineId = machineId;
        setAlwaysOnTop(true);
        setSize(300, 200);
        
        initComponents();
        initButtons();
        initLayout();
        
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        setLocationRelativeTo(null);
    }

    private void initLayout() {
        centralBox.add(lblParameters);
        paramsPanel.add(jparamsPanel);
        centralBox.add(paramsPanel);
        
        buttonsPanel.add(btnCreate);
        buttonsPanel.add(btnCancel);
        
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(centralBox, BorderLayout.CENTER);
        
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
    }

    private void initComponents() {
        jparamsPanel.setMaximumSize(new Dimension(200, 100));
        jparamsPanel.setPreferredSize(new Dimension(200, 100));
        
        for(String parameter : this.sicClient.getModelInfo().getParameters())
            paramsModel.addRow(new String [] { parameter, ""});
    }

    private void initButtons() {
        btnCreate.addActionListener(new ActionListener() { 
            @Override public void actionPerformed(ActionEvent event) {
                if(allParametersFilled()) {
                    try {
    //                    sicClient.newInstance();
                    } catch(SicException e) {
                        JOptionPane.showMessageDialog(null, e.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
                    }
                    listener.instanceCreated();
                    NewInstanceFrame.this.dispose();
                } else JOptionPane.showMessageDialog(null, "Some parameters not filled", "Exception", JOptionPane.ERROR_MESSAGE);
            }
        });
        btnCancel.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent event) { NewInstanceFrame.this.dispose(); } });
    }
    
    private boolean allParametersFilled() {
        for(int i = 0; i < tblParameters.getRowCount(); i++)
            if("".equals(tblParameters.getValueAt(i, 1))) return false;
        return true;
    }
}
