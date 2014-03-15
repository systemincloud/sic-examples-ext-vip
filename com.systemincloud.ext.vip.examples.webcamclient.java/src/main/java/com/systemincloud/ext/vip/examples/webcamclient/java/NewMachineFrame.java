package com.systemincloud.ext.vip.examples.webcamclient.java;

import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;

import com.systemincloud.sdk.java.MachineType;
import com.systemincloud.sdk.java.Provider;
import com.systemincloud.sdk.java.Region;
import com.systemincloud.sdk.java.SicClient;
import com.systemincloud.sdk.java.SicException;
import com.systemincloud.sdk.java.msg.MachineInfo;

public class NewMachineFrame extends JFrame {
    
    private static final long serialVersionUID = 1L;
    
    private JPanel mainPanel = new JPanel();
    
    private final JPanel            comboPanel    = new JPanel();
    private final JLabel            lblProvider   = new JLabel("Provider");
    private final JComboBox<String> comboProvider = new JComboBox<>();
    private final JLabel            lblRegion     = new JLabel("Region");
    private final JComboBox<String> comboRegion   = new JComboBox<>();
    private final JLabel            lblType       = new JLabel("Type");
    private final JComboBox<String> comboType     = new JComboBox<>();
    
    private final JPanel            buttonsPanel  = new JPanel();
    private final JButton           btnCreate     = new JButton("Create");
    private final JButton           btnCancel     = new JButton("Cancel");
    
    private SicClient sicClient;
    private NewMachineFrameListener listener;
    
    public NewMachineFrame(SicClient sicClient, NewMachineFrameListener listener) {
        this.setTitle("New Machine");
        this.sicClient = sicClient;
        this.listener = listener;
        setAlwaysOnTop(true);
        setSize(700, 100);
        
        initLayout();
        initComponents();
        initButtons();
        
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        setLocationRelativeTo(null);
    }

    private void initLayout() {
        comboPanel.add(lblProvider);
        comboPanel.add(comboProvider);
        comboPanel.add(lblRegion);
        comboPanel.add(comboRegion);
        comboPanel.add(lblType);
        comboPanel.add(comboType);
        
        buttonsPanel.add(btnCreate);
        buttonsPanel.add(btnCancel);
        
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(comboPanel, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
    }
    
    private void initComponents() {
        for(Provider p : Provider.values()) comboProvider.addItem(p.getFullName());
        comboProvider.addItemListener(new ItemListener() {
            @Override public void itemStateChanged(ItemEvent event) {
                if(event.getStateChange() == ItemEvent.SELECTED) {
                    fillRegions(Provider.getByFullName((String) event.getItem()));
                }
            }
        });
        fillRegions(Provider.getByFullName((String) comboProvider.getSelectedItem()));
        comboRegion.addItemListener(new ItemListener() {
            @Override public void itemStateChanged(ItemEvent event) {
                if(event.getStateChange() == ItemEvent.SELECTED) {
                    fillMachineTypes(Region.getByName((String) event.getItem()));
                }
            }
        });
        fillMachineTypes(Region.getByName((String) comboRegion.getSelectedItem()));
    }

    private void fillRegions(Provider provider) {
        comboRegion.removeAllItems();
        if(Provider.DEFAULT.equals(provider)) comboRegion.addItem(Region.DEFAULT.getName());
        else for(Region r : Region.getForProvider(provider)) comboRegion.addItem(r.getName());
    }
    
    private void fillMachineTypes(Region region) {
        comboType.removeAllItems();
        if(Region.DEFAULT.equals(region)) comboType.addItem(MachineType.DEFAULT.getName());
        else for(MachineType mt : MachineType.getForRegion(region)) comboType.addItem(mt.getName());
    }
    
    private void initButtons() {
        btnCreate.addActionListener(new ActionListener() { 
            @Override public void actionPerformed(ActionEvent event) {
                MachineInfo mi = null;
                try {
                    mi = sicClient.newMachine((String) comboRegion.getSelectedItem(), (String) comboType.getSelectedItem());
                } catch(SicException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                listener.machineCreated(mi);
                NewMachineFrame.this.dispose();
            }
        });
        btnCancel.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent event) { NewMachineFrame.this.dispose(); } });
    }
}
