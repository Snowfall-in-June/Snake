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
                int x = random.nextInt(34) * 25 + 25;
                int y = random.nextInt(24) * 25 + 75;
                ItemType type = ItemType.values()[random.nextInt(ItemType.values().length)];
                currentItem = new Item(x, y, type);
            }
        }
    }
    
    public void render(Graphics g) {
        if (currentItem != null && currentItem.active) {
            g.setColor(Color.YELLOW);
            g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
            g.drawString("I", currentItem.x, currentItem.y + 20);
        }
    }
    
    public void checkSnakeCollision() {
        if (currentItem != null && currentItem.active) {
            if (panel.snakex[0] == currentItem.x && panel.snakey[0] == currentItem.y) {
                applyItemEffect(currentItem.type);
                currentItem.active = false;
                currentItem = null;
            }
        }
    }
    
    private void applyItemEffect(ItemType type) {
        switch (type) {
            case SCORE_ITEM:
                score += 10;
                break;
            case SPEED_ITEM:
                // 临时加速效果
                panel.timer.setDelay(Math.max(50, panel.timer.getDelay() - 30));
                break;
            case RANDOM_ITEM:
                System.out.println("Random item effect triggered!");
                break;
        }
    }
}