package homework;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

import static java.lang.Math.abs;


public class GameFrame2 extends JFrame implements KeyListener {
    public static final int HEIGHT = 1200;
    public static final int WIDTH = 1200;//提高可维护性
    private Tank playerTank = new Tank(150,250);
    private  int[][] position = {{100,50},{300,50},{100,150}};
    private ArrayList<NpcTank> npcTanks = new ArrayList<NpcTank>();
    Image offScreenImage;//实现取消双闪烁,同时减少内存使用量
    private ArrayList<Bullet> playerBullets = new ArrayList<Bullet>();
    private ArrayList<Bullet> npcBullets = new ArrayList<>();
    private ArrayList<Boom> cartoonsList = new ArrayList<>();
    private IFinish ifinish;
    private Map map;
    public GameFrame2(){

        setTitle("坦克大战");

        setSize(WIDTH,HEIGHT);

        setLocation(200, 100);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //这里添加了监听器，使用了匿名类的实现
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                var key = e.getKeyCode();
                switch (key) {
                    case KeyEvent.VK_UP:  playerTank.setDir(DIR.UP);break;
                    case KeyEvent.VK_RIGHT: playerTank.setDir(DIR.RIGHT);break;
                    case KeyEvent.VK_LEFT: playerTank.setDir(DIR.LEFT);break;
                    case KeyEvent.VK_DOWN: playerTank.setDir(DIR.DOWN);break;
                    case KeyEvent.VK_SPACE:
                            Bullet b = playerTank.fire();
                            if(b!=null)
                                playerBullets.add(b);
                        break;
                }
            }
        });
        map = new  Map();
        try{
            map.loadData("E:\\Java_learning\\homework\\src\\assets\\map1.txt");
        }catch(IOException e){
            e.printStackTrace();
        }
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
        map.draw(gOffScreen);//画上地图
        playerTank.draw(gOffScreen);
        for(var tank: npcTanks){
            tank.draw(gOffScreen);
        }
        if(!playerBullets.isEmpty()){
            for(var b: playerBullets){
                b.draw(gOffScreen);
            }
        }
        if(!npcBullets.isEmpty()){
            for(var b: npcBullets){
                b.draw(gOffScreen);
            }
        }
        for(int i = cartoonsList.size() - 1; i >= 0; i -- ){
            Boom cartoon = cartoonsList.get(i);
            cartoon.draw(gOffScreen);
            if(cartoon.isFinish()){
                cartoonsList.remove(cartoon);
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
            case KeyEvent.VK_UP:  playerTank.setDir(DIR.UP);break;
            case KeyEvent.VK_RIGHT: playerTank.setDir(DIR.RIGHT);break;
            case KeyEvent.VK_LEFT: playerTank.setDir(DIR.LEFT);break;
            case KeyEvent.VK_DOWN: playerTank.setDir(DIR.DOWN);break;
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

    private class MyThread extends Thread{
        //这里可以使用接口或者使用匿名类
        @Override
        public void run() {
            while(true) {
                Random rnd = new Random();
                if(rnd.nextInt()%10 == 5 && NpcTank.getCount() < 50){
                    int p = abs(rnd.nextInt()%3);
                    NpcTank npcTank1 = new NpcTank(position[p][0],position[p][1]);
                    npcTank1.setDir(DIR.values()[Math.abs(rnd.nextInt()%4)]);
                    npcTanks.add(npcTank1);
                }
                if(rnd.nextInt() %10 == 1 && !npcTanks.isEmpty()){
                    int index = rnd.nextInt()% npcTanks.size();
                    index = Math.abs(index);
                    Bullet b = npcTanks.get(index).fire();
                    if(b != null){
                            npcBullets.add(b);
                    }
                }
                //npc坦克的子弹打击
                if(!npcBullets.isEmpty()){
                    //遍历坦克导弹
                    for(int i = npcBullets.size() - 1; i >= 0; i--){//如果这里用foreach循环会导致并发异常，删除的时候同时又在遍历并不安全
                        Bullet b = npcBullets.get(i);
                        b.move();
                        if(b.isHitTank(playerTank)){
                            cartoonsList.add(new Boom(b.getX(),b.getY()));//爆炸动画
                            Reverse cartoon = new Reverse(WIDTH/2, HEIGHT/2);
                            //使用匿名类或者lambda表达式
                            cartoon.addListener(() -> {
                                playerTank.setX(WIDTH/2);
                                playerTank.setY(HEIGHT/2);
                                playerTank.setDir(DIR.UP);
                            });
                            cartoonsList.add(cartoon);
                            playerTank.setX(10000);//将玩家坦克放逐出屏幕
                            npcBullets.remove(b);//删除该炮弹，直接foreach并不安全
                        }
                    }
                }
                //实现npc坦克移动
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
                                cartoonsList.add(new Boom(b.getX(),b.getY()));
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
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }


    }


}

