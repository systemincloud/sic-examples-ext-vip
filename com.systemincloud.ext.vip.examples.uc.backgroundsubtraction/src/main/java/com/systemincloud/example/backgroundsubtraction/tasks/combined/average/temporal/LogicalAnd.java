package com.systemincloud.example.backgroundsubtraction.tasks.combined.average.temporal;

import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.data.Int32;

@JavaTaskInfo
public class LogicalAnd extends JavaTask {

	@InputPortInfo(name = "in1", dataType = Int32.class)
	public InputPort in1;
	@InputPortInfo(name = "in2", dataType = Int32.class)
	public InputPort in2;
	@OutputPortInfo(name = "out", dataType = Int32.class)
	public OutputPort out;

	@Override
	public void execute() {
		Int32 frame1 = in1.getData(Int32.class);
		int[] frameData1 = frame1.getValues();
		
		Int32 frame2 = in2.getData(Int32.class);
		int[] frameData2 = frame2.getValues();
		
		int[] outputData = new int[frame1.getNumberOfElements()];
		
		for(int i = 0; i < outputData.length; i++){
			outputData[i] = frameData1[i] & frameData2[i];
		}
		
		out.putData(new Int32(frame1.getDimensions(),	outputData));
		
	}

}
