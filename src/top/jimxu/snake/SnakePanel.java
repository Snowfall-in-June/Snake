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
	ImageIcon food = new ImageIcon("food.png");
	
	//蛇的数据结构设计 - 玩家1 (P1)
	int[] snakex1 = new int[750];
	int[] snakey1 = new int[750];
	int len1 = 3;
	String direction1 = "R";//R右L左U上D下
	boolean isAlive1 = true;
	int score1 = 0;
	
	//蛇的数据结构设计 - 玩家2 (P2)
	int[] snakex2 = new int[750];
	int[] snakey2 = new int[750];
	int len2 = 3;
	String direction2 = "L";//R右L左U上D下
	boolean isAlive2 = true;
	int score2 = 0;
	
	//食物生成
	Random r = new Random();
	int foodx = r.nextInt(34)*25+25; // 34个格子，一个格子25排像素，还有25像素空白
	int foody = r.nextInt(24)*25+75; // 24个格子，一个格子25排像素，还有75像素空白
	
	//游戏是否开始
	boolean isStarted = false;
	
	//游戏是否结束
	boolean isGameOver = false;
	
	//游戏结果
	String gameResult = "";

	
	//	初始化蛇
	public void initSnake(){
		isStarted = false;
		isGameOver = false;
		gameResult = "";
		
		// 初始化玩家1 (P1) - 红色，从左侧开始向右移动
		len1 = 3;
		direction1 = "R";
		isAlive1 = true;
		score1 = 0;
		snakex1[0] = 100;
		snakey1[0] = 100;
		snakex1[1] = 75;
		snakey1[1] = 100;
		snakex1[2] = 50;
		snakey1[2] = 100;
		
		// 初始化玩家2 (P2) - 蓝色，从右侧开始向左移动
		len2 = 3;
		direction2 = "L";
		isAlive2 = true;
		score2 = 0;
		snakex2[0] = 750;
		snakey2[0] = 100;
		snakex2[1] = 775;
		snakey2[1] = 100;
		snakex2[2] = 800;
		snakey2[2] = 100;
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
		
		// 绘制分数
		g.setColor(Color.RED);
		g.setFont(new Font("arial", Font.BOLD, 20));
		g.drawString("P1 Score: " + score1, 50, 60);
		
		g.setColor(Color.BLUE);
		g.setFont(new Font("arial", Font.BOLD, 20));
		g.drawString("P2 Score: " + score2, 750, 60);
		
		// 绘制玩家1 (P1) - 红色
		if(isAlive1){
			// 画蛇头
			if(direction1.equals("R")){
				right.paintIcon(this, g, snakex1[0], snakey1[0]);
			}else if(direction1.equals("L")){
				left.paintIcon(this, g, snakex1[0], snakey1[0]);
			}else if(direction1.equals("U")){
				up.paintIcon(this, g, snakex1[0], snakey1[0]);
			}else if(direction1.equals("D")){
				down.paintIcon(this, g, snakex1[0], snakey1[0]);
			}
			// 画蛇身
			for(int i=1;i<len1;i++){
				g.setColor(Color.RED);
				g.fillRect(snakex1[i], snakey1[i], 25, 25);
			}
		}
		
		// 绘制玩家2 (P2) - 蓝色
		if(isAlive2){
			// 画蛇头
			if(direction2.equals("R")){
				right.paintIcon(this, g, snakex2[0], snakey2[0]);
			}else if(direction2.equals("L")){
				left.paintIcon(this, g, snakex2[0], snakey2[0]);
			}else if(direction2.equals("U")){
				up.paintIcon(this, g, snakex2[0], snakey2[0]);
			}else if(direction2.equals("D")){
				down.paintIcon(this, g, snakex2[0], snakey2[0]);
			}
			// 画蛇身
			for(int i=1;i<len2;i++){
				g.setColor(Color.BLUE);
				g.fillRect(snakex2[i], snakey2[i], 25, 25);
			}
		}
		
		// 绘制开始提示语
		if(!isStarted){
			g.setColor(Color.WHITE);
			g.setFont(new Font("arial", Font.BOLD, 30));
			g.drawString("Press Space to Start or Pause", 230, 350);
		}
		
		// 绘制游戏结束结果
		if (isGameOver) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("arial", Font.BOLD, 40));
			g.drawString(gameResult, 350, 350);
			g.setFont(new Font("arial", Font.BOLD, 20));
			g.drawString("Press Space to Start New Game", 300, 400);
		}
		
		// 绘制食物
		food.paintIcon(this, g, foodx, foody);
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
			if(isGameOver){
				initSnake();
			}
			else{
			isStarted = !isStarted;
			}
		} 
		// 玩家1 (P1) 控制 - WASD
		else if(keyCode == KeyEvent.VK_W && !direction1.equals("D") && isAlive1){
			direction1="U";
		}else if(keyCode == KeyEvent.VK_S && !direction1.equals("U") && isAlive1){
			direction1="D";
		}else if(keyCode == KeyEvent.VK_A && !direction1.equals("R") && isAlive1){
			direction1="L";
		}else if(keyCode == KeyEvent.VK_D && !direction1.equals("L") && isAlive1){
			direction1="R";
		}
		// 玩家2 (P2) 控制 - 方向键
		else if(keyCode == KeyEvent.VK_UP && !direction2.equals("D") && isAlive2){
			direction2="U";
		}else if(keyCode == KeyEvent.VK_DOWN && !direction2.equals("U") && isAlive2){
			direction2="D";
		}else if(keyCode == KeyEvent.VK_LEFT && !direction2.equals("R") && isAlive2){
			direction2="L";
		}else if(keyCode == KeyEvent.VK_RIGHT && !direction2.equals("L") && isAlive2){
			direction2="R";
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
	@Override
	public void actionPerformed(ActionEvent e) {
		timer.start();
		
		if(isStarted && !isGameOver){
			// 移动玩家1 (P1) 的身体
			if(isAlive1){
				for(int i=len1;i>0;i--){
					snakex1[i] = snakex1[i-1];
					snakey1[i] = snakey1[i-1];
				}
				// 移动玩家1的头部
				if(direction1.equals("R")){
					snakex1[0] += 25;
				}else if(direction1.equals("L")){
					snakex1[0] -= 25;
				}else if(direction1.equals("U")){
					snakey1[0] -= 25;
				}else if(direction1.equals("D")){
					snakey1[0] += 25;
				}
			}
			
			// 移动玩家2 (P2) 的身体
			if(isAlive2){
				for(int i=len2;i>0;i--){
					snakex2[i] = snakex2[i-1];
					snakey2[i] = snakey2[i-1];
				}
				// 移动玩家2的头部
				if(direction2.equals("R")){
					snakex2[0] += 25;
				}else if(direction2.equals("L")){
					snakex2[0] -= 25;
				}else if(direction2.equals("U")){
					snakey2[0] -= 25;
				}else if(direction2.equals("D")){
					snakey2[0] += 25;
				}
			}
			
			// 检查玩家1是否吃到食物
			if(isAlive1 && snakex1[0] == foodx && snakey1[0] == foody){
				len1++;
				score1++;
				foodx = r.nextInt(34)*25+25;
				foody = r.nextInt(24)*25+75;
			}
			
			// 检查玩家2是否吃到食物
			if(isAlive2 && snakex2[0] == foodx && snakey2[0] == foody){
				len2++;
				score2++;
				foodx = r.nextInt(34)*25+25;
				foody = r.nextInt(24)*25+75;
			}
			
			// 检查碰撞
			checkCollisions();
			
			// 检查游戏是否结束
			checkGameOver();
		}
		repaint();
	}
	
	// 检查碰撞
	private void checkCollisions(){
		// 检查玩家1的碰撞
		if(isAlive1){
			// 撞墙
			if(snakex1[0] < 25 || snakex1[0] > 850 || snakey1[0] < 75 || snakey1[0] > 650){
				isAlive1 = false;
			}
			// 撞到自己的身体
			for(int i=1;i<len1;i++){
				if(snakex1[0] == snakex1[i] && snakey1[0] == snakey1[i]){
					isAlive1 = false;
					break;
				}
			}
			// 撞到玩家2的身体
			if(isAlive2){
				for(int i=0;i<len2;i++){
					if(snakex1[0] == snakex2[i] && snakey1[0] == snakey2[i]){
						isAlive1 = false;
						break;
					}
				}
			}
		}
		
		// 检查玩家2的碰撞
		if(isAlive2){
			// 撞墙
			if(snakex2[0] < 25 || snakex2[0] > 850 || snakey2[0] < 75 || snakey2[0] > 650){
				isAlive2 = false;
			}
			// 撞到自己的身体
			for(int i=1;i<len2;i++){
				if(snakex2[0] == snakex2[i] && snakey2[0] == snakey2[i]){
					isAlive2 = false;
					break;
				}
			}
			// 撞到玩家1的身体
			if(isAlive1){
				for(int i=0;i<len1;i++){
					if(snakex2[0] == snakex1[i] && snakey2[0] == snakey1[i]){
						isAlive2 = false;
						break;
					}
				}
			}
		}
		
		// 头对撞
		if(isAlive1 && isAlive2 && snakex1[0] == snakex2[0] && snakey1[0] == snakey2[0]){
			isAlive1 = false;
			isAlive2 = false;
		}
	}
	
	// 检查游戏是否结束
	private void checkGameOver(){
		if(!isAlive1 && !isAlive2){
			isGameOver = true;
			if(score1 > score2){
				gameResult = "P1 胜";
			}else if(score2 > score1){
				gameResult = "P2 胜";
			}else{
				gameResult = "平局";
			}
		}else if(!isAlive1){
			isGameOver = true;
			gameResult = "P2 胜";
		}else if(!isAlive2){
			isGameOver = true;
			gameResult = "P1 胜";
		}
	}
}
