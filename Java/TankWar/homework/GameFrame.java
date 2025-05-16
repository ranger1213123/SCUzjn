package homework;

import javax.swing.*;
import java.awt.*;

//实现Runnable接口
public class GameFrame extends JFrame implements Runnable {
    private int offset=0;//控制移动
    private boolean running=true;//控制运行状态
    private int[] res={0,50,100,150};
    private int direction = 0;
    GameFrame(){
        super("坦克大战");
        setBounds(300,300,500,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.BLACK);
        setVisible(true);

        //使用线程进行控制
        Thread thread = new Thread(this);
        thread.start();
    }
    //重写父类函数paint
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.RED);
        for(int i=0;i<4;i++){
            int x=170+res[i],y=200+offset;
            g.drawRect(x-20, y-20, 4, 40);
            g.drawRect(x+16,y-20,4,40);
            g.drawRect(x-16,y-16,32,32);
            g.drawOval(x-14,y-14,28,28);
            g.drawRect(x-1, y, 2, 30);
        }
    }

    @Override
    public void run() {
        while (running) {
            switch(direction){
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
            }
            offset +=2; // 移动
            repaint(); // 重新绘制界面
            try {
                Thread.sleep(200); // 控制刷新速度
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
