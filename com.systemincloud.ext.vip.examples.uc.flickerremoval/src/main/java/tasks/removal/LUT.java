package tasks.removal;

import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.data.Int32;

@JavaTaskInfo
public class LUT extends JavaTask {

	@InputPortInfo(name = "Y", dataType = Int32.class)
	public InputPort y;
	@InputPortInfo(name = "table", dataType = Int32.class)
	public InputPort table;
	@OutputPortInfo(name = "YOut", dataType = Int32.class)
	public OutputPort yout;

	@Override
	public void execute() {
	}

}
