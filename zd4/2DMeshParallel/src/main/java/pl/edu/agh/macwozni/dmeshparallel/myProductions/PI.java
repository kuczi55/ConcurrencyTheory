package pl.edu.agh.macwozni.dmeshparallel.myProductions;

import pl.edu.agh.macwozni.dmeshparallel.mesh.Vertex;
import pl.edu.agh.macwozni.dmeshparallel.production.AbstractProduction;
import pl.edu.agh.macwozni.dmeshparallel.production.PDrawer;

public class PI extends AbstractProduction<Vertex> {

    public PI(Vertex _obj, PDrawer<Vertex> _drawer) {
        super(_obj, _drawer);
    }

    @Override
    public Vertex apply(Vertex m) {
        System.out.print("PI");
        //Vertex t1 = new Vertex(null, null, "T1");
        //Vertex t2 = new Vertex(t1, null, "T1");
        m.setLabel("M");
        return m;
    }
}
