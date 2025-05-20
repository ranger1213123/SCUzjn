package homework;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public abstract class VirtueItem {
    protected int x;
    protected int y;
    protected static Image img;//创建一个静态变量,类变量
    {
        try{
            img = ImageIO.read(new File("C:\\Users\\range\\Downloads\\insect_sprite.png"));
        }catch(IOException e){
            e.printStackTrace(); //错误详情
        }

    }
    public VirtueItem(int ax,int ay){
            this.x = ax;
            this.y = ay;
    }
    public abstract void draw(Graphics g);
}
