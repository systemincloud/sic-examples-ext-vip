package tasks;

import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.data.Bool;
import com.systemincloud.modeler.tasks.javatask.api.data.Int32;

@JavaTaskInfo
public class Equal extends JavaTask {

	@InputPortInfo(name = "a", dataType = Int32.class)
	public InputPort a;
	@InputPortInfo(name = "b", dataType = Int32.class)
	public InputPort b;
	@OutputPortInfo(name = "o", dataType = Bool.class)
	public OutputPort o;

	@Override
	public void execute() {
		if(a.getData(Int32.class).getValue() == b.getData(Int32.class).getValue())
			o.putData(new Bool(true));
		else o.putData(new Bool(false));
	}

}
