import Services.Utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TwoPL {

    public static void main(String[] args) {
        String input_file = "E:\\database\\project1\\TwoPhaseLocking\\src\\test3.txt";
        //如果程序运行时传入了文件路径参数，则使用该路径
        if(args.length > 0) {
            input_file = args[0];
        }
        //调用函数解析输入文件
        parseInput(input_file);
    }

    protected static void parseInput(String file_name) {
        //创建文件对象
        File file = new File(file_name);
        //判断文件是否存在
        if(file.exists()) {
            BufferedReader reader;
            try {
                reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();

                // 逐行读取并执行操作
                while(line != null) {
                    Utility.runOperation(line);// 调用工具类执行操作
                    line = reader.readLine();
                }

            } catch (FileNotFoundException e) {
                // 文件未找到异常处理
                System.out.println("文件不存在，检查文件");
                e.printStackTrace();
            } catch (IOException e) {
                // 其他I/O错误处理
                System.out.println("读取文件失败");
                e.printStackTrace();
            }
        }
    }

}