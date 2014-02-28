package com.systemincloud.ext.vip.examples.webcamclient.java;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.systemincloud.sdk.java.SicClient;
import com.systemincloud.sdk.java.SicException;

public class NewInstanceFrame extends JFrame {
    
    private static final long serialVersionUID = 1L;
    
    private JPanel mainPanel = new JPanel();
    
    private final JPanel            buttonsPanel  = new JPanel();
    private final JButton           btnCreate     = new JButton("Create");
    private final JButton           btnCancel     = new JButton("Cancel");
    
    private SicClient sicClient;
    private NewInstanceFrameListener listener;
    
    public NewInstanceFrame(SicClient sicClient, NewInstanceFrameListener listener) {
        this.sicClient = sicClient;
        this.listener = listener;
        setAlwaysOnTop(true);
        setSize(700, 100);
        
        initLayout();
        initComponents();
        initButtons();
    }

    private void initLayout() {
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
    }

    private void initComponents() {
        // TODO Auto-generated method stub
        
    }

    private void initButtons() {
        btnCreate.addActionListener(new ActionListener() { 
            @Override public void actionPerformed(ActionEvent event) {
                try {
//                    sicClient.newInstance();
                } catch(SicException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
                }
                listener.instanceCreated();
                NewInstanceFrame.this.dispose();
            }
        });
        btnCancel.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent event) { NewInstanceFrame.this.dispose(); } });
    }
}
