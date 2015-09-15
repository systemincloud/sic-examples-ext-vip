package com.systemincloud.example.backgroundsubtraction.common;

import com.systemincloud.ext.vip.modeler.api.javatask.data.Image;
import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.data.Int32;

@JavaTaskInfo
public class RgbSeparator extends JavaTask {

	@InputPortInfo(name = "In", dataType = Image.class)
	public InputPort in;
	@OutputPortInfo(name = "R", dataType = Int32.class)
	public OutputPort red;
	@OutputPortInfo(name = "G", dataType = Int32.class)
	public OutputPort green;
	@OutputPortInfo(name = "B", dataType = Int32.class)
	public OutputPort blue;

	@Override
	public void execute(int grp) {
		Image img = in.getData(Image.class);
		int[] inValues = img.getValues();
		int[] rValues = new int[inValues.length];
		int[] gValues = new int[inValues.length];
		int[] bValues = new int[inValues.length];
				
		for(int i = 0; i < inValues.length; i++) {
			rValues[i] = (inValues[i] >> 16) & 0xFF;
			gValues[i] = (inValues[i] >> 8) & 0xFF;
			bValues[i] = inValues[i] & 0xFF;
		}
		red.putData(new Int32(img.getDimensions(), rValues));
		green.putData(new Int32(img.getDimensions(), gValues));
		blue.putData(new Int32(img.getDimensions(), bValues));
	}
}