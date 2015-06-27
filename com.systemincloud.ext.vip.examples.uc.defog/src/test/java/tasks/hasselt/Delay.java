package tasks.hasselt;

import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.SicParameters;
import com.systemincloud.modeler.tasks.javatask.api.data.Control;
import com.systemincloud.modeler.tasks.javatask.api.annotations.SicParameter;

@JavaTaskInfo
@SicParameters({
	@SicParameter(name=Delay.DELAY)
}) public class Delay extends JavaTask {

	protected static final String DELAY = "delay";
	
	@InputPortInfo(name = "In", dataType = Control.class)
	public InputPort in;
	@OutputPortInfo(name = "Out", dataType = Control.class)
	public OutputPort out;

	@Override
	public void execute(int grp) {
		try {
			Thread.sleep(1000*Integer.parseInt(getParameter(DELAY)));
		} catch (NumberFormatException | InterruptedException e) {
			e.printStackTrace();
		}
		out.putData(in.getData(Control.class));
	}

}
