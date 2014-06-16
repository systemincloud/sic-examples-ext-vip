package tasks.fourier;

import tasks.Util;
import boofcv.abst.transform.fft.DiscreteFourierTransform;
import boofcv.alg.transform.fft.DiscreteFourierTransformOps;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.InterleavedF32;

import com.systemincloud.ext.vip.modeler.api.javatask.data.Image;
import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.data.Float32;

@JavaTaskInfo
public class IDFT extends JavaTask {

    @InputPortInfo(name = "In", dataType = Image.class)
    public InputPort in;
    @OutputPortInfo(name = "Out", dataType = Image.class)
    public OutputPort out;

    private DiscreteFourierTransform<ImageFloat32, InterleavedF32> dft = DiscreteFourierTransformOps.createTransformF32();
    
    @Override
    public void execute() {
        Float32 inData = in.getData(Float32.class);
        
        int h = inData.getDimensions().get(0);
        int w = inData.getDimensions().get(1);
        
        ImageFloat32 filteredImage = new ImageFloat32(w, h);
        
        InterleavedF32 transform   = new InterleavedF32(w, h, 2);
        transform.data = inData.getValues();
        
        dft.inverse(transform, filteredImage);
        
        out.putData(Util.fromImageFloat32(filteredImage, 255));
    }

}
