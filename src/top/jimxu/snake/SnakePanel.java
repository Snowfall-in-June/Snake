package top.jimxu.snake;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SnakePanel extends JPanel implements KeyListener,ActionListener{
	//食物类型枚举
	public enum FoodType {
		NORMAL_FOOD, BIG_FOOD, FAST_FOOD, SLOW_FOOD
	}

	//食物类
	public class Food {
		int x;
		int y;
		FoodType type;
		boolean active;

		public Food(int x, int y, FoodType type) {
			this.x = x;
			this.y = y;
			this.type = type;
			this.active = true;
		}
	}

	//加载所有图片
	ImageIcon up = new ImageIcon("up.png");
	ImageIcon down = new ImageIcon("down.png");
	ImageIcon left = new ImageIcon("left.png");
	ImageIcon right = new ImageIcon("right.png");
	ImageIcon title = new ImageIcon("title.jpg");
	ImageIcon body = new ImageIcon("body.png");
	ImageIcon food = new ImageIcon("food.png");
	
	//蛇的数据结构设计
	int[] snakex = new int[750];
	int[] snakey = new int[750];
	int len = 3;
	String direction = "R";//R右L左U上D下

	//分数
	int score = 0;
	
	//食物相关
	Random r = new Random();
	List<Food> foods = new ArrayList<>();
	int tickCounter = 0; // 用于计算每15 tick
	int speed = 150; // 初始速度
	
	//游戏是否开始
	boolean isStarted = false;
	
	//游戏是否失败
	boolean isFaild = false;

	//	初始化蛇
	public void initSnake(){
		isStarted = false;
		isFaild = false;
		len = 3;
		score = 0;
		speed = 150;
		timer.setDelay(speed);
		direction = "R";
		snakex[0] = 100;
		snakey[0] = 100;
		snakex[1] = 75;
		snakey[1] = 100;
		snakex[2] = 50;
		snakey[2] = 100;

		// 清空食物列表
		foods.clear();
		tickCounter = 0;
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

		//画分数
		g.setColor(Color.WHITE);
		g.setFont(new Font("arial", Font.BOLD, 20));
		g.drawString("Score: " + score, 750, 50);
		
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
		for(Food f : foods){
			if(f.active){
				// 根据食物类型设置不同颜色
				switch(f.type){
					case NORMAL_FOOD:
						g.setColor(Color.GREEN);
						break;
					case BIG_FOOD:
						g.setColor(Color.RED);
						break;
					case FAST_FOOD:
						g.setColor(Color.YELLOW);
						break;
					case SLOW_FOOD:
						g.setColor(Color.BLUE);
						break;
				}
				g.fillOval(f.x, f.y, 25, 25);
			}
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
//			repaint();
		}//实现转向
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
	/*
	 * 1.定个闹钟
	 * 2.蛇移动
	 * 3.重画一次蛇
	 */
	// 检查位置是否可以放置食物
	private boolean isPositionValid(int x, int y) {
		// 检查是否与蛇重叠
		for(int i=0; i<len; i++){
			if(snakex[i] == x && snakey[i] == y){
				return false;
			}
		}

		// 检查是否与其他食物重叠
		for(Food f : foods){
			if(f.active && f.x == x && f.y == y){
				return false;
			}
		}

		return true;
	}

	// 生成食物
	private void generateFood() {
		if(foods.size() >= 3) return; // 最多3个食物

		// 生成概率30%
		if(r.nextDouble() < 0.3) {
			// 确定食物类型
			FoodType type;
			int rand = r.nextInt(100);
			if(rand < 60) {
				type = FoodType.NORMAL_FOOD;
			} else if(rand < 80) {
				type = FoodType.BIG_FOOD;
			} else if(rand < 90) {
				type = FoodType.FAST_FOOD;
			} else {
				type = FoodType.SLOW_FOOD;
			}

			// 找到有效的位置
			int x, y;
			int attempts = 0;
			do {
				x = r.nextInt(34)*25 + 25;
				y = r.nextInt(24)*25 + 75;
				attempts++;
			} while(!isPositionValid(x, y) && attempts < 100);

			if(attempts < 100) {
				foods.add(new Food(x, y, type));
			}
		}
	}

	// 处理吃食物的效果
	private void handleFoodEffect(FoodType type) {
		switch(type) {
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
				// 加速，减少延迟
				speed = Math.max(50, speed - 10);
				timer.setDelay(speed);
				break;
			case SLOW_FOOD:
				score += 2;
				// 减速，增加延迟
				speed += 10;
				timer.setDelay(speed);
				break;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		timer.start();
		tickCounter++;

		// 每15 tick尝试生成食物
		if(tickCounter % 15 == 0) {
			generateFood();
		}

		if(isStarted && !isFaild){
			//移动身体
			for(int i=len; i>0; i--){
				snakex[i] = snakex[i-1];
				snakey[i] = snakey[i-1];
			}

			//头移动
			if(direction.equals("R")){
				snakex[0] += 25;
				if(snakex[0] > 850) snakex[0] = 25;
			}else if(direction.equals("L")){
				snakex[0] -= 25;
				if(snakex[0] < 25) snakex[0] = 850;
			}else if(direction.equals("U")){
				snakey[0] -= 25;
				if(snakey[0] < 75) snakey[0] = 650;
			}else if(direction.equals("D")){
				snakey[0] += 25;
				if(snakey[0] > 650) snakey[0] = 75;
			}

			// 检查是否吃了食物
			for(Food f : foods){
				if(f.active && snakex[0] == f.x && snakey[0] == f.y){
					f.active = false;
					handleFoodEffect(f.type);
				}
			}

			// 清理非活跃的食物
			foods.removeIf(f -> !f.active);

			//判断游戏失败
			for(int i=1; i<len; i++){
				if(snakex[0] == snakex[i] && snakey[0] == snakey[i]){
					isFaild = true;
				}
			}
		}
		repaint();
	}
}
