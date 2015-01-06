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
public class Viterbi extends JavaTask {

	@InputPortInfo(name = "In", dataType = Int32.class)
	public InputPort in;
	@OutputPortInfo(name = "Out", dataType = Int32.class)
	public OutputPort out;

	@Override
	public void execute() {
		log().debug("Viterbi");
		Int32 inData = in.getData(Int32.class);
		int[] inValues = inData.getValues();
		
		
		
		int[] outValues = new int[256];
		out.putData(new Int32(Arrays.asList(256), outValues));
	}

}
