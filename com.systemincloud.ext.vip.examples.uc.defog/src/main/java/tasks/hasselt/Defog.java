package tasks.hasselt;

import java.util.LinkedList;
import java.util.List;

import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.ext.vip.modeler.api.javatask.data.Image;
import com.systemincloud.modeler.tasks.javatask.api.data.Control;
import com.systemincloud.modeler.tasks.javatask.api.data.Bool;
import com.systemincloud.modeler.tasks.javatask.api.data.Float32;
import com.systemincloud.modeler.tasks.javatask.api.data.Int32;

@JavaTaskInfo
public class Defog extends JavaTask {

	@InputPortInfo(name = "In", dataType = Image.class)
	public InputPort in;
	@InputPortInfo(name = "A8", dataType = Int32.class)
	public InputPort a8;
	
	@InputPortInfo(name = "Mask", dataType = Bool.class)
	public InputPort m;
	@InputPortInfo(name = "c_i", dataType = Float32.class)
	public InputPort c_i;
	
	@InputPortInfo(name = "end", dataType = Control.class, asynchronous = true)
	public InputPort end;
	
	
	@OutputPortInfo(name = "Ack", dataType = Control.class)
	public OutputPort ack;
	
	@OutputPortInfo(name = "Out", dataType = Image.class)
	public OutputPort out;

	private List<Bool>  masks = new LinkedList<>();
	private List<Float> cs    = new LinkedList<>();
	
	private boolean initialized = false;
	private Image   img;
	private int     ar;
	private int     ag;
	private int     ab;
	
	@Override
	public void execute(int grp) {
		if(!initialized) init();
		masks.add(m.getData(Bool.class));
		cs   .add(c_i.getData(Float32.class).getValue());
		ack.putData(new Control());
	}

	private void init() {
		this.img = in.getData(Image.class);
		int[] a = a8.getData(Int32.class).getValues();
		this.ar = a[0];
		this.ag = a[1];
		this.ab = a[2];
		this.initialized = true;
	}
	
	
	@Override
	public void executeAsync(InputPort asynchIn) {
		int[] inValues = img.getValues();
		
		int[] r = new int[img.getNumberOfElements()];
		int[] g = new int[img.getNumberOfElements()];
		int[] b = new int[img.getNumberOfElements()];
		
		boolean[] fog = masks.get(masks.size() - 1).getValues();
		for(int i = 0; i < r.length; i++) {
			if(fog[i]) {
				int pixel = inValues[i];
				r[i] = (pixel >> 16 & 0xff);
				g[i] = (pixel >>  8 & 0xff);
				b[i] = (pixel & 0xff);
			}
		}
		
		for(int i = masks.size() - 2; i >= 0; i--) {
			boolean[] fog1 = masks.get(i + 1).getValues();
			boolean[] fog2 = masks.get(i).getValues();
			float c = cs.get(i).floatValue();
			for(int j = 0; j < r.length; j++) {
				if(fog2[j]) {
					int pixel = inValues[j];
					int r_tmp = (int) (((pixel >> 16 & 0xff) - c*ar)*(1/(1-c)));
					int g_tmp = (int) (((pixel >>  8 & 0xff) - c*ag)*(1/(1-c)));
					int b_tmp = (int) (((pixel & 0xff) - c*ab)*(1/(1-c)));
					if(fog2[j] && !fog1[j]) {
						r[j] = r_tmp;
						g[j] = g_tmp;
						b[j] = b_tmp;
					} else if(fog2[j] && fog1[j]) {
						r[j] = 3*(r[j] >> 2) + (r_tmp >> 2);
						g[j] = 3*(g[j] >> 2) + (g_tmp >> 2);
						b[j] = 3*(b[j] >> 2) + (b_tmp >> 2);
					}
	                if(r[j] < 0)   r[j] = 0;   if(g[j] < 0)   g[j] = 0;   if(b[j] < 0)   b[j] = 0;
	                if(r[j] > 255) r[j] = 255; if(g[j] > 255) g[j] = 255; if(b[j] > 255) b[j] = 255;
				}
			}
		}
		
		boolean[] noFog = masks.get(0).getValues();
		for(int i = 0; i < r.length; i++) {
			int pixel = inValues[i];
			if(!noFog[i]) {
				r[i] = (pixel >> 16 & 0xff);
				g[i] = (pixel >>  8 & 0xff);
				b[i] = (pixel & 0xff);
			} else {
				r[i] = 3*(r[i] >> 2) + ((pixel >> 16 & 0xff) >> 2);
				g[i] = 3*(g[i] >> 2) + ((pixel >>  8 & 0xff) >> 2);
				b[i] = 3*(b[i] >> 2) + ((pixel & 0xff) >> 2);
                if(r[i] < 0)   r[i] = 0;   if(g[i] < 0)   g[i] = 0;   if(b[i] < 0)   b[i] = 0;
                if(r[i] > 255) r[i] = 255; if(g[i] > 255) g[i] = 255; if(b[i] > 255) b[i] = 255;
			}
		}
		
		int[] outValues = new int[r.length];
		for(int i = 0; i < outValues.length; i++)
        	outValues[i] = (r[i] << 16) | (g[i] << 8) | b[i];
		out.putData(new Image(outValues, img.getH(), img.getW()));
		this.initialized = false;
	}
	
}
