package tasks.removal;

import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.data.Text;
import com.systemincloud.modeler.tasks.javatask.api.data.Int32;

@JavaTaskInfo(onlyLocal = true)
public class Count extends JavaTask {

	@InputPortInfo(name = "In", dataType = Text.class)
	public InputPort in;
	@OutputPortInfo(name = "Out", dataType = Int32.class)
	public OutputPort out;

	private int i = 0;

	@Override
	public void execute(int grp) {
		in.getData(Text.class);
		i++;
		out.putData(new Int32(i));
	}

}
