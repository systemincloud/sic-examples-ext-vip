package tasks.fourier;

import java.util.Arrays;

import tasks.Util;
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
import com.systemincloud.modeler.tasks.javatask.api.data.Int32;

@JavaTaskInfo
public class MultiplyDFTs extends JavaTask {

    @InputPortInfo(name = "In1", dataType = Float32.class)
    public InputPort in1;
    @InputPortInfo(name = "Filter", dataType = Float32.class, asynchronous = true)
    public InputPort filter;
    @OutputPortInfo(name = "Out", dataType = Int32.class)
    public OutputPort out;
    @OutputPortInfo(name = "Magnitude", dataType = Image.class)
    public OutputPort outMagnitude;
    @OutputPortInfo(name = "Phase", dataType = Image.class)
    public OutputPort outPhase;

    private InterleavedF32 transformFilter;
    
    @Override
    public void executeAsync(InputPort inputAsync) {
        Float32 inFilter     = inputAsync.getData(Float32.class);
        int h = inFilter.getDimensions().get(0);
        int w = inFilter.getDimensions().get(1);
        this.transformFilter = new InterleavedF32(w, h, 2);
        transformFilter.data = inFilter.getValues();
    }
    
    @Override
    public void execute() {
        if(transformFilter == null) return;
        
        Float32 in1Data = in1.getData(Float32.class);
        
        int h = in1Data.getDimensions().get(0);
        int w = in1Data.getDimensions().get(1);
        
        InterleavedF32 transform1   = new InterleavedF32(w, h, 2);
        transform1.data = in1Data.getValues();

        InterleavedF32 transformOut = new InterleavedF32(w, h, 2);
        
        DiscreteFourierTransformOps.multiplyComplex(transform1, transformFilter, transformOut);
        
        out.putData(new Float32(Arrays.asList(transformOut.height, transformOut.width), transformOut.data));
        
        // declare storage
        ImageFloat32 magnitude = new ImageFloat32(transformOut.width, transformOut.height);
        ImageFloat32 phase     = new ImageFloat32(transformOut.width, transformOut.height);
 
        Util.getMagnituteAndPhaseFromDFT(transformOut, magnitude, phase);
 
        // Convert it to a log scale for visibility
        PixelMath.log(magnitude, magnitude);
 
        outMagnitude.putData(new Image(VisualizeImageData.grayMagnitude(magnitude, null, -1)));
        outPhase    .putData(new Image(VisualizeImageData.colorizeSign(phase, null, Math.PI)));
    }

}
