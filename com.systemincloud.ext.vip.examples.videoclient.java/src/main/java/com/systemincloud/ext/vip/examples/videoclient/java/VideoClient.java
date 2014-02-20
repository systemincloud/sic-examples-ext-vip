package com.systemincloud.ext.vip.examples.videoclient.java;

import javax.swing.JFrame;

import com.github.sarxos.webcam.Webcam;

public class VideoClient {

    private Webcam webcam = Webcam.getDefault();
    
    private JFrame window = new JFrame("System in Cloud - Video Client");
    
    public VideoClient() {
        
    }
    
    private void startUI() {
        // TODO Auto-generated method stub
    }
    
    public static void main(String[] args) {
        VideoClient vc = new VideoClient();
        vc.startUI();
    }
}
