package homework;

import java.awt.*;
import java.util.Random;

public class NpcTank extends Sprite{
    private static int count = 0;
    public static int getCount(){
        return count;
    }
    public NpcTank(int ax, int ay) {
        super(ax,ay,2);
    }
    @Override
    public void draw( Graphics g){
        g.drawImage(img,x,y,x+34,y+34,68*dir.ordinal()+flag*34,68,68*dir.ordinal()+flag*34+34,104,null);
        flag = (flag + 1) % 2;
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
