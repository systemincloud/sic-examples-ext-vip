package tasks.generator;

import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.ext.vip.modeler.api.javatask.data.Image;

@JavaTaskInfo
public class Blotches extends JavaTask {

	@InputPortInfo(name = "In", dataType = Image.class)
	public InputPort in;
	@OutputPortInfo(name = "Out", dataType = Image.class)
	public OutputPort out;

	@Override
	public void execute() {
		out.putData(in.getData(Image.class));
	}

}
