package tasks.removal;

import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.SicParameter;
import com.systemincloud.modeler.tasks.javatask.api.annotations.SicParameters;
import com.systemincloud.modeler.tasks.javatask.api.data.Control;
import com.systemincloud.modeler.tasks.javatask.api.data.Int32;

@JavaTaskInfo
@SicParameters({ 
	@SicParameter(name = Core.SIZE, defaultValue = "15") 
})
public class Core extends JavaTask {

	protected static final String SIZE = "size";

	@InputPortInfo(name = "End", dataType = Control.class, asynchronous = true)
	public InputPort end;
	@InputPortInfo(name = "ImgN", dataType = Int32.class, asynchronous = true)
	public InputPort imgn;
	@InputPortInfo(name = "IdxN", dataType = Int32.class, asynchronous = true)
	public InputPort idxn;
	@OutputPortInfo(name = "ImgIdx", dataType = Int32.class)
	public OutputPort imgidx;
	@OutputPortInfo(name = "IdxIdx", dataType = Int32.class)
	public OutputPort idxidx;
	@OutputPortInfo(name = "IdxOut", dataType = Int32.class)
	public OutputPort idxout;
	@OutputPortInfo(name = "ImgToRestore", dataType = Int32.class)
	public OutputPort imgtorestore;

	@Override
	public void executeAsync(InputPort asynchIn) {
		
	}

}
