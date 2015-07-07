package tasks.data;

import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.ext.vip.modeler.api.javatask.data.Image;

@JavaTaskInfo
public class DataImage extends JavaTask {

    private static int N = 100;
    
    @InputPortInfo(name = "In", dataType = Image.class)
    public InputPort in;
    @OutputPortInfo(name = "Out", dataType = Image.class)
    public OutputPort out;

    @Override
    public void execute(int grp) {
        Image img = in.getData(Image.class);
        int[] inValues = img.getValues();
        int[] outValues = new int[img.getNumberOfElements()];
        for(int i = 0; i < outValues.length; i++) {
            int inRGB = inValues[i];
            int r = (inRGB >> 16 & 0xff) + N;
            int g = (inRGB >> 8 & 0xff) + N;
            int b = (inRGB & 0xff) + N;
            if(r > 255) r = 255; if(r < 0) r = 0;
            if(g > 255) g = 255; if(g < 0) g = 0;
            if(b > 255) b = 255; if(b < 0) b = 0;
            outValues[i] = (r << 16) | (g << 8) | b;
        }
        out.putData(new Image(outValues, img.getH(), img.getW()));
    }

}
