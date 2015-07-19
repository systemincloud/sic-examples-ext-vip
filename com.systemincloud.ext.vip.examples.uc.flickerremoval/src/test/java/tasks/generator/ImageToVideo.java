package tasks.generator;

import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.SicParameter;
import com.systemincloud.modeler.tasks.javatask.api.annotations.SicParameters;
import com.systemincloud.ext.vip.modeler.api.javatask.data.Image;

@JavaTaskInfo(generator = true)
@SicParameters({ @SicParameter(name = ImageToVideo.RATE, defaultValue = "25") })
public class ImageToVideo extends JavaTask {

	protected static final String RATE = "rate";

	@InputPortInfo(name = "In", dataType = Image.class)
	public InputPort in;
	@OutputPortInfo(name = "Out", dataType = Image.class)
	public OutputPort out;

	@Override
	public void runnerStart() {
		
	}
	
	@Override
	public void generate() {
	}

	@Override
	public void execute(int grp) {
	}

}
