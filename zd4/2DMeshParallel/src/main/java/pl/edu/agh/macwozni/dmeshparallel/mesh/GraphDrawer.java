package pl.edu.agh.macwozni.dmeshparallel.mesh;

import pl.edu.agh.macwozni.dmeshparallel.production.PDrawer;

public class GraphDrawer implements PDrawer<Vertex> {

    @Override
    public void draw(Vertex v) {
        System.out.println("\n");
        boolean last = false;
        int level = 0;

        while(!last) {
            last = true;

            //go left
            while (v.mWest != null) {
                v = v.mWest;
            }

            do {
                Vertex lastOnSouth = v;

                for(int i = 0; i < level; i++) {
                    if(lastOnSouth != null) {
                        lastOnSouth = lastOnSouth.mSouth;
                    }
                }

                if(lastOnSouth != null) {
                    System.out.print(lastOnSouth.mLabel + (lastOnSouth.mEast != null ? "--" : "  "));
                }
                else {
                    System.out.print("   ");
                }
                if(v.mEast != null) {
                    v = v.mEast;
                }
                else {
                    break;
                }
            } while(true);

            System.out.println();

            while(v.mWest != null) {
                v = v.mWest;
            }

            do {
                Vertex lastOnSouth = v;

                for(int i = 0; i < level; i++) {
                    if(lastOnSouth != null) {
                        lastOnSouth = lastOnSouth.mSouth;
                    }
                }

                System.out.print(lastOnSouth != null && lastOnSouth.mSouth != null
                        ? "|  " : "   ");

                if(lastOnSouth != null && lastOnSouth.mSouth != null) {
                    last = false;
                }

                if(v.mEast != null) {
                    v = v.mEast;
                }
                else {
                    break;
                }
            } while(true);

            System.out.println();
            level++;
        }
    }
}
