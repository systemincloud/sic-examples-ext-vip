package tasks.simple;

import static org.bytedeco.javacpp.opencv_core.cvFlip;

import org.bytedeco.javacpp.opencv_core.IplImage;

import tasks.Util;

import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.ext.vip.modeler.api.javatask.data.Image;

@JavaTaskInfo
public class Simple extends JavaTask {

    @InputPortInfo(name = "In", dataType = Image.class)
    public InputPort in;
    @OutputPortInfo(name = "Out", dataType = Image.class)
    public OutputPort out;
    
    @Override
    public void execute(int grp) {
        Image inData = in.getData(Image.class);
        IplImage img = Util.toIplImage(inData);
        cvFlip(img, img, 0);
        out.putData(Util.fromIplImage(img));
    }

}
