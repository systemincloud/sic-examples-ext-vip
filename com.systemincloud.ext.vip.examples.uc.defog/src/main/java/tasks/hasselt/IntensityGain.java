package tasks.hasselt;

import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.SicParameters;
import com.systemincloud.ext.vip.modeler.api.javatask.data.Image;

@JavaTaskInfo
@SicParameters(names=IntensityGain.XI)
public class IntensityGain extends JavaTask {

	protected static final String XI = "Xi";
	
	@InputPortInfo(name = "In", dataType = Image.class)
	public InputPort in;
	
	@OutputPortInfo(name = "Out", dataType = Image.class)
	public OutputPort out;

	@Override
	public void execute() {
        Image img = in.getData(Image.class);
        float gain = Float.parseFloat(getParameter(XI));
        if(gain == 0) out.putData(img);
        else {
            int[] inValues = img.getValues();
            int[] outValues = new int[img.getNumberOfElements()];
            for(int i = 0; i < outValues.length; i++) {
                int inRGB = inValues[i];
                int r = (inRGB >> 16 & 0xff);
                int g = (inRGB >> 8 & 0xff);
                int b = (inRGB & 0xff);
                r += r + r*gain;
                g += g + g*gain;
                b += b + b*gain;
                if(r > 255) r = 255;
                if(g > 255) g = 255;
                if(b > 255) b = 255;
                outValues[i] = (r << 16) | (g << 8) | b;
            }
            out.putData(new Image(outValues, img.getH(), img.getW()));
        }
	}
}
