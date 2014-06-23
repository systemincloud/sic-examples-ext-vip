package tasks.blur;

import org.bytedeco.javacpp.opencv_core.CvMat;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import tasks.Util;

import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.ext.vip.modeler.api.javatask.data.Image;

@JavaTaskInfo
public class Blur extends JavaTask {

    @InputPortInfo(name = "In", dataType = Image.class)
    public InputPort in;
    @OutputPortInfo(name = "Out", dataType = Image.class)
    public OutputPort out;

    @Override
    public void execute() {
        Image inData = in.getData(Image.class);
        CvMat src = Util.toCvMat(inData);
        CvMat dest = CvMat.create(src.rows(), src.cols(), src.depth(), 4);

        CvMat kernel = cvCreateMat(3, 3, CV_32F);
        kernel.put(0, 0, 0.11);
        kernel.put(1, 0, 0.11);
        kernel.put(2, 0, 0.11);
        kernel.put(0, 1, 0.11);
        kernel.put(1, 1, 0.12);
        kernel.put(2, 1, 0.11);
        kernel.put(0, 2, 0.11);
        kernel.put(1, 2, 0.11);
        kernel.put(2, 2, 0.11);

        cvFilter2D(src, dest, kernel, cvPoint(-1, -1));
        
        out.putData(Util.fromCvMat(dest));
    }

}
