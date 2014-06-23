package tasks.salt;

import java.util.Random;

import org.bytedeco.javacpp.opencv_core.CvMat;

import tasks.Util;

import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.ext.vip.modeler.api.javatask.data.Image;

@JavaTaskInfo
public class Salt extends JavaTask {

    @InputPortInfo(name = "In", dataType = Image.class)
    public InputPort in;
    @OutputPortInfo(name = "Out", dataType = Image.class)
    public OutputPort out;

    @Override
    public void execute() {
        Image inData = in.getData(Image.class);
        CvMat img = Util.toCvMat(inData);
        salt(img, 2000);
        out.putData(Util.fromCvMat(img));
    }

    private CvMat salt(CvMat img, int n) {
        int size = img.width() * img.height();
        int nbChannels = img.channels();
        Random random = new Random();
        for(int i = 0; i < n; i++) {
            int index = random.nextInt(size);
            int offset = index * nbChannels;
            for(int j = 0; j < nbChannels; j++)
                img.put(offset + j, 255);
        }
        return img;
    }
}
