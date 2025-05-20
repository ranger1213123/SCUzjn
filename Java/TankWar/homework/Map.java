package homework;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Map {
    public Map() {
        blocks = new ArrayList<>();
    }
    private ArrayList<Block> blocks;
    public void draw(Graphics g){
        for(Block n : blocks){
            n.draw(g);
        }
    }
    public void loadData(String file) throws IOException {
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;//读取每一行
        while((line = br.readLine())!=null){
            String[] str;
            //进行字符串分割
            str = line.split("=");//.split("=") 方法会返回一个 String[] 数组，包含按等号切开的部分
            String[] xy;
            xy = str[1].split(",");
            int x = Integer.parseInt(xy[0])*34;
            int y = Integer.parseInt(xy[1])*34;//转换 + 装箱拆箱
            switch (str[0]) {
                case "brick" -> blocks.add(new BlockBrick(x, y));
                case "grass" -> blocks.add(new BlockGrass(x, y));
                case "stone" -> blocks.add(new BlockStone(x, y));
                case "water" -> blocks.add(new BlockWater(x, y));
            }
        }
    }
}
