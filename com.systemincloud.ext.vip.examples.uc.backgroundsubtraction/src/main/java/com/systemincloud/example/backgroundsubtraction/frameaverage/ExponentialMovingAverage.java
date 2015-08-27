package com.systemincloud.example.backgroundsubtraction.frameaverage;

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
@SicParameters({ @SicParameter(name = ExponentialMovingAverage.LEARNING_CONSTANT, defaultValue = "0.1") })
public class ExponentialMovingAverage extends JavaTask {

	@InputPortInfo(name = "video", dataType = Int32.class)
	public InputPort video;
	@OutputPortInfo(name = "average", dataType = Int32.class)
	public OutputPort average;
	
	int[] averageBackground;
	double learningConstant;
	
	protected static final String LEARNING_CONSTANT = "LearningConstant";
	
	@Override
	public void runnerStart() {
		learningConstant = Double.parseDouble(getParameter(LEARNING_CONSTANT));
	}

	@Override
	public void execute(int grp) {
		Int32 frame = video.getData(Int32.class);
		int[] frameData = frame.getValues();
		if(averageBackground == null){
			averageBackground = frameData;
		} else {
			for(int i = 0; i < frame.getNumberOfElements(); i++){
				averageBackground[i] = 	(int) (learningConstant * frameData[i] + (1 - learningConstant) * averageBackground[i]);
			}
		}
		average.putData(new Int32(frame.getDimensions(), averageBackground));
	}

}
