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
	@OutputPortInfo(name = "Dmx", dataType = Int32.class)
	public OutputPort dmx;
	@OutputPortInfo(name = "IdxF", dataType = Int32.class)
	public OutputPort idxF;
	@OutputPortInfo(name = "Mean", dataType = Int32.class)
	public OutputPort mean;
	
	private int size;
	
	private int j = 0;

	@Override
	public void runnerStart() {
		this.size = Integer.parseInt(getParameter(SIZE));
		log().debug("Size is {}", size);
	}
	
	@Override
	public void execute() {
		int n1 = imgn.getData(Int32.class).getValue();
		int n2 = idxn.getData(Int32.class).getValue();

		log().info("There are {} elements in Img register", n1);
		log().info("There are {} elements in Idx register", n2);

		if(n1 == size + 1) {
			log().info("Start processing frame");

			j = j == size ? size : j + 1;
			mean.putData(new Int32(j));

			dmx   .putData(new Int32(1));
			imgidx.putData(new Int32(size));
			for(int i = 0; i < size; i++) {
				dmx   .putData(new Int32(0));
				imgidx.putData(new Int32(i));
			}
			
			log().debug("Choose fs for calculation");
			for(int i = 0; i < size; i++) {
				log().debug("i {}", i);
				idxF.putData(new Int32(i));
			}

			for(int i = 0; i < size; i++) {
				log().debug("AAAAAAAAAA {}", i);
				if(i == j - 1)  break;
				log().debug("i {}", 2*size - 1 + i*(size-1));
				idxF.putData(new Int32(2*size - 1 + i*(size-1)));
			}
			
			log().debug("Send index");
			idxidx.putData(new Int32(size));
			log().info("End processing frame");
		}
	}
	
	
	@Override
	public void executeAsync(InputPort asynchIn) {
		if(asynchIn == end) {
			for(int j = size + 1; j > 0; j--) {
				
			}
		}
	}
}
