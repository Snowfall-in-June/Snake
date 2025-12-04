package top.jimxu.snake;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SnakePanel extends JPanel implements KeyListener, ActionListener {
    // 道具类型枚举
    enum ItemType {
        SCORE_ITEM, SPEED_ITEM, RANDOM_ITEM
    }
    
    // 道具类
    class Item {
        int x, y;
        ItemType type;
        boolean active;
        int lifetime; // 存活时间（tick数）
        int maxLifetime; // 最大存活时间
        
        Item() {
            maxLifetime = 50; // 默认50 tick
            reset();
        }
        
        void reset() {
            x = 0;
            y = 0;
            type = ItemType.SCORE_ITEM;
            active = false;
            lifetime = 0;
        }
        
        void spawn(int x, int y, ItemType type) {
            this.x = x;
            this.y = y;
            this.type = type;
            this.active = true;
            this.lifetime = maxLifetime;
        }
        
        void update() {
            if (active && lifetime > 0) {
                lifetime--;
                if (lifetime <= 0) {
                    active = false;
                }
            }
        }
    }
    
    // 加载所有图片
    ImageIcon up = new ImageIcon("up.png");
    ImageIcon down = new ImageIcon("down.png");
    ImageIcon left = new ImageIcon("left.png");
    ImageIcon right = new ImageIcon("right.png");
    ImageIcon title = new ImageIcon("title.jpg");
    ImageIcon body = new ImageIcon("body.png");
    ImageIcon food = new ImageIcon("food.png");
    
    // 蛇的数据结构设计
    int[] snakex = new int[750];
    int[] snakey = new int[750];
    int len = 3;
    String direction = "R"; // R右L左U上D下
    
    // 食物生成
    Random r = new Random();
    int foodx = r.nextInt(34) * 25 + 25; // 34个格子，一个格子25排像素，还有25像素空白
    int foody = r.nextInt(24) * 25 + 75; // 24个格子，一个格子25排像素，还有75像素空白
    
    // 游戏是否开始
    boolean isStarted = false;
    
    // 游戏是否失败
    boolean isFaild = false;
    
    // 分数
    int score = 0;
    
    // 道具系统
    Item currentItem;
    int tickCounter; // 用于控制道具生成频率
    final int SPAWN_INTERVAL = 20; // 每20 tick判断一次是否生成道具
    final double SPAWN_PROBABILITY = 0.15; // 15%的概率生成道具
    
    // 初始化蛇
    public void initSnake() {
        isStarted = false;
        isFaild = false;
        len = 3;
        direction = "R";
        snakex[0] = 100;
        snakey[0] = 100;
        snakex[1] = 75;
        snakey[1] = 100;
        snakex[2] = 50;
        snakey[2] = 100;
        score = 0;
        if (currentItem != null) {
            currentItem.reset();
        }
        tickCounter = 0;
    }
    
    public SnakePanel() {
        this.setFocusable(true);
        initSnake(); // 放置静态蛇；
        this.addKeyListener(this); // 添加键盘监听接口
        timer.start();
        currentItem = new Item();
        tickCounter = 0;
    }
    
    // 设置蛇移动速度
    Timer timer = new Timer(150, this);
    
    public void paint(Graphics g) {
        // 设置背景黑色
        this.setBackground(Color.black);
        g.fillRect(25, 75, 850, 600);
        // 设置标题
        title.paintIcon(this, g, 25, 11);
        
        // 画分数
        g.setColor(Color.WHITE);
        g.setFont(new Font("arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 750, 30);
        
        // 画蛇头
        if (direction.equals("R")) {
            right.paintIcon(this, g, snakex[0], snakey[0]);
        } else if (direction.equals("L")) {
            left.paintIcon(this, g, snakex[0], snakey[0]);
        } else if (direction.equals("U")) {
            up.paintIcon(this, g, snakex[0], snakey[0]);
        } else if (direction.equals("D")) {
            down.paintIcon(this, g, snakex[0], snakey[0]);
        }
        
        // 画蛇身
        for (int i = 1; i < len; i++) {
            body.paintIcon(this, g, snakex[i], snakey[i]);
        }
        
        // 画开始提示语
        if (!isStarted) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("arial", Font.BOLD, 30));
            g.drawString("Press Space to Start or Pause", 230, 350);
        }
        
        // 画失败提示语
        if (isFaild) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("arial", Font.BOLD, 30));
            g.drawString("Game Over,Press Space to Start", 230, 350);
        }
        
        // 画食物
        food.paintIcon(this, g, foodx, foody);
        
        // 画道具
        renderItem(g);
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
    }
    
    // 监听按键
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        // 实现空格暂停 继续
        if (keyCode == KeyEvent.VK_SPACE) {
            if (isFaild) {
                initSnake();
            } else {
                isStarted = !isStarted;
            }
        }
        // 实现转向
        else if (keyCode == KeyEvent.VK_UP && !direction.equals("D")) {
            direction = "U";
        } else if (keyCode == KeyEvent.VK_DOWN && !direction.equals("U")) {
            direction = "D";
        } else if (keyCode == KeyEvent.VK_LEFT && !direction.equals("R")) {
            direction = "L";
        } else if (keyCode == KeyEvent.VK_RIGHT && !direction.equals("L")) {
            direction = "R";
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
    }
    
    /*
     * 1.定个闹钟
     * 2.蛇移动
     * 3.重画一次蛇
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        
        if (isStarted && !isFaild) {
            // 移动身体
            for (int i = len; i > 0; i--) {
                snakex[i] = snakex[i - 1];
                snakey[i] = snakey[i - 1];
            }
            
            // 头移动
            if (direction.equals("R")) {
                // 横坐标+25
                snakex[0] = snakex[0] + 25;
                if (snakex[0] > 850) snakex[0] = 25;
            } else if (direction.equals("L")) {
                // 横坐标-25
                snakex[0] = snakex[0] - 25;
                if (snakex[0] < 25) snakex[0] = 850;
            } else if (direction.equals("U")) {
                // 纵坐标-25
                snakey[0] = snakey[0] - 25;
                if (snakey[0] < 75) snakey[0] = 650;
            } else if (direction.equals("D")) {
                // 纵坐标+25
                snakey[0] = snakey[0] + 25;
                if (snakey[0] > 650) snakey[0] = 75;
            }
            
            // 吃食物
            if (snakex[0] == foodx && snakey[0] == foody) {
                len++;
                score += 10; // 吃食物得10分
                foodx = r.nextInt(34) * 25 + 25;
                foody = r.nextInt(24) * 25 + 75;
            }
            
            // 判断游戏失败
            for (int i = 1; i < len; i++) {
                if (snakex[0] == snakex[i] && snakey[0] == snakey[i]) {
                    isFaild = true;
                }
            }
            
            // 道具系统逻辑
            updateItem();
            spawnItemIfNecessary();
            checkSnakeItemCollision();
        }
        repaint();
    }
    
    // 更新道具状态
    private void updateItem() {
        currentItem.update();
        tickCounter++;
    }
    
    // 检查是否需要生成道具
    private void spawnItemIfNecessary() {
        // 每20 tick判断一次
        if (tickCounter >= SPAWN_INTERVAL) {
            tickCounter = 0;
            
            // 只有当前没有活跃道具时才考虑生成
            if (!currentItem.active) {
                // 15%的概率生成道具
                if (r.nextDouble() < SPAWN_PROBABILITY) {
                    // 随机选择道具类型
                    ItemType[] types = ItemType.values();
                    ItemType randomType = types[r.nextInt(types.length)];
                    
                    // 生成不与蛇和食物重叠的位置
                    int x, y;
                    boolean validPosition;
                    
                    do {
                        x = r.nextInt(34) * 25 + 25;
                        y = r.nextInt(24) * 25 + 75;
                        validPosition = true;
                        
                        // 检查是否与食物重叠
                        if (x == foodx && y == foody) {
                            validPosition = false;
                        }
                        
                        // 检查是否与蛇重叠
                        for (int i = 0; i < len; i++) {
                            if (x == snakex[i] && y == snakey[i]) {
                                validPosition = false;
                                break;
                            }
                        }
                    } while (!validPosition);
                    
                    // 生成道具
                    currentItem.spawn(x, y, randomType);
                }
            }
        }
    }
    
    // 检查蛇是否吃到道具
    private void checkSnakeItemCollision() {
        if (currentItem.active && snakex[0] == currentItem.x && snakey[0] == currentItem.y) {
            // 处理道具效果
            switch (currentItem.type) {
                case SCORE_ITEM:
                    score += 5;
                    break;
                case SPEED_ITEM:
                    // 降低游戏速度（减少10ms延迟）
                    int currentDelay = timer.getDelay();
                    if (currentDelay > 50) { // 最小延迟限制为50ms
                        timer.setDelay(currentDelay - 10);
                    }
                    break;
                case RANDOM_ITEM:
                    System.out.println("Random item collected!");
                    break;
            }
            
            // 道具被吃掉后失效
            currentItem.active = false;
        }
    }
    
    // 绘制道具
    private void renderItem(Graphics g) {
        if (currentItem.active) {
            // 使用黄色绘制道具
            g.setColor(Color.YELLOW);
            g.setFont(new Font("arial", Font.BOLD, 20));
            
            // 根据道具类型显示不同符号
            String symbol;
            switch (currentItem.type) {
                case SCORE_ITEM:
                    symbol = "S";
                    break;
                case SPEED_ITEM:
                    symbol = "V";
                    break;
                case RANDOM_ITEM:
                    symbol = "?";
                    break;
                default:
                    symbol = "*";
            }
            
            // 绘制道具符号
            g.drawString(symbol, currentItem.x + 8, currentItem.y + 20);
        }
    }
}