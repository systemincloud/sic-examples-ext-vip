package tasks.removal;

import java.util.LinkedList;
import java.util.List;

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
	@SicParameter(name = TemporalRobustMean.SIZE, defaultValue = "7") 
})
public class TemporalRobustMean extends JavaTask {

	protected static final String SIZE = "size";

	@InputPortInfo(name = "In", dataType = Int32.class)
	public InputPort in;
	@InputPortInfo(name = "N", dataType = Int32.class, hold=true)
	public InputPort n;
	
	@OutputPortInfo(name = "Out", dataType = Int32.class)
	public OutputPort out;

	private int size;
	
	private List<int[]> fs = new LinkedList<>();

	private int i = 0;

	
	@Override
	public void runnerStart() {
		this.size = Integer.parseInt(getParameter(SIZE));
		log().debug("Size is {}", size);
	}

	@Override
	public void execute() {
		log().debug("Robust mean");
		Int32 inData   = in.getData(Int32.class);
		int[] inValues = inData.getValues();
		int nValue = n.getData(Int32.class).getValue();
		
		if(i < size) {
			fs.add(in.getData(Int32.class).getValues());
			i++;
			log().debug("There is {} fs", i);
		} else if(i <= size >> 1) {
			int[] tmp = new int[inValues.length];
			for(int j = 0; j < tmp.length; j++) tmp[j] = inValues[inValues.length - 1 - j];
			fs.add(in.getData(Int32.class).getValues());
			i++;
			log().debug("There is {} fs", i);
		} 
		
		if(i - size == nValue) {
			log().debug("Do mean");
			int[] outValues = new int[inValues.length];
			for(int[] f : fs)
				for(int j = 0; j < outValues.length; j++) outValues[j] += f[j];
			
			for(int j = 0; j < outValues.length; j++) outValues[j] /= fs.size();
			
			// reset
			fs = new LinkedList<>();
			i = 0;
			
			out.putData(new Int32(inData.getDimensions(), outValues));
		}
	}
}
