package tasks.fourier;

import java.util.Arrays;

import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.data.Float32;

@JavaTaskInfo(constant = true)
public class Box extends JavaTask {

    private static int SIZE = 15;
    
    @OutputPortInfo(name = "Out", dataType = Float32.class)
    public OutputPort out;

    @Override
    public void execute(int grp) {
        float[] outData = new float[SIZE*SIZE];
        
        int k = 0;
        for(int y = 0; y < SIZE; y++) {
            for(int x = 0; x < SIZE; x++ ) {
                outData[k++] = 1.0f;
            }
        }
        
        out.putData(new Float32(Arrays.asList(SIZE, SIZE), outData));
    }
}
