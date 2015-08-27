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
@SicParameters({ @SicParameter(name = Variance.LEARNINGCONST, defaultValue = "0.005"),
	             @SicParameter(name = Variance.HIGHSIGMAMULTIPLIER, defaultValue = "3"), 
	             @SicParameter(name = Variance.LOWSIGMA, defaultValue = "5") })
public class Variance extends JavaTask {

	@InputPortInfo(name = "in", dataType = Int32.class)
	public InputPort in;
	@InputPortInfo(name = "avg", dataType = Int32.class)
	public InputPort avg;
	@OutputPortInfo(name = "var", dataType = Int32.class)
	public OutputPort var;
	
	protected static final String LEARNINGCONST = "learningConst";
	protected static final String HIGHSIGMAMULTIPLIER = "highSigmaMultiplier";
	protected static final String LOWSIGMA = "lowSigma";
	
	private int[] sigma;
	private double learningConst = 0.005;
	private int[] frameValues;
	private int[] averageValues;
	
	private int absoluteLowSigma = 5;
	private int highSigmaMultiplier = 3;
	
	@Override
	public void runnerStart() {
		learningConst = Double.parseDouble(getParameter(LEARNINGCONST));
		highSigmaMultiplier = Integer.parseInt(getParameter(HIGHSIGMAMULTIPLIER));
		absoluteLowSigma = Integer.parseInt(getParameter(LOWSIGMA));
	}

	@Override
	public void execute(int grp) {
		Int32 frame = in.getData(Int32.class);
		Int32 averageData = avg.getData(Int32.class);
		frameValues = frame.getValues();
		averageValues = averageData.getValues();
		if(sigma == null){
			sigma = new int[frame.getNumberOfElements()];
		}
		updateSigma();
	    var.putData(new Int32(frame.getDimensions(), sigma));
	}
	
	private void updateSigma(){
		for(int i = 0; i < sigma.length; i++){
			int newSigma = (int) Math.sqrt(learningConst * (frameValues[i] - averageValues[i]) * (frameValues[i] - averageValues[i]) + (1 - learningConst) * sigma[i] * sigma[i]);
		    if(newSigma < absoluteLowSigma){
		    	//too low value -> in noisy result
		    	sigma[i] = absoluteLowSigma;
		    } else if(newSigma > highSigmaMultiplier * sigma[i]){
		    	sigma[i] = highSigmaMultiplier * sigma[i];
		    } else {
		    	sigma[i] = newSigma;
		    }
		}
	}
}
