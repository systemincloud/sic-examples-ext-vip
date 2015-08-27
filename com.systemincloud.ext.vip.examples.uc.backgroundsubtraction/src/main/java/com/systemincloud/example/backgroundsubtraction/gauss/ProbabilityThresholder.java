package com.systemincloud.example.backgroundsubtraction.gauss;

import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.SicParameter;
import com.systemincloud.modeler.tasks.javatask.api.annotations.SicParameters;
import com.systemincloud.modeler.tasks.javatask.api.data.Float32;
import com.systemincloud.modeler.tasks.javatask.api.data.Int32;

@JavaTaskInfo
@SicParameters({ @SicParameter(name = ProbabilityThresholder.THRESHOLD, defaultValue = "0.1") })
public class ProbabilityThresholder extends JavaTask {

	@InputPortInfo(name = "probability", dataType = Float32.class)
	public InputPort probability;
	@OutputPortInfo(name = "mask", dataType = Int32.class)
	public OutputPort mask;
	
	private double threshold;
	
	protected static final String THRESHOLD = "Threshold";
	
	@Override
	public void runnerStart() {
		threshold = Double.parseDouble(getParameter(THRESHOLD));
	}

	@Override
	public void execute(int grp) {
		Float32 prop = probability.getData(Float32.class);
		float[] data = prop.getValues();
		int[] vardata = new int[data.length];
		for(int i = 0; i < prop.getNumberOfElements(); i++){
			vardata[i] = (data[i] < threshold) ? 0xFF : 0x00;
			}
		mask.putData(new Int32(prop.getDimensions(), vardata));
	}

}
