package homework;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public abstract class Block {
    protected int x;
    protected int y;
    protected Image img;

    protected Block(int x, int y){
        this.x = x;
        this.y = y;
        try {
            File f = new File("C:\\Users\\range\\Downloads\\insect_sprite.png");
            img = ImageIO.read(f);
        } catch (IOException e) {
            e.printStackTrace();       // 打印错误
            img = null;                // 或者设置一个默认图片或空值
        }
    }
    public void draw(Graphics g) {
    }
}
