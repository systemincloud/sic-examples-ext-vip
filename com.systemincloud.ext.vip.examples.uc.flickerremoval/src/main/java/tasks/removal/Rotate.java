package tasks.removal;

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
	@SicParameter(name = Rotate.DEGRES,     defaultValue = "0"),
	@SicParameter(name = Rotate.NEW_DOMAIN, defaultValue = "361")
})
public class Rotate extends JavaTask {

	protected static final String DEGRES     = "degres";
	protected static final String NEW_DOMAIN = "domain";

	@InputPortInfo(name = "In", dataType = Int32.class)
	public InputPort in;
	@OutputPortInfo(name = "Out", dataType = Int32.class)
	public OutputPort out;

	public double cosPhi;
	public double sinPhi;

	public int    domain;

	@Override
	public void runnerStart() {
		double deg = Double.parseDouble(getParameter(DEGRES));
		double rad = Math.toRadians(deg);
		cosPhi = Math.cos(rad);
		sinPhi = Math.sin(rad);

		domain = Integer.parseInt(getParameter(NEW_DOMAIN));
	}

	@Override
	public void execute() {
		log().debug("Rotate");
		Int32 inData    = in.getData(Int32.class);
		int[] inValues  = inData.getValues();
		int[] outValues = rotate(inValues);
		out.putData(new Int32(inData.getDimensions(), outValues));
	}

	public int[] rotate(int[] inValues) {
		double[] x = new double[inValues.length];
		double[] y = new double[inValues.length];

		for(int i = 0; i < inValues.length; i++) {
			x[i] =           i*cosPhi - inValues[i]*sinPhi;
			y[i] = inValues[i]*cosPhi +           i*sinPhi;
		}

		int[] outValues = new int[domain];

		for(int i = 0; i < outValues.length; i++) {
			double sum = 0;
			int    n   = 0;
			for(int k = 0; k < x.length; k++) {
				if(Math.round(x[k]) == i) {
					sum += y[k];
					n++;
				}
			}
			outValues[i] = n > 0 ? (int) Math.round(sum / n) : 0;
		}
		outValues[0] = 0;

		for(int i = 1; i < outValues.length; i++) {
			if(outValues[i] == 0) {
				int n = 1;
				int v = 0;
				while(true)
					if(i + n >= outValues.length - 1) break;
					else if(outValues[i + n] != 0) {
						v = outValues[i + n] - outValues[i - 1];
						n++;
						break;
					} else n++;
				
				for(int j = 1; j <= n; j++)
					outValues[i + n  - j - 1]  = outValues[i + n - 1] - (int) Math.ceil(j * (double) v / (double) n);
				i += n - 1;
			}
		}

		return outValues;
	}
}
