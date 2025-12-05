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
	
	//玩家蛇的数据结构设计
	int[] playerSnakex = new int[750];
	int[] playerSnakey = new int[750];
	int playerLen = 3;
	String playerDirection = "R";//R右L左U上D下
	boolean playerAlive = true;
	int playerScore = 0;
	
	//AI蛇的数据结构设计
	int[] aiSnakex = new int[750];
	int[] aiSnakey = new int[750];
	int aiLen = 3;
	String aiDirection = "L";//R右L左U上D下
	boolean aiAlive = true;
	int aiScore = 0;
	
	//食物生成
	Random r = new Random();
	int foodx = r.nextInt(34)*25+25; // 34个格子，一个格子25排像素，还有25像素空白
	int foody = r.nextInt(24)*25+75; // 24个格子，一个格子25排像素，还有75像素空白
	
	//游戏是否开始
	boolean isStarted = false;
	
	//游戏是否结束
	boolean isGameOver = false;
	
	//胜负结果
	String gameResult = "";

	
	//	初始化蛇
	public void initSnake(){
		isStarted = false;
		isGameOver = false;
		gameResult = "";
		
		// 初始化玩家蛇
		playerLen = 3;
		playerDirection = "R";
		playerAlive = true;
		playerScore = 0;
		playerSnakex[0] = 100;
		playerSnakey[0] = 100;
		playerSnakex[1] = 75;
		playerSnakey[1] = 100;
		playerSnakex[2] = 50;
		playerSnakey[2] = 100;
		
		// 初始化AI蛇
		aiLen = 3;
		aiDirection = "L";
		aiAlive = true;
		aiScore = 0;
		aiSnakex[0] = 750;
		aiSnakey[0] = 100;
		aiSnakex[1] = 775;
		aiSnakey[1] = 100;
		aiSnakex[2] = 800;
		aiSnakey[2] = 100;
		
		// 生成食物
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
		
		//画玩家蛇头
		if(playerDirection.equals("R")){
			right.paintIcon(this, g, playerSnakex[0], playerSnakey[0]);
		}else if(playerDirection.equals("L")){
			left.paintIcon(this, g, playerSnakex[0], playerSnakey[0]);
		}else if(playerDirection.equals("U")){
			up.paintIcon(this, g, playerSnakex[0], playerSnakey[0]);
		}else if(playerDirection.equals("D")){
			down.paintIcon(this, g, playerSnakex[0], playerSnakey[0]);
		}
		//画玩家蛇身
		for(int i=1;i<playerLen;i++){
			body.paintIcon(this, g, playerSnakex[i],playerSnakey[i]);
		}
		
		//画AI蛇头
		if(aiDirection.equals("R")){
			right.paintIcon(this, g, aiSnakex[0], aiSnakey[0]);
		}else if(aiDirection.equals("L")){
			left.paintIcon(this, g, aiSnakex[0], aiSnakey[0]);
		}else if(aiDirection.equals("U")){
			up.paintIcon(this, g, aiSnakex[0], aiSnakey[0]);
		}else if(aiDirection.equals("D")){
			down.paintIcon(this, g, aiSnakex[0], aiSnakey[0]);
		}
		//画AI蛇身
		for(int i=1;i<aiLen;i++){
			body.paintIcon(this, g, aiSnakex[i],aiSnakey[i]);
		}
		
		//画分数
		g.setColor(Color.WHITE);
		g.setFont(new Font("arial",Font.BOLD,20));
		g.drawString("Player Score: " + playerScore, 50, 50);
		g.drawString("AI Score: " + aiScore, 700, 50);
		
		//画开始提示语
		if(!isStarted && !isGameOver){
			g.setColor(Color.WHITE);
			g.setFont(new Font("arial",Font.BOLD,30));
			g.drawString("Press Space to Start or Pause", 230, 350);
		}
		
		//画游戏结束提示语
		if (isGameOver) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("arial",Font.BOLD,30));
			g.drawString(gameResult, 350, 350);
			g.drawString("Press Space to Start Again", 280, 400);
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
		}//实现转向
		else if(keyCode == KeyEvent.VK_UP && !playerDirection.equals("D")){
			playerDirection="U";
		}else if(keyCode == KeyEvent.VK_DOWN && !playerDirection.equals("U")){
			playerDirection="D";
		}else if(keyCode == KeyEvent.VK_LEFT && !playerDirection.equals("R")){
			playerDirection="L";
		}else if(keyCode == KeyEvent.VK_RIGHT && !playerDirection.equals("L")){
			playerDirection="R";
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	// AI蛇的决策方法
	private void aiDecision() {
		if (!aiAlive) return;
		
		// 简单的AI逻辑：优先向食物方向移动
		int headX = aiSnakex[0];
		int headY = aiSnakey[0];
		
		// 计算与食物的距离
		int dx = foodx - headX;
		int dy = foody - headY;
		
		// 尝试向食物方向移动
		String newDirection = aiDirection;
		
		// 优先考虑x方向
		if (Math.abs(dx) > Math.abs(dy)) {
			if (dx > 0 && !aiDirection.equals("L")) {
				newDirection = "R";
			} else if (dx < 0 && !aiDirection.equals("R")) {
				newDirection = "L";
			}
		} else {
			// 优先考虑y方向
			if (dy > 0 && !aiDirection.equals("U")) {
				newDirection = "D";
			} else if (dy < 0 && !aiDirection.equals("D")) {
				newDirection = "U";
			}
		}
		
		// 检查新方向是否会导致碰撞
		if (!willCollide(headX, headY, newDirection, true)) {
			aiDirection = newDirection;
		} else {
			// 如果直接移动会碰撞，尝试其他方向
			String[] directions = {"R", "L", "U", "D"};
			for (String dir : directions) {
				if (!dir.equals(aiDirection) && !willCollide(headX, headY, dir, true)) {
					aiDirection = dir;
					break;
				}
			}
		}
	}
	
	// 检查移动是否会导致碰撞
	private boolean willCollide(int x, int y, String direction, boolean isAI) {
		int nextX = x;
		int nextY = y;
		
		// 计算下一个位置
		if (direction.equals("R")) {
			nextX += 25;
			if (nextX > 850) nextX = 25;
		} else if (direction.equals("L")) {
			nextX -= 25;
			if (nextX < 25) nextX = 850;
		} else if (direction.equals("U")) {
			nextY -= 25;
			if (nextY < 75) nextY = 650;
		} else if (direction.equals("D")) {
			nextY += 25;
			if (nextY > 650) nextY = 75;
		}
		
		// 检查是否会撞到自己
		if (isAI) {
			for (int i = 1; i < aiLen; i++) {
				if (aiSnakex[i] == nextX && aiSnakey[i] == nextY) {
					return true;
				}
			}
		} else {
			for (int i = 1; i < playerLen; i++) {
				if (playerSnakex[i] == nextX && playerSnakey[i] == nextY) {
					return true;
				}
			}
		}
		
		// 检查是否会撞到对方
		if (isAI) {
			for (int i = 0; i < playerLen; i++) {
				if (playerSnakex[i] == nextX && playerSnakey[i] == nextY) {
					return true;
				}
			}
		} else {
			for (int i = 0; i < aiLen; i++) {
				if (aiSnakex[i] == nextX && aiSnakey[i] == nextY) {
					return true;
				}
			}
		}
		
		return false;
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
			// AI蛇决策
			aiDecision();
			
			// 移动玩家蛇身体
			if (playerAlive) {
				for(int i=playerLen;i>0;i--){
					playerSnakex[i] = playerSnakex[i-1];
					playerSnakey[i] = playerSnakey[i-1];
				}
				// 玩家蛇头移动
				if(playerDirection.equals("R")){
					playerSnakex[0] += 25;
					if(playerSnakex[0]>850) playerSnakex[0] = 25;
				}else if(playerDirection.equals("L")){
					playerSnakex[0] -= 25;
					if(playerSnakex[0]<25) playerSnakex[0] = 850;
				}else if(playerDirection.equals("U")){
					playerSnakey[0] -= 25;
					if(playerSnakey[0]<75) playerSnakey[0] = 650;
				}else if(playerDirection.equals("D")){
					playerSnakey[0] += 25;
					if(playerSnakey[0]>650) playerSnakey[0] = 75;
				}
			}
			
			// 移动AI蛇身体
			if (aiAlive) {
				for(int i=aiLen;i>0;i--){
					aiSnakex[i] = aiSnakex[i-1];
					aiSnakey[i] = aiSnakey[i-1];
				}
				// AI蛇头移动
				if(aiDirection.equals("R")){
					aiSnakex[0] += 25;
					if(aiSnakex[0]>850) aiSnakex[0] = 25;
				}else if(aiDirection.equals("L")){
					aiSnakex[0] -= 25;
					if(aiSnakex[0]<25) aiSnakex[0] = 850;
				}else if(aiDirection.equals("U")){
					aiSnakey[0] -= 25;
					if(aiSnakey[0]<75) aiSnakey[0] = 650;
				}else if(aiDirection.equals("D")){
					aiSnakey[0] += 25;
					if(aiSnakey[0]>650) aiSnakey[0] = 75;
				}
			}
			
			// 检查玩家蛇是否吃到食物
			if (playerAlive && playerSnakex[0] == foodx && playerSnakey[0] == foody) {
				playerLen++;
				playerScore++;
				foodx = r.nextInt(34)*25+25;
				foody = r.nextInt(24)*25+75;
			}
			
			// 检查AI蛇是否吃到食物
			if (aiAlive && aiSnakex[0] == foodx && aiSnakey[0] == foody) {
				aiLen++;
				aiScore++;
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
	private void checkCollisions() {
		// 检查玩家蛇是否撞到自己
		if (playerAlive) {
			for (int i = 1; i < playerLen; i++) {
				if (playerSnakex[0] == playerSnakex[i] && playerSnakey[0] == playerSnakey[i]) {
					playerAlive = false;
					break;
				}
			}
		}
		
		// 检查AI蛇是否撞到自己
		if (aiAlive) {
			for (int i = 1; i < aiLen; i++) {
				if (aiSnakex[0] == aiSnakex[i] && aiSnakey[0] == aiSnakey[i]) {
					aiAlive = false;
					break;
				}
			}
		}
		
		// 检查玩家蛇是否撞到AI蛇
		if (playerAlive && aiAlive) {
			for (int i = 0; i < aiLen; i++) {
				if (playerSnakex[0] == aiSnakex[i] && playerSnakey[0] == aiSnakey[i]) {
					playerAlive = false;
					break;
				}
			}
		}
		
		// 检查AI蛇是否撞到玩家蛇
		if (aiAlive && playerAlive) {
			for (int i = 0; i < playerLen; i++) {
				if (aiSnakex[0] == playerSnakex[i] && aiSnakey[0] == playerSnakey[i]) {
					aiAlive = false;
					break;
				}
			}
		}
		
		// 检查头对头碰撞
		if (playerAlive && aiAlive) {
			if (playerSnakex[0] == aiSnakex[0] && playerSnakey[0] == aiSnakey[0]) {
				playerAlive = false;
				aiAlive = false;
			}
		}
	}
	
	// 检查游戏是否结束
	private void checkGameOver() {
		if (!playerAlive && !aiAlive) {
			isGameOver = true;
			gameResult = "平局";
		} else if (!playerAlive) {
			isGameOver = true;
			gameResult = "AI胜";
		} else if (!aiAlive) {
			isGameOver = true;
			gameResult = "玩家胜";
		}
	}
}
