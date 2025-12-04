package top.jimxu.snake;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Random;

public class ItemSystem {
    // 道具类型枚举
    public enum ItemType {
        SCORE_ITEM,  // 加分道具
        SPEED_ITEM,  // 一次性加速道具
        RANDOM_ITEM  // 随机提示道具
    }
    
    // 道具对象
    private class Item {
        int x;         // x坐标
        int y;         // y坐标
        ItemType type; // 道具类型
        boolean active; // 是否激活
        int lifetime;  // 剩余生命周期
        
        public Item(int x, int y, ItemType type) {
            this.x = x;
            this.y = y;
            this.type = type;
            this.active = true;
            this.lifetime = 50; // 默认50 tick
        }
    }
    
    private SnakePanel snakePanel; // 引用主面板
    private Random random;          // 随机数生成器
    private Item currentItem;       // 当前激活的道具
    private int tickCounter;         // 用于控制掉落频率的计数器
    
    // 构造函数
    public ItemSystem(SnakePanel snakePanel) {
        this.snakePanel = snakePanel;
        this.random = new Random();
        this.currentItem = null;
        this.tickCounter = 0;
    }
    
    // 更新道具状态
    public void update() {
        if (currentItem != null && currentItem.active) {
            currentItem.lifetime--;
            
            // 超过生命周期，移除道具
            if (currentItem.lifetime <= 0) {
                currentItem.active = false;
                currentItem = null;
            }
        }
    }
    
    // 检查是否需要生成道具
    public void spawnIfNecessary() {
        tickCounter++;
        
        // 每20 tick判断一次是否生成道具
        if (tickCounter % 20 == 0) {
            // 掉落概率0.15，且当前没有激活的道具
            if (random.nextDouble() < 0.15 && currentItem == null) {
                spawnItem();
            }
        }
    }
    
    // 生成道具
    private void spawnItem() {
        int x, y;
        boolean validPosition;
        
        do {
            // 随机生成道具位置（与食物位置逻辑相同）
            x = random.nextInt(34) * 25 + 25;
            y = random.nextInt(24) * 25 + 75;
            
            // 检查是否与蛇重叠
            validPosition = !isPositionOccupiedBySnake(x, y);
            
            // 检查是否与食物重叠
            if (validPosition) {
                validPosition = !(x == snakePanel.foodx && y == snakePanel.foody);
            }
        } while (!validPosition);
        
        // 随机选择道具类型
        ItemType[] itemTypes = ItemType.values();
        ItemType randomType = itemTypes[random.nextInt(itemTypes.length)];
        
        // 创建道具
        currentItem = new Item(x, y, randomType);
    }
    
    // 检查位置是否被蛇占据
    private boolean isPositionOccupiedBySnake(int x, int y) {
        for (int i = 0; i < snakePanel.len; i++) {
            if (snakePanel.snakex[i] == x && snakePanel.snakey[i] == y) {
                return true;
            }
        }
        return false;
    }
    
    // 检查蛇是否与道具碰撞
    public void checkSnakeCollision() {
        if (currentItem != null && currentItem.active) {
            if (snakePanel.snakex[0] == currentItem.x && snakePanel.snakey[0] == currentItem.y) {
                // 吃到道具，触发效果
                applyItemEffect(currentItem.type);
                
                // 移除道具
                currentItem.active = false;
                currentItem = null;
            }
        }
    }
    
    // 应用道具效果
    private void applyItemEffect(ItemType type) {
        switch (type) {
            case SCORE_ITEM:
                // 加分效果（由于现有代码没有score变量，这里先不实现具体加分逻辑）
                System.out.println("Score item collected!");
                break;
            case SPEED_ITEM:
                // 一次性加速效果（减少游戏速度10ms）
                int currentDelay = snakePanel.timer.getDelay();
                snakePanel.timer.setDelay(currentDelay - 10);
                System.out.println("Speed item collected! Current speed: " + snakePanel.timer.getDelay() + "ms");
                break;
            case RANDOM_ITEM:
                // 随机提示效果
                System.out.println("Random item collected!");
                break;
        }
    }
    
    // 绘制道具
    public void render(Graphics g) {
        if (currentItem != null && currentItem.active) {
            // 使用黄色绘制道具符号'I'
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("I", currentItem.x + 8, currentItem.y + 20);
        }
    }
}
