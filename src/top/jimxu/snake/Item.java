package top.jimxu.snake;

public class Item {
    public enum ItemType {
        SCORE_ITEM,
        SPEED_ITEM,
        RANDOM_ITEM
    }
    
    private int x;
    private int y;
    private ItemType type;
    private boolean active;
    private int lifetime;
    
    public Item(int x, int y, ItemType type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.active = true;
        this.lifetime = 50;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public ItemType getType() {
        return type;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public int getLifetime() {
        return lifetime;
    }
    
    public void decreaseLifetime() {
        lifetime--;
        if (lifetime <= 0) {
            active = false;
        }
    }
}