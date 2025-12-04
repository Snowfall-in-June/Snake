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
		SPEED_UP, SPEED_DOWN, REVERSE_CONTROL, SPAWN_EXTRA_FOOD, CLEAR_FOOD, SCORE_BONUS
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
	
	// 食物生成 - 使用列表存储多个食物
	Random r = new Random();
	List<int[]> foods = new ArrayList<>();
	
	// 游戏是否开始
	boolean isStarted = false;
	
	// 游戏是否失败
	boolean isFaild = false;
	
	// 分数
	int score = 0;
	
	// 事件系统相关变量
	int tickCounter = 0; // 全局tick计数器
	int lastEventTick = -20; // 上次事件触发的tick
	int reverseControlCounter = 0; // 反转控制计数器
	int baseDelay = 150; // 基础延迟时间
	
	// 事件权重
	private static final EventType[] eventPool = {
		EventType.SPEED_UP, EventType.SPEED_UP, EventType.SPEED_UP, EventType.SPEED_UP, EventType.SPEED_UP, // 25%
		EventType.SPEED_DOWN, EventType.SPEED_DOWN, EventType.SPEED_DOWN, EventType.SPEED_DOWN, EventType.SPEED_DOWN, // 25%
		EventType.REVERSE_CONTROL, EventType.REVERSE_CONTROL, EventType.REVERSE_CONTROL, // 15%
		EventType.SPAWN_EXTRA_FOOD, EventType.SPAWN_EXTRA_FOOD, EventType.SPAWN_EXTRA_FOOD, EventType.SPAWN_EXTRA_FOOD, // 20%
		EventType.CLEAR_FOOD, // 5%
		EventType.SCORE_BONUS, EventType.SCORE_BONUS // 10%
	};

	
	// 初始化蛇
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
		
		// 初始化食物
		foods.clear();
		generateFood();
		
		// 初始化分数和事件系统
		score = 0;
		tickCounter = 0;
		lastEventTick = -20;
		reverseControlCounter = 0;
		timer.setDelay(baseDelay);
	}
	
	// 生成食物
	private void generateFood() {
		int x = r.nextInt(34)*25+25;
		int y = r.nextInt(24)*25+75;
		foods.add(new int[]{x, y});
	}
	
	// 随机事件触发和执行
	private void handleRandomEvents() {
		tickCounter++;
		
		// 每30 tick检查一次，且两次事件间隔至少20 tick
		if (tickCounter % 30 == 0 && (tickCounter - lastEventTick) >= 20) {
			// 0.2的概率触发事件
			if (r.nextDouble() < 0.2) {
				lastEventTick = tickCounter;
				// 按权重选择事件
				EventType event = eventPool[r.nextInt(eventPool.length)];
				executeEvent(event);
			}
		}
	}
	
	// 执行事件
	private void executeEvent(EventType event) {
		switch (event) {
			case SPEED_UP:
				// 减少延迟，最小为50ms
				int currentDelay = timer.getDelay();
				if (currentDelay > 50) {
					timer.setDelay(currentDelay - 10);
				}
				break;
			case SPEED_DOWN:
				// 增加延迟，最大为300ms
				currentDelay = timer.getDelay();
				if (currentDelay < 300) {
					timer.setDelay(currentDelay + 10);
				}
				break;
			case REVERSE_CONTROL:
				reverseControlCounter = 3;
				break;
			case SPAWN_EXTRA_FOOD:
				// 生成1-2个新食物
				int count = r.nextInt(2) + 1;
				for (int i = 0; i < count; i++) {
					generateFood();
				}
				break;
			case CLEAR_FOOD:
				foods.clear();
				// 清空后生成一个新食物
				generateFood();
				break;
			case SCORE_BONUS:
				// 分数随机+3~8
				score += r.nextInt(6) + 3;
				break;
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
		// 设置背景黑色
		this.setBackground(Color.black);
		g.fillRect(25, 75, 850, 600);
		// 设置标题
		title.paintIcon(this, g, 25, 11);
		
		// 画分数
		g.setColor(Color.WHITE);
		g.setFont(new Font("arial", Font.BOLD, 20));
		g.drawString("Score: " + score, 750, 30);
		
		// 画当前速度
		int currentDelay = timer.getDelay();
		int speed = (baseDelay - currentDelay + 50) / 10; // 速度值，越大越快
		g.drawString("Speed: " + speed, 750, 55);
		
		// 画反转控制剩余次数
		if (reverseControlCounter > 0) {
			g.setColor(Color.RED);
			g.drawString("Reverse: " + reverseControlCounter, 750, 80);
		}
		
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
		
		// 画所有食物
		for (int[] food : foods) {
			this.food.paintIcon(this, g, food[0], food[1]);
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
		} else {
			// 处理方向控制，考虑反转控制
			if (reverseControlCounter > 0) {
				// 反转控制：上变下，下变上，左变右，右变左
				switch(keyCode) {
					case KeyEvent.VK_UP:
						if (!direction.equals("U")) direction="D";
						break;
					case KeyEvent.VK_DOWN:
						if (!direction.equals("D")) direction="U";
						break;
					case KeyEvent.VK_LEFT:
						if (!direction.equals("L")) direction="R";
						break;
					case KeyEvent.VK_RIGHT:
						if (!direction.equals("R")) direction="L";
						break;
				}
				reverseControlCounter--;
			} else {
				// 正常控制
				switch(keyCode) {
					case KeyEvent.VK_UP:
						if (!direction.equals("D")) direction="U";
						break;
					case KeyEvent.VK_DOWN:
						if (!direction.equals("U")) direction="D";
						break;
					case KeyEvent.VK_LEFT:
						if (!direction.equals("R")) direction="L";
						break;
					case KeyEvent.VK_RIGHT:
						if (!direction.equals("L")) direction="R";
						break;
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	/*
	 * 1.定个闹钟
	 * 2.处理随机事件
	 * 3.蛇移动
	 * 4.重画一次蛇
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		timer.start();
		
		// 处理随机事件
		handleRandomEvents();
		
		if(isStarted && !isFaild){
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
			// 吃食物
			for (int i = foods.size() - 1; i >= 0; i--) {
				int[] food = foods.get(i);
				if(snakex[0] == food[0] && snakey[0] == food[1]){
					len++;
					score += 10; // 吃食物得10分
					foods.remove(i);
					// 生成新食物
					generateFood();
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
