package com.systemincloud.example.backgroundsubtraction.gauss;

import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.data.Int32;

@JavaTaskInfo
public class Median extends JavaTask {

	@InputPortInfo(name = "data1", dataType = Int32.class)
	public InputPort data1;
	@InputPortInfo(name = "data2", dataType = Int32.class)
	public InputPort data2;
	@InputPortInfo(name = "data3", dataType = Int32.class)
	public InputPort data3;
	@OutputPortInfo(name = "median", dataType = Int32.class)
	public OutputPort median;
	
	
	@Override
	public void execute(int grp) {
		Int32 val1 = data1.getData(Int32.class);
		int[] dataValues1 = val1.getValues();
		Int32 val2 = data2.getData(Int32.class);
		int[] dataValues2 = val2.getValues();
		Int32 val3 = data3.getData(Int32.class);
		int[] dataValues3 = val3.getValues();
		
		int[] medianData = new int[dataValues1.length];
		for (int i = 0; i < dataValues1.length; i++) {
			medianData[i] = (dataValues1[i] + dataValues2[i] + dataValues3[i] > 0xFF) ? 0xFF : 0x00;
//          true median:
//			if (dataValues1[i] > dataValues2[i]) {
//				if (dataValues2[i] > dataValues3[i]) {
//					medianData[i] = dataValues2[i];
//				} else if (dataValues1[i] > dataValues3[i]) {
//					medianData[i] = dataValues3[i];
//				} else {
//					medianData[i] = dataValues1[i];
//				}
//			} else {
//				if (dataValues1[i] > dataValues3[i]) {
//					medianData[i] = dataValues1[i];
//				} else if (dataValues2[i] > dataValues3[i]) {
//					medianData[i] = dataValues3[i];
//				} else {
//					medianData[i] = dataValues2[i];
//				}
//			}
		}
		median.putData(new Int32(val1.getDimensions(), medianData));
	}

}
