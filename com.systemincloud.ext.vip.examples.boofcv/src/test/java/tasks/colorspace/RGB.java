package tasks.colorspace;

import tasks.Util;
import boofcv.struct.image.ImageUInt8;
import boofcv.struct.image.MultiSpectral;

import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.ext.vip.modeler.api.javatask.data.Image;

@JavaTaskInfo
public class RGB extends JavaTask {

    @InputPortInfo(name = "In", dataType = Image.class)
    public InputPort in;
    @OutputPortInfo(name = "R", dataType = Image.class)
    public OutputPort outR;
    @OutputPortInfo(name = "G", dataType = Image.class)
    public OutputPort outG;
    @OutputPortInfo(name = "B", dataType = Image.class)
    public OutputPort outB;

    @Override
    public void execute(int grp) {
        Image inImg = in.getData(Image.class);
        MultiSpectral<ImageUInt8> rgb = Util.toMultiSpectralRGBImageUInt8(inImg);
        
        outR.putData(Util.fromImageUInt8(rgb.getBand(0)));
        outG.putData(Util.fromImageUInt8(rgb.getBand(1)));
        outB.putData(Util.fromImageUInt8(rgb.getBand(2)));
    }

}
