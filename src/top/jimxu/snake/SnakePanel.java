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
		NORMAL_FOOD("●", Color.GREEN, 1, 1, 0),
		BIG_FOOD("◆", Color.YELLOW, 2, 3, 0),
		FAST_FOOD("▲", Color.RED, 0, 2, -10),
		SLOW_FOOD("▼", Color.BLUE, 0, 2, 10);
		
		private String symbol;
		private Color color;
		private int lengthIncrease;
		private int scoreIncrease;
		private int speedChange;
		
		FoodType(String symbol, Color color, int lengthIncrease, int scoreIncrease, int speedChange) {
			this.symbol = symbol;
			this.color = color;
			this.lengthIncrease = lengthIncrease;
			this.scoreIncrease = scoreIncrease;
			this.speedChange = speedChange;
		}
		
		public String getSymbol() { return symbol; }
		public Color getColor() { return color; }
		public int getLengthIncrease() { return lengthIncrease; }
		public int getScoreIncrease() { return scoreIncrease; }
		public int getSpeedChange() { return speedChange; }
	}
	
	//食物类
	public class Food {
		int x;
		int y;
		FoodType type;
		boolean active;
		
		Food(int x, int y, FoodType type) {
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
	
	//蛇的数据结构设计
	int[] snakex = new int[750];
	int[] snakey = new int[750];
	int len = 3;
	String direction = "R"; // R右L左U上D下
	int score = 0; // 分数
	
	//食物相关
	Random r = new Random();
	List<Food> foods = new ArrayList<>();
	int maxFoodCount = 3; // 最大食物数量
	int tickCounter = 0; // tick计数器
	final int spawnInterval = 15; // 生成判定间隔
	final double spawnProbability = 0.3; // 生成概率
	
	//游戏是否开始
	boolean isStarted = false;
	
	//游戏是否失败
	boolean isFaild = false;

	
	//初始化蛇
	public void initSnake(){
		isStarted = false;
		isFaild = false;
		len = 3;
		direction = "R";
		score = 0;
		foods.clear();
		tickCounter = 0;
		snakex[0] = 100;
		snakey[0] = 100;
		snakex[1] = 75;
		snakey[1] = 100;
		snakex[2] = 50;
		snakey[2] = 100;
		// 重置速度
		timer.setDelay(150);
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
		for(Food food : foods){
			if(food.active){
				g.setColor(food.type.getColor());
				g.setFont(new Font("Arial", Font.BOLD, 25));
				g.drawString(food.type.getSymbol(), food.x + 8, food.y + 20);
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
	// 检查位置是否有效（不与蛇、其他食物重叠）
	private boolean isValidPosition(int x, int y) {
		// 检查是否与蛇重叠
		for (int i = 0; i < len; i++) {
			if (snakex[i] == x && snakey[i] == y) {
				return false;
			}
		}
		// 检查是否与其他食物重叠
		for (Food food : foods) {
			if (food.active && food.x == x && food.y == y) {
				return false;
			}
		}
		return true;
	}
	
	// 生成随机食物类型
	private FoodType generateRandomFoodType() {
		int random = r.nextInt(100);
		if (random < 60) {
			return FoodType.NORMAL_FOOD;
		} else if (random < 80) {
			return FoodType.BIG_FOOD;
		} else if (random < 90) {
			return FoodType.FAST_FOOD;
		} else {
			return FoodType.SLOW_FOOD;
		}
	}
	
	// 尝试生成食物
	private void trySpawnFood() {
		if (foods.size() < maxFoodCount && r.nextDouble() < spawnProbability) {
			int x, y;
			int attempts = 0;
			final int maxAttempts = 100;
			
			// 尝试生成有效的位置
			do {
				x = r.nextInt(34) * 25 + 25;
				y = r.nextInt(24) * 25 + 75;
				attempts++;
			} while (!isValidPosition(x, y) && attempts < maxAttempts);
			
			if (attempts < maxAttempts) {
				foods.add(new Food(x, y, generateRandomFoodType()));
			}
		}
	}
	
	// 处理食物被吃掉的效果
	private void handleFoodEffect(Food food) {
		// 增加长度
		len += food.type.getLengthIncrease();
		// 增加分数
		score += food.type.getScoreIncrease();
		// 改变速度
		if (food.type.getSpeedChange() != 0) {
			int currentDelay = timer.getDelay();
			int newDelay = Math.max(50, currentDelay + food.type.getSpeedChange()); // 最小延迟50ms
			timer.setDelay(newDelay);
		}
		// 标记食物为非活动
		food.active = false;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		timer.start();
		tickCounter++;
		
		if(isStarted && !isFaild){
			// 移动身体
			for(int i=len;i>0;i--){
				snakex[i] = snakex[i-1];
				snakey[i] = snakey[i-1];
			}
			// 头移动
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
			
			// 检查是否吃到食物
			for (Food food : foods) {
				if (food.active && snakex[0] == food.x && snakey[0] == food.y) {
					handleFoodEffect(food);
				}
			}
			// 移除非活动的食物
			foods.removeIf(food -> !food.active);
			
			// 每15 tick尝试生成食物
			if (tickCounter % spawnInterval == 0) {
				trySpawnFood();
			}
			
			// 判断游戏失败
			for(int i=1;i<len;i++){
				if(snakex[0] == snakex[i] && snakey[0] == snakey[i]){
					isFaild = true;
				}
			}
		}
		repaint();
	}
}
