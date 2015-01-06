package tasks.hasselt;

import com.systemincloud.ext.vip.modeler.api.javatask.data.Image;
import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.SicParameters;
import com.systemincloud.modeler.tasks.javatask.api.data.Control;
import com.systemincloud.modeler.tasks.javatask.api.data.Float32;
import com.systemincloud.modeler.tasks.javatask.api.annotations.SicParameter;

@JavaTaskInfo
@SicParameters({@SicParameter(name=Core.NB_OF_LAYERS)}) public class Core extends JavaTask {

	protected final static String NB_OF_LAYERS = "nb-of-layers";
	
	@InputPortInfo(name = "In", dataType = Image.class)
	public InputPort in;
	@InputPortInfo(name = "Ack", dataType = Control.class, asynchronous=true)
	public InputPort ack;
	
	@OutputPortInfo(name = "c_i", dataType = Float32.class)
	public OutputPort c_i;
	@OutputPortInfo(name = "end", dataType = Control.class)
	public OutputPort end;
	
	private int nb_lay;
	private int nb_ack;
	
	private float dc;
	private int i = 0;
	
	@Override
	public void executeAsync(InputPort asynchIn) {
		if(++nb_ack == nb_lay) end.putData(new Control());
		else c_i.putData(new Float32((++i)*dc));
	};
	
	@Override
	public void execute() {
		this.nb_ack = 0;
		this.nb_lay = Integer.parseInt(getParameter(NB_OF_LAYERS));
		this.dc = 1 / (float) (nb_lay);
		c_i.putData(new Float32((++i)*dc));
	};
}
