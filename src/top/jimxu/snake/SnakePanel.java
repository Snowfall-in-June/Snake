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

public class SnakePanel extends JPanel implements KeyListener,ActionListener{
	//加载所有图片
	ImageIcon up = new ImageIcon("up.png");
	ImageIcon down = new ImageIcon("down.png");
	ImageIcon left = new ImageIcon("left.png");
	ImageIcon right = new ImageIcon("right.png");
	ImageIcon title = new ImageIcon("title.jpg");
	ImageIcon body = new ImageIcon("body.png");
	
	//食物类型枚举
	enum FoodType {
		NORMAL_FOOD, BIG_FOOD, FAST_FOOD, SLOW_FOOD
	}
	
	//食物类
	class Food {
		int x, y;
		FoodType type;
		boolean active;
		
		Food(int x, int y, FoodType type) {
			this.x = x;
			this.y = y;
			this.type = type;
			this.active = true;
		}
	}
	
	//蛇的数据结构设计
	int[] snakex = new int[750];
	int[] snakey = new int[750];
	int len = 3;
	String direction = "R";//R右L左U上D下
	
	//多食物系统
	Random r = new Random();
	Food[] foods = new Food[3]; // 最多3个食物
	int tickCount = 0; // 用于计数tick
	int score = 0; // 分数
	
	//游戏是否开始
	boolean isStarted = false;
	
	//游戏是否失败
	boolean isFaild = false;
	
	//初始化蛇
	public void initSnake(){
		isStarted = false;
		isFaild = false;
		len = 3;
		score = 0;
		tickCount = 0;
		direction = "R";
		snakex[0] = 100;
		snakey[0] = 100;
		snakex[1] = 75;
		snakey[1] = 100;
		snakex[2] = 50;
		snakey[2] = 100;
		
		//初始化食物数组
		for (int i = 0; i < foods.length; i++) {
			foods[i] = null;
		}
	}
	
	public SnakePanel() {
		this.setFocusable(true);
		initSnake(); //放置静态蛇；
		this.addKeyListener(this);//添加键盘监听接口
		timer.start();
	}
	
	//设置蛇移动速度
	Timer timer = new Timer(150, this);
	
