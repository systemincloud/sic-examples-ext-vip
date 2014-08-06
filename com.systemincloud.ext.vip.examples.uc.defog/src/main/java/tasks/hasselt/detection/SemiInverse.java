package tasks.hasselt.detection;

import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.ext.vip.modeler.api.javatask.data.Image;

@JavaTaskInfo
public class SemiInverse extends JavaTask {

	@InputPortInfo(name = "In", dataType = Image.class)
	public InputPort in;
	@OutputPortInfo(name = "Out", dataType = Image.class)
	public OutputPort out;

	@Override
	public void execute() {
        Image img = in.getData(Image.class);
        int[] inValues = img.getValues();
        int[] outValues = new int[img.getNumberOfElements()];
        for(int i = 0; i < outValues.length; i++) {
            int inRGB = inValues[i];
            int r = (inRGB >> 16 & 0xff);
            int g = (inRGB >> 8 & 0xff);
            int b = (inRGB & 0xff);
            if(r < 128) r = 255 - r;
            if(g < 128) g = 255 - g;
            if(b < 128) b = 255 - b;
            outValues[i] = (r << 16) | (g << 8) | b;
        }
        out.putData(new Image(outValues, img.getH(), img.getW()));
	}
}
