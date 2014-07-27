package tasks.bool;

import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.data.Bool;

@JavaTaskInfo
public class And extends JavaTask {

	@InputPortInfo(name = "In1", dataType = Bool.class)
	public InputPort in1;
	@InputPortInfo(name = "In2", dataType = Bool.class)
	public InputPort in2;
	@OutputPortInfo(name = "Out", dataType = Bool.class)
	public OutputPort out;

	@Override
	public void execute() {
	}

}
