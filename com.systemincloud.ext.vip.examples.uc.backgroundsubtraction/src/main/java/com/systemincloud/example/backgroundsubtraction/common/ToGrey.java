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
public class ToGrey extends JavaTask {

	@InputPortInfo(name = "In", dataType = Image.class)
	public InputPort in;
	@OutputPortInfo(name = "Out", dataType = Int32.class)
	public OutputPort out;

	@Override
	public void execute(int grp) {
		Image img = in.getData(Image.class);
		int[] inValues = img.getValues();
		int[] outValues = new int[inValues.length];
				
		for(int i = 0; i < inValues.length; i++) {
			int pixel = inValues[i];
			int mean = ((pixel >> 16 & 0xff) + (pixel >>  8 & 0xff) + (pixel & 0xff)) / 3;
			outValues[i] = mean;
		}
		out.putData(new Int32(img.getDimensions(), outValues));
	}
}