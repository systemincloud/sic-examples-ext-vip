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
	@SicParameter(name=Flicker.NON_LINEARITY),
	@SicParameter(name=Flicker.MAGNITUDE),
	@SicParameter(name=Flicker.PERIOD)
})
public class Flicker extends JavaTask {

	protected static final String NON_LINEARITY  = "non-linearity";
	protected static final String MAGNITUDE      = "magnitude";
	protected static final String PERIOD         = "period";
	
	@InputPortInfo(name = "In", dataType = Int32.class)
	public InputPort in;
	@OutputPortInfo(name = "Out", dataType = Int32.class)
	public OutputPort out;

	private int   nl;
	private float mg;
	private int   pd;
	
	private int[] data;
	
	private int frameCounter = 0;
	
	@Override
	public void runnerStart() {
		nl = Integer.parseInt(getParameter(NON_LINEARITY));
		mg = Float.parseFloat(getParameter(MAGNITUDE));
		pd = Integer.parseInt(getParameter(PERIOD));
		
		data = new int[pd];

		for(int i = 0; i < pd; i++) data[i] = (int)(15*Math.sin(i));
	}
	
	@Override
	public void execute() {
		Int32 inData    = in.getData(Int32.class);
		int[] inValues  = inData.getValues();
		int   ne        = inData.getNumberOfElements();
		
		int[] outValues = new int[ne];
		
		int beta = data[frameCounter];
		if(nl != 0) beta = beta*(4 << nl);
		for(int i = 0; i < ne; i++) {
			if(nl != 0) {
				int tmp = inValues[i] - 128;
				double scale = tmp < 0 ? -tmp : tmp;
				scale = 128 - scale;
				scale = mg*0.2*Math.sin(scale*3.14/256)*beta;
				outValues[i] = (int) (inValues[i] +  scale);
			} else outValues[i] = (int) (inValues[i] + mg*beta);
			
			if(outValues[i] > 255) outValues[i] = 255;
			if(outValues[i] < 0 )  outValues[i] = 0;	
		}

		++frameCounter;
		frameCounter = frameCounter%pd;
		
		out.putData(new Int32(inData.getDimensions(), outValues));
	}
}
