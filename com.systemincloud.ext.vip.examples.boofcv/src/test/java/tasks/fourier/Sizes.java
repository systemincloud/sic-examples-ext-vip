package tasks.fourier;

import com.systemincloud.modeler.tasks.javatask.api.InputPort;
import com.systemincloud.modeler.tasks.javatask.api.JavaTask;
import com.systemincloud.modeler.tasks.javatask.api.OutputPort;
import com.systemincloud.modeler.tasks.javatask.api.annotations.InputPortInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.JavaTaskInfo;
import com.systemincloud.modeler.tasks.javatask.api.annotations.OutputPortInfo;
import com.systemincloud.ext.vip.modeler.api.javatask.data.Image;
import com.systemincloud.modeler.tasks.javatask.api.data.Int32;

@JavaTaskInfo
public class Sizes extends JavaTask {

    @InputPortInfo(name = "In", dataType = Image.class)
    public InputPort in;
    @OutputPortInfo(name = "H", dataType = Int32.class)
    public OutputPort h;
    @OutputPortInfo(name = "W", dataType = Int32.class)
    public OutputPort w;

    @Override
    public void execute() {
        Image img = in.getData(Image.class);
        h.putData(new Int32(img.getH()));
        w.putData(new Int32(img.getW()));
    }
}
