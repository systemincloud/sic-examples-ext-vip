/**
 * Based on:
 * https://github.com/lessthanoptimal/BoofCV/blob/v0.16/examples/src/boofcv/examples/imageprocessing/ExampleFourierTransform.java
 */
package tasks.fourier;

import java.util.Arrays;

import tasks.Util;
import boofcv.abst.transform.fft.DiscreteFourierTransform;
import boofcv.alg.misc.PixelMath;
import boofcv.alg.transform.fft.DiscreteFourierTransformOps;
import boofcv.gui.image.VisualizeImageData;
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
public class DFT extends JavaTask {

    @InputPortInfo(name = "In", dataType = Image.class)
    public InputPort in;
    @OutputPortInfo(name = "Out", dataType = Float32.class)
    public OutputPort out;
    @OutputPortInfo(name = "Magnitude", dataType = Image.class)
    public OutputPort outMagnitude;
    @OutputPortInfo(name = "Phase", dataType = Image.class)
    public OutputPort outPhase;

    private DiscreteFourierTransform<ImageFloat32, InterleavedF32> dft = DiscreteFourierTransformOps.createTransformF32();
    
    @Override
    public void execute() {
        Image inImg = in.getData(Image.class);
        int w = inImg.getW();
        int h = inImg.getH();
        ImageFloat32 img = Util.toImageFloat32(inImg);

        InterleavedF32 transform = new InterleavedF32(w, h, 2);

        // Make the image scaled from 0 to 1 to reduce overflow issues
        PixelMath.divide(img, 255.0f, img);
        
        // compute the Fourier Transform
        dft.forward(img, transform);
        
        // declare storage
        ImageFloat32 magnitude = new ImageFloat32(transform.width,transform.height);
        ImageFloat32 phase     = new ImageFloat32(transform.width,transform.height);
 
        Util.getMagnituteAndPhaseFromDFT(transform, magnitude, phase);
 
        // Convert it to a log scale for visibility
        PixelMath.log(magnitude, magnitude);
 
        outMagnitude.putData(new Image(VisualizeImageData.grayMagnitude(magnitude, null, -1)));
        outPhase    .putData(new Image(VisualizeImageData.colorizeSign(phase, null, Math.PI)));
        
        out.putData(new Float32(Arrays.asList(transform.height, transform.width), transform.data));
    }

}
