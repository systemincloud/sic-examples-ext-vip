package tasks.colorspace;

import tasks.Util;
import boofcv.gui.image.VisualizeImageData;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.MultiSpectral;

import com.systemincloud.ext.vip.modeler.api.javatask.data.Image;
import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;

@JavaTaskInfo
public class YUV extends JavaTask {

    @InputPortInfo(name = "In", dataType = Image.class)
    public InputPort in;
    @OutputPortInfo(name = "Y", dataType = Image.class)
    public OutputPort outY;
    @OutputPortInfo(name = "U", dataType = Image.class)
    public OutputPort outU;
    @OutputPortInfo(name = "V", dataType = Image.class)
    public OutputPort outV;

    @Override
    public void execute(int grp) {
        Image inImg = in.getData(Image.class);
        MultiSpectral<ImageFloat32> yuv = Util.toMultiSpectralYUVImageFloat32(inImg);
        
        outY.putData(Util.fromImageFloat32(yuv.getBand(0)));
        
        outU.putData(new Image(VisualizeImageData.colorizeSign(yuv.getBand(1), null, -1)));
        outV.putData(new Image(VisualizeImageData.colorizeSign(yuv.getBand(2), null, -1)));
    }

}
