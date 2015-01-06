package tasks.generator;

import java.util.Random;

import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.SicParameter;
import com.systemincloud.modeler.tasks.javatask.api.annotations.SicParameters;
import com.systemincloud.modeler.tasks.javatask.api.data.Int32;

@JavaTaskInfo
@SicParameters({
	@SicParameter(name=Blotches.COLOR),
	@SicParameter(name=Blotches.SIZE),
	@SicParameter(name=Blotches.NUMBER)
})
public class Blotches extends JavaTask {

	protected static final String COLOR  = "color";
	protected static final String SIZE   = "size";
	protected static final String NUMBER = "number";
	
	private Random rand = new Random();
	
	@InputPortInfo(name = "In", dataType = Int32.class)
	public InputPort in;
	@OutputPortInfo(name = "Out", dataType = Int32.class)
	public OutputPort out;

	private int cl;
	private int nb;
	private int sz;
	
	@Override
	public void runnerStart() {
		cl = Integer.parseInt(getParameter(COLOR));
		nb = Integer.parseInt(getParameter(NUMBER));
		sz = Integer.parseInt(getParameter(SIZE));
	}
	
	@Override
	public void execute() {
		Int32 inData    = in.getData(Int32.class);
		int[] inValues  = inData.getValues();
		int   ne        = inData.getNumberOfElements();
		int   h         = inData.getDimensions().get(0);
		int   w         = inData.getDimensions().get(1);
		
		int[] outValues = new int[ne];
		
		for(int i = 0; i < ne; i++) outValues[i] = inValues[i];
		
		int nob = rand.nextInt(2 * nb);

		for(int i = 0; i < nob; i++) {
			int bs = rand.nextInt(2 * sz);
			int x = rand.nextInt(w - bs);
			int y = rand.nextInt(h - bs);
			for(int j = 0; j < bs; j++)
				for(int k = 0; k < bs; k++)
					outValues[y*w + j*w + x + k] = cl;
		}
		
		out.putData(new Int32(inData.getDimensions(), outValues));
	}
}
