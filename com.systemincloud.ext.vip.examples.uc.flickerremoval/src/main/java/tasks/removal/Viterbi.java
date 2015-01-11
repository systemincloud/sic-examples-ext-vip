package tasks.removal;

import java.util.Arrays;

import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.data.Int32;
import com.systemincloud.modeler.tasks.javatask.api.data.Int64;

@JavaTaskInfo
public class Viterbi extends JavaTask {

	@InputPortInfo(name = "In", dataType = Int32.class)
	public InputPort in;
	@OutputPortInfo(name = "Out", dataType = Int32.class)
	public OutputPort out;

	@OutputPortInfo(name = "W", dataType = Int64.class)
	public OutputPort outW;
	
	@Override
	public void execute() {
		log().debug("Viterbi");
		Int32 inData = in.getData(Int32.class);
		int size = inData.getDimensions().get(0);
		int[] inValues = inData.getValues();
		
		long tmp[] = createTempTable(inValues, size);
		int[] outValues = generateF(tmp, size);
		
		out.putData(new Int32(Arrays.asList(size), outValues));
		
		outW.putData(new Int64(Arrays.asList(size, size), tmp));
	}

	public long[] createTempTable(int[] inValues, int size) {
		long table[] = new long[size*size];
		
		for(int i = 1; i < size; i++) {
			long maxFromLineBeforeUpToJ = 0;
			for(int j = 1; j < size - 1; j++) {
				long OneUp   = i != 1 ? table[i*size + j - size] : 0;
				long OneLeft = table[i*size + j - 1];
				if(OneUp > maxFromLineBeforeUpToJ) maxFromLineBeforeUpToJ = OneUp;
				table[i*size + j] = (maxFromLineBeforeUpToJ > OneLeft ? maxFromLineBeforeUpToJ : OneLeft) + inValues[i*size + j];
			}
			table[i*size + size - 1] = table[i*size + size - 2];
		}
		return table;
	}
	
	public int[] generateF(long[] tmp, int size) {
		int[] out = new int[size];
		
		out[size - 1] = size - 1;
		for(int i = size - 2; i > 0; i--) {
			int j = out[i + 1];
			int idx = i*size + j;
			long l = tmp[idx];
			int m = 0;
			while(m < j && tmp[idx - m - 1] == l) m++;
			int n = 0;
			while(n < i && tmp[idx - n*size - size] == l) n++;
			if(m != 0 && n != 0) {
				for(int z = 0; z <= n; z++) {
					int tmpJ = (int) (j - m + Math.ceil(z * (double) n / (double) m));
					out[i - n + z] = tmpJ;
				}
				i -= n - 1;
			} else if(n != 0) {
				for(int z = 0; z <= n; z++) out[i--] = j;
			} else if(m != 0) out[i] = j - m;
			else out[i] = j;
		}
		out[0] = 0;
		return out;
	}

}
