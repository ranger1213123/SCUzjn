package homework;

public abstract class Cartoon extends VirtueItem {
    protected int frame;
    public abstract boolean isFinish();
    protected IFinish listener;
    public Cartoon(int ax, int ay) {
       super(ax,ay);
    }
    public void addListener(IFinish lsn){
        this.listener = lsn;
    }
    public int nextFrame() {
        return frame++;
    }

}
