package com.systemincloud.example.backgroundsubtraction.temporaldifference;

import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.SicParameter;
import com.systemincloud.modeler.tasks.javatask.api.annotations.SicParameters;
import com.systemincloud.modeler.tasks.javatask.api.data.Int32;

@JavaTaskInfo
@SicParameters({ @SicParameter(name = TemporalDifference.THRESHOLD, defaultValue = "10") })
public class TemporalDifference extends JavaTask {

	@InputPortInfo(name = "In", dataType = Int32.class)
	public InputPort in;
	@OutputPortInfo(name = "Out", dataType = Int32.class)
	public OutputPort out;
	
	protected static final String THRESHOLD = "Threshold";

	private int[] previousFrame;
	private int tolerance;
	private static int BLACK = 0x00;
	private static int WHITE = 0xff;
	
	@Override
	public void runnerStart() {
		tolerance = Integer.parseInt(getParameter(THRESHOLD));
	}
	
	@Override
	public void execute(int grp) {
		Int32 frame = in.getData(Int32.class);
		int[] frameData = frame.getValues();
		if (previousFrame != null) {
			int[] maskData = new int[frameData.length];

			for (int i = 0; i < frame.getNumberOfElements(); i++) {
				if (Math.abs(frameData[i] - previousFrame[i]) < tolerance) {
					maskData[i] = BLACK;
				} else {
					maskData[i] = WHITE;
				}
			}
			out.putData(new Int32(frame.getDimensions(), maskData));
		}
		previousFrame = frameData;
	}

}
