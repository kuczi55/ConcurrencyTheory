package pl.edu.agh.macwozni.dmeshparallel.mesh;

public class Vertex {

    //label
    String mLabel;
    //links to adjacent elements
    Vertex mWest;
    Vertex mEast;
    Vertex mNorth;
    Vertex mSouth;

    //methods for adding links
    public Vertex(Vertex _west, Vertex _east, Vertex _north,
                  Vertex _south, String _lab) {
        this.mWest = _west;
        this.mEast = _east;
        this.mNorth = _north;
        this.mSouth = _south;
        this.mLabel = _lab;
    }
    //empty constructor

    public Vertex() {
    }

    public void setWest(Vertex _west) {
        this.mWest = _west;
    }
    public void setEast(Vertex _east) {
        this.mEast = _east;
    }
    public void setNorth(Vertex _north) {
        this.mNorth = _north;
    }
    public void setSouth(Vertex _south) {
        this.mSouth = _south;
    }
    public void setLabel(String _lab) {
        this.mLabel = _lab;
    }

    public Vertex getWest() {
        return this.mWest;
    }
    public Vertex getEast() {
        return this.mEast;
    }
    public Vertex getNorth() {
        return this.mNorth;
    }
    public Vertex getSouth() {
        return this.mSouth;
    }
    public String getLabel() {
        return this.mLabel;
    }
}
