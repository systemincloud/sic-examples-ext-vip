package tasks;

import boofcv.alg.color.ColorHsv;
import boofcv.alg.color.ColorYuv;
import boofcv.alg.transform.fft.DiscreteFourierTransformOps;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageUInt8;
import boofcv.struct.image.InterleavedF32;
import boofcv.struct.image.MultiSpectral;

import com.systemincloud.ext.vip.modeler.api.javatask.data.Image;
import com.systemincloud.modeler.tasks.javatask.api.data.Data;

public class Util {
    
    public static ImageUInt8 toImageUInt8(Image img) {
        int w = img.getW(); int h = img.getH();
        int[] inData = img.getValues();
        ImageUInt8 ret = new ImageUInt8(w, h);
        int k = 0;
        for(int j = 0; j < h; j++) {
            for(int i = 0; i < w; i++) {
                int pixel = inData[k++];
                int r = (pixel >> 16 & 0xff); int g = (pixel >> 8 & 0xff); int b = (pixel & 0xff);
                ret.set(i, j, (r + g + b)/3);
            }
        }
        return ret;
    }

    public static ImageFloat32 toImageFloat32(Image img) {
        int w = img.getW(); int h = img.getH();
        int[] inData = img.getValues();
        ImageFloat32 ret = new ImageFloat32(w, h);
        int k = 0;
        for(int j = 0; j < h; j++) {
            for(int i = 0; i < w; i++) {
                int pixel = inData[k++];
                int r = (pixel >> 16 & 0xff); int g = (pixel >> 8 & 0xff); int b = (pixel & 0xff);
                ret.set(i, j, (r + g + b)/3);
            }
        }
        return ret;
    }
    
    public static MultiSpectral<ImageUInt8> toMultiSpectralRGBImageUInt8(Image img) {
        int w = img.getW(); int h = img.getH();
        int[] inData = img.getValues();
        MultiSpectral<ImageUInt8> ret = new MultiSpectral<>(ImageUInt8.class, w, h, 3);
        ImageUInt8 m0 = ret.getBand(0); ImageUInt8 m1 = ret.getBand(1); ImageUInt8 m2 = ret.getBand(2);
        int k = 0;
        for(int j = 0; j < h; j++) {
            for(int i = 0; i < w; i++) {
                int pixel = inData[k++];
                m0.set(i, j, pixel >> 16 & 0xff); m1.set(i, j, pixel >> 8 & 0xff); m2.set(i, j, pixel & 0xff);
            }
        }
        return ret;
    }
    
    public static MultiSpectral<ImageFloat32> toMultiSpectralYUVImageFloat32(Image img) {
        int w = img.getW(); int h = img.getH();
        int[] inData = img.getValues();
        MultiSpectral<ImageFloat32> ret = new MultiSpectral<>(ImageFloat32.class, w, h, 3);
        ImageFloat32 m0 = ret.getBand(0); ImageFloat32 m1 = ret.getBand(1); ImageFloat32 m2 = ret.getBand(2);
        int k = 0;
        for(int j = 0; j < h; j++) {
            for(int i = 0; i < w; i++) {
                int pixel = inData[k++];
                float[] pixelYuv = new float[3];
                ColorYuv.rgbToYuv(pixel >> 16 & 0xff, pixel >> 8 & 0xff, pixel & 0xff, pixelYuv);
                m0.set(i, j, pixelYuv[0]);
                m1.set(i, j, pixelYuv[1]);
                m2.set(i, j, pixelYuv[2]);
            }
        }
        return ret;
    }
    
    public static MultiSpectral<ImageFloat32> toMultiSpectralHSVImageFloat32(Image img) {
        int w = img.getW(); int h = img.getH();
        int[] inData = img.getValues();
        MultiSpectral<ImageFloat32> ret = new MultiSpectral<>(ImageFloat32.class, w, h, 3);
        ImageFloat32 m0 = ret.getBand(0); ImageFloat32 m1 = ret.getBand(1); ImageFloat32 m2 = ret.getBand(2);
        int k = 0;
        for(int j = 0; j < h; j++) {
            for(int i = 0; i < w; i++) {
                int pixel = inData[k++];
                float[] pixelHSV = new float[3];
                ColorHsv.rgbToHsv(pixel >> 16 & 0xff, pixel >> 8 & 0xff, pixel & 0xff, pixelHSV);
                if(Float.isNaN(pixelHSV[0]) || Float.isInfinite(pixelHSV[0])) m0.unsafe_set(i, j,0);
                else                                                          m0.set(i, j, pixelHSV[0]);
                m1.set(i, j, pixelHSV[1]);
                m2.set(i, j, pixelHSV[2]);
            }
        }
        return ret;
    }
    
    public static Image fromImageUInt8(ImageUInt8 img) {
        int w = img.getWidth(); int h = img.getHeight();
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
    
    public static Data fromImageFloat32(ImageFloat32 img, int scale) {
        int w = img.getWidth(); int h = img.getHeight();
        int[] outData = new int[w*h];
        int k = 0;
        for(int j = 0; j < h; j++) {
            for(int i = 0; i < w; i++) {
                int p = (int) (img.get(i, j) * scale);
                outData[k++] = (p << 16) | (p << 8) | p;
            }
        }
        return new Image(outData, h, w);
    }
    
    public static Data fromImageFloat32(ImageFloat32 img) {
        return fromImageFloat32(img, 1);
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

    public static void getMagnituteAndPhaseFromDFT(InterleavedF32 transform, ImageFloat32 magnitude, ImageFloat32 phase) {
        // Make a copy so that you don't modify the input
        InterleavedF32 tmp = transform.clone();
 
        // shift the zero-frequency into the image center, as is standard in image processing
        DiscreteFourierTransformOps.shiftZeroFrequency(tmp, true);
 
        // Compute the transform's magnitude and phase
        DiscreteFourierTransformOps.magnitude(tmp, magnitude);
        DiscreteFourierTransformOps.phase(tmp, phase);
    }
}
