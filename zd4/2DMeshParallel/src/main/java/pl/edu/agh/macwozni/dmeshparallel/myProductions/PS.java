package pl.edu.agh.macwozni.dmeshparallel.myProductions;

import pl.edu.agh.macwozni.dmeshparallel.mesh.Vertex;
import pl.edu.agh.macwozni.dmeshparallel.production.AbstractProduction;
import pl.edu.agh.macwozni.dmeshparallel.production.PDrawer;

public class PS extends AbstractProduction<Vertex> {

    public PS(Vertex _obj, PDrawer<Vertex> _drawer) {
        super(_obj, _drawer);
    }

    @Override
    public Vertex apply(Vertex m) {
        System.out.print("->PS");
        Vertex m1 = new Vertex(null, null, m, null, "M");
        m.setSouth(m1);
        return m1;
    }
}
