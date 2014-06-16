package tasks.colorspace;

import tasks.Util;
import boofcv.alg.misc.PixelMath;
import boofcv.core.image.ConvertBufferedImage;
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
public class HSV extends JavaTask {

    @InputPortInfo(name = "In", dataType = Image.class)
    public InputPort in;
    @OutputPortInfo(name = "H", dataType = Image.class)
    public OutputPort outH;
    @OutputPortInfo(name = "S", dataType = Image.class)
    public OutputPort outS;
    @OutputPortInfo(name = "V", dataType = Image.class)
    public OutputPort outV;

    @Override
    public void execute() {
        Image inImg = in.getData(Image.class);
        MultiSpectral<ImageFloat32> hsv = Util.toMultiSpectralHSVImageFloat32(inImg);
        
        PixelMath.multiply(hsv.getBand(0), (float) (255.0 / (2 * Math.PI)), hsv.getBand(0));
        PixelMath.multiply(hsv.getBand(1), 255.0f, hsv.getBand(1));
        ConvertBufferedImage.convertTo(hsv.getBand(1), null);
        ConvertBufferedImage.convertTo(hsv.getBand(2), null);
        
        outH.putData(new Image(ConvertBufferedImage.convertTo(hsv.getBand(0), null)));
        outS.putData(new Image(ConvertBufferedImage.convertTo(hsv.getBand(1), null)));
        outV.putData(new Image(ConvertBufferedImage.convertTo(hsv.getBand(2), null)));
    }
}
