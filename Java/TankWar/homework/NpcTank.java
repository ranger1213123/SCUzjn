package homework;

import java.awt.*;
import java.util.Random;

public class NpcTank extends Tank{

    public NpcTank(int ax, int ay) {
        super(ax, ay);
        //实现让npc坦克走的更慢，重写speed
        speed =1;
    }
    public void draw( Graphics g){
        g.drawImage(image,x,y,x+34,y+34,68*dir.ordinal()+flag*34,68,68*dir.ordinal()+flag*34+34,104,null);
        flag++;
        flag = flag == 2?0 :flag;
    }

    @Override
    public void move() {
        super.move();
        Random rad = new Random();
        if(rad.nextInt()%10 == 1){
            DIR adir = DIR.values()[Math.abs(rad.nextInt()%4)];//可能为负数！使用Math.abs
            setDir(adir);
        }

    }
}
