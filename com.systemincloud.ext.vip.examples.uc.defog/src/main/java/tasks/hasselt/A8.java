package tasks.hasselt;

import java.util.Arrays;

import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.data.Bool;
import com.systemincloud.ext.vip.modeler.api.javatask.data.Image;
import com.systemincloud.modeler.tasks.javatask.api.data.Int32;

@JavaTaskInfo
public class A8 extends JavaTask {

	@InputPortInfo(name = "Mask", dataType = Bool.class)
	public InputPort mask;
	@InputPortInfo(name = "In", dataType = Image.class)
	public InputPort in;
	@OutputPortInfo(name = "Out", dataType = Int32.class)
	public OutputPort out;

	@Override
	public void execute(int grp) {
		Image img = in.getData(Image.class);
        int[]     inValues   = img.getValues();
        boolean[] maskValues = mask.getData(Bool.class).getValues();

        int[] outRGB = new int[3];

        int maxIntesity = 0;
        for(int i = 0; i < inValues.length; i++) {
        	if(!maskValues[i]) continue;
        	
            int inRGB = inValues[i];
            int r = (inRGB >> 16 & 0xff);
            int g = (inRGB >> 8 & 0xff);
            int b = (inRGB & 0xff);
            int intensity = (r + g + b) / 3;
            if(intensity > maxIntesity) {
            	outRGB[0] = r; outRGB[1] = g; outRGB[2] = b;
            	maxIntesity = intensity;
            }
        }
		out.putData(new Int32(Arrays.asList(1, 3), outRGB));
	}
}
