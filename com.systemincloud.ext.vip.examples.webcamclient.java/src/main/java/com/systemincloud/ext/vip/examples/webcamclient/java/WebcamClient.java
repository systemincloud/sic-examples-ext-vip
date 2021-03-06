package com.systemincloud.ext.vip.examples.webcamclient.java;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import com.github.sarxos.webcam.Webcam;
import com.systemincloud.sdk.java.SicClient;
import com.systemincloud.sdk.java.SicClientFactory;
import com.systemincloud.sdk.java.SicException;
import com.systemincloud.sdk.java.SicListener;
import com.systemincloud.sdk.java.msg.InstanceInfo;
import com.systemincloud.sdk.java.msg.MachineInfo;

public class WebcamClient implements ActionListener, SicListener, NewMachineFrameListener,
                                                                  NewInstanceFrameListener {

    private Webcam webcam = Webcam.getDefault();
    
    private JFrame window = new JFrame("System in Cloud - Webcam Client");
    
    private final JPanel mainPanel = new JPanel();
    
    private final JPanel videoPanels  = new JPanel();
    
    private JPanel videoIn;
    private JPanel videoOut;
    
    private final JPanel            controlPanel        = new JPanel();
   
    private final JPanel            credentials         = new JPanel();
    private final Box               credentialsBox      = Box.createVerticalBox();
    
    private final Box               accountNumberBox    = Box.createHorizontalBox();
    private final JLabel            lblAccountNumber    = new JLabel("Account Number: ");
    private final JTextField        textAccountNumber   = new JTextField();
    
    private final Box               systemNameBox       = Box.createHorizontalBox();
    private final JLabel            lblSystemName       = new JLabel("System Name: ");
    private final JTextField        textSystemName      = new JTextField();
    
    private final Box               systemKeyBox        = Box.createHorizontalBox();
    private final JLabel            lblSystemKey        = new JLabel("System Key: ");
    private final JTextField        textSystemKey       = new JTextField();
    
    private final Box               reconnectionBox     = Box.createHorizontalBox();
    private final JButton           btnReconnect        = new JButton("Reconnect");
    private final JLabel            lblStatus           = new JLabel("  --");
    
    private final JLabel            lblMachines         = new JLabel("Machines:");
    private final JPanel            machines            = new JPanel();
    private final Box               machinesBox         = Box.createVerticalBox();
    private final DefaultTableModel machinesModel       = new DefaultTableModel(new Object[][] { }, new String[] { "Id", "Provider", "Region", "Type", "State"}) {
        private static final long serialVersionUID = 1L;
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable            machinesList        = new JTable(machinesModel);
    private final JScrollPane       jmachinesPanel      = new JScrollPane(machinesList);
    private final JPanel            machinesPanel       = new JPanel();
    private final Box               machinesBtnsBox     = Box.createHorizontalBox();
    private final JButton           btnRefreshMachines  = new JButton("Refresh");
    private final JButton           btnNewMachine       = new JButton("New");
    private final JButton           btnDeleteMachine    = new JButton("Delete");
    
    private final JLabel            lblInstances        = new JLabel("Instances:");
    private final JPanel            instances           = new JPanel();
    private final Box               instancesBox        = Box.createVerticalBox();
    private final DefaultTableModel instancesModel      = new DefaultTableModel(new Object[][] { }, new String[] { "Id", "Machine", "Model Upload Time"}) {
        private static final long serialVersionUID = 1L;
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable            instancesList       = new JTable(instancesModel);
    private final JScrollPane       jinstancesPanel     = new JScrollPane(instancesList);
    private final JPanel            instancesPanel      = new JPanel();
    private final Box               instancesBtnsBox    = Box.createHorizontalBox();
    private final JButton           btnRefreshInstances = new JButton("Refresh");
    private final JButton           btnNewInstance      = new JButton("New");
    private final JButton           btnDeleteInstance   = new JButton("Delete");
    
    private JToggleButton onOffButton = new JToggleButton("On/Off");
    
    private Timer timer = new Timer(100, this);
    
    private BufferedImage in = null;
    private BufferedImage out = null;
    
    private SicClient sicClient;
    
    public void startUI() {
        initPanels();
        initComponents();
        initActions();
        initLayout();
        addStyles();
        
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocation(100, 100);
        window.pack();
        window.setVisible(true);
    }
    
    private void initPanels() {
        videoIn = new JPanel() { private static final long serialVersionUID = 1L;
            @Override public void paintComponent(Graphics g) {
                super.paintComponent(g);
                if(in != null) {
                    Image scaledImage = in.getScaledInstance(getWidth(), getHeight(), Image.SCALE_DEFAULT);
                    g.drawImage(scaledImage, 0, 0, null);
                }
            }    
        };
        videoOut = new JPanel() { private static final long serialVersionUID = 1L;
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                if(out != null) {
                    Image scaledImage = out.getScaledInstance(getWidth(), getHeight(), Image.SCALE_DEFAULT);
                    g.drawImage(scaledImage, 0, 0, null);
                }
            }    
        };
    }

    private void initComponents() {
        textAccountNumber  .setColumns(15);
        textSystemName     .setColumns(15);
        textSystemKey      .setColumns(15);
        
        jmachinesPanel     .setMaximumSize(new Dimension(400, 100));
        jmachinesPanel     .setPreferredSize(new Dimension(400, 100));
        jinstancesPanel    .setMaximumSize(new Dimension(400, 100));
        jinstancesPanel    .setPreferredSize(new Dimension(400, 100));
        machinesList       .setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        instancesList      .setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        btnRefreshMachines .setEnabled(false);
        btnNewMachine      .setEnabled(false);
        btnDeleteMachine   .setEnabled(false);
        btnRefreshInstances.setEnabled(false);
        btnNewInstance     .setEnabled(false);
        btnDeleteInstance  .setEnabled(false);
        
        onOffButton        .setEnabled(false);
        
        Properties config = new Properties();
        try { config.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("credentials.properties"));
        } catch (Exception e) { } 
        if(config.getProperty("AccountNumber") != null) textAccountNumber.setText(config.getProperty("AccountNumber"));
        if(config.getProperty("SystemName")    != null) textSystemName   .setText(config.getProperty("SystemName"));
        if(config.getProperty("SystemKey")     != null) textSystemKey    .setText(config.getProperty("SystemKey"));
    }
    
    private void initActions() {
        mainPanel          .addMouseListener(new MouseAdapter()    { @Override public void mouseClicked(MouseEvent e)     { machinesList.clearSelection(); instancesList.clearSelection(); } });
        btnReconnect       .addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { initSystemInCloud(); } });
        machinesList       .getSelectionModel().addListSelectionListener(new ListSelectionListener() { 
            @Override public void valueChanged(ListSelectionEvent event) { 
                if(((DefaultListSelectionModel) event.getSource()).isSelectionEmpty()) {
                    btnDeleteMachine.setEnabled(false);
                    btnNewInstance  .setEnabled(false);
                } else {
                    btnDeleteMachine.setEnabled(true);
                    btnNewInstance  .setEnabled(true);
                    // Filter instances
                }
            }});
        
        btnRefreshMachines .addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { refreshMachines(); } });
        btnNewMachine      .addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { new NewMachineFrame(sicClient, WebcamClient.this).setVisible(true); } });
        btnDeleteMachine   .addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent event) {
                String machineId =  (String) machinesList.getValueAt(machinesList.getSelectedRow(), 0);
                int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure to delete machine " + machineId + "\n" +
                                                                       "All intances on that machine will be also removed");
                if(dialogResult == JOptionPane.YES_OPTION) {
                    try { sicClient.deleteMachine(machineId);
                    } catch(SicException e) { JOptionPane.showMessageDialog(null, e.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE); }
                    refreshMachines();
                    refreshInstances();
                }
            }});
        
        instancesList      .getSelectionModel().addListSelectionListener(new ListSelectionListener() { 
            @Override public void valueChanged(ListSelectionEvent event) { 
                if(((DefaultListSelectionModel) event.getSource()).isSelectionEmpty()) btnDeleteInstance.setEnabled(false);
                else                                                                   btnDeleteInstance.setEnabled(true);
            }});
        
        btnRefreshInstances.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { refreshInstances(); } });
        btnNewInstance     .addActionListener(new ActionListener() { 
            @Override public void actionPerformed(ActionEvent e) {
                String machineId = (String) machinesModel.getValueAt(machinesList.getSelectedRow(), 0);
                new NewInstanceFrame(sicClient, WebcamClient.this, machineId).setVisible(true); 
            }});
        
        btnDeleteInstance  .addActionListener(new ActionListener() { 
            @Override public void actionPerformed(ActionEvent event) {
                String instanceId =  (String) instancesList.getValueAt(instancesList.getSelectedRow(), 0);
                int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure to delete instance " + instanceId);
                if(dialogResult == JOptionPane.YES_OPTION) {
                    try { sicClient.deleteInstance(instanceId);
                    } catch(SicException e) { JOptionPane.showMessageDialog(null, e.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE); }
                    refreshInstances();
                }
            }});
        
        onOffButton        .addItemListener  (new ItemListener() {
            @Override public void itemStateChanged(ItemEvent itemEvent) {
                int state = itemEvent.getStateChange();
                if (state == ItemEvent.SELECTED) {
                    webcam.open();
                    timer.start();
                } else {
                    webcam.close();
                    timer.stop();
                }
            }});
    }
    
    private void initLayout() {
        videoIn .setPreferredSize(new Dimension(500, 500));
        videoOut.setPreferredSize(new Dimension(500, 500));
        
        videoPanels.setLayout(new BoxLayout(videoPanels, BoxLayout.X_AXIS));
        videoPanels.add(videoIn); videoPanels.add(videoOut);

        accountNumberBox.add(lblAccountNumber); accountNumberBox.add(textAccountNumber);
        systemNameBox   .add(lblSystemName);    systemNameBox   .add(textSystemName);
        systemKeyBox    .add(lblSystemKey);     systemKeyBox    .add(textSystemKey);
        
        credentialsBox.add(accountNumberBox);
        credentialsBox.add(systemNameBox);
        credentialsBox.add(systemKeyBox);
        
        reconnectionBox.add(btnReconnect); reconnectionBox.add(lblStatus);
        
        credentialsBox.add(reconnectionBox);
        
        credentials.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        credentials.add(credentialsBox);
        
        lblMachines  .setAlignmentX(Component.RIGHT_ALIGNMENT);
        machinesBox  .add(lblMachines);
        machinesPanel.add(jmachinesPanel);
        machinesBox  .add(machinesPanel);
        
        machinesBtnsBox.add(btnRefreshMachines); machinesBtnsBox.add(btnNewMachine); machinesBtnsBox.add(btnDeleteMachine);
        
        machinesBox.add(machinesBtnsBox);
        machines   .add(machinesBox);
        
        lblInstances.setAlignmentX(Component.RIGHT_ALIGNMENT);
        instancesBox.add(lblInstances);
        instancesBox.add(instancesPanel);
        
        instancesBtnsBox.add(btnRefreshInstances); instancesBtnsBox.add(btnNewInstance); instancesBtnsBox.add(btnDeleteInstance);
        
        instancesBox  .add(instancesBtnsBox);
        instancesPanel.add(jinstancesPanel);
        instances     .add(instancesBox);
        
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        controlPanel.add(credentials);
        controlPanel.add(machines);
        controlPanel.add(instances);
        controlPanel.add(onOffButton);
        
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(videoPanels, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);
        
        window.getContentPane().add(mainPanel);
    }
    
    private void addStyles() {
        videoIn .setBackground(Color.LIGHT_GRAY);
        videoIn .setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        videoOut.setBackground(Color.LIGHT_GRAY);
        videoOut.setBorder(BorderFactory.createLineBorder(Color.black, 2));
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BufferedImage bi = webcam.getImage();
            ImageIO.write(bi, "JPEG", baos);
            byte[] bytesOut = baos.toByteArray();
            //Send Data to specified system instance and port
            List<Integer> dims = new ArrayList<>();
            dims.add(1);
//            sicConnection.send(instanceID, "Img In", new Data(DatatypeVip.JPEG, dims, bytesOut));
            this.in = bi;
        } catch (IOException e) {
            e.printStackTrace();
        }
        videoIn.repaint();
    }
    
    private void initSystemInCloud() {
        this.sicClient = SicClientFactory.createClient(textAccountNumber.getText(),
                                                       textSystemName.getText(),
                                                       textSystemKey.getText());
        if(sicClient.isTestPassed()) {
            lblStatus.setText("  OK");
            lblStatus.setForeground(Color.GREEN);
            refreshMachines();
            refreshInstances();
            btnRefreshMachines .setEnabled(true);
            btnNewMachine      .setEnabled(true);
            btnRefreshInstances.setEnabled(true);
        } else {
            lblStatus.setText("  KO");
            lblStatus.setForeground(Color.RED);
            while(machinesModel .getRowCount() != 0) machinesModel .removeRow(0);
            while(instancesModel.getRowCount() != 0) instancesModel.removeRow(0);
            btnRefreshMachines .setEnabled(false);
            btnNewMachine      .setEnabled(false);
            btnDeleteMachine   .setEnabled(false);
            btnRefreshInstances.setEnabled(false);
            btnNewInstance     .setEnabled(false);
            btnDeleteInstance  .setEnabled(false);
            onOffButton        .setEnabled(false);
        }
    }
    
    private void refreshMachines() {
        while(machinesModel.getRowCount() != 0) machinesModel.removeRow(0);
        for(MachineInfo mi : this.sicClient.getMachines()) {
            machinesModel.addRow(new String [] { mi.getMachineId(), 
                                                 mi.getProvider(), 
                                                 mi.getRegion(), 
                                                 mi.getMachineType(), 
                                                 mi.getStatus()});
        }
    }

    private void refreshInstances() {
        while(instancesModel.getRowCount() != 0) instancesModel.removeRow(0);
        for(InstanceInfo ii : this.sicClient.getInstances()) {
            instancesModel.addRow(new String [] { ii.getInstanceId(), 
                                                  ii.getMachineId(), 
                                                  ii.getModelUploaded()});
        }
    }
    
    @Override public void machineCreated(MachineInfo mi) { machinesModel.addRow(new String [] { mi.getMachineId(), 
                                                                                                mi.getProvider(), 
                                                                                                mi.getRegion(), 
                                                                                                mi.getMachineType(), 
                                                                                                mi.getStatus()}); }
    @Override public void instanceCreated(InstanceInfo ii) { instancesModel.addRow(new String [] { ii.getInstanceId(), 
                                                                                                   ii.getMachineId(), 
                                                                                                   ii.getModelUploaded() }); }
    
    // Data received from the system instance
//    @Override
    public void receivedData(String instanceID, String portName, byte[] data) {
        
    }
    
    public static void main(String[] args) {
        WebcamClient wc = new WebcamClient();
        wc.startUI();
    }
}
