package com.systemincloud.ext.vip.examples.opencl;

import static org.bridj.Pointer.pointerToInts;

import java.io.IOException;

import org.bridj.Pointer;

import com.nativelibs4java.opencl.CLBuffer;
import com.nativelibs4java.opencl.CLContext;
import com.nativelibs4java.opencl.CLEvent;
import com.nativelibs4java.opencl.CLKernel;
import com.nativelibs4java.opencl.CLMem.Usage;
import com.nativelibs4java.opencl.CLProgram;
import com.nativelibs4java.opencl.CLQueue;
import com.nativelibs4java.opencl.JavaCL;
import com.nativelibs4java.util.IOUtils;
import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.data.Int32;


@JavaTaskInfo
public class MovingAverage extends JavaTask {

	@InputPortInfo(name = "in", dataType = Int32.class)
	public InputPort in;
	@OutputPortInfo(name = "out", dataType = Int32.class)
	public OutputPort out;
	
	int[] averageBackground;
	
	private CLContext context = JavaCL.createBestContext();
    private CLQueue queue = context.createDefaultQueue();
    CLKernel averageKernel;
	
	@Override
	public void runnerStart(){
		String device = queue.getDevice().toString();
		System.out.println("OpenCL device: " + device);
		
		String src;
		try {
			src = IOUtils.readText(MovingAverage.class.getResource("Average.cl"));
			CLProgram program = context.createProgram(src);
			averageKernel = program.createKernel("average");
		} catch (IOException e) {
			e.printStackTrace();
		}
        
	}

	@Override
	public void execute(int grp) {
        
		Int32 frame = in.getData(Int32.class);
		int[] frameData = frame.getValues();
		if(averageBackground == null){
			averageBackground = new int[frameData.length];
		} else {
			Pointer<Integer> 
			    aPtr = pointerToInts(frameData),
			    bPtr = pointerToInts(averageBackground);

			CLBuffer<Integer> 
                a = context.createIntBuffer(Usage.Input, aPtr),
                b = context.createIntBuffer(Usage.Input, bPtr);
			
			CLBuffer<Integer> out = context.createIntBuffer(Usage.Output, frameData.length);
			
			int[] globalSizes = new int[] { frameData.length };
			averageKernel.setArgs(a, b, out, frameData.length);
			CLEvent avgEvt = averageKernel.enqueueNDRange(queue, globalSizes);
			
			Pointer<Integer> outPtr = out.read(queue, avgEvt);
			
			averageBackground = outPtr.getInts();
			
		}
		out.putData(new Int32(frame.getDimensions(), averageBackground));
	}

}
