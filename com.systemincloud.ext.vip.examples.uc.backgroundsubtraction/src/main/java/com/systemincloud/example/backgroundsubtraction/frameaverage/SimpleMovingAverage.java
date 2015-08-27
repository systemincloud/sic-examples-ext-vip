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
@SicParameters({ @SicParameter(name = SimpleMovingAverage.WINDOW_SIZE, defaultValue = "20") })
public class SimpleMovingAverage extends JavaTask {
	private class Queue {
		private int windowSize;
		private int sampleCount = 0;
		private int[][] data;
		private int[] total;
		private int length;
		
        private int nextInsert = 0;
		
		Queue(int windowSize, int length){
			this.windowSize = windowSize;
			this.length = length;
			data = new int[windowSize][length];//primitives - not null
			total = new int[length];
		}
		
		void insert(int[] frame){
			if(sampleCount == windowSize){
				for(int index = 0; index < length; index++){
					total[index] -= data[nextInsert][index];
				}
			} else{
				sampleCount++;
			}
			for(int index = 0; index < length; index++){
				total[index] += frame[index];
			}
			
			data[nextInsert] = frame;
			//rotate over array, overwrite oldest value
			nextInsert = (nextInsert == windowSize -1) ? 0 : nextInsert + 1;
		}
		int[] getAverage(){
			int[] average = new int[length];
			for(int index = 0; index < length; index++){
				average[index] = total[index] / sampleCount;
			}
			return average;
		}
	}

	@InputPortInfo(name = "video", dataType = Int32.class)
	public InputPort video;
	@OutputPortInfo(name = "average", dataType = Int32.class)
	public OutputPort average;
	
	
	private int windowSize;
	private Queue queue;
	
	protected static final String WINDOW_SIZE = "WindowSize";
	
	@Override
	public void runnerStart() {
		windowSize = Integer.parseInt(getParameter(WINDOW_SIZE));
		
	}

	@Override
	public void execute(int grp) {
		Int32 frame = video.getData(Int32.class);
		int[] frameData = frame.getValues();
		if(queue == null){
			queue = new Queue(windowSize, frameData.length);
		} 
		queue.insert(frameData);
		
		average.putData(new Int32(frame.getDimensions(), queue.getAverage()));
	}

}
