from sicpythontask.PythonTaskInfo import PythonTaskInfo
from sicpythontask.PythonTask import PythonTask
from sicpythontask.InputPort import InputPort
from sicpythontask.OutputPort import OutputPort
from sicpythontask.data.Image import Image

@PythonTaskInfo
class DataImage(PythonTask):

    def __init_ports__(self):
        self.in_ = InputPort(name="in", data_type=Image)
        self.out = OutputPort(name="out", data_type=Image)
     
    def execute(self, grp):
        """ this will run when all synchronous ports 
            from group receive data """
        
