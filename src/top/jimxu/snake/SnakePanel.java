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
	
	//玩家蛇的数据结构
	int[] playerSnakex = new int[750];
	int[] playerSnakey = new int[750];
	int playerLen = 3;
	String playerDirection = "R";//R右L左U上D下
	boolean playerAlive = true;
	int playerScore = 0;
	
	//AI蛇的数据结构
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
	boolean isStarted = true; // 启动后直接进入游戏
	
	//游戏是否结束
	boolean isGameOver = false;
	String gameResult = ""; // 游戏结果：玩家胜、AI胜、平局

	
	//	初始化玩家蛇
	public void initPlayerSnake(){
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
	}
	
	//	初始化AI蛇
	public void initAISnake(){
		aiLen = 3;
		aiDirection = "L";
		aiAlive = true;
		aiScore = 0;
		aiSnakex[0] = 800;
		aiSnakey[0] = 500;
		aiSnakex[1] = 825;
		aiSnakey[1] = 500;
		aiSnakex[2] = 850;
		aiSnakey[2] = 500;
	}
	
	//	初始化游戏
	public void initGame(){
		isStarted = true;
		isGameOver = false;
		gameResult = "";
		initPlayerSnake();
		initAISnake();
		generateFood();
	}
	public SnakePanel() {
		this.setFocusable(true);
		initGame(); // 初始化游戏
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
		
		//画玩家蛇分数
		g.setColor(Color.WHITE);
		g.setFont(new Font("arial",Font.BOLD,20));
		g.drawString("Player Score: " + playerScore, 25, 60);
		
		//画AI蛇分数
		g.setColor(Color.WHITE);
		g.setFont(new Font("arial",Font.BOLD,20));
		g.drawString("AI Score: " + aiScore, 700, 60);
		
		//画玩家蛇
		if(playerAlive){
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
				body.paintIcon(this, g, playerSnakex[i], playerSnakey[i]);
			}
		}
		
		//画AI蛇
		if(aiAlive){
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
				body.paintIcon(this, g, aiSnakex[i], aiSnakey[i]);
			}
		}
		
		//画开始提示语
		if(!isStarted){
			g.setColor(Color.WHITE);
			g.setFont(new Font("arial",Font.BOLD,30));
			g.drawString("Press Space to Start or Pause", 230, 350);
		}
		
		//画游戏结束提示语
		if(isGameOver){
			g.setColor(Color.WHITE);
			g.setFont(new Font("arial",Font.BOLD,40));
			g.drawString(gameResult, 350, 350);
			g.setFont(new Font("arial",Font.BOLD,20));
			g.drawString("Press Space to Restart", 350, 400);
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
		//实现空格暂停/继续/重启
		if(keyCode == KeyEvent.VK_SPACE){
			if(isGameOver){
				initGame(); // 重启游戏
			}
			else{
				isStarted = !isStarted; // 暂停/继续
			}
		}//实现玩家蛇转向（WASD控制）
		else if(keyCode == KeyEvent.VK_W && !playerDirection.equals("D")){
			playerDirection="U";
		}else if(keyCode == KeyEvent.VK_S && !playerDirection.equals("U")){
			playerDirection="D";
		}else if(keyCode == KeyEvent.VK_A && !playerDirection.equals("R")){
			playerDirection="L";
		}else if(keyCode == KeyEvent.VK_D && !playerDirection.equals("L")){
			playerDirection="R";
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
	//生成新食物
	public void generateFood(){
		foodx = r.nextInt(34)*25+25;
		foody = r.nextInt(24)*25+75;
	}
	
	//AI蛇移动逻辑
	public void moveAISnake(){
		if(!aiAlive) return;
		
		//简单的AI逻辑：向食物靠近
		int headX = aiSnakex[0];
		int headY = aiSnakey[0];
		String newDirection = aiDirection;
		
		//优先考虑x方向
		if(headX < foodx && !aiDirection.equals("L")){
			newDirection = "R";
		}else if(headX > foodx && !aiDirection.equals("R")){
			newDirection = "L";
		}
		//如果x方向无法靠近，考虑y方向
		else if(headY < foody && !aiDirection.equals("U")){
			newDirection = "D";
		}else if(headY > foody && !aiDirection.equals("D")){
			newDirection = "U";
		}
		
		//检查新方向是否会撞墙
		int nextX = headX;
		int nextY = headY;
		if(newDirection.equals("R")) nextX +=25;
		else if(newDirection.equals("L")) nextX -=25;
		else if(newDirection.equals("U")) nextY -=25;
		else if(newDirection.equals("D")) nextY +=25;
		
		//如果撞墙，尝试其他方向
		if(nextX <25 || nextX >850 || nextY <75 || nextY >650){
			//尝试所有可能的方向
			String[] directions = {"R","L","U","D"};
			for(String dir : directions){
				if(dir.equals(aiDirection)) continue; //跳过当前方向
				if(dir.equals("R") && aiDirection.equals("L")) continue; //跳过相反方向
				if(dir.equals("L") && aiDirection.equals("R")) continue;
				if(dir.equals("U") && aiDirection.equals("D")) continue;
				if(dir.equals("D") && aiDirection.equals("U")) continue;
				
				//检查该方向是否撞墙
				int testX = headX;
				int testY = headY;
				if(dir.equals("R")) testX +=25;
				else if(dir.equals("L")) testX -=25;
				else if(dir.equals("U")) testY -=25;
				else if(dir.equals("D")) testY +=25;
				
				if(testX >=25 && testX <=850 && testY >=75 && testY <=650){
					newDirection = dir;
					break;
				}
			}
		}
		
		//检查新方向是否会撞自己身体
		nextX = headX;
		nextY = headY;
		if(newDirection.equals("R")) nextX +=25;
		else if(newDirection.equals("L")) nextX -=25;
		else if(newDirection.equals("U")) nextY -=25;
		else if(newDirection.equals("D")) nextY +=25;
		
		for(int i=1;i<aiLen;i++){
			if(nextX == aiSnakex[i] && nextY == aiSnakey[i]){
				//撞自己，尝试其他方向
				String[] directions = {"R","L","U","D"};
				for(String dir : directions){
					if(dir.equals(aiDirection)) continue; //跳过当前方向
					if(dir.equals("R") && aiDirection.equals("L")) continue; //跳过相反方向
					if(dir.equals("L") && aiDirection.equals("R")) continue;
					if(dir.equals("U") && aiDirection.equals("D")) continue;
					if(dir.equals("D") && aiDirection.equals("U")) continue;
					
					//检查该方向是否撞自己
					int testX = headX;
					int testY = headY;
					if(dir.equals("R")) testX +=25;
					else if(dir.equals("L")) testX -=25;
					else if(dir.equals("U")) testY -=25;
					else if(dir.equals("D")) testY +=25;
					
					boolean hitSelf = false;
					for(int j=1;j<aiLen;j++){
						if(testX == aiSnakex[j] && testY == aiSnakey[j]){
							hitSelf = true;
							break;
						}
					}
					
					if(!hitSelf){
						newDirection = dir;
						break;
					}
				}
				break;
			}
		}
		
		//更新AI方向
		aiDirection = newDirection;
		
		//移动AI身体
		for(int i=aiLen;i>0;i--){
			aiSnakex[i] = aiSnakex[i-1];
			aiSnakey[i] = aiSnakey[i-1];
		}
		
		//移动AI头
		if(aiDirection.equals("R")){
			aiSnakex[0] +=25;
			if(aiSnakex[0]>850) aiSnakex[0] = 25;
		}else if(aiDirection.equals("L")){
			aiSnakex[0] -=25;
			if(aiSnakex[0]<25) aiSnakex[0] = 850;
		}else if(aiDirection.equals("U")){
			aiSnakey[0] -=25;
			if(aiSnakey[0]<75) aiSnakey[0] = 650;
		}else if(aiDirection.equals("D")){
			aiSnakey[0] +=25;
			if(aiSnakey[0]>650) aiSnakey[0] = 75;
		}
	}
	
	//检查蛇是否死亡
	public void checkSnakeDeath(){
		//检查玩家蛇是否死亡
		if(playerAlive){
			//撞自己身体
			for(int i=1;i<playerLen;i++){
				if(playerSnakex[0] == playerSnakex[i] && playerSnakey[0] == playerSnakey[i]){
					playerAlive = false;
					break;
				}
			}
			
			//撞AI身体
			if(playerAlive){
				for(int i=0;i<aiLen;i++){
					if(playerSnakex[0] == aiSnakex[i] && playerSnakey[0] == aiSnakey[i]){
						playerAlive = false;
						break;
					}
				}
			}
		}
		
		//检查AI蛇是否死亡
		if(aiAlive){
			//撞自己身体
			for(int i=1;i<aiLen;i++){
				if(aiSnakex[0] == aiSnakex[i] && aiSnakey[0] == aiSnakey[i]){
					aiAlive = false;
					break;
				}
			}
			
			//撞玩家身体
			if(aiAlive){
				for(int i=0;i<playerLen;i++){
					if(aiSnakex[0] == playerSnakex[i] && aiSnakey[0] == playerSnakey[i]){
						aiAlive = false;
						break;
					}
				}
			}
		}
		
		//检查蛇头对撞
		if(playerAlive && aiAlive){
			if(playerSnakex[0] == aiSnakex[0] && playerSnakey[0] == aiSnakey[0]){
				playerAlive = false;
				aiAlive = false;
			}
		}
	}
	
	//检查游戏是否结束
	public void checkGameOver(){
		if(!playerAlive && !aiAlive){
			isGameOver = true;
			gameResult = "平局";
		}else if(!playerAlive){
			isGameOver = true;
			gameResult = "AI胜";
		}else if(!aiAlive){
			isGameOver = true;
			gameResult = "玩家胜";
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		timer.start();
		
		if(isStarted && !isGameOver){
			//移动玩家蛇
			if(playerAlive){
				//移动玩家身体
				for(int i=playerLen;i>0;i--){
					playerSnakex[i] = playerSnakex[i-1];
					playerSnakey[i] = playerSnakey[i-1];
				}
				//移动玩家头
				if(playerDirection.equals("R")){
					playerSnakex[0] +=25;
					if(playerSnakex[0]>850) playerSnakex[0] = 25;
				}else if(playerDirection.equals("L")){
					playerSnakex[0] -=25;
					if(playerSnakex[0]<25) playerSnakex[0] = 850;
				}else if(playerDirection.equals("U")){
					playerSnakey[0] -=25;
					if(playerSnakey[0]<75) playerSnakey[0] = 650;
				}else if(playerDirection.equals("D")){
					playerSnakey[0] +=25;
					if(playerSnakey[0]>650) playerSnakey[0] = 75;
				}
			}
			
			//移动AI蛇
			moveAISnake();
			
			//检查玩家蛇是否吃食物
			if(playerAlive && playerSnakex[0] == foodx && playerSnakey[0] == foody){
				playerLen++;
				playerScore +=10;
				generateFood();
			}
			
			//检查AI蛇是否吃食物
			if(aiAlive && aiSnakex[0] == foodx && aiSnakey[0] == foody){
				aiLen++;
				aiScore +=10;
				generateFood();
			}
			
			//检查蛇是否死亡
			checkSnakeDeath();
			
			//检查游戏是否结束
			checkGameOver();
		}
		repaint();
	}
}
