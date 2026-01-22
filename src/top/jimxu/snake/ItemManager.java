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
        tickCounter++;
        if (currentItem != null) {
            currentItem.update();
            if (!currentItem.active) {
                currentItem = null;
            }
        }
    }
    
    public void spawnIfNecessary() {
        if (currentItem == null && tickCounter % SPAWN_INTERVAL == 0) {
            if (random.nextDouble() < SPAWN_PROBABILITY) {
                // 生成道具的位置，与食物生成逻辑类似
                int x = random.nextInt(34) * 25 + 25;
                int y = random.nextInt(24) * 25 + 75;
                
                // 随机选择道具类型
                ItemType[] types = ItemType.values();
                ItemType type = types[random.nextInt(types.length)];
                
                currentItem = new Item(x, y, type);
            }
        }
    }
    
    public void render(Graphics g) {
        if (currentItem != null && currentItem.active) {
            // 绘制道具，使用I表示
            g.setColor(Color.YELLOW);
            g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
            g.drawString("I", currentItem.x, currentItem.y + 20);
        }
    }
    
    public void checkSnakeCollision() {
        if (currentItem != null && currentItem.active) {
            if (panel.snakex[0] == currentItem.x && panel.snakey[0] == currentItem.y) {
                // 蛇头碰到道具
                handleItemEffect(currentItem.type);
                currentItem.active = false;
                currentItem = null;
            }
        }
        
        // 检查蛇与食物的碰撞
        if (panel.snakex[0] == panel.foodx && panel.snakey[0] == panel.foody) {
            // 蛇头碰到食物
            panel.len++;
            score += 10;
            
            // 重新生成食物
            // 确保食物位置在有效范围内，并且不会与蛇身重叠
            boolean validPosition = false;
            while (!validPosition) {
                int newFoodx = panel.r.nextInt(34) * 25 + 25;
                int newFoody = panel.r.nextInt(24) * 25 + 75;
                
                // 检查是否与蛇身重叠
                validPosition = true;
                for (int i = 0; i < panel.len; i++) {
                    if (panel.snakex[i] == newFoodx && panel.snakey[i] == newFoody) {
                        validPosition = false;
                        break;
                    }
                }
                
                if (validPosition) {
                    panel.foodx = newFoodx;
                    panel.foody = newFoody;
                }
            }
        }
    }
    
    private void handleItemEffect(ItemType type) {
        switch (type) {
            case SCORE_ITEM:
                score += 50;
                break;
            case SPEED_ITEM:
                // 一次性加速，这里可以通过修改定时器延迟实现
                // 暂时不实现具体逻辑
                break;
            case RANDOM_ITEM:
                System.out.println("获得随机道具！");
                break;
        }
    }
}