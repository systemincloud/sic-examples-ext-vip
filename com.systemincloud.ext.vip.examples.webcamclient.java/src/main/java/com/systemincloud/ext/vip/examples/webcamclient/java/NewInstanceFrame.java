package com.systemincloud.ext.vip.examples.webcamclient.java;

import javax.swing.JFrame;

import com.systemincloud.sdk.java.SicClient;

public class NewInstanceFrame extends JFrame {
    
    private static final long serialVersionUID = 1L;
    
    private SicClient sicClient;
    private NewInstanceFrameListener listener;
    
    public NewInstanceFrame(SicClient sicClient, NewInstanceFrameListener listener) {
        this.sicClient = sicClient;
        this.listener = listener;
    }
}
