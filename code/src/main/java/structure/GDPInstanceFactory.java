package structure;

import grafo.optilib.structure.InstanceFactory;

public class GDPInstanceFactory extends InstanceFactory<GDPInstance> {
    @Override
    public GDPInstance readInstance(String s) {
        return new GDPInstance(s);
    }
}
