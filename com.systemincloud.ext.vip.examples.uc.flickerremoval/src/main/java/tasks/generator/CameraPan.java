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
	@SicParameter(name=CameraPan.SPEED)
})
public class CameraPan extends JavaTask {

	protected static final String SPEED = "speed";

	@InputPortInfo(name = "In", dataType = Int32.class)
	public InputPort in;
	@OutputPortInfo(name = "Out", dataType = Int32.class)
	public OutputPort out;

	private int sp;
	
	private int x = 0;
	private int y;
	
	private boolean goingRight = true;
	
	@Override
	public void runnerStart() {
		sp = Integer.parseInt(getParameter(SPEED));
	}
	
	@Override
	public void execute() {
		Int32 inData    = in.getData(Int32.class);
		int[] inValues  = inData.getValues();
		int   ne        = inData.getNumberOfElements();
		int   h         = inData.getDimensions().get(0);
		int   w         = inData.getDimensions().get(1);
		
		int[] outValues = new int[ne];

		int[] tmp = new int[4*h*w];

		int tmpH = 2*h;
		int tmpW = 2*w;
		int ptr = 0;
		for(int i = 0; i < tmpH; i++)
			for(int j = 0; j < tmpW; j++)
				tmp[ptr++] = inValues[(i >> 1)*w + (j >> 1)];

		y = h >> 1;

		for(int i = 0; i < h; i++) {
			int idx = y*tmpW + i*tmpW + x;
			for(int j = 0; j < w; j++) outValues[i*w + j] = tmp[idx + j];
		}

		if(goingRight) x += sp;
		else           x -= sp;

		if(x > w) {
			goingRight = false;
			x = w;
		} else if(x < 0){
			goingRight = true;
			x = 0;
		}
		
		out.putData(new Int32(inData.getDimensions(), outValues));
	}
}
