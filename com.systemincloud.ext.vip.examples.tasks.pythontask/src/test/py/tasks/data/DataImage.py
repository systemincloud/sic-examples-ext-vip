from sicpythontask.PythonTaskInfo import PythonTaskInfo
from sicpythontask.PythonTask import PythonTask
from sicpythontask.InputPort import InputPort
from sicpythontask.OutputPort import OutputPort
from sicpythontask_vip.data.Image import Image

@PythonTaskInfo
class DataImage(PythonTask):

    def __init_ports__(self):
        self.in_ = InputPort(name="in", data_type=Image)
        self.out = OutputPort(name="out", data_type=Image)
     
    def execute(self, grp):
        img = self.in_.get_data(Image)
        r = img.r
        g = img.g
        b = img.b
        self.out.put_data(Image.from_RGB(img.dims, r, g, b))
        
