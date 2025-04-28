
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;

public class LocalCalender extends JFrame{
    private JTextPane textPane = new JTextPane();
    protected JFrame jframe;

    //年月日相关
    String [] month1 = {"1","2","3","4","5","6","7","8","9","10","11","12"};
    String[] year1 = {"2015", "2016", "2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025"};
    private final JComboBox<String> yearNumber = new JComboBox<String>(year1);//下拉列表组件表示年份
    private final JComboBox<String> monthNumber =new JComboBox<String>(month1);//月份
    private final JLabel jf1 = new JLabel("年：");
    private final JLabel jf2 = new JLabel("月：");



    //设置日期问题
    private LocalDate date = LocalDate.now();
    private int month = date.getMonthValue();
    private final int today = date.getDayOfMonth();
    private int year = date.getYear();

    //年份选择，月份选择以及整体内容显示
    private String year_number;
    private String month_number;
    private StringBuilder sb = new StringBuilder();//使用Stringbuilder类

    public LocalCalender(){
        //基础设置
        this.jframe= new JFrame("2023141480038_周佳楠");
        jframe.setBounds(400,400,600,400);
        jframe.setLayout(null);

        jf1.setBounds(100,40,150,30);
        jf2.setBounds(320,40,150,30);

        yearNumber.setBounds(120,40,130,30);
        monthNumber.setBounds(340,40,130,30);

        //设置默认值，显示当前的月份与年份
        yearNumber.setSelectedItem(Integer.toString(date.getYear()));
        monthNumber.setSelectedItem(Integer.toString(date.getMonthValue()));

        jframe.add(yearNumber);
        jframe.add(monthNumber);
        jframe.add(jf1);
        jframe.add(jf2);

        // 初始化 JTextArea
        textPane.setEditable(false);
        textPane.setFont(new Font("Monospaced", Font.PLAIN, 14)); // 等宽字体保持对齐
        textPane.setBounds(170,90,320,200);
        textPane.setBackground(getContentPane().getBackground());//保持默认背景
        jframe.add(textPane);
        showCalendar();

        //下拉菜单的选择
        click();

        jframe.setVisible(true);
        jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void click(){
        //实现选择年份
        yearNumber.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                year_number=(String)yearNumber.getSelectedItem();
                showCalendarAfter(year_number,true);
            }
        });
        //实现选择月份
        monthNumber.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                month_number=(String)monthNumber.getSelectedItem();
                showCalendarAfter(month_number,false);
            }
        });
    }
    private void showCalendar(){

        date = LocalDate.of(year, month, 1);
        DayOfWeek weekday = date.getDayOfWeek();
        int value = weekday.getValue();
        sb.setLength(0);
        sb.append("Mon Tue Wed Thu Fri Sat Sun\n");
        for(int i = 1; i<value ;i++){
            sb.append("    ");
        }
        while(date.getMonthValue() == month){
            sb.append(String.format("%3d",date.getDayOfMonth()));//这里使用转换
            if(date.getYear() == LocalDate.now().getYear() &&
               date.getMonthValue() == LocalDate.now().getMonthValue() &&
               date.getDayOfMonth() == today){
                sb.append("*");
            }else{
                sb.append(" ");
            }
            date = date.plusDays(1);
            if(date.getDayOfWeek().getValue() == 1) {
                sb.append("\n");
            }
        }
        if(date.getDayOfWeek().getValue()!= 1) {
            sb.append("\n");
        }
        textPane.setText(sb.toString());
    }

    //实现点选内容
    private void showCalendarAfter(String input, boolean judge){
        sb.setLength(0);//清空内容，从而能够直接覆盖
        if(judge){
            year =Integer.parseInt(input);
            date = date.withYear(year);
        }else{
            month =Integer.parseInt(input);
            date = date.withMonth(month);
        }

        showCalendar();
    }
}