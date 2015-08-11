package com.systemincloud.ext.vip.examples.opencl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.data.Int32;

@JavaTaskInfo(generator = true)
public class NoiseGenerator extends JavaTask {

	@OutputPortInfo(name = "out", dataType = Int32.class)
	public OutputPort out;
	private static final int WIDTH = 1280;
	private static final int HEIGTH = 720;
	private static final List<Integer> dimmensions = new ArrayList<Integer>();
	private static final int framenumber = 50;
	private Random generator = new Random();
	
	int randomFrames[][] = new int[framenumber][WIDTH * HEIGTH];

	@Override
	public void runnerStart(){
		sleep(0);
		dimmensions.add(WIDTH);
		dimmensions.add(HEIGTH);
		for (int frame = 0; frame < framenumber; frame++) {
			for (int pixel = 0; pixel < WIDTH * HEIGTH; pixel++) {
				randomFrames[frame][pixel] = generator.nextBoolean() ? 0 : 0xFF;
				//randomFrames[frame][pixel] = generator.nextInt(0xFF);
			}
		}
	}
	
	@Override
	public void generate() {
		out.putData(new Int32(dimmensions, randomFrames[generator.nextInt(framenumber)]));
	}

}
