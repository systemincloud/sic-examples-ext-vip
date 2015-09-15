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
@SicParameters({ @SicParameter(name = ConfidenceSelector.THRESHOLD, defaultValue = "150") })
public class ConfidenceSelector extends JavaTask {

	@InputPortInfo(name = "confidence1", dataType = Int32.class)
	public InputPort confidence1;
	@InputPortInfo(name = "confidence2", dataType = Int32.class)
	public InputPort confidence2;
	@InputPortInfo(name = "confidence3", dataType = Int32.class)
	public InputPort confidence3;
	@OutputPortInfo(name = "mask", dataType = Int32.class)
	public OutputPort mask;
	
	private int threshold;
	
	public static final String THRESHOLD = "Threshold";
	
	@Override
	public void runnerStart() {
		threshold = Integer.parseInt(getParameter(THRESHOLD));
	}

	@Override
	public void execute(int grp) {
		Int32 confidencedata1 = confidence1.getData(Int32.class);
		Int32 confidencedata2 = confidence2.getData(Int32.class);
		Int32 confidencedata3 = confidence3.getData(Int32.class);
		int[] confidencevalues1 = confidencedata1.getValues();
		int[] confidencevalues2 = confidencedata2.getValues();
		int[] confidencevalues3 = confidencedata3.getValues();
		int[] bgdata = new int[confidencevalues1.length];
		//find max value and compare with threshold
		for(int i = 0; i < confidencevalues1.length; i++){
			int maxvalue = 0;
			if(confidencevalues1[i]  > confidencevalues2[1]){
				if(confidencevalues1[i]  > confidencevalues3[1]) {
					maxvalue = confidencevalues1[i];
				} else {
					maxvalue = confidencevalues3[i];
				}
			} else {
				if(confidencevalues2[i]  > confidencevalues3[1]) {
					maxvalue = confidencevalues2[i];
				} else {
					maxvalue = confidencevalues3[i];
				}
			}
			bgdata[i] = (maxvalue > threshold) ? 0xFF : 0x00;
			}
		mask.putData(new Int32(confidencedata1.getDimensions(), bgdata));
	}

}
