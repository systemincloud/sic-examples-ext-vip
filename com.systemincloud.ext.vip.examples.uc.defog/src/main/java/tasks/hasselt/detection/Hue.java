package tasks.hasselt.detection;

import com.systemincloud.ext.vip.modeler.api.javatask.data.Image;
import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.data.Float64;

@JavaTaskInfo
public class Hue extends JavaTask {

	@InputPortInfo(name = "In", dataType = Image.class)
	public InputPort in;
	@OutputPortInfo(name = "Out", dataType = Float64.class)
	public OutputPort out;

    private static final double x_wp = .950456;
    private static final double y_wp = 1;
    private static final double z_wp = 1.088754;
    private static final double th   = .008856;
	
	@Override
	public void execute() {
		Image img = in.getData(Image.class);
        int[] inValues = img.getValues();
        double[] outValues = new double[img.getNumberOfElements()];
        for(int i = 0; i < outValues.length; i++) {
            int inRGB = inValues[i];
            int r = (inRGB >> 16 & 0xff);
            int g = (inRGB >> 8 & 0xff);
            int b = (inRGB & 0xff);
            
            double x = .412453*r+.357580*g+.180423*b;
            double y = .212671*r+.715160*g+.072169*b;
            double z = .019334*r+.119193*g+.950227*b;
            
            double x_n = x/x_wp;
            double y_n = y/y_wp;
            double z_n = z/z_wp;
            
            double a  = 500.*(f(x_n)-f(y_n));
            double b_ = 500.*(f(y_n)-f(z_n));
            
            if     (a > 100) a = 100;
            else if(a <-100) a = -100;
            if     (b_>100)  b_= 100;
            else if(b_<-100) b_= -100;

            outValues[i] = 57.29*arctan(a,b_)/(Math.PI*2);
        }
        out.putData(new Float64(img.getDimensions(), outValues));
	}
	
    private static double f(double x) {
        return x > th ? Math.pow(x,1./3) : 7.787*x+16./116;
    }
    
    private static double arctan(double a, double b) {
        double an;
        if(a == 0) {
            if     (b > 0) an = Math.PI/2;
            else if(b < 0) an = Math.PI*3./2;
            else           an = 0;
        } else if(a >= 0)  an =  Math.atan(b/a);
        else               an = Math.PI + Math.atan(b/a);
        
        if(an < 0) an += 2*Math.PI;
        return an;
    }
}
