package top.jimxu.snake;

import javax.swing.JFrame;

public class SnakeGame {
    public static void main(String[] args) {
        JFrame frame = new JFrame("贪吃蛇游戏");
        frame.setSize(950, 750);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        SnakePanel panel = new SnakePanel();
        frame.add(panel);
        
        frame.setVisible(true);
    }
}