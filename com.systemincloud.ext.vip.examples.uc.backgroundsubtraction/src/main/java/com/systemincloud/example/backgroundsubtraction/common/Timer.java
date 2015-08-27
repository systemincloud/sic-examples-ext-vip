package com.systemincloud.example.backgroundsubtraction.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;

@JavaTaskInfo
public class Timer extends JavaTask {
	
	private long startTime;
	private long stopTime;

	@Override
	public void runnerStart() {
		startTime = System.currentTimeMillis();
	}

	@Override
	public void runnerStop() {
		stopTime = System.currentTimeMillis();
		Date date = new Date(stopTime - startTime);
		DateFormat formatter = new SimpleDateFormat("mm:ss:SSS");
		String dateFormatted = formatter.format(date);
		System.out.println("Total time:" + dateFormatted);
	}

}
