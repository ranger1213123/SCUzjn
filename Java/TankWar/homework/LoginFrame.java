package homework;
/**
 * 本类用于创建一个登录窗口
 */

import java.awt.event.*;
import javax.swing.*;
//继承了JFrame的函数setVisible()而不用重写
public class LoginFrame extends JFrame{


    //设置按钮实现事件响应
    JButton check=new JButton("确定");
    JButton cancel=new JButton("取消");
    //正确的密码以及用户名
    String name="admin";
    String code="123";

    //提示及文本框
    JLabel userLabel=new JLabel("登录名:");
    JTextField userText=new JTextField();

    JLabel answerLabel =new JLabel("密码：");
    JTextField answerText=new JPasswordField();

    //按钮添加事件监听，用于实现登录确认机制
    class ButtonListener1 implements ActionListener{
            public void actionPerformed(ActionEvent args0){
                String nameText = userText.getText();
                String codeText = new String(((JPasswordField) answerText).getPassword());//JPasswordField的特性
                    if(name.equals(nameText)&&code.equals(codeText)){
                        JOptionPane.showMessageDialog(null,"登录成功","成功",JOptionPane.INFORMATION_MESSAGE);
                    }else{
                        JOptionPane.showMessageDialog(null,"登录名或密码错误","失败",JOptionPane.ERROR_MESSAGE);

                    }
            }
    }

    //窗口的关闭机制，静态类避免内存泄露
    static class ButtonListener2 implements ActionListener{
        //实现取消关闭窗口的机制，当然也可以直接使用dispose去完成操作
        private final JFrame frame;

        public ButtonListener2(JFrame frame){
                this.frame=frame;
        }
        public void actionPerformed(ActionEvent args0){
            frame.dispose();
        }
    }

    //构造函数
    public LoginFrame(String str){
        //创建基础的窗口
        super(str);
        this.setLayout(null);//使用绝对布局
        this.setBounds(620,350,300,200);

        check.addActionListener(new ButtonListener1());
        cancel.addActionListener(new ButtonListener2(this));

        //组件包含两个输入文本框+两个按钮+两个提示文本框

        //提示文本
        userLabel.setBounds(35,20,100,20);
        answerLabel.setBounds(155,20,100,20);
        this.add(userLabel);
        this.add(answerLabel);

        //输入文本
        userText.setBounds(35,50,100,20);
        answerText.setBounds(155,50,100,20);
        this.add(userText);
        this.add(answerText);

        //按钮
        check.setBounds(35,80,100,20);
        cancel.setBounds(155,80,100,20);
        this.add(check);
        this.add(cancel);
        //本身的窗口关闭程序
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
