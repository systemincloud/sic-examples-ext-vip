package tasks.generator;

import java.util.ArrayList;
import java.util.Random;

import org.javatuples.Pair;

import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.SicParameters;
import com.systemincloud.modeler.tasks.javatask.api.data.Int32;

@JavaTaskInfo
@SicParameters(names = { LocalMotions.NUMBER, 
		                 LocalMotions.SIZE,
		                 LocalMotions.SPEED })
public class LocalMotions extends JavaTask {

	protected static final String NUMBER = "number";
	protected static final String SIZE   = "size";
	protected static final String SPEED  = "speed";
	
	private Random rand = new Random();
	
	@InputPortInfo(name = "In", dataType = Int32.class)
	public InputPort in;
	@OutputPortInfo(name = "Out", dataType = Int32.class)
	public OutputPort out;
	
	private int nb;
	private int sz;
	private int sp;
	
	private boolean initialized = false;
	private ArrayList<Pair<Integer, Integer>> initPositions;
	private ArrayList<Pair<Integer, Integer>> vectors;
	
	private int[] mask;
	
	@Override
	public void runnerStart() {
		nb = Integer.parseInt(getParameter(NUMBER));
		sz = Integer.parseInt(getParameter(SIZE));
		sp = Integer.parseInt(getParameter(SPEED));
		initPositions = new ArrayList<>(nb);
		vectors       = new ArrayList<>(nb);
	}
	
	@Override
	public void execute() {
		Int32 inData    = in.getData(Int32.class);
		int[] inValues  = inData.getValues();
		int   ne        = inData.getNumberOfElements();
		int   h         = inData.getDimensions().get(0);
		int   w         = inData.getDimensions().get(1);

		int[] outValues = new int[ne];
		
		if(!initialized) initMask(h, w);
		
		for(int i = 0; i < ne; i++) outValues[i] = inValues[i];

		for(int i = 1; i <= nb; i++)
			for(int j = 0; j < ne; j++)
				if(mask[j] == i)
					outValues[j + vectors.get(i - 1).getValue1()*w + vectors.get(i - 1).getValue0()] = inValues[j];
		
		for(int i = 0; i < nb; i++) {
			int move = rand.nextInt(9);
			Pair<Integer, Integer> p = vectors.get(i);
			int new_x = 0;
			int new_y = 0;
			switch(move) {
				case 0 : new_x = p.getValue0() - sp; new_y = p.getValue1() - sp; break;
				case 1 : new_x = p.getValue0() - sp; new_y = p.getValue1()     ; break;
				case 2 : new_x = p.getValue0() - sp; new_y = p.getValue1() + sp; break;
				case 3 : new_x = p.getValue0()     ; new_y = p.getValue1() - sp; break;
				case 4 : new_x = p.getValue0()     ; new_y = p.getValue1()     ; break;
				case 5 : new_x = p.getValue0()     ; new_y = p.getValue1() + sp; break;
				case 6 : new_x = p.getValue0() + sp; new_y = p.getValue1() - sp; break;
				case 7 : new_x = p.getValue0() + sp; new_y = p.getValue1()     ; break;
				case 8 : new_x = p.getValue0() + sp; new_y = p.getValue1() + sp; break;
			}
			if((initPositions.get(i).getValue0() + new_x) < 0) new_x = -initPositions.get(i).getValue0(); 
			if((initPositions.get(i).getValue1() + new_y) < 0) new_y = -initPositions.get(i).getValue1(); 
			if((initPositions.get(i).getValue0() + sz + new_x) >= w) new_x = w - initPositions.get(i).getValue0() - sz; 
			if((initPositions.get(i).getValue1() + sz + new_y) >= h) new_y = h - initPositions.get(i).getValue1() - sz;
			
			vectors.add(i, new Pair<>(new_x, new_y));
		}
		out.putData(new Int32(inData.getDimensions(), outValues));
	}

	private void initMask(int h, int w) {
		mask = new int[h*w];
		for(int i = 1; i <= nb; i++) {
			vectors.add(new Pair<Integer, Integer>(0, 0));
		    int x = rand.nextInt(w - sz);
		    int y = rand.nextInt(h - sz);
		    initPositions.add(new Pair<Integer, Integer>(x, y));
			for(int j = 0; j < h; j++)
				for(int k = 0; k < w; k++)
					if((j >= y) && (j < y + sz) && (k >= x) && (k < x + sz)) mask[j*w + k] = i;
		}
		initialized = true;
	}
}
