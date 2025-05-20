package homework;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;

public class Tank  extends Sprite{
    public Tank(int ax, int ay) {
        super(ax,ay,2);
    }
    @Override
    public void draw(Graphics g) {
        // 动画帧切换，简化绘制逻辑
        g.drawImage(img, x, y, x + 34, y + 34,
                68 * dir.ordinal() + flag * 34, 0,
                68 * dir.ordinal() + flag * 34 + 34, 34, null);
        flag = (flag + 1) % 2;  // 0 -> 1 -> 0
    }

}
