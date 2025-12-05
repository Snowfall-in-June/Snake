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
	
	//玩家1（红色蛇）数据结构
	int[] snakex1 = new int[750];
	int[] snakey1 = new int[750];
	int len1 = 3;
	String direction1 = "R"; //R右L左U上D下
	boolean isAlive1 = true;
	int score1 = 0;
	
	//玩家2（蓝色蛇）数据结构
	int[] snakex2 = new int[750];
	int[] snakey2 = new int[750];
	int len2 = 3;
	String direction2 = "L"; //R右L左U上D下
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

	//初始化两条蛇
	public void initSnake(){
		isStarted = false;
		isGameOver = false;
		
		//初始化玩家1
		len1 = 3;
		direction1 = "R";
		isAlive1 = true;
		score1 = 0;
		snakex1[0] = 150;
		snakey1[0] = 200;
		snakex1[1] = 125;
		snakey1[1] = 200;
		snakex1[2] = 100;
		snakey1[2] = 200;
		
		//初始化玩家2
		len2 = 3;
		direction2 = "L";
		isAlive2 = true;
		score2 = 0;
		snakex2[0] = 700;
		snakey2[0] = 400;
		snakex2[1] = 725;
		snakey2[1] = 400;
		snakex2[2] = 750;
		snakey2[2] = 400;
		
		//生成初始食物
		generateFood();
	}
	
	//生成新食物
	public void generateFood(){
		foodx = r.nextInt(34)*25+25;
		foody = r.nextInt(24)*25+75;
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
		
		//显示分数
		g.setColor(Color.RED);
		g.setFont(new Font("arial",Font.BOLD,20));
		g.drawString("P1 Score: " + score1, 25, 40);
		g.setColor(Color.BLUE);
		g.drawString("P2 Score: " + score2, 725, 40);
		
		//画玩家1的蛇（红色）
		if(isAlive1){
			//画蛇头
			if(direction1.equals("R")){
				right.paintIcon(this, g, snakex1[0], snakey1[0]);
			}else if(direction1.equals("L")){
				left.paintIcon(this, g, snakex1[0], snakey1[0]);
			}else if(direction1.equals("U")){
				up.paintIcon(this, g, snakex1[0], snakey1[0]);
			}else if(direction1.equals("D")){
				down.paintIcon(this, g, snakex1[0], snakey1[0]);
			}
			//画蛇身
			for(int i=1;i<len1;i++){
				g.setColor(Color.RED);
				g.fillRect(snakex1[i], snakey1[i], 24, 24);
			}
		}
		
		//画玩家2的蛇（蓝色）
		if(isAlive2){
			//画蛇头
			if(direction2.equals("R")){
				right.paintIcon(this, g, snakex2[0], snakey2[0]);
			}else if(direction2.equals("L")){
				left.paintIcon(this, g, snakex2[0], snakey2[0]);
			}else if(direction2.equals("U")){
				up.paintIcon(this, g, snakex2[0], snakey2[0]);
			}else if(direction2.equals("D")){
				down.paintIcon(this, g, snakex2[0], snakey2[0]);
			}
			//画蛇身
			for(int i=1;i<len2;i++){
				g.setColor(Color.BLUE);
				g.fillRect(snakex2[i], snakey2[i], 24, 24);
			}
		}
		
		//画开始提示语
		if(!isStarted){
			g.setColor(Color.WHITE);
			g.setFont(new Font("arial",Font.BOLD,30));
			g.drawString("Press Space to Start or Pause", 230, 350);
		}
		
		//画游戏结束提示语
		if (isGameOver) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("arial",Font.BOLD,30));
			String result;
			if(!isAlive1 && !isAlive2){
				if(score1 > score2) result = "P1 Wins!";
				else if(score2 > score1) result = "P2 Wins!";
				else result = "Draw!";
			}else if(!isAlive1){
				result = "P2 Wins!";
			}else{
				result = "P1 Wins!";
			}
			g.drawString(result, 380, 350);
			g.setFont(new Font("arial",Font.BOLD,20));
			g.drawString("Press Space to Restart", 320, 400);
		}
		
		//画食物
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
		
		//玩家1控制（WASD）
		if(isAlive1 && isStarted && !isGameOver){
			if(keyCode == KeyEvent.VK_W && !direction1.equals("D")){
				direction1="U";
			}else if(keyCode == KeyEvent.VK_S && !direction1.equals("U")){
				direction1="D";
			}else if(keyCode == KeyEvent.VK_A && !direction1.equals("R")){
				direction1="L";
			}else if(keyCode == KeyEvent.VK_D && !direction1.equals("L")){
				direction1="R";
			}
		}
		
		//玩家2控制（方向键）
		if(isAlive2 && isStarted && !isGameOver){
			if(keyCode == KeyEvent.VK_UP && !direction2.equals("D")){
				direction2="U";
			}else if(keyCode == KeyEvent.VK_DOWN && !direction2.equals("U")){
				direction2="D";
			}else if(keyCode == KeyEvent.VK_LEFT && !direction2.equals("R")){
				direction2="L";
			}else if(keyCode == KeyEvent.VK_RIGHT && !direction2.equals("L")){
				direction2="R";
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
	@Override
	public void actionPerformed(ActionEvent e) {
		timer.start();
		
		if(isStarted && !isGameOver){
			//移动玩家1的蛇
			if(isAlive1){
				//移动身体
				for(int i=len1;i>0;i--){
					snakex1[i] = snakex1[i-1];
					snakey1[i] = snakey1[i-1];
				}
				//移动头部
				if(direction1.equals("R")){
					snakex1[0] += 25;
					if(snakex1[0]>850) snakex1[0] = 25;
				}else if(direction1.equals("L")){
					snakex1[0] -= 25;
					if(snakex1[0]<25) snakex1[0] = 850;
				}else if(direction1.equals("U")){
					snakey1[0] -= 25;
					if(snakey1[0]<75) snakey1[0] = 650;
				}else if(direction1.equals("D")){
					snakey1[0] += 25;
					if(snakey1[0]>650) snakey1[0] = 75;
				}
				
				//玩家1吃食物
				if(snakex1[0] == foodx && snakey1[0] == foody){
					len1++;
					score1++;
					generateFood();
				}
			}
			
			//移动玩家2的蛇
			if(isAlive2){
				//移动身体
				for(int i=len2;i>0;i--){
					snakex2[i] = snakex2[i-1];
					snakey2[i] = snakey2[i-1];
				}
				//移动头部
				if(direction2.equals("R")){
					snakex2[0] += 25;
					if(snakex2[0]>850) snakex2[0] = 25;
				}else if(direction2.equals("L")){
					snakex2[0] -= 25;
					if(snakex2[0]<25) snakex2[0] = 850;
				}else if(direction2.equals("U")){
					snakey2[0] -= 25;
					if(snakey2[0]<75) snakey2[0] = 650;
				}else if(direction2.equals("D")){
					snakey2[0] += 25;
					if(snakey2[0]>650) snakey2[0] = 75;
				}
				
				//玩家2吃食物
				if(snakex2[0] == foodx && snakey2[0] == foody){
					len2++;
					score2++;
					generateFood();
				}
			}
			
			//碰撞检测
			checkCollision();
			
			//检查游戏是否结束
			if(!isAlive1 || !isAlive2){
				//如果只剩一条蛇存活，游戏继续直到另一条也死亡
				if((!isAlive1 && isAlive2) || (isAlive1 && !isAlive2)){
					//游戏继续，直到最后一条蛇死亡
				}else{
					//两条蛇都死亡，游戏结束
					isGameOver = true;
				}
			}
		}
		repaint();
	}
	
	//碰撞检测
	public void checkCollision(){
		//玩家1的碰撞检测
		if(isAlive1){
			//撞自己身体
			for(int i=1;i<len1;i++){
				if(snakex1[0] == snakex1[i] && snakey1[0] == snakey1[i]){
					isAlive1 = false;
				}
			}
			
			//撞玩家2身体
			for(int i=0;i<len2;i++){
				if(snakex1[0] == snakex2[i] && snakey1[0] == snakey2[i]){
					isAlive1 = false;
				}
			}
		}
		
		//玩家2的碰撞检测
		if(isAlive2){
			//撞自己身体
			for(int i=1;i<len2;i++){
				if(snakex2[0] == snakex2[i] && snakey2[0] == snakey2[i]){
					isAlive2 = false;
				}
			}
			
			//撞玩家1身体
			for(int i=0;i<len1;i++){
				if(snakex2[0] == snakex1[i] && snakey2[0] == snakey1[i]){
					isAlive2 = false;
				}
			}
		}
		
		//蛇头对撞
		if(isAlive1 && isAlive2){
			if(snakex1[0] == snakex2[0] && snakey1[0] == snakey2[0]){
				isAlive1 = false;
				isAlive2 = false;
			}
		}
	}
}
