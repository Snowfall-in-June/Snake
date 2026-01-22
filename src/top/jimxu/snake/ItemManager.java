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
    
}