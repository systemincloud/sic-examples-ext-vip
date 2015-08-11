package tasks.removal;

import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.ext.vip.modeler.api.javatask.data.Image;
import com.systemincloud.modeler.tasks.javatask.api.data.Int32;

@JavaTaskInfo(onlyLocal=true)
public class AddViterbi extends JavaTask {

	@InputPortInfo(name = "ColorMap", dataType = Image.class)
	public InputPort colormap;
	@InputPortInfo(name = "Viterbi", dataType = Int32.class)
	public InputPort viterbi;
	@OutputPortInfo(name = "Out", dataType = Image.class)
	public OutputPort out;

	@Override
	public void execute(int grp) {
		Image colormapData = colormap.getData(Image.class);
		int[] colormapValues = colormapData.getValues();
		int w = colormapData.getW();
		int h = colormapData.getH();
		
		Int32 viterbiData  = viterbi .getData(Int32.class);
		int[] viterbiValues = viterbiData.getValues();
		
		int[] outValues = new int[colormapData.getNumberOfElements()];
		
		for(int i = 0; i < outValues.length; i++)
			outValues[i] = colormapValues[i];
		
		for(int i = 0; i < viterbiValues.length; i++)
			outValues[i*w + viterbiValues[i]] = 0xFFFFFF;
		
		out.putData(new Image(outValues, h, w));
	}

}
