package homework;
import java.awt.*;
public class Bullet extends CommonBullet{
    public Bullet(int ax, int ay){
        super(ax,ay,6);
    }
    public void draw(Graphics g){
        g.drawImage(img,x,y,x+34,y+34,5*34,6*34,6*34,7*34,null);
    }
    public boolean isHitTank(Sprite s){
        double dx = Math.pow(s.x-x,2);
        double dy = Math.pow(s.y-y,2);
        double d = Math.sqrt(dx + dy);
        return d < 30;
    }
    public void move(){
        switch (dir){
            case UP:y-=speed;break;
            case RIGHT:x+=speed;break;
            case DOWN:y+=speed;break;
            case LEFT:x-=speed;break;
        }
    }
}
