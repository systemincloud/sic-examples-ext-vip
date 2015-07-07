package tasks.fourier;

import tasks.Util;
import boofcv.struct.image.ImageFloat32;

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
public class PrepareFilterImage extends JavaTask {

    @InputPortInfo(name = "In", dataType = Float32.class)
    public InputPort in;
    @InputPortInfo(name = "W", dataType = Int32.class)
    public InputPort w;
    @InputPortInfo(name = "H", dataType = Int32.class)
    public InputPort h;
    @OutputPortInfo(name = "Out", dataType = Image.class)
    public OutputPort out;

    @Override
    public void execute(int grp) {
        Float32 inData = in.getData(Float32.class);
        float[] inValues = inData.getValues();
        
        float sum = 0; for(float f : inValues) sum += f;
        for(int i = 0; i < inValues.length; i++) inValues[i] = 255 * inValues[i] / sum;
        
        ImageFloat32 filterImage = new ImageFloat32(w.getData(Int32.class).getValue(), h.getData(Int32.class).getValue());
        
        int size0 = inData.getDimensions().get(0);
        int halfSize0 = (size0 >> 1);
        int size1 = inData.getDimensions().get(1);
        int halfSize1 = (size1 >> 1);
        
        int k = 0;
        for(int y = 0; y < size0; y++) {
            int yy = y - halfSize0 < 0 ? filterImage.height + (y - halfSize0) : y - halfSize0;
            for(int x = 0; x < size1; x++ ) {
                int xx = x - halfSize1 < 0 ? filterImage.width + (x - halfSize1) : x - halfSize1;
                filterImage.set(xx, yy, inValues[k]);
            }
        }
        
        out.putData(Util.fromImageFloat32(filterImage));
    }
}
