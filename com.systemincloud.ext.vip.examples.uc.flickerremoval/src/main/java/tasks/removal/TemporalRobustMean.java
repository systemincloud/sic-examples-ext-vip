package tasks.removal;

import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.data.Control;
import com.systemincloud.modeler.tasks.javatask.api.data.Int32;

@JavaTaskInfo
public class TemporalRobustMean extends JavaTask {

	@InputPortInfo(name = "In", dataType = Int32.class)
	public InputPort in;
	@InputPortInfo(name = "Ctrl", dataType = Control.class, asynchronous = true)
	public InputPort ctrl;
	
	@OutputPortInfo(name = "Out", dataType = Int32.class)
	public OutputPort out;

	@Override
	public void execute() {
		log().debug("Robust mean");
	}

}
