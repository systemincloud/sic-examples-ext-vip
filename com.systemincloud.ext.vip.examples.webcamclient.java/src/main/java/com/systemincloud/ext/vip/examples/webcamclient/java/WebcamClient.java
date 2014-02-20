package com.systemincloud.ext.vip.examples.webcamclient.java;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.Timer;

import com.github.sarxos.webcam.Webcam;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.GridLayout;

public class WebcamClient implements ActionListener {

    private Webcam webcam = Webcam.getDefault();
    
    private JFrame window = new JFrame("System in Cloud - Webcam Client");
    
    private JPanel mainPanel = new JPanel();
    
    private JPanel videoPanels  = new JPanel();
    private JPanel controlPanel = new JPanel();
    
    private JPanel videoIn;
    private JPanel videoOut;
   
    private final JLabel     lblAccount  = new JLabel("Account:");
    private final JTextField textAccount = new JTextField();
    
    private JToggleButton onOffButton = new JToggleButton("On/Off");
    
    private Timer timer = new Timer(100, this);
    
    private BufferedImage in = null;
    private BufferedImage out = null;

    
    public WebcamClient() {
        initSic();
    }
    
    private void initSic() {
        
    }

    private void startUI() {
        textAccount.setColumns(10);
        initPanels();
        initButtons();
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

    private void initButtons() {
        onOffButton.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent) {
                int state = itemEvent.getStateChange();
                if (state == ItemEvent.SELECTED) {
                        // Send Data to specified system instance and port
//                        sicConnection.send(instanceID, "RST", new Data(1));
                        timer.start();
                } else timer.stop();
            }
        });
    }
    
    private void initLayout() {
        
        videoIn.setPreferredSize(new Dimension(320, 240));
        videoIn.setMinimumSize(new Dimension(320,240));
        
        videoOut.setPreferredSize(new Dimension(320, 240));
        videoOut.setMinimumSize(new Dimension(320,240));
        
        videoPanels.setLayout(new BoxLayout(videoPanels, BoxLayout.X_AXIS));
        videoPanels.add(videoIn);
        videoPanels.add(videoOut);
        
        controlPanel.setLayout(new GridLayout(0, 4, 1, 1));
        controlPanel.add(lblAccount);
        controlPanel.add(textAccount);
        controlPanel.add(onOffButton);
        
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(videoPanels, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);
        
        window.getContentPane().add(mainPanel);
    }
    
    private void addStyles() {
        videoIn.setBackground(Color.LIGHT_GRAY);
        videoIn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        videoOut.setBackground(Color.LIGHT_GRAY);
        videoOut.setBorder(BorderFactory.createLineBorder(Color.black, 2));
    }

    
    public static void main(String[] args) {
        WebcamClient vc = new WebcamClient();
        vc.startUI();
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
//        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            System.out.println(webcam.getImage());
//            ImageIO.write(webcam.getImage(), "JPEG", baos);
//            byte[] bytesOut = baos.toByteArray();
            // Send Data to specified system instance and port
//            List<Integer> dims = new ArrayList<Integer>();
//            dims.add(1);
//            sicConnection.send(instanceID, "Img In", new Data(DatatypeVip.JPEG, dims, bytesOut));
//            this.in = webcam.getImage();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        videoIn.repaint();
    }
    
    // Data received from the system instance
//    @Override
//    public void receivedData(String instanceID, String portName, byte[] data) {
//            
//    }
}
