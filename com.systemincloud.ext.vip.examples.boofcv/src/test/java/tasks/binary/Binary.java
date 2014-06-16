/**
 * Based on:
 * https://github.com/lessthanoptimal/BoofCV/blob/v0.16/examples/src/boofcv/examples/imageprocessing/ExampleBinaryOps.java
 */
package tasks.binary;

import java.awt.image.BufferedImage;
import java.util.List;

import tasks.Util;
import boofcv.alg.filter.binary.BinaryImageOps;
import boofcv.alg.filter.binary.Contour;
import boofcv.alg.filter.binary.ThresholdImageOps;
import boofcv.alg.misc.ImageStatistics;
import boofcv.gui.binary.VisualizeBinaryData;
import boofcv.struct.image.ImageSInt32;
import boofcv.struct.image.ImageUInt8;

import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.ext.vip.modeler.api.javatask.data.Image;

@JavaTaskInfo
public class Binary extends JavaTask {

    @InputPortInfo(name = "In", dataType = Image.class)
    public InputPort in;
    @OutputPortInfo(name = "Grey", dataType = Image.class)
    public OutputPort outGrey;
    @OutputPortInfo(name = "Binary", dataType = Image.class)
    public OutputPort outBinary;
    @OutputPortInfo(name = "Filtered", dataType = Image.class)
    public OutputPort outFiltered;
    @OutputPortInfo(name = "Label", dataType = Image.class)
    public OutputPort outLabel;
    @OutputPortInfo(name = "Contour", dataType = Image.class)
    public OutputPort outContour;

    @Override
    public void execute() {
        Image inImg = in.getData(Image.class);
        ImageUInt8 img = Util.toImageUInt8(inImg);
        
        ImageUInt8  binary = new ImageUInt8 (img.width, img.height);
        ImageSInt32 label  = new ImageSInt32(img.width,img.height);
        
        int mean = (int) ImageStatistics.mean(img);
        ThresholdImageOps.threshold(img, binary, mean, true);
        
        ImageUInt8 filtered = BinaryImageOps.erode8(binary,null);
        filtered = BinaryImageOps.dilate8(filtered, null);
        
        List<Contour> contours = BinaryImageOps.contour(filtered, 8, label);
        
        int colorExternal = 0xFFFFFF;
        int colorInternal = 0xFF2020;
        
        BufferedImage visualLabel   = VisualizeBinaryData.renderLabeled(label, contours.size(), null);
        BufferedImage visualContour = VisualizeBinaryData.renderContours(contours,colorExternal,colorInternal, img.width, img.height,null);
        
        outGrey    .putData(Util.fromImageUInt8(img));
        outBinary  .putData(Util.fromBinary(binary));
        outFiltered.putData(Util.fromBinary(filtered));
        outLabel   .putData(new Image(visualLabel));
        outContour .putData(new Image(visualContour));
    }

}
