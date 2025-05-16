package homework;

import java.awt.*;

public class BlockGrass extends Block {
    public BlockGrass(int x , int y){
        super(x,y);
    }
    @Override
    public void draw(Graphics g){
        g.drawImage(img,x,y,x+34,y + 34, 4*34, 7* 34,5*34,7*34 +34,null);
    }
}
