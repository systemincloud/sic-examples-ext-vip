package tasks.removal;

import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.data.Int32;

@JavaTaskInfo
public class DmxMock extends JavaTask {

	@InputPortInfo(name = "In", dataType = Int32.class, asynchronous = true)
	public InputPort in;
	@InputPortInfo(name = "Idx", dataType = Int32.class)
	public InputPort idx;

	@Override
	public void executeAsync(InputPort asynchIn) {
		asynchIn.getData(Int32.class);
	}

	@Override
	public void execute() {
		in.getData(Int32.class);
	}

}
