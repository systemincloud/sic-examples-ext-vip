package com.systemincloud.example.backgroundsubtraction.frameaverage;

import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.SicParameter;
import com.systemincloud.modeler.tasks.javatask.api.annotations.SicParameters;
import com.systemincloud.modeler.tasks.javatask.api.data.Int32;
import java.lang.Math;

@JavaTaskInfo
@SicParameters({ @SicParameter(name = Subtractor.TOLERANCE, defaultValue = "10") })
public class Subtractor extends JavaTask {

	@InputPortInfo(name = "video", dataType = Int32.class)
	public InputPort video;
	@InputPortInfo(name = "background", dataType = Int32.class)
	public InputPort backgroundModel;
	@OutputPortInfo(name = "mask", dataType = Int32.class)
	public OutputPort mask;
	
	private int BLACK = 0x00;
	private int WHITE = 0xff;
	
	private int THRESHOLD;
	
	protected static final String TOLERANCE = "DiffThreshold";
	
	@Override
	public void runnerStart() {
		THRESHOLD = Integer.parseInt(getParameter(TOLERANCE));
	}

	@Override
	public void execute() {
		Int32 frame = video.getData(Int32.class);
		int[] frameData = frame.getValues();
		
		Int32 background = backgroundModel.getData(Int32.class);
		int[] backgroundData = background.getValues();
		
		int[] maskData = new int[frameData.length];
		
		for(int i = 0; i < frame.getNumberOfElements(); i++){
			if(Math.abs(frameData[i] - backgroundData[i]) < THRESHOLD){
				maskData[i] = BLACK;
			} else {
				maskData[i] = WHITE;
			}
		}
		mask.putData(new Int32(frame.getDimensions(), maskData));
	}

}
