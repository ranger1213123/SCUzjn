package homework;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;

public class Tank {
    protected int speed =2;
    protected int x;
    protected int y;
    protected  int dir;
    protected static final int UP =0,RIGHT = 1,LEFT = 3, DOWN =2;
    protected Image image;
    protected int flag = 0;
    private int coolTime = 10;

    public Tank(int ax, int ay){
        try {
            x = ax;
            y = ay;
            dir = 0;
            File f = new File("C:\\Users\\range\\Downloads\\insect_sprite.png");
            image = ImageIO.read(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDir(int adir) {
        dir = adir;
    }
    public void move(){
        coolTime++;
        switch (dir){
            case 0:y-=speed;break;
            case 1:x+=speed;break;
            case 2:y+=speed;break;
            case 3:x-=speed;break;
        }
    }
    public void draw( Graphics g){
        //利用好算法，减少工作量，但是一定要写注释去讲述算法功能
        /*通过迭代完成了功能代码的简化，虽然一定程度上减少了可读性
        * 当有dir时选择方法去实现
        * 同时使用flag去完成四个方向上的选择
        * */
        g.drawImage(image,x,y,x+34,y+34,68*dir+flag*34,0,68*dir+flag*34+34,34,null);
        flag++;
        flag = flag == 2?0 :flag;
    }

    public Bullet fire(){
        Bullet bullet = null;
        if(coolTime>=10){
            bullet = new Bullet(x, y);
            bullet.setDir(dir);
            coolTime = 0;
        }

        return bullet;
    }
    //取消双闪烁
}
