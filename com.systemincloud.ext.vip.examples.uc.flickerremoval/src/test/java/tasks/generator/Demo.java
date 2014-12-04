package tasks.generator;

import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.ext.vip.modeler.api.javatask.data.Image;

@JavaTaskInfo
public class Demo extends JavaTask {

	@InputPortInfo(name = "ImgOrg", dataType = Image.class, queue = -1)
	public InputPort imgorg;
	@InputPortInfo(name = "ImgNew", dataType = Image.class, queue = -1)
	public InputPort imgnew;
	@OutputPortInfo(name = "Out", dataType = Image.class)
	public OutputPort out;

	@Override
	public void execute() {
	}

}
