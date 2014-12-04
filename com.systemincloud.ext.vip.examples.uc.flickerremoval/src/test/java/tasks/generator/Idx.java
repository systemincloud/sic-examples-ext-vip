package tasks.generator;

import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.data.Control;
import com.systemincloud.modeler.tasks.javatask.api.data.Int32;

@JavaTaskInfo
public class Idx extends JavaTask {

	@InputPortInfo(name = "In", dataType = Control.class)
	public InputPort in;
	@InputPortInfo(name = "RST", dataType = Control.class, asynchronous = true)
	public InputPort rst;
	@OutputPortInfo(name = "Out", dataType = Int32.class)
	public OutputPort out;

	private int idx = 0;
	
	@Override
	public void execute() {
		out.putData(new Int32(idx));
		idx++;
	}
	
	@Override
	public void executeAsync(InputPort asynchIn) {
		idx = 0;
	}
}
