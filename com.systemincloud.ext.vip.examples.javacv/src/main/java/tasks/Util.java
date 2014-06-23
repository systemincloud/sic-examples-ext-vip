package tasks;

import java.awt.image.BufferedImage;

import org.bytedeco.javacpp.opencv_core.IplImage;

import com.systemincloud.ext.vip.modeler.api.javatask.data.Image;

public class Util {

    public static IplImage toIplImage(Image img) {
        BufferedImage image = new BufferedImage(img.getW(), img.getH(), BufferedImage.TYPE_INT_RGB);
        image.getRaster().setDataElements(0, 0, img.getW(), img.getH(), img.getValues());
        IplImage ipl = IplImage.createFrom(image);
        return ipl;
    }

    public static Image fromIplImage(IplImage ipl) {
        return new Image(ipl.getBufferedImage());
    }
}
