package tasks.hasselt;

import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.data.Float32;
import com.systemincloud.ext.vip.modeler.api.javatask.data.Image;
import com.systemincloud.modeler.tasks.javatask.api.data.Int32;

@JavaTaskInfo
public class I_i extends JavaTask {

	@InputPortInfo(name = "c_i", dataType = Float32.class)
	public InputPort c_i;
	@InputPortInfo(name = "I", dataType = Image.class)
	public InputPort i;
	@InputPortInfo(name = "A8", dataType = Int32.class)
	public InputPort a8;
	@OutputPortInfo(name = "I_i", dataType = Image.class)
	public OutputPort i_i;

	@Override
	public void execute() {
		System.out.println(":)");
		float c = c_i.getData(Float32.class).getValue();
		int[] a8Values = a8.getData(Int32.class).getValues();
		Image iData = i.getData(Image.class);
        int[] inValues = iData.getValues();
        
        int[] outValues = new int[iData.getNumberOfElements()];
        
        for(int i = 0; i < outValues.length; i++) {
            int inRGB = inValues[i];
            int r = (inRGB >> 16 & 0xff);
            int g = (inRGB >> 8 & 0xff);
            int b = (inRGB & 0xff);
            r -= c*a8Values[0];
            g -= c*a8Values[1];
            b -= c*a8Values[2];
            if(r < 0) r = 0;
            if(g < 0) g = 0;
            if(b < 0) b = 0;
            outValues[i] = (r << 16) | (g << 8) | b;
        }
        i_i.putData(new Image(outValues, iData.getH(), iData.getW()));
	}
}
