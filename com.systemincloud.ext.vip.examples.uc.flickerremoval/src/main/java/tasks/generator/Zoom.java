package tasks.generator;

import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.SicParameter;
import com.systemincloud.modeler.tasks.javatask.api.annotations.SicParameters;
import com.systemincloud.modeler.tasks.javatask.api.data.Int32;

@JavaTaskInfo
@SicParameters({
	@SicParameter(name=Zoom.MAX),
	@SicParameter(name=Zoom.SPEED)
})
public class Zoom extends JavaTask {

	protected static final String MAX   = "max";
	protected static final String SPEED = "speed";

	@InputPortInfo(name = "In", dataType = Int32.class)
	public InputPort in;
	@OutputPortInfo(name = "Out", dataType = Int32.class)
	public OutputPort out;

	private int   mx;
	private float sp;
	
	private float   zoom    = 1;
	private boolean goingIn = true;
	
	@Override
	public void runnerStart() {
		mx = Integer.parseInt(getParameter(MAX));
		sp = Float.parseFloat(getParameter(SPEED));
	}
	
	@Override
	public void execute(int grp) {
		Int32 inData    = in.getData(Int32.class);
		int[] inValues  = inData.getValues();
		int   ne        = inData.getNumberOfElements();
		int   h         = inData.getDimensions().get(0);
		int   w         = inData.getDimensions().get(1);
		
		int[] outValues = new int[ne];
		
		float inv_zoom = 1/zoom;
		int tmpH = (int) (zoom*h);
		int tmpW = (int) (zoom*w);
		int[] tmp = new int[tmpH*tmpW];
		int ptr = 0;
		for(int i = 0; i < tmpH; i++)
			for(int j = 0; j < tmpW; j++)
				tmp[ptr++] = inValues[((int)(i*inv_zoom))*w + ((int)(j*inv_zoom))];

		int y = (tmpH - h) >> 1;
		int x = (tmpW - w) >> 1;
		ptr = 0;
		for(int i = 0; i < h; i++) {
			int idx = y*tmpW + i*tmpW + x;
			for(int j = 0; j < w; j++)
				outValues[ptr++] = tmp[idx + j];
		}

		if(goingIn) zoom += sp;
		else        zoom -= sp;

		if     (zoom > mx)   goingIn = false;
		else if(zoom <= 1) { goingIn = true; zoom = 1; }
		
		out.putData(new Int32(inData.getDimensions(), outValues));
	}
}
