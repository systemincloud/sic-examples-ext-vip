/**
 * based on http://docs.opencv.org/doc/tutorials/introduction/desktop_java/java_dev_intro.html
 */
package tasks;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.objdetect.CascadeClassifier;

import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.ext.vip.modeler.api.javatask.data.Image;

@JavaTaskInfo
public class FaceDetection extends JavaTask {

    static { System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
    
    @InputPortInfo(name = "In", dataType = Image.class)
    public InputPort in;
    @OutputPortInfo(name = "Out", dataType = Image.class)
    public OutputPort out;

    private CascadeClassifier faceDetector = new CascadeClassifier(FaceDetection.class.getResource("lbpcascade_frontalface.xml").getPath());
    
    private MatOfRect faceDetections;
    private Mat image;
    
    {
        new Thread(new Runnable() {
            public void run() {
                while(true) {
                    if(image != null) {
                        MatOfRect faceDetections = new MatOfRect();
                        faceDetector.detectMultiScale(image, faceDetections);
                        if(faceDetections.toArray().length > 0) FaceDetection.this.faceDetections = faceDetections;
                        else FaceDetection.this.faceDetections = null;
                    }
                    try { Thread.sleep(500);
                    } catch (InterruptedException e) { break; }
                }
            }
        }).start();
    }

    @Override
    public void execute() {
        Image inImg = in.getData(Image.class);
        this.image = Util.toMat(inImg);
        
        if(faceDetections != null) {
            for(Rect rect : faceDetections.toArray()) {
                Core.rectangle(image, 
                               new Point(rect.x, rect.y), 
                               new Point(rect.x + rect.width, rect.y + rect.height), 
                               new Scalar(0, 255, 0),
                               2);
            }
        }
        
        out.putData(Util.fromMat(image));
    }

}
