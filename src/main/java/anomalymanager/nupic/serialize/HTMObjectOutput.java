package anomalymanager.nupic.serialize;

import java.io.IOException;
import java.io.OutputStream;

import anomalymanager.nupic.model.Persistable;
import org.nustaq.serialization.FSTConfiguration;
import org.nustaq.serialization.FSTObjectOutput;

public class HTMObjectOutput extends FSTObjectOutput {
    public HTMObjectOutput(OutputStream out, FSTConfiguration config) {
        super(out, config);
    }
    
    @SuppressWarnings("rawtypes")
    public void writeObject(Object t, Class... c) throws IOException {
        if(t instanceof Persistable) {
            ((Persistable) t).preSerialize();
        }
        
        super.writeObject(t, c);
    }
}