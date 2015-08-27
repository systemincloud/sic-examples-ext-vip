package com.systemincloud.example.backgroundsubtraction.gauss;

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
@SicParameters({ @SicParameter(name = SigmaDistance.THRESHOLD, defaultValue = "3.5") })
public class SigmaDistance extends JavaTask {

	@InputPortInfo(name = "diff", dataType = Int32.class)
	public InputPort diff;
	@InputPortInfo(name = "var", dataType = Int32.class)
	public InputPort var;
	@OutputPortInfo(name = "mask", dataType = Int32.class)
	public OutputPort mask;
	
	protected static final String THRESHOLD = "sigmaThreshold";
	
	private int[] diffValues;
	private int[] varValues;
	
	private double sigmaThreshold;
	
	@Override
	public void runnerStart() {
		sigmaThreshold = Double.parseDouble(getParameter(THRESHOLD));
	}

	@Override
	public void execute(int grp) {
		Int32 diffData = diff.getData(Int32.class);
		Int32 VarData = var.getData(Int32.class);
		diffValues = diffData.getValues();
		varValues = VarData.getValues();
		
		int[] bgMask = new int[diffValues.length];
		for(int i= 0; i < diffValues.length; i++){
			bgMask[i] = (diffValues[i] < sigmaThreshold * varValues[i]) ? 0x00 : 0xFF;
		}
		mask.putData(new Int32(diffData.getDimensions(), bgMask));
	}
}
