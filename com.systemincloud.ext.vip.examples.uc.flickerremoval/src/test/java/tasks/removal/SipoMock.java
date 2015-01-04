package tasks.removal;

import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.SicParameter;
import com.systemincloud.modeler.tasks.javatask.api.annotations.SicParameters;
import com.systemincloud.modeler.tasks.javatask.api.data.Int32;

@JavaTaskInfo(generator = true)
@SicParameters({ 
	@SicParameter(name = SipoMock.SIZE, defaultValue = "3") 
})
public class SipoMock extends JavaTask {

	protected static final String SIZE = "size";

	@OutputPortInfo(name = "Out", dataType = Int32.class)
	public OutputPort out;

	int size;
	int i = 1;
	
	@Override
	public void runnerStart() {
		this.size = (Integer) super.runExpression(super.getParameter(SIZE));
	}
	
	@Override
	public void generate() {
		sleep(1000);
		out.putData(new Int32(i));
		if(i < size) i++;
	}
}
