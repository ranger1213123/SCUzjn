package homework;
public abstract class CommonBullet extends VirtueItem {
    protected int speed;
    protected DIR dir;
    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }
    public CommonBullet(int ax, int ay,int v){
            super(ax,ay);
            this.speed = v;
            this.dir = DIR.UP;
    }
    public void setDir(DIR adir) {
        dir = adir;
    }
}
