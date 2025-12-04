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
	// 事件类型枚举
	private enum EventType {
		SPEED_UP,
		SPEED_DOWN,
		REVERSE_CONTROL,
		SPAWN_EXTRA_FOOD,
		CLEAR_FOOD,
		SCORE_BONUS
	}
	
	// 加载所有图片
	ImageIcon up = new ImageIcon("up.png");
	ImageIcon down = new ImageIcon("down.png");
	ImageIcon left = new ImageIcon("left.png");
	ImageIcon right = new ImageIcon("right.png");
	ImageIcon title = new ImageIcon("title.jpg");
	ImageIcon body = new ImageIcon("body.png");
	ImageIcon food = new ImageIcon("food.png");
	
	// 蛇的数据结构设计
	int[] snakex = new int[750];
	int[] snakey = new int[750];
	int len = 3;
	String direction = "R"; // R右L左U上D下
	
	// 食物生成
	Random r = new Random();
	int foodx = r.nextInt(34)*25+25; // 34个格子，一个格子25排像素，还有25像素空白
	int foody = r.nextInt(24)*25+75; // 24个格子，一个格子25排像素，还有75像素空白
	// 额外食物列表
	List<int[]> extraFoods = new ArrayList<>();
	
	// 游戏是否开始
	boolean isStarted = false;
	
	// 游戏是否失败
	boolean isFaild = false;
	
	// 分数
	int score = 0;
	
	// 随机事件相关
	int tickCounter = 0; // tick计数器
	int lastEventTick = -20; // 上次事件发生的tick
	int reverseControlCounter = 0; // 控制反转剩余次数
	int baseDelay = 150; // 基础延迟
	int currentDelay = baseDelay; // 当前延迟

	
	//	初始化蛇
	public void initSnake(){
		isStarted = false;
		isFaild = false;
		len = 3;
		direction = "R";
		snakex[0] = 100;
		snakey[0] = 100;
		snakex[1] = 75;
		snakey[1] = 100;
		snakex[2] = 50;
		snakey[2] = 100;
		
		// 重置事件相关变量
		tickCounter = 0;
		lastEventTick = -20;
		reverseControlCounter = 0;
		currentDelay = baseDelay;
		timer.setDelay(currentDelay);
		
		// 重置食物
		foodx = r.nextInt(34)*25+25;
		foody = r.nextInt(24)*25+75;
		extraFoods.clear();
		
		// 重置分数
		score = 0;
	}
	public SnakePanel() {
		this.setFocusable(true);
		initSnake(); //放置静态蛇；
		this.addKeyListener(this);//添加键盘监听接口
		timer.start();
	}
	// 设置蛇移动速度
	Timer timer = new Timer(currentDelay, this);
	
	public void paint(Graphics g){
		// 设置背景黑色
		this.setBackground(Color.black);
		g.fillRect(25, 75, 850, 600);
		// 设置标题
		title.paintIcon(this, g, 25, 11);
		
		// 画分数
		g.setColor(Color.WHITE);
		g.setFont(new Font("arial", Font.BOLD, 20));
		g.drawString("Score: " + score, 750, 50);
		
		// 画蛇头
		if(direction.equals("R")){
			right.paintIcon(this, g, snakex[0], snakey[0]);
		}else if(direction.equals("L")){
			left.paintIcon(this, g, snakex[0], snakey[0]);
		}else if(direction.equals("U")){
			up.paintIcon(this, g, snakex[0], snakey[0]);
		}else if(direction.equals("D")){
			down.paintIcon(this, g, snakex[0], snakey[0]);
		}
		// 画蛇身
		for(int i=1;i<len;i++){
			body.paintIcon(this, g, snakex[i],snakey[i]);
		}
		
		// 画开始提示语
		if(!isStarted){
			g.setColor(Color.WHITE);
			g.setFont(new Font("arial",Font.BOLD,30));
			g.drawString("Press Space to Start or Pause", 230, 350);
		}
		// 画失败提示语
		if (isFaild) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("arial",Font.BOLD,30));
			g.drawString("Game Over,Press Space to Start", 230, 350);
		}
		
		// 画主食物
		food.paintIcon(this, g, foodx, foody);
		
		// 画额外食物
		for(int[] extraFood : extraFoods){
			food.paintIcon(this, g, extraFood[0], extraFood[1]);
		}
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	// 监听按键
	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		// 实现空格暂停 继续
		if(keyCode == KeyEvent.VK_SPACE){
			if(isFaild){
				initSnake();
			}
			else{
			isStarted = !isStarted;
			}
//			repaint();
		}// 实现转向
		else if(reverseControlCounter > 0){
			// 控制反转时，按键方向相反
			if(keyCode == KeyEvent.VK_UP && !direction.equals("U")){
				direction="D";
				reverseControlCounter--;
			}else if(keyCode == KeyEvent.VK_DOWN && !direction.equals("D")){
				direction="U";
				reverseControlCounter--;
			}else if(keyCode == KeyEvent.VK_LEFT && !direction.equals("L")){
				direction="R";
				reverseControlCounter--;
			}else if(keyCode == KeyEvent.VK_RIGHT && !direction.equals("R")){
				direction="L";
				reverseControlCounter--;
			}
		}
		else{
			// 正常控制
			if(keyCode == KeyEvent.VK_UP && !direction.equals("D")){
				direction="U";
			}else if(keyCode == KeyEvent.VK_DOWN && !direction.equals("U")){
				direction="D";
			}else if(keyCode == KeyEvent.VK_LEFT && !direction.equals("R")){
				direction="L";
			}else if(keyCode == KeyEvent.VK_RIGHT && !direction.equals("L")){
				direction="R";
			}
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
	// 随机事件处理
	private void handleRandomEvent(){
		tickCounter++;
		
		// 每30 tick检查一次
		if(tickCounter % 30 == 0){
			// 触发概率0.2，且两次事件间隔至少20 tick
			if(r.nextDouble() < 0.2 && tickCounter - lastEventTick >= 20){
				lastEventTick = tickCounter;
				// 按权重选择事件
				EventType event = selectRandomEvent();
				// 执行事件
				executeEvent(event);
			}
		}
	}
	
	// 按权重选择随机事件
	private EventType selectRandomEvent(){
		double random = r.nextDouble();
		if(random < 0.25) return EventType.SPEED_UP; // 25%
		else if(random < 0.50) return EventType.SPEED_DOWN; // 25%
		else if(random < 0.65) return EventType.REVERSE_CONTROL; // 15%
		else if(random < 0.85) return EventType.SPAWN_EXTRA_FOOD; // 20%
		else if(random < 0.90) return EventType.CLEAR_FOOD; // 5%
		else return EventType.SCORE_BONUS; // 10%
	}
	
	// 执行事件
	private void executeEvent(EventType event){
		switch(event){
			case SPEED_UP:
				// 减少一次tick（如-10ms）
				currentDelay = Math.max(50, currentDelay - 10);
				timer.setDelay(currentDelay);
				break;
			case SPEED_DOWN:
				// 增加一次tick（如+10ms）
				currentDelay = Math.min(300, currentDelay + 10);
				timer.setDelay(currentDelay);
				break;
			case REVERSE_CONTROL:
				// 设置reverseControlCounter = 3
				reverseControlCounter = 3;
				break;
			case SPAWN_EXTRA_FOOD:
				// 调用FoodManager生成1~2个新食物
				int count = r.nextInt(2) + 1; // 1~2个
				for(int i=0;i<count;i++){
					int[] newFood = {r.nextInt(34)*25+25, r.nextInt(24)*25+75};
					extraFoods.add(newFood);
				}
				break;
			case CLEAR_FOOD:
				// 删除所有food
				foodx = r.nextInt(34)*25+25;
				foody = r.nextInt(24)*25+75;
				extraFoods.clear();
				break;
			case SCORE_BONUS:
				// 分数随机+3～8
				score += r.nextInt(6) + 3; // 3~8
				break;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		timer.start();
		
		if(isStarted && !isFaild){
			// 处理随机事件
			handleRandomEvent();
			
			// 移动身体
			for(int i=len;i>0;i--){
				snakex[i] = snakex[i-1];
				snakey[i] = snakey[i-1];
			}
			// 头移动
			if(direction.equals("R")){
				// 横坐标+25
				snakex[0] = snakex[0]+25;
				if(snakex[0]>850) snakex[0] = 25;
				
			}else if(direction.equals("L")){
				// 横坐标-25
				snakex[0] = snakex[0]-25;
				if(snakex[0]<25) snakex[0] = 850;
			}else if(direction.equals("U")){
				// 纵坐标-25
				snakey[0] = snakey[0]-25;
				if(snakey[0]<75) snakey[0] = 650;
			}else if(direction.equals("D")){
				// 纵坐标+25
				snakey[0] = snakey[0]+25;
				if(snakey[0]>650) snakey[0] = 75;
			}
			// 吃主食物
			if(snakex[0] == foodx && snakey[0] == foody){
				len++;
				score += 10; // 吃主食物得10分
				foodx = r.nextInt(34)*25+25;
				foody = r.nextInt(24)*25+75;
			}
			// 吃额外食物
			for(int i=extraFoods.size()-1;i>=0;i--){
				int[] extraFood = extraFoods.get(i);
				if(snakex[0] == extraFood[0] && snakey[0] == extraFood[1]){
					len++;
					score += 15; // 吃额外食物得15分
					extraFoods.remove(i);
				}
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
