package homework;

import java.awt.*;

public class Reverse extends Boom{
    private IFinish listener;
    public  void addListener(IFinish lsn){
        this.listener = lsn;
    }
    private int frame = 0;
    public Reverse(int ax, int ay) {
        super(ax, ay);
    }
    public void draw(Graphics g){
        int tempf = frame % 3;
        if (frame >=6)
            g.drawImage(img,x, y, x + 34, y + 34, (13 + tempf) * 34, 34* 7,(14 +tempf) * 34, 34 * 8, null);
        frame ++;
    }


    public boolean isFinish(){
        boolean check = frame >= 18;
        if(check){
            if(listener != null){
                listener.doFinish();
            }
        }
        return check;
    }
}
