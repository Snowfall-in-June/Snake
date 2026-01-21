package top.jimxu.snake;

enum ItemType {
    SCORE_ITEM,  // 加分
    SPEED_ITEM,  // 一次性加速
    RANDOM_ITEM  // 打印提示
}

class Item {
    public int x;
    public int y;
    public ItemType type;
    public boolean active;
    public int lifetime;
    public static final int DEFAULT_LIFETIME = 50; // 默认50个tick
    
    public Item(int x, int y, ItemType type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.active = true;
        this.lifetime = DEFAULT_LIFETIME;
    }
    
    public void update() {
        if (active) {
            lifetime--;
            if (lifetime <= 0) {
                active = false;
            }
        }
    }
}