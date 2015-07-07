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
	@SicParameter(name=HugeObject.COLOR),
	@SicParameter(name=HugeObject.SPEED)
})
public class HugeObject extends JavaTask {

	protected static final String COLOR = "color";
	protected static final String SPEED = "speed";

	@InputPortInfo(name = "In", dataType = Int32.class)
	public InputPort in;
	@OutputPortInfo(name = "Out", dataType = Int32.class)
	public OutputPort out;

	private int cl;
	private int sp;
	
	private int x;
	private int y;
	
	private boolean goingUp = false;
	
	@Override
	public void runnerStart() {
		cl = Integer.parseInt(getParameter(COLOR));
		sp = Integer.parseInt(getParameter(SPEED));
	}
	
	@Override
	public void execute(int grp) {
		Int32 inData    = in.getData(Int32.class);
		int[] inValues  = inData.getValues();
		int   ne        = inData.getNumberOfElements();
		int   h         = inData.getDimensions().get(0);
		int   w         = inData.getDimensions().get(1);
		
		int[] outValues = new int[ne];

		for(int i = 0; i < h; i++)
			for(int j = 0; j < w; j++) {
				int idx = i*w + j;
				if(i < y && j < x) outValues[idx] = cl;
				else               outValues[idx] = inValues[idx];
			}

		if(goingUp) { x -= sp; y -= sp; } 
		else        { x += sp; y += sp; }

		if( (y > h) || (x > w) || (y < 0) || (x < 0)) goingUp = !goingUp;

		out.putData(new Int32(inData.getDimensions(), outValues));
	}
}
