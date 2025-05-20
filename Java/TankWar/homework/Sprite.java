package homework;
public abstract class Sprite extends VirtueItem{
    protected int speed;
    protected int flag;
    protected DIR dir;
    protected int coolTime;

    public void move() {
        coolTime++;
        switch (dir) {
            case UP: y -= speed; break;
            case RIGHT: x += speed; break;
            case DOWN: y += speed; break;
            case LEFT: x -= speed; break;
        }

        // 碰到边界反弹
        if (x <= 34 || y <= 34 || x >= GameFrame2.WIDTH - 34 || y >= GameFrame2.HEIGHT - 34) {
            int num = (dir.ordinal() + 2) % 4;
            dir = DIR.values()[num];
        }
    }
    public Bullet fire() {
        if (coolTime >= 10) {
            Bullet bullet = new Bullet(x, y);
            bullet.setDir(dir);
            coolTime = 0;
            return bullet;
        }
        return null;
    }
    public void setDir(DIR adir) {
        dir = adir;
    }
    public Sprite(int ax, int ay,int v){
            super(ax,ay);
            this.speed = v;
            this.dir = DIR.UP;
            this.coolTime = 10;
    }
    public void setX(int ax){
        this.x = ax;
    }
    public void setY(int ay){
        this.y = ay;
    }
}
