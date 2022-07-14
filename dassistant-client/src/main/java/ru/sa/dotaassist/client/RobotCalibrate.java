package ru.sa.dotaassist.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/**
 * Класс калибровки робота.
 * При создании, открывает окно в котором
 * следует сделать скриншот игры
 * с текущими настройками графики и предоставить его программе.
 * Следом необходимо выбрать область в которой расположен таймер и выделить ее.
 */
public class RobotCalibrate extends JFrame {
    private JLabel message = new JLabel();
    public boolean active = true;
    final public JPanel control = new JPanel() {
        {

        JButton load =  new JButton("load");
        JButton choose = new JButton("choose image");
        JTextField path = new JTextField(20);
        add(choose);
        add(message);
        choose.addActionListener(actionEvent -> {
           JFileChooser fileChooser = new JFileChooser();
           if(fileChooser.showOpenDialog(this)!= JFileChooser.ABORT) {
               path.setText(fileChooser.getSelectedFile().toString());
           }
        });
        load.addActionListener(actionEvent -> {
            picker.load(path.getText());
            message.setText("");
            active = true;
        });
        add(path);
        add(load);
    }};
    final private Picker picker = new Picker(this);
    final private JScrollPane scroll = new JScrollPane(picker);
    final private JPanel panel = new JPanel() {{
        add(scroll);
        //add(picker);
        add(control);
    }};
    public void setMessage(String text) {
        message.setText(text);
    }
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
    final private JLabel image = new JLabel("Here should be your screenshot");
    private ImageIcon imageIcon = null;
    private RobotCalibrate robotCalibrate;
    public Picker(RobotCalibrate robotCalibrate){
        this.robotCalibrate = robotCalibrate;
        add(image);
        image.addMouseListener(new MouseAdapter() {
            private Point point1 = null;

            private Point point2 = null;
            private void onDone() {
                if (point1!=null&&point2!=null) {
                    BufferedImage cutImage = new BufferedImage(
                            imageIcon.getIconWidth(),
                            imageIcon.getIconHeight(),
                            BufferedImage.TYPE_INT_RGB);
                    Graphics g = cutImage.createGraphics();
// paint the Icon to the BufferedImage.
                    imageIcon.paintIcon(null, g, 0,0);
                    g.dispose();
                    cutImage = cutImage.getSubimage(point1.x, point1.y, point2.x-point1.x, point2.y-point1.y);
                    image.setIcon(new ImageIcon(cutImage));
                    robotCalibrate.active=false;
                    PropertyManager.Companion.set("robot-xPos", String.valueOf(point1.x));
                    PropertyManager.Companion.set("robot-yPos", String.valueOf(point1.y));
                    PropertyManager.Companion.set("robot-width", String.valueOf(point2.x));
                    PropertyManager.Companion.set("robot-height", String.valueOf(point2.y));
                    point1=null;
                    point2=null;
                }
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if ((imageIcon!=null) &&(robotCalibrate.active)) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        point1 = e.getPoint();
                        robotCalibrate.setMessage("Point one has been set");

                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        point2 = e.getPoint();
                        robotCalibrate.setMessage("Point two has been set");
                    }
                    onDone();
                }
            }
        });
    }
    public void load(String path) {
        try {
            imageIcon = new ImageIcon(path);
            image.setIcon(imageIcon);
            //robotCalibrate.setSize(imageIcon.getIconWidth(), imageIcon.getIconHeight());
            //Toolkit toolkit = Toolkit.getDefaultToolkit();
            //Dimension screenSize =  toolkit.getScreenSize();
            //robotCalibrate.setLocation((screenSize.width-imageIcon.getIconWidth())/2,(screenSize.height-imageIcon.getIconHeight())/2);
            image.setText("");
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
