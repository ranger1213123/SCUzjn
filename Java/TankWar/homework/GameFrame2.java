package homework;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

import static java.lang.Math.abs;


public class GameFrame2 extends JFrame implements KeyListener {
    private final int HEIGHT = 600;
    private final int WIDTH = 600;//提高可维护性
    private Tank playerTank = new Tank(150,250);
    private  int[][] position = {{100,50},{300,50},{100,150}};
    private ArrayList<NpcTank> npcTanks = new ArrayList<NpcTank>();
    Image offScreenImage;//实现取消双闪烁,同时减少内存使用量
    private ArrayList<Bullet> playerBullets = new ArrayList<Bullet>();
    public GameFrame2() {

        setTitle("坦克大战");

        setSize(500,300);

        setLocation(200, 100);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //这里添加了监听器，使用了匿名类的实现
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                var key = e.getKeyCode();
                int dir = 0;
                switch (key) {
                    case KeyEvent.VK_UP:  playerTank.setDir(Tank.UP);break;
                    case KeyEvent.VK_RIGHT: playerTank.setDir(Tank.RIGHT);break;
                    case KeyEvent.VK_LEFT: playerTank.setDir(Tank.LEFT);break;
                    case KeyEvent.VK_DOWN: playerTank.setDir(Tank.DOWN);break;
                    case KeyEvent.VK_SPACE:
                            Bullet b = playerTank.fire();
                            if(b!=null)
                                playerBullets.add(b);
                        break;
                }
            }
        });

        setFocusable(true);


        setVisible(true);
        Thread gameThread = new Thread(new MyThread());
        gameThread.start();

    }

    //把tank画在image上去完成
    //因为显示器的刷新频率高于程序。paint方法还没有来得及画完整幅画面，所以就出现了闪烁问题。
    //解决办法就是将使用双缓冲消除，先把所有东西画在虚拟屏幕上,前面什么都不画，然后一次性把画好的内容显示出来
    public void paint(Graphics g) {
        if(offScreenImage == null){
            offScreenImage = this.createImage(WIDTH,HEIGHT);
        }//为空时才可以创建，只需要创建一次，减少内存消耗,不放在构造函数是因为窗体尚未创建时创建会报错
        Graphics gOffScreen;
        gOffScreen = offScreenImage.getGraphics();
        gOffScreen.setColor(Color.black);
        gOffScreen.fillRect(0, 0, WIDTH, HEIGHT);
        playerTank.draw(gOffScreen);
        for(var tank: npcTanks){
            tank.draw(gOffScreen);
        }
        if(!playerBullets.isEmpty()){
            for(var b: playerBullets){
                b.draw(gOffScreen);
            }
        }
        g.drawImage(offScreenImage,0,0,null);

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        var key = e.getKeyCode();

        switch (key) {
            case KeyEvent.VK_UP:  playerTank.setDir(Tank.UP);break;
            case KeyEvent.VK_RIGHT: playerTank.setDir(Tank.RIGHT);break;
            case KeyEvent.VK_LEFT: playerTank.setDir(Tank.LEFT);break;
            case KeyEvent.VK_DOWN: playerTank.setDir(Tank.DOWN);break;
            case KeyEvent.VK_SPACE:
                    Bullet b = playerTank.fire();
                    if(b!=null)
                        playerBullets.add(b);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private class MyThread implements Runnable{
        @Override
        public void run() {
            while(true) {
                Random rnd = new Random();
                if(rnd.nextInt()%10 == 1){
                    int p = abs(rnd.nextInt()%3);
                    NpcTank npcTank1 = new NpcTank(position[p][0],position[p][1]);
                    npcTank1.setDir(Math.abs(rnd.nextInt()%4));
                    npcTanks.add(npcTank1);
                }

                for(var tank: npcTanks){
                    tank.move();
                }
                playerTank.move();
                for(int  i =playerBullets.size()-1;i>=0;i--){
                      Bullet b = playerBullets.get(i);
                      b.move();//完全可以放在这里
                    if(b.getX()<0||b.getX()>WIDTH||b.getY()<0||b.getY()>HEIGHT){
                        playerBullets.remove(b);
                    }else {
                        for(int j = npcTanks.size()-1;j>=0;j--){
                            NpcTank t = npcTanks.get(j);
                            if(b.isHitTank(t)){
                                npcTanks.remove(t);
                                playerBullets.remove(b);
                                break;
                            }
                        }

                    }
                 }//防止出现并发性问题
                /*
                *使用size
                * for(int  i =0;i<playerBullets.size();i++){
                *       Bullet b = playerBullets.get(i);
                *       b.move();
                * }
                *
                * 同时也可以使用迭代器去完成
                * Iterator<Bullet>itr;
                * itr = playerBullets.iterator();
                * while(iterator.hashNext()){
                *   Bullet b = itr.next();
                *   b.move();
                * }
                * */
                repaint();
                try {
                    Thread.sleep(20);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}