	public void paint(Graphics g){
		//设置背景黑色
		this.setBackground(Color.black);
		g.fillRect(25, 75, 850, 600);
		//设置标题
		title.paintIcon(this, g, 25, 11);
		
		//画蛇头
		if(direction.equals("R")){
			right.paintIcon(this, g, snakex[0], snakey[0]);
		}else if(direction.equals("L")){
			left.paintIcon(this, g, snakex[0], snakey[0]);
		}else if(direction.equals("U")){
			up.paintIcon(this, g, snakex[0], snakey[0]);
		}else if(direction.equals("D")){
			down.paintIcon(this, g, snakex[0], snakey[0]);
		}
		
		//画蛇身
		for(int i=1;i<len;i++){
			body.paintIcon(this, g, snakex[i],snakey[i]);
		}
		
		//画开始提示语
		if(!isStarted){
			g.setColor(Color.WHITE);
			g.setFont(new Font("arial",Font.BOLD,30));
			g.drawString("Press Space to Start or Pause", 230, 350);
		}
		
		//画失败提示语
		if (isFaild) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("arial",Font.BOLD,30));
			g.drawString("Game Over,Press Space to Start", 230, 350);
		}
		
		//画食物
		for (Food food : foods) {
			if (food != null && food.active) {
				drawFood(g, food);
			}
		}
		
		//画分数
		g.setColor(Color.WHITE);
		g.setFont(new Font("arial",Font.PLAIN,18));
		g.drawString("Score: " + score, 750, 30);
	}
	
	//画不同类型的食物
	private void drawFood(Graphics g, Food food) {
		g.setFont(new Font("arial", Font.BOLD, 25));
		switch (food.type) {
		case NORMAL_FOOD:
			g.setColor(Color.GREEN);
			g.drawString("●", food.x + 8, food.y + 20);
			break;
		case BIG_FOOD:
			g.setColor(Color.RED);
			g.drawString("◆", food.x + 6, food.y + 22);
			break;
		case FAST_FOOD:
			g.setColor(Color.YELLOW);
			g.drawString("▲", food.x + 8, food.y + 22);
			break;
		case SLOW_FOOD:
			g.setColor(Color.BLUE);
			g.drawString("▼", food.x + 8, food.y + 20);
			break;
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}
	
	//监听按键
	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		//实现空格暂停 继续
		if(keyCode == KeyEvent.VK_SPACE){
			if(isFaild){
				initSnake();
			}
			else{
				isStarted = !isStarted;
			}
		}
		//实现转向
		else if(keyCode == KeyEvent.VK_UP && !direction.equals("D")){
			direction="U";
		}else if(keyCode == KeyEvent.VK_DOWN && !direction.equals("U")){
			direction="D";
		}else if(keyCode == KeyEvent.VK_LEFT && !direction.equals("R")){
			direction="L";
		}else if(keyCode == KeyEvent.VK_RIGHT && !direction.equals("L")){
			direction="R";
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		timer.start();
		
		if(isStarted && !isFaild){
			//移动身体
			for(int i=len;i>0;i--){				snakex[i] = snakex[i-1];
				snakey[i] = snakey[i-1];
			}
			
			//头移动
			if(direction.equals("R")){
				snakex[0] = snakex[0]+25;
				if(snakex[0]>850) snakex[0] = 25;
			}else if(direction.equals("L")){
				snakex[0] = snakex[0]-25;
				if(snakex[0]<25) snakex[0] = 850;
			}else if(direction.equals("U")){
				snakey[0] = snakey[0]-25;
				if(snakey[0]<75) snakey[0] = 650;
			}else if(direction.equals("D")){
				snakey[0] = snakey[0]+25;
				if(snakey[0]>650) snakey[0] = 75;
			}
			
			//检查是否吃到食物
			for (Food food : foods) {
				if (food != null && food.active && snakex[0] == food.x && snakey[0] == food.y) {
					handleFoodEaten(food);
					break;
				}
			}
			
			//判断游戏失败
			for(int i=1;i<len;i++){
				if(snakex[0] == snakex[i] && snakey[0] == snakey[i]){
					isFaild = true;
				}
			}
			
			//食物生成逻辑
			tickCount++;
			if (tickCount % 15 == 0) {
				spawnFood();
			}
		}
		repaint();
	}
	
	//处理吃到食物的效果
	private void handleFoodEaten(Food food) {
		food.active = false;
		
		switch (food.type) {
		case NORMAL_FOOD:
			len++;
			score++;
			break;
		case BIG_FOOD:
			len += 2;
			score += 3;
			break;
		case FAST_FOOD:
			score += 2;
			int currentDelay = timer.getDelay();
			if (currentDelay > 50) { // 限制最小速度
				timer.setDelay(currentDelay - 10);
			}
			break;
		case SLOW_FOOD:
			score += 2;
			currentDelay = timer.getDelay();
			if (currentDelay < 500) { // 限制最大速度
				timer.setDelay(currentDelay + 10);
			}
			break;
		}
	}
	
	//生成食物
	private void spawnFood() {
		//检查是否有空闲位置
		for (int i = 0; i < foods.length; i++) {
			if (foods[i] == null || !foods[i].active) {
				//30%的概率生成食物
				if (r.nextDouble() < 0.3) {
					//随机生成食物类型
					FoodType type = randomFoodType();
					//生成不重叠的位置
					int x, y;
					do {
						x = r.nextInt(34) * 25 + 25;
						y = r.nextInt(24) * 25 + 75;
					} while (isPositionOccupied(x, y));
					
					//创建食物
					foods[i] = new Food(x, y, type);
				}
				break;
			}
		}
	}
	
	//随机生成食物类型
	private FoodType randomFoodType() {
		double rand = r.nextDouble();
		if (rand < 0.6) {
			return FoodType.NORMAL_FOOD;
		} else if (rand < 0.8) {
			return FoodType.BIG_FOOD;
		} else if (rand < 0.9) {
			return FoodType.FAST_FOOD;
		} else {
			return FoodType.SLOW_FOOD;
		}
	}
	
	//检查位置是否被蛇或其他食物占据
	private boolean isPositionOccupied(int x, int y) {
		//检查蛇身
		for (int i = 0; i < len; i++) {
			if (snakex[i] == x && snakey[i] == y) {
				return true;
			}
		}
		
		//检查其他食物
		for (Food food : foods) {
			if (food != null && food.active && food.x == x && food.y == y) {
				return true;
			}
		}
		
		return false;
	}
}