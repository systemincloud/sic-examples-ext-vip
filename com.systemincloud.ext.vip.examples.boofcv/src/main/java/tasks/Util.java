package tasks;

import boofcv.struct.image.ImageUInt8;

import com.systemincloud.ext.vip.modeler.api.javatask.data.Image;
import com.systemincloud.modeler.tasks.javatask.api.data.Data;

public class Util {
    
    public static ImageUInt8 toImageUInt8(Image img) {
        int w = img.getW();
        int h = img.getH();
        int[] inData = img.getValues();
        ImageUInt8 ret = new ImageUInt8(w, h);
        int k = 0;
        for(int j = 0; j < h; j++) {
            for(int i = 0; i < w; i++) {
                int pixel = inData[k++];
                int r = (pixel >> 16 & 0xff);
                int g = (pixel >> 8 & 0xff);
                int b = (pixel & 0xff);
                ret.set(i, j, (r + g + b)/3);
            }
        }
        return ret;
    }

    public static Image fromImageUInt8(ImageUInt8 img) {
        int w = img.getWidth();
        int h = img.getHeight();
        int[] outData = new int[w*h];
        int k = 0;
        for(int j = 0; j < h; j++) {
            for(int i = 0; i < w; i++) {
                int p = img.get(i, j);
                outData[k++] = (p << 16) | (p << 8) | p;
            }
        }
        return new Image(outData, h, w);
    }

    public static Data fromBinary(ImageUInt8 img) {
        int w = img.getWidth();
        int h = img.getHeight();
        int[] outData = new int[w*h];
        int k = 0;
        for(int j = 0; j < h; j++) {
            for(int i = 0; i < w; i++) {
                int p = img.get(i, j);
                p ^= 1; p *= 255;
                outData[k++] = (p << 16) | (p << 8) | p;
            }
        }
        return new Image(outData, h, w);
    }
}
