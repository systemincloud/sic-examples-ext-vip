package tasks.hasselt;

import com.systemincloud.ext.vip.modeler.api.javatask.data.Image;
import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.SicParameters;
import com.systemincloud.modeler.tasks.javatask.api.data.Float32;

@JavaTaskInfo
@SicParameters(names={Core.NB_OF_LAYERS})
public class Core extends JavaTask {

	protected final static String NB_OF_LAYERS = "nb-of-layers";
	
	@InputPortInfo(name = "In", dataType = Image.class)
	public InputPort in;
	
	@OutputPortInfo(name = "c_i", dataType = Float32.class)
	public OutputPort c_i;
	
	@Override
	public void execute() {
		int nb   = Integer.parseInt(getParameter(NB_OF_LAYERS));
		float dc = 1 / (float) nb;
		for(int i = 1; i <= nb; i++) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) { }
			c_i.putData(new Float32(i*dc));
		}
	};
	
}
