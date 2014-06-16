package tasks.fourier;

import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.data.Data;

@JavaTaskInfo
public class Trigged extends JavaTask {

    @InputPortInfo(name = "Trigger", dataType = Data.class)
    public InputPort in1;
    @InputPortInfo(name = "In2", dataType = Data.class)
    public InputPort in2;
    @OutputPortInfo(name = "Out", dataType = Data.class)
    public OutputPort out;

    @Override
    public void execute() {
        out.putData(in2.getData(Data.class));
    }

}
