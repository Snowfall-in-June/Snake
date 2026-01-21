package top.jimxu.snake;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

class ItemManager {
    private Item currentItem;
    private Random random;
    private int tickCounter;
    private static final int SPAWN_INTERVAL = 10; // 每10tick判断一次，增加掉落频率
    private static final double SPAWN_PROBABILITY = 0.25; // 掉落概率0.25，增加掉落频率
    private SnakePanel panel;
    public int score;
    
    public ItemManager(SnakePanel panel) {
        this.panel = panel;
        this.random = new Random();
        this.tickCounter = 0;
        this.score = 0;
    }
    
    public void update() {
        if (currentItem != null && currentItem.active) {
            currentItem.update();
        } else if (currentItem != null && !currentItem.active) {
            currentItem = null;
        }
        
        tickCounter++;
    }
    
    public void spawnIfNecessary() {
        if (tickCounter % SPAWN_INTERVAL == 0 && currentItem == null) {
            if (random.nextDouble() < SPAWN_PROBABILITY) {
                spawnItem();
            }
        }
    }
    
    private void spawnItem() {
        // 生成不与蛇和食物重叠的道具位置
        int x, y;
        boolean validPosition;
        
        do {
            validPosition = true;
            // 生成随机位置，确保在网格上
            x = random.nextInt(34) * 25 + 25;
            y = random.nextInt(24) * 25 + 75;
            
            // 检查是否与食物重叠
            if (x == panel.foodx && y == panel.foody) {
                validPosition = false;
                continue;
            }
            
            // 检查是否与蛇重叠
            for (int i = 0; i < panel.len; i++) {
                if (x == panel.snakex[i] && y == panel.snakey[i]) {
                    validPosition = false;
                    break;
                }
            }
        } while (!validPosition);
        
        // 随机选择道具类型
        ItemType[] types = ItemType.values();
        ItemType type = types[random.nextInt(types.length)];
        
        currentItem = new Item(x, y, type);
    }
    
    public void checkSnakeCollision() {
        if (currentItem != null && currentItem.active) {
            // 检查蛇头是否碰到道具
            if (panel.snakex[0] == currentItem.x && panel.snakey[0] == currentItem.y) {
                handleItemEffect(currentItem.type);
                currentItem.active = false;
                currentItem = null;
            }
        }
    }
    
    private void handleItemEffect(ItemType type) {
        switch (type) {
            case SCORE_ITEM:
                score += 5;
                break;
            case SPEED_ITEM:
                // 降低游戏速度，减少10ms
                int currentDelay = panel.timer.getDelay();
                if (currentDelay > 50) { // 确保速度不会太低
                    panel.timer.setDelay(currentDelay - 10);
                }
                break;
            case RANDOM_ITEM:
                System.out.println("Random item collected!");
                break;
        }
    }
    
    public void render(Graphics g) {
        if (currentItem != null && currentItem.active) {
            // 使用黄色绘制道具
            g.setColor(Color.YELLOW);
            // 绘制道具符号 'I'
            g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
            g.drawString("I", currentItem.x + 8, currentItem.y + 18);
        }
    }
}