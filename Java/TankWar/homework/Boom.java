package homework;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;

public class Boom {
    private int x;
    private int y;
    private int frame;
    private Image img;
    public Boom(int ax, int ay){
        try {
            x = ax;
            y = ay;
            File f = new File("C:\\Users\\range\\Downloads\\insect_sprite.png");
            img = ImageIO.read(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void draw(Graphics g){
        g.drawImage(img,x,y,x+34,y+34,(20+frame)*34,4*34,(21+frame)*34,5*34,null);
    }
    public boolean isFinish(){
        if(frame == 3){
            frame = 0;
            return true;
        }
        return false;
    }
    public void frame_add(){
        this.frame++;
    }
}
