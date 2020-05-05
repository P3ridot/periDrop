package me.peridot.peridrop.modifiable;

public abstract class Modifiable {

    private boolean modified = false;

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public boolean isModified() {
        return modified;
    }
}
