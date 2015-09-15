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
@SicParameters({ @SicParameter(name = SigmaConfidence.THRESHOLD_LOW, defaultValue = "1"),
                 @SicParameter(name = SigmaConfidence.THRESHOLD_HIGH, defaultValue = "20") })
public class SigmaConfidence extends JavaTask {

	@InputPortInfo(name = "diff", dataType = Int32.class)
	public InputPort diff;
	@InputPortInfo(name = "var", dataType = Int32.class)
	public InputPort var;
	@OutputPortInfo(name = "confidence", dataType = Int32.class)
	public OutputPort confidence;
	
	public static final String THRESHOLD_LOW = "sigmaThresholdLow";
	public static final String THRESHOLD_HIGH = "sigmaThresholdHigh";
	
	private int[] diffValues;
	private int[] varValues;
	
	private double sigmaThresholdLow;
	private double sigmaThresholdHigh;
	
	@Override
	public void runnerStart() {
		sigmaThresholdLow = Double.parseDouble(getParameter(THRESHOLD_LOW));
		sigmaThresholdHigh = Double.parseDouble(getParameter(THRESHOLD_HIGH));
	}

	@Override
	public void execute(int grp) {
		Int32 diffData = diff.getData(Int32.class);
		Int32 VarData = var.getData(Int32.class);
		diffValues = diffData.getValues();
		varValues = VarData.getValues();
		
		int[] confidenceValues = new int[diffValues.length];
		for(int i= 0; i < diffValues.length; i++){
			//confidence scaled to range 0-255 instead of 0-1 for easy visualisation
			if(diffValues[i] < sigmaThresholdLow * varValues[i]){
				confidenceValues[i] = 0;
			} else if(diffValues[i] > sigmaThresholdHigh * varValues[i]){
				confidenceValues[i] = 0xFF;
			} else {
				//lerp in-between
				confidenceValues[i] = (int) (0xFF * (diffValues[i] - sigmaThresholdLow * varValues[i]) / (sigmaThresholdHigh * varValues[i] - sigmaThresholdLow * varValues[i]));
			}
		}
		confidence.putData(new Int32(diffData.getDimensions(), confidenceValues));
	}
}
