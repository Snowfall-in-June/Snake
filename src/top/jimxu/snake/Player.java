package top.jimxu.snake;

public class Player {
    // 蛇的坐标数组
    int[] snakex = new int[750];
    int[] snakey = new int[750];
    
    // 蛇的长度
    int len = 3;
    
    // 移动方向：R右 L左 U上 D下
    String direction;
    
    // 分数
    int score = 0;
    
    // 存活状态
    boolean isAlive = true;
    
    // 构造方法
    public Player(String initialDirection) {
        this.direction = initialDirection;
        initSnake();
    }
    
    // 初始化蛇的位置
    public void initSnake() {
        len = 3;
        score = 0;
        isAlive = true;
        
        // 为了让两条蛇初始位置不重叠，我们需要设置不同的初始坐标
        if (direction.equals("R")) {
            // P1初始位置（红色）
            snakex[0] = 100;
            snakey[0] = 100;
            snakex[1] = 75;
            snakey[1] = 100;
            snakex[2] = 50;
            snakey[2] = 100;
        } else if (direction.equals("L")) {
            // P2初始位置（蓝色）
            snakex[0] = 750;
            snakey[0] = 500;
            snakex[1] = 775;
            snakey[1] = 500;
            snakex[2] = 800;
            snakey[2] = 500;
        }
    }
}
