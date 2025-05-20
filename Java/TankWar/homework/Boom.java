package homework;

import java.awt.*;

public class Boom extends Cartoon {

    public Boom(int ax, int ay) {
        super(ax, ay);
    }

    @Override
    public void draw(Graphics g) {
        int currentFrame = nextFrame();  // 自动帧管理
        if (currentFrame < 2) {
            g.drawImage(img, x, y, x + 34, y + 34,
                    (20 + currentFrame) * 34, 4 * 34,
                    (21 + currentFrame) * 34, 5 * 34, null);
        }
    }

    @Override
    public boolean isFinish() {
        return frame >= 2;
    }
}
