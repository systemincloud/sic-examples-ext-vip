package com.systemincloud.ext.vip.examples.webcamclient.java;

import javax.swing.JFrame;

import com.github.sarxos.webcam.Webcam;

public class WebcamClient {

    private Webcam webcam = Webcam.getDefault();
    
    private JFrame window = new JFrame("System in Cloud - Webcam Client");
    
    public WebcamClient() {
        
    }
    
    private void startUI() {
        // TODO Auto-generated method stub
    }
    
    public static void main(String[] args) {
        WebcamClient vc = new WebcamClient();
        vc.startUI();
    }
}
