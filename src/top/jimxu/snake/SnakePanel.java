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
	int[] playerx = new int[750];
	int[] playery = new int[750];
	int playerLen = 3;
	String playerDirection = "R"; //R右L左U上D下
	boolean playerAlive = true;
	int playerScore = 0;
	
	//AI蛇的数据结构
	int[] aix = new int[750];
	int[] aiy = new int[750];
	int aiLen = 3;
	String aiDirection = "R"; //R右L左U上D下
	boolean aiAlive = true;
	int aiScore = 0;
	
	//食物生成
	Random r = new Random();
	int foodx = r.nextInt(34)*25+25; // 34个格子，一个格子25排像素，还有25像素空白
	int foody = r.nextInt(24)*25+75; // 24个格子，一个格子25排像素，还有75像素空白
	
	//游戏是否开始
	boolean isStarted = true; //直接开始游戏
	
	//游戏结束后的状态
	String gameResult = ""; //玩家胜、AI胜、平局

	
	//初始化玩家蛇
	public void initPlayerSnake(){
		playerLen = 3;
		playerDirection = "R";
		playerx[0] = 100;
		playery[0] = 100;
		playerx[1] = 75;
		playery[1] = 100;
		playerx[2] = 50;
		playery[2] = 100;
		playerAlive = true;
		playerScore = 0;
	}
	
	//初始化AI蛇
	public void initAISnake(){
		aiLen = 3;
		aiDirection = "L";
		aix[0] = 800;
		aiy[0] = 500;
		aix[1] = 825;
		aiy[1] = 500;
		aix[2] = 850;
		aiy[2] = 500;
		aiAlive = true;
		aiScore = 0;
	}
	
	//初始化游戏
	public void initGame(){
		initPlayerSnake();
		initAISnake();
		isStarted = true;
		gameResult = "";
		//重新生成食物
		foodx = r.nextInt(34)*25+25;
		foody = r.nextInt(24)*25+75;
	}
	public SnakePanel() {
		this.setFocusable(true);
		initGame(); //初始化游戏
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
		
		//画玩家分数
		g.setColor(Color.WHITE);
		g.setFont(new Font("arial",Font.BOLD,20));
		g.drawString("Player: " + playerScore, 300, 50);
		
		//画AI分数
		g.setColor(Color.WHITE);
		g.setFont(new Font("arial",Font.BOLD,20));
		g.drawString("AI: " + aiScore, 500, 50);
		
		//画玩家蛇
		if(playerAlive){
			//画玩家蛇头
			if(playerDirection.equals("R")){
				right.paintIcon(this, g, playerx[0], playery[0]);
			}else if(playerDirection.equals("L")){
				left.paintIcon(this, g, playerx[0], playery[0]);
			}else if(playerDirection.equals("U")){
				up.paintIcon(this, g, playerx[0], playery[0]);
			}else if(playerDirection.equals("D")){
				down.paintIcon(this, g, playerx[0], playery[0]);
			}
			//画玩家蛇身
			for(int i=1;i<playerLen;i++){
				body.paintIcon(this, g, playerx[i], playery[i]);
			}
		}
		
		//画AI蛇
		if(aiAlive){
			//画AI蛇头
			if(aiDirection.equals("R")){
				right.paintIcon(this, g, aix[0], aiy[0]);
			}else if(aiDirection.equals("L")){
				left.paintIcon(this, g, aix[0], aiy[0]);
			}else if(aiDirection.equals("U")){
				up.paintIcon(this, g, aix[0], aiy[0]);
			}else if(aiDirection.equals("D")){
				down.paintIcon(this, g, aix[0], aiy[0]);
			}
			//画AI蛇身
			for(int i=1;i<aiLen;i++){
				body.paintIcon(this, g, aix[i], aiy[i]);
			}
		}
		
		//画食物
		food.paintIcon(this, g, foodx, foody);
		
		//画游戏结果
		if(!gameResult.equals("")){
			g.setColor(Color.WHITE);
			g.setFont(new Font("arial",Font.BOLD,40));
			g.drawString(gameResult, 350, 350);
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
		//实现空格重新开始
		if(keyCode == KeyEvent.VK_SPACE){
			initGame();
		}
		//玩家蛇控制 - 使用WASD键
		else if(keyCode == KeyEvent.VK_W && !playerDirection.equals("D")){
			playerDirection = "U";
		}else if(keyCode == KeyEvent.VK_S && !playerDirection.equals("U")){
			playerDirection = "D";
		}else if(keyCode == KeyEvent.VK_A && !playerDirection.equals("R")){
			playerDirection = "L";
		}else if(keyCode == KeyEvent.VK_D && !playerDirection.equals("L")){
			playerDirection = "R";
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
	//AI蛇移动逻辑
	public void moveAISnake(){
		if(!aiAlive) return;
		
		//AI决策方向 - 简单逻辑：向食物靠近
		int dx = foodx - aix[0];
		int dy = foody - aiy[0];
		
		//优先考虑x方向
		if(Math.abs(dx) > Math.abs(dy)){
			if(dx > 0 && !aiDirection.equals("L")){
				aiDirection = "R";
			}else if(dx < 0 && !aiDirection.equals("R")){
				aiDirection = "L";
			}
		}else{
			if(dy > 0 && !aiDirection.equals("U")){
				aiDirection = "D";
			}else if(dy < 0 && !aiDirection.equals("D")){
				aiDirection = "U";
			}
		}
		
		//移动AI蛇身体
		for(int i=aiLen;i>0;i--){
			aix[i] = aix[i-1];
			aiy[i] = aiy[i-1];
		}
		
		//移动AI蛇头
		if(aiDirection.equals("R")){
			aix[0] += 25;
			if(aix[0] > 850) aix[0] = 25;
		}else if(aiDirection.equals("L")){
			aix[0] -= 25;
			if(aix[0] < 25) aix[0] = 850;
		}else if(aiDirection.equals("U")){
			aiy[0] -= 25;
			if(aiy[0] < 75) aiy[0] = 650;
		}else if(aiDirection.equals("D")){
			aiy[0] += 25;
			if(aiy[0] > 650) aiy[0] = 75;
		}
	}
	
	//检查蛇是否碰撞
	public boolean checkCollision(int[] snakex, int[] snakey, int len, int headx, int heady){
		//检查是否撞自己身体
		for(int i=1;i<len;i++){
			if(headx == snakex[i] && heady == snakey[i]){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		timer.start();
		
		if(isStarted){
			//移动玩家蛇
			if(playerAlive){
				//移动玩家蛇身体
				for(int i=playerLen;i>0;i--){
					playerx[i] = playerx[i-1];
					playery[i] = playery[i-1];
				}
				
				//移动玩家蛇头
				if(playerDirection.equals("R")){
					playerx[0] += 25;
					if(playerx[0] > 850) playerx[0] = 25;
				}else if(playerDirection.equals("L")){
					playerx[0] -= 25;
					if(playerx[0] < 25) playerx[0] = 850;
				}else if(playerDirection.equals("U")){
					playery[0] -= 25;
					if(playery[0] < 75) playery[0] = 650;
				}else if(playerDirection.equals("D")){
					playery[0] += 25;
					if(playery[0] > 650) playery[0] = 75;
				}
				
				//玩家蛇吃食物
				if(playerx[0] == foodx && playery[0] == foody){
					playerLen++;
					playerScore++;
					//重新生成食物
					foodx = r.nextInt(34)*25+25;
					foody = r.nextInt(24)*25+75;
				}
				
				//检查玩家蛇碰撞
				if(checkCollision(playerx, playery, playerLen, playerx[0], playery[0]) ||
				   checkCollision(aix, aiy, aiLen, playerx[0], playery[0])){
					playerAlive = false;
				}
			}
			
			//移动AI蛇
			moveAISnake();
			
			//AI蛇吃食物
			if(aiAlive && aix[0] == foodx && aiy[0] == foody){
				aiLen++;
				aiScore++;
				//重新生成食物
				foodx = r.nextInt(34)*25+25;
				foody = r.nextInt(24)*25+75;
			}
			
			//检查AI蛇碰撞
			if(aiAlive && (checkCollision(aix, aiy, aiLen, aix[0], aiy[0]) ||
			               checkCollision(playerx, playery, playerLen, aix[0], aiy[0])) ){
				aiAlive = false;
			}
			
			//检查蛇头对撞
			if(playerAlive && aiAlive && playerx[0] == aix[0] && playery[0] == aiy[0]){
				playerAlive = false;
				aiAlive = false;
			}
			
			//判断游戏结束
			if(!playerAlive && !aiAlive){
				gameResult = "平局";
			}else if(!playerAlive){
				gameResult = "AI胜";
			}else if(!aiAlive){
				gameResult = "玩家胜";
			}
		}
		repaint();
	}
}
