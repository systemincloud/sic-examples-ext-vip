package com.systemincloud.example.backgroundsubtraction.selectiveframeaverage;

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
@SicParameters({ @SicParameter(name = SelectiveMean.LEARNING_CONSTANT, defaultValue = "0.1") })
public class SelectiveMean extends JavaTask {

	@InputPortInfo(name = "video", dataType = Int32.class, group = 1)
	public InputPort video;
	@InputPortInfo(name = "feedback", dataType = Int32.class, group = 2)
	public InputPort feedback;
	@OutputPortInfo(name = "background", dataType = Int32.class)
	public OutputPort background;

	int[] averageBackground;
	int[] feedbackFrame;
	double learningConstant;
	
	protected static final String LEARNING_CONSTANT = "LearningConstant";
	
	@Override
	public void runnerStart() {
		learningConstant = Double.parseDouble(getParameter(LEARNING_CONSTANT));
	}
	
	@Override
	public void execute(int grp) {
		if(grp == 1) {
			Int32 frame = video.getData(Int32.class);
			int[] frameData = frame.getValues();
	
			if (feedbackFrame == null) {
				feedbackFrame = new int[frame.getNumberOfElements()];// first run with no feedback, initialized by default with zeroes
			}
			if (averageBackground == null) {
				averageBackground = frameData;
			} else {
				for (int i = 0; i < frame.getNumberOfElements(); i++) {
					if (feedbackFrame[i] == 0x00) {//do not add foreground pixels to background model
						averageBackground[i] = (int) (learningConstant * frameData[i] + (1 - learningConstant) * averageBackground[i]);
					}
				}
				background.putData(new Int32(frame.getDimensions(),	averageBackground));
			}
		} else if(grp == 2) {
			Int32 frame = feedback.getData(Int32.class);
			feedbackFrame = frame.getValues();
		}
	}

}