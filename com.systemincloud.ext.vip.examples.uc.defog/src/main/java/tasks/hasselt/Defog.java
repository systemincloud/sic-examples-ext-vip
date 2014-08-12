package tasks.hasselt;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

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
	
	@InputPortInfo(name = "end", dataType = Control.class, asynchronus = true)
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
	public void execute() {
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

//		out.putData(masks.get(0));
//		System.out.println(cs.get(0));
		ListIterator<Bool> li = masks.listIterator(masks.size() - 1);
		while(li.hasPrevious()) {
			out.putData(li.previous());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
//		int[] inValues = img.getValues();
//		
//		int[] r = new int[img.getNumberOfElements()];
//		int[] g = new int[img.getNumberOfElements()];
//		int[] b = new int[img.getNumberOfElements()];
//		
//		for(int i = masks.size() - 2; i >= 0; i--) {
//			boolean[] fog1 = masks.get(i + 1).getValues();
//			boolean[] fog2 = masks.get(i).getValues();
//			float c = cs.get(i).floatValue();
//			for(int j = 0; j < r.length; j++) {
//				if(fog2[j] && !fog1[j]) {
//					int pixel = inValues[j];
//					r[j] = (int) (((pixel >> 16 & 0xff) - c*ar)*(1/(1-c)));
//					g[j] = (int) (((pixel >>  8 & 0xff) - c*ar)*(1/(1-c)));
//					b[j] = (int) (((pixel & 0xff) - c*ar)*(1/(1-c)));
//	                if(r[j] < 0) r[j] = 0;
//	                if(g[j] < 0) g[j] = 0;
//	                if(b[j] < 0) b[j] = 0;
//	                if(r[j] > 255) r[j] = 255;
//	                if(g[j] > 255) g[j] = 255;
//	                if(b[j] > 255) b[j] = 255;
//				}
//			}
//		}
//		
//		boolean[] noFog = masks.get(0).getValues();
//		for(int i = 0; i < r.length; i++) {
//			if(!noFog[i]) {
//				int pixel = inValues[i];
//				r[i] = (pixel >> 16 & 0xff);
//				g[i] = (pixel >>  8 & 0xff);
//				b[i] = (pixel & 0xff);
//			}
//		}
		
//		ListIterator<Bool>  mi = masks.listIterator(masks.size());
//		ListIterator<Float> ci = cs.listIterator(cs.size());
//		int x = 0;
		
//		while(mi.hasPrevious()) {
//			if(x++ == 2) break;
//			boolean[] m = mi.previous().getValues();
//			float     c = ci.previous().floatValue();
//			for(int i = 0; i < r.length; i++) {
				
//				if(m[i]) {
//					int pixel = inValues[i];
//					r[i] = (int) (r[i] + (pixel >> 16 & 0xff) - c*ar);
//					g[i] = (int) (g[i] + (pixel >>  8 & 0xff) - c*ag);
//					b[i] = (int) (b[i] + (pixel & 0xff) - c*ab);

//					r[i] = r[i] + (int)(c*ar*(pixel >> 16 & 0xff));
//					g[i] = g[i] + (int)(c*ag*(pixel >> 8 & 0xff));
//					b[i] = b[i] + (int)(c*ab*(pixel & 0xff));
//				}
//			}
			
//				int pixel = inPixels[i];
////                r[i] = (pixel >> 16 & 0xff);
////                g[i] = (pixel >> 8 & 0xff);
////                b[i] = (pixel & 0xff);
//                r[i] = (int) ((pixel >> 16 & 0xff) - (Math.pow(0.4, (1/c)))*ar);
//                g[i] = (int) ((pixel >> 8 & 0xff)  - (Math.pow(0.4, (1/c)))*ag);
//                b[i] = (int) ((pixel & 0xff)       - (Math.pow(0.4, (1/c)))*ab);

//			} else 
//		}
//		}
		
//		int[] outValues = new int[r.length];
//		for(int i = 0; i < outValues.length; i++)
//        	outValues[i] = (r[i] << 16) | (g[i] << 8) | b[i];
//		out.putData(new Image(outValues, img.getH(), img.getW()));
//		this.initialized = false;
	}
	
	
//		if(initialized) {
//			int[] outValues = new int[r.length];
//            
//			for(int i = 0; i < outValues.length; i++)
//                outValues[i] = (r[i] << 16) | (g[i] << 8) | b[i];
//			
////			out.putData(new Image(outValues, img.getH(), img.getW()));
//			this.r = null;
//			this.g = null;
//			this.b = null;
//			this.initialized = false;
//		}
//	};
	
//	@Override
//	public void execute() {
//		m.getData(Bool.class)
//		if(!initialized) init();
//		
//		int[] inPixels = img.getValues();
//		boolean[] mask = m.getData(Bool.class).getValues();
//		float c = c_i.getData(Float32.class).getValue(); 
//		System.out.println(c);
//		if(lastMask == null) {
//			for(int i = 0; i < r.length; i++) {
//				if(!mask[i]) {
//					int pixel = inPixels[i];
//	                r[i] = (pixel >> 16 & 0xff);
//	                g[i] = (pixel >> 8 & 0xff);
//	                b[i] = (pixel & 0xff);
//				}
//			}
//		} else {
//			for(int i = 0; i < r.length; i++) {
//				if(!mask[i] && lastMask[i]) {
//					int pixel = inPixels[i];
////	                r[i] = (pixel >> 16 & 0xff);
////	                g[i] = (pixel >> 8 & 0xff);
////	                b[i] = (pixel & 0xff);
//	                r[i] = (int) ((pixel >> 16 & 0xff) - (Math.pow(0.4, (1/c)))*ar);
//	                g[i] = (int) ((pixel >> 8 & 0xff)  - (Math.pow(0.4, (1/c)))*ag);
//	                b[i] = (int) ((pixel & 0xff)       - (Math.pow(0.4, (1/c)))*ab);
//	                if(r[i] < 0) r[i] = 0;
//	                if(g[i] < 0) g[i] = 0;
//	                if(b[i] < 0) b[i] = 0;
//				} else 
//			}
//		}
//		this.lastMask = mask;
//		this.lastC = c;
//		
//		int[] outValues = new int[r.length];
//		for(int i = 0; i < outValues.length; i++)
//            outValues[i] = (r[i] << 16) | (g[i] << 8) | b[i];
//		out.putData(new Image(outValues, img.getH(), img.getW()));
//	}
}
