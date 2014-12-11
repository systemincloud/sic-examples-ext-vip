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
	@SicParameter(name = Core.SIZE, defaultValue = "7") 
})
public class Core extends JavaTask {

	protected static final String SIZE = "size";

	@InputPortInfo(name = "ImgN", dataType = Int32.class)
	public InputPort imgn;
	@InputPortInfo(name = "IdxN", dataType = Int32.class)
	public InputPort idxn;
	@InputPortInfo(name = "End", dataType = Control.class, asynchronous = true)
	public InputPort end;
	
	@OutputPortInfo(name = "ImgIdx", dataType = Int32.class)
	public OutputPort imgidx;
	@OutputPortInfo(name = "IdxIdx", dataType = Int32.class)
	public OutputPort idxidx;
	@OutputPortInfo(name = "IdxOut", dataType = Int32.class)
	public OutputPort idxout;
	@OutputPortInfo(name = "Dmx1", dataType = Int32.class)
	public OutputPort dmx1;
	@OutputPortInfo(name = "IdxF", dataType = Int32.class)
	public OutputPort idxF;
	
	private int size;
	
	@Override
	public void runnerStart() {
		this.size = Integer.parseInt(getParameter(SIZE));
	}
	
	@Override
	public void execute() {
		int n1 = imgn.getData(Int32.class).getValue();
		int n2 = idxn.getData(Int32.class).getValue();
		
		log().trace("There are {} elements in Img register", n1);
		log().trace("There are {} elements in Idx register", n2);
		
		if(n1 == size + 1) {
			dmx1.putData(new Int32(1));
//			idxidx
//			for()
			
//			std::list<util::SP<Library::Data> >::iterator it = elements.begin();
//			for(unsigned int i = 0; i < core->imageToRestore; ++i ) ++it;
//
//			core->putData("ImageToRestore", &core->imageToRestore, sizeof(core->imageToRestore),1, 1);
//			core->putData("IdxOut", *(it));
//			core->putData("IdxRegOut", core->getData("IdxRegIn"));
//			core->putData("ImgRegOut", core->getData("ImgRegIn"));
		}
	}
	
	
	@Override
	public void executeAsync(InputPort asynchIn) {
		if(asynchIn == end) {
			
		}

	}
}
