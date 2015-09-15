package com.systemincloud.example.backgroundsubtraction.gauss;

import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.data.Int32;
import com.systemincloud.modeler.tasks.javatask.api.data.Float32;

@JavaTaskInfo
public class Gauss extends JavaTask {

	@InputPortInfo(name = "mu", dataType = Int32.class)
	public InputPort mu;
	@InputPortInfo(name = "sigma", dataType = Int32.class)
	public InputPort sigma;
	@InputPortInfo(name = "x", dataType = Int32.class)
	public InputPort x;
	@OutputPortInfo(name = "probability", dataType = Float32.class)
	public OutputPort probability;

	@Override
	public void execute(int grp) {
		Int32 muData = mu.getData(Int32.class);
		Int32 sigmaData = sigma.getData(Int32.class);
		Float32 xData = x.getData(Float32.class);
		int[] muValues = muData.getValues();
		int[] sigmaValues = sigmaData.getValues();
		float[] xValues = xData.getValues();
		
		float[] propabilityValues = new float[xData.getNumberOfElements()];
		
		for(int i = 0; i < xData.getNumberOfElements(); i++){
			propabilityValues[i] = (float) gauss(xValues[i], muValues[i], sigmaValues[i]);
		}
		probability.putData(new Float32(xData.getDimensions(), propabilityValues));
	}

	private double fastExp(double val){
		long tmp = (long) (1512775 * val + 1072632447);
	    return Double.longBitsToDouble(tmp << 32);
	}
	private double gauss(double x, double m, double s){
		return(fastExp(-((x - m) * (x - m)) / (2 * s * s)) / s * Math.sqrt(2 * Math.PI));
	}
}
