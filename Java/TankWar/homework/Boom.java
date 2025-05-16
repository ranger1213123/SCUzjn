package homework;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class Boom {
    protected int x;
    protected int y;
    protected int frame;
    protected Image img;

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
        if(frame == 2)
            frame = 0;
        g.drawImage(img,x,y,x+34,y+34,(20+frame)*34,4*34,(21+frame)*34,5*34,null);
        frame++;
    }
    public boolean isFinish(){
        return frame == 2;
    }

}
