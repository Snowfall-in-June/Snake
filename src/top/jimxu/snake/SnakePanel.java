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
	
	// 两个玩家
	Player p1;
	Player p2;
	
	//食物生成
	Random r = new Random();
	int foodx = r.nextInt(34)*25+25; // 34个格子，一个格子25排像素，还有25像素空白
	int foody = r.nextInt(24)*25+75; // 24个格子，一个格子25排像素，还有75像素空白
	
	//游戏是否开始
	boolean isStarted = false;
	
	//游戏是否结束
	boolean isGameOver = false;
	
	// 游戏结果
	String gameResult = "";

	
	//	初始化游戏
	public void initGame(){
		isStarted = false;
		isGameOver = false;
		gameResult = "";
		
		// 初始化两个玩家
		p1 = new Player("R");
		p2 = new Player("L");
		
		// 重新生成食物
		foodx = r.nextInt(34)*25+25;
		foody = r.nextInt(24)*25+75;
	}
	public SnakePanel() {
		this.setFocusable(true);
		initGame(); // 初始化游戏；
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
		
		// 画P1分数（红色）
		g.setColor(Color.RED);
		g.setFont(new Font("arial", Font.BOLD, 20));
		g.drawString("P1: " + p1.score, 50, 50);
		
		// 画P2分数（蓝色）
		g.setColor(Color.BLUE);
		g.setFont(new Font("arial", Font.BOLD, 20));
		g.drawString("P2: " + p2.score, 750, 50);
		
		// 画P1的蛇
		if(p1.isAlive) {
			// 画蛇头
			if(p1.direction.equals("R")){
				right.paintIcon(this, g, p1.snakex[0], p1.snakey[0]);
			}else if(p1.direction.equals("L")){
				left.paintIcon(this, g, p1.snakex[0], p1.snakey[0]);
			}else if(p1.direction.equals("U")){
				up.paintIcon(this, g, p1.snakex[0], p1.snakey[0]);
			}else if(p1.direction.equals("D")){
				down.paintIcon(this, g, p1.snakex[0], p1.snakey[0]);
			}
			// 画蛇身
			for(int i=1;i<p1.len;i++){
				body.paintIcon(this, g, p1.snakex[i], p1.snakey[i]);
			}
		}
		
		// 画P2的蛇
		if(p2.isAlive) {
			// 画蛇头
			if(p2.direction.equals("R")){
				right.paintIcon(this, g, p2.snakex[0], p2.snakey[0]);
			}else if(p2.direction.equals("L")){
				left.paintIcon(this, g, p2.snakex[0], p2.snakey[0]);
			}else if(p2.direction.equals("U")){
				up.paintIcon(this, g, p2.snakex[0], p2.snakey[0]);
			}else if(p2.direction.equals("D")){
				down.paintIcon(this, g, p2.snakex[0], p2.snakey[0]);
			}
			// 画蛇身
			for(int i=1;i<p2.len;i++){
				body.paintIcon(this, g, p2.snakex[i], p2.snakey[i]);
			}
		}
		
		// 画开始提示语
		if(!isStarted){
			g.setColor(Color.WHITE);
			g.setFont(new Font("arial",Font.BOLD,30));
			g.drawString("Press Space to Start or Pause", 230, 350);
		}
		
		// 画游戏结束提示语和结果
		if(isGameOver) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("arial", Font.BOLD, 30));
			g.drawString(gameResult, 350, 350);
			g.setFont(new Font("arial", Font.BOLD, 20));
			g.drawString("Press Space to Restart", 320, 400);
		}
		
		// 画食物
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
		//实现空格暂停 继续/重新开始
		if(keyCode == KeyEvent.VK_SPACE){
			if(isGameOver){
				initGame();
			}
			else{
			isStarted = !isStarted;
			}
		}
		// P1控制（WASD）
		else if(keyCode == KeyEvent.VK_W && p1.isAlive && !p1.direction.equals("D")){
			p1.direction="U";
		}else if(keyCode == KeyEvent.VK_S && p1.isAlive && !p1.direction.equals("U")){
			p1.direction="D";
		}else if(keyCode == KeyEvent.VK_A && p1.isAlive && !p1.direction.equals("R")){
			p1.direction="L";
		}else if(keyCode == KeyEvent.VK_D && p1.isAlive && !p1.direction.equals("L")){
			p1.direction="R";
		}
		// P2控制（方向键）
		else if(keyCode == KeyEvent.VK_UP && p2.isAlive && !p2.direction.equals("D")){
			p2.direction="U";
		}else if(keyCode == KeyEvent.VK_DOWN && p2.isAlive && !p2.direction.equals("U")){
			p2.direction="D";
		}else if(keyCode == KeyEvent.VK_LEFT && p2.isAlive && !p2.direction.equals("R")){
			p2.direction="L";
		}else if(keyCode == KeyEvent.VK_RIGHT && p2.isAlive && !p2.direction.equals("L")){
			p2.direction="R";
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
			// 处理P1蛇的移动
			if(p1.isAlive) {
				// 移动身体
				for(int i=p1.len;i>0;i--){
					p1.snakex[i] = p1.snakex[i-1];
					p1.snakey[i] = p1.snakey[i-1];
				}
				// 移动头部
				if(p1.direction.equals("R")){
					p1.snakex[0] += 25;
					// 撞墙检测
					if(p1.snakex[0] > 850) {
						p1.isAlive = false;
					}
				}else if(p1.direction.equals("L")){
					p1.snakex[0] -= 25;
					// 撞墙检测
					if(p1.snakex[0] < 25) {
						p1.isAlive = false;
					}
				}else if(p1.direction.equals("U")){
					p1.snakey[0] -= 25;
					// 撞墙检测
					if(p1.snakey[0] < 75) {
						p1.isAlive = false;
					}
				}else if(p1.direction.equals("D")){
					p1.snakey[0] += 25;
					// 撞墙检测
					if(p1.snakey[0] > 650) {
						p1.isAlive = false;
					}
				}
				
				// 检测P1是否吃到食物
				if(p1.snakex[0] == foodx && p1.snakey[0] == foody) {
					p1.len++;
					p1.score++;
					// 重新生成食物
					foodx = r.nextInt(34)*25+25;
					foody = r.nextInt(24)*25+75;
				}
				
				// 检测P1是否撞到自己的身体
				for(int i=1;i<p1.len;i++) {
					if(p1.snakex[0] == p1.snakex[i] && p1.snakey[0] == p1.snakey[i]) {
						p1.isAlive = false;
					}
				}
				
				// 检测P1是否撞到P2的身体
				for(int i=0;i<p2.len;i++) {
					if(p1.snakex[0] == p2.snakex[i] && p1.snakey[0] == p2.snakey[i]) {
						p1.isAlive = false;
					}
				}
			}
			
			// 处理P2蛇的移动
			if(p2.isAlive) {
				// 移动身体
				for(int i=p2.len;i>0;i--){
					p2.snakex[i] = p2.snakex[i-1];
					p2.snakey[i] = p2.snakey[i-1];
				}
				// 移动头部
				if(p2.direction.equals("R")){
					p2.snakex[0] += 25;
					// 撞墙检测
					if(p2.snakex[0] > 850) {
						p2.isAlive = false;
					}
				}else if(p2.direction.equals("L")){
					p2.snakex[0] -= 25;
					// 撞墙检测
					if(p2.snakex[0] < 25) {
						p2.isAlive = false;
					}
				}else if(p2.direction.equals("U")){
					p2.snakey[0] -= 25;
					// 撞墙检测
					if(p2.snakey[0] < 75) {
						p2.isAlive = false;
					}
				}else if(p2.direction.equals("D")){
					p2.snakey[0] += 25;
					// 撞墙检测
					if(p2.snakey[0] > 650) {
						p2.isAlive = false;
					}
				}
				
				// 检测P2是否吃到食物
				if(p2.snakex[0] == foodx && p2.snakey[0] == foody) {
					p2.len++;
					p2.score++;
					// 重新生成食物
					foodx = r.nextInt(34)*25+25;
					foody = r.nextInt(24)*25+75;
				}
				
				// 检测P2是否撞到自己的身体
				for(int i=1;i<p2.len;i++) {
					if(p2.snakex[0] == p2.snakex[i] && p2.snakey[0] == p2.snakey[i]) {
						p2.isAlive = false;
					}
				}
				
				// 检测P2是否撞到P1的身体
				for(int i=0;i<p1.len;i++) {
					if(p2.snakex[0] == p1.snakex[i] && p2.snakey[0] == p1.snakey[i]) {
						p2.isAlive = false;
					}
				}
			}
			
			// 检测蛇头对撞
			if(p1.isAlive && p2.isAlive) {
				if(p1.snakex[0] == p2.snakex[0] && p1.snakey[0] == p2.snakey[0]) {
					p1.isAlive = false;
					p2.isAlive = false;
				}
			}
			
			// 检查游戏是否结束
			checkGameOver();
		}
		repaint();
	}
	
	// 检查游戏是否结束
	private void checkGameOver() {
		// 游戏结束条件：两条蛇都死亡
		if(!p1.isAlive && !p2.isAlive) {
			isGameOver = true;
			// 判断平局或按分数判胜
			if(p1.score == p2.score) {
				gameResult = "平局";
			} else if(p1.score > p2.score) {
				gameResult = "P1 胜";
			} else {
				gameResult = "P2 胜";
			}
		} else if(!p1.isAlive) {
			// 只有P1死亡，P2获胜
			isGameOver = true;
			gameResult = "P2 胜";
		} else if(!p2.isAlive) {
			// 只有P2死亡，P1获胜
			isGameOver = true;
			gameResult = "P1 胜";
		}
	}
}
