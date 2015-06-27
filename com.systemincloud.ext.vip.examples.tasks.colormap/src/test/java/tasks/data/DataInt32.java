package tasks.data;

import java.util.Arrays;

import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.data.Int32;

@JavaTaskInfo(constant = true)
public class DataInt32 extends JavaTask {

	@OutputPortInfo(name = "Out", dataType = Int32.class)
	public OutputPort out;

	private int w = 300;
	private int h = 200;
	
	@Override
	public void execute(int grp) {
		int[] values = new int[w*h];
		int k = 0;
		for(int i = 0; i < h; i++)
			for(int j = 0; j < w; j++)
				values[k++] = j;
		out.putData(new Int32(Arrays.asList(new Integer[] {h, w}), values));
	}

}
