package homework;

import java.awt.*;
//注意： 抽象类也可以提供实例函数
public class Revive extends Cartoon {

    public Revive(int ax, int ay) {
        super(ax, ay);
    }

    @Override
    public void draw(Graphics g) {
        int frameIndex = frame % 3;
        if (frame >= 6) {
            g.drawImage(img, x, y, x + 34, y + 34,
                    (13 + frameIndex) * 34, 7 * 34,
                    (14 + frameIndex) * 34, 8 * 34, null);
        }
        nextFrame(); // 管理帧数
    }

    @Override
    public boolean isFinish() {
        if (frame >= 18) {
            if (listener != null) {
                listener.doFinish();
            }
            return true;
        }
        return false;
    }
}
