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

	private int   interval;
	private Image img;
	
	@Override
	public void runnerStart() {
		pause();
		this.interval = (int) (1000 / Float.parseFloat(getParameter(RATE)));
	}
	
	@Override
	public void generate() {
		sleep(interval);
		out.putData(img);
	}

	@Override
	public void execute(int grp) {
		this.img = in.getData(Image.class);
		resume();
	}
}
