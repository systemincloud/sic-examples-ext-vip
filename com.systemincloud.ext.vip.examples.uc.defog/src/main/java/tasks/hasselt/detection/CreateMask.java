package tasks.hasselt.detection;

import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.SicParameter;
import com.systemincloud.modeler.tasks.javatask.api.annotations.SicParameters;
import com.systemincloud.modeler.tasks.javatask.api.data.Bool;
import com.systemincloud.modeler.tasks.javatask.api.data.Float64;

@JavaTaskInfo
@SicParameters({
	@SicParameter(name=CreateMask.TAU)
}) public class CreateMask extends JavaTask {

	protected final static String TAU = "tau";
	
	@InputPortInfo(name = "a1", dataType = Float64.class)
	public InputPort a1;
	@InputPortInfo(name = "b1", dataType = Float64.class)
	public InputPort b1;
	@InputPortInfo(name = "a2", dataType = Float64.class)
	public InputPort a2;
	@InputPortInfo(name = "b2", dataType = Float64.class)
	public InputPort b2;
	
	@OutputPortInfo(name = "Out", dataType = Bool.class)
	public OutputPort out;

	private double tau;
	
	@Override
	public void runnerStart() {
		this.tau = Math.abs(Math.tan(0.0174532925*Integer.parseInt(getParameter(TAU))*(Math.PI*2)));
	};
	
	@Override
	public void execute(int grp) {
		Float64 a1data = a1.getData(Float64.class);
		Float64 b1data = b1.getData(Float64.class);
		Float64 a2data = a2.getData(Float64.class);
		Float64 b2data = b2.getData(Float64.class);
	    double[] a1Values = a1data.getValues();
	    double[] b1Values = b1data.getValues();
	    double[] a2Values = a2data.getValues();
	    double[] b2Values = b2data.getValues();
	    
	    //
	    // arctan(b1/a1) - arctan(b2/a2) = arctan((b1*a2 - a1*b2)/(a1*a2 + b1*b2))
	    //
	    boolean[] outValues = new boolean[a1data.getNumberOfElements()];
	    for(int i = 0; i < outValues.length; i++) {
	    	double numerator = b1Values[i]*a2Values[i] - a1Values[i]*b2Values[i];
	    	double denominator = a1Values[i]*a2Values[i] + b1Values[i]*b2Values[i];
	    	if(denominator == 0) outValues[i] = false;
	    	else                 outValues[i] = Double.compare(Math.abs(numerator/denominator), tau) < 0;
	    }
	    out.putData(new Bool(a1data.getDimensions(), outValues));
	}
}
