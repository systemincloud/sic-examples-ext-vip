package tasks.removal;

import java.util.Arrays;

import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.data.Int32;

@JavaTaskInfo
public class JointHistogram extends JavaTask {

	@InputPortInfo(name = "In1", dataType = Int32.class)
	public InputPort in1;
	@InputPortInfo(name = "In0", dataType = Int32.class)
	public InputPort in2;
	
	@OutputPortInfo(name = "Out", dataType = Int32.class)
	public OutputPort out;
	
	@Override
	public void execute() {
		log().debug("Count joint historgram");
		
		Int32 in1Data = in1.getData(Int32.class);
		Int32 in2Data = in2.getData(Int32.class);
		
		int[] in1Values = in1Data.getValues();
		int[] in2Values = in2Data.getValues();
		
		int size = in1Data.getNumberOfElements();
		
		int[] outValues = new int[256*256];
		
		for(int i = 0; i < size; i++) {
			int pos = in1Values[i] + in2Values[i]*256;
			outValues[pos]++;
		}
		
		out.putData(new Int32(Arrays.asList(256, 256), outValues));
	}
}
