package tasks.hasselt.detection;

import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.SicParameters;
import com.systemincloud.modeler.tasks.javatask.api.data.Bool;
import com.systemincloud.modeler.tasks.javatask.api.data.Float64;

@JavaTaskInfo
@SicParameters(names={CreateMask.TAU})
public class CreateMask extends JavaTask {

	protected final static String TAU = "tau";
	
	@InputPortInfo(name = "In1", dataType = Float64.class)
	public InputPort in1;
	@InputPortInfo(name = "In2", dataType = Float64.class)
	public InputPort in2;
	@OutputPortInfo(name = "Out", dataType = Bool.class)
	public OutputPort out;

	private int tau;
	
	@Override
	public void runnerStart() {
		this.tau = Integer.parseInt(getParameter(TAU));
	};
	
	@Override
	public void execute() {
		Float64 in1data = in1.getData(Float64.class);
		Float64 in2data = in2.getData(Float64.class);
	    double[] in1Values = in1data.getValues();
	    double[] in2Values = in2data.getValues();
	    
	    boolean[] outValues = new boolean[in1data.getNumberOfElements()];
	    for(int i = 0; i < outValues.length; i++)
	    	outValues[i] = Double.compare(Math.abs(in1Values[i] - in2Values[i]), tau) < 0;
	    out.putData(new Bool(in1data.getDimensions(), outValues));
	}
}
