package tasks.removal;

import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.data.Int32;
import com.systemincloud.modeler.tasks.javatask.api.data.Text;

@JavaTaskInfo
public class DmxMock extends JavaTask {

	@InputPortInfo(name = "In", dataType = Int32.class, asynchronous = true)
	public InputPort in;
	@InputPortInfo(name = "Idx", dataType = Int32.class)
	public InputPort idx;
	@OutputPortInfo(name = "Out", dataType = Text.class)
	public OutputPort out;

	private int i;

	@Override
	public void executeAsync(InputPort asynchIn) {
		log().debug("executeAsync");
		out.putData(new Text(i + "_" + asynchIn.getData(Int32.class).getValue()));
	}

	@Override
	public void execute() {
		log().debug("execute");
		i = idx.getData(Int32.class).getValue();
		log().debug("change i to {}", i);
	}

}
