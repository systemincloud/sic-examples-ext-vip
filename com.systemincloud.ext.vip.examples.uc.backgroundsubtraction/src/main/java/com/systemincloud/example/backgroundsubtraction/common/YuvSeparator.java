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
public class YuvSeparator extends JavaTask {

	@InputPortInfo(name = "In", dataType = Image.class)
	public InputPort in;
	@OutputPortInfo(name = "Y", dataType = Int32.class)
	public OutputPort y;
	@OutputPortInfo(name = "U", dataType = Int32.class)
	public OutputPort u;
	@OutputPortInfo(name = "V", dataType = Int32.class)
	public OutputPort v;

	@Override
	public void execute(int grp) {
		Image img = in.getData(Image.class);
		int[] inValues = img.getValues();
		int[] yValues = new int[inValues.length];
		int[] uValues = new int[inValues.length];
		int[] vValues = new int[inValues.length];
				
		for(int i = 0; i < inValues.length; i++) {
			int red = (inValues[i] >> 16) & 0xFF;
			int green = (inValues[i] >> 8) & 0xFF;
			int blue = inValues[i] & 0xFF;
			
			yValues[i] = (int) (0.299 * red + 0.597 * green + 0.114 * blue);
			uValues[i] = (int) (-0.147 * red - 0.289 * green + 0.436 * blue);
			vValues[i] = (int) (0.615 * red - 0.515 * green - 0.1 * blue);
		}
		y.putData(new Int32(img.getDimensions(), yValues));
		u.putData(new Int32(img.getDimensions(), uValues));
		v.putData(new Int32(img.getDimensions(), vValues));
	}
}