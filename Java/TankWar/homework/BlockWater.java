package homework;
import java.awt.*;

public class BlockWater extends Block {
    public BlockWater(int x , int y){
        super(x,y);
    }
    @Override
    public void draw(Graphics g){
        g.drawImage(img,x,y,x+34,y + 34, 0, 7* 34,34,7*34 +34,null);
    }
}
