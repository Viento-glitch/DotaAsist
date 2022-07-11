package ru.sa.dotaassist.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Класс калибровки робота.
 * При создании, открывает окно в котором
 * следует сделать скриншот игры
 * с текущими настройками графики и предоставить его программе.
 * Следом необходимо выбрать область в которой расположен таймер и выделить ее.
 */
public class RobotCalibrate extends JFrame {
    private JPanel control = new JPanel() {{
        JButton load =  new JButton("load");
        JButton choose = new JButton("choose image");
        JTextField path = new JTextField(20);
        add(choose);
        choose.addActionListener(actionEvent -> {
           JFileChooser fileChooser = new JFileChooser();
           if(fileChooser.showOpenDialog(this)!= JFileChooser.ABORT) {
               path.setText(fileChooser.getSelectedFile().toString());
           }
        });
        load.addActionListener(actionEvent -> {
            picker.load(path.getText());
        });
        add(path);
        add(load);
    }};
    Picker picker = new Picker();
    private JScrollPane scroll = new JScrollPane(picker);
    private JPanel panel = new JPanel() {{
        add(scroll);
        add(control);
    }};
    public RobotCalibrate(JFrame parent) {
        setTitle("Calibrate \"timer catch\" function");
        setSize(600, 400);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize =  toolkit.getScreenSize();
        setLocation((screenSize.width-600)/2, (screenSize.height-400)/2);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        addWindowListener(new WindowRobotListener(parent));
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        Dimension scrollSize = new Dimension(550, 300);
        scroll.setMinimumSize(scrollSize);
        scroll.setPreferredSize(scrollSize);
        scroll.setMinimumSize(scrollSize);
        scroll.setSize(scrollSize);

        add(panel);
        setVisible(true);
    }
}

class Picker extends JPanel {
    JLabel image = new JLabel("Here should be your screenshot");
    {
        add(image);
    }
    public void load(String path) {
        try {
            image.setIcon(new ImageIcon(path));
        } catch (Exception e) {
            image.setText("could not load your screenshot");
        }
    }
}

/**
 * Костыль
 * Следит за состоянием текущего окна.
 * Ответственен за сокрытие и появление окна родителя.
 */
class WindowRobotListener extends WindowAdapter {
    JFrame parent;
        public WindowRobotListener(JFrame parent) {
            this.parent = parent;
        }

    @Override
    public void windowOpened(WindowEvent e) {
        super.windowOpened(e);
        parent.setVisible(false);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        super.windowClosing(e);
        parent.setVisible(true);
    }
}
