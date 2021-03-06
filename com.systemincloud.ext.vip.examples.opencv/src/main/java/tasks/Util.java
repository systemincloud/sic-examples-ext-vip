package tasks;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import com.systemincloud.ext.vip.modeler.api.javatask.data.Image;

public class Util {

    public static Mat toMat(Image img) {
        Mat mat = new Mat(img.getH(), img.getW(), CvType.CV_8UC4);
        ByteBuffer byteBuffer = ByteBuffer.allocate(img.getNumberOfElements() * 4);        
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(img.getValues());
        byte[] array = byteBuffer.array();
        mat.put(0, 0, array);
        return mat;
    }

    public static Image fromMat(Mat mat) {
        byte[] buff = new byte[(int) (mat.total() * mat.channels())];
        mat.get(0, 0, buff);
        IntBuffer intBuf = ByteBuffer.wrap(buff)
                                     .order(ByteOrder.BIG_ENDIAN)
                                     .asIntBuffer();
        int[] array = new int[intBuf.remaining()];
        intBuf.get(array);
        return new Image(array, mat.height(), mat.width());
    }
}
