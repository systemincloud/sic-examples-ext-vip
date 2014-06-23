package tasks;

import java.awt.image.BufferedImage;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.IplImage;

import com.systemincloud.ext.vip.modeler.api.javatask.data.Image;

public class Util {

    public static IplImage toIplImage(Image img) {
        BufferedImage image = new BufferedImage(img.getW(), img.getH(), BufferedImage.TYPE_INT_ARGB);
        image.getRaster().setDataElements(0, 0, img.getW(), img.getH(), img.getValues());
        IplImage ipl = IplImage.createFrom(image);
        return ipl;
    }

    public static CvMat toCvMat(Image img) {
        CvMat mat = CvMat.create(img.getH(), img.getW(), 0, 4);
        int i = 0;
        for(int v : img.getValues()) {
            mat.put(i++, (v >> 24) & 0xff);
            mat.put(i++, (v >> 16) & 0xff);
            mat.put(i++, (v >> 8) & 0xff);
            mat.put(i++, v & 0xff);
        }
        return mat;
    }
    
    public static Image fromIplImage(IplImage ipl) {
        return new Image(ipl.getBufferedImage());
    }

    public static Image fromCvMat(CvMat mat) {
//        int[] array = new int[mat.width() * mat.height()];
//        int idx = 0;
//        for(int i = 0; i < array.length; i++) {
//            array[i] = (((int) mat.get(idx++)) << 16) | (((int) mat.get(idx++)) << 8) | ((int)(mat.get(idx++)));
//        }
//        return new Image(array, mat.height(), mat.width());
        
        IntBuffer intBuf = mat.getByteBuffer()
                                     .order(ByteOrder.BIG_ENDIAN)
                                     .asIntBuffer();
        int[] array = new int[intBuf.remaining()];
        intBuf.get(array);
        return new Image(array, mat.height(), mat.width());
    }
}
