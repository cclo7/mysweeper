//Author: Chiachi Lo
//September 2008

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.Calendar;
import java.io.*;

public class Mysweeper3{
	
	private static JFrame win;
	private static JPanel buttonPanel;
	private static JLabel countLabel;
	private static JRadioButtonMenuItem item3;
	private static JRadioButtonMenuItem item4;
	private static JRadioButtonMenuItem item5;
	
	private static int size;
	private static int row;
	private static int col;
	private static int numOfMines;
	private static int flagCount;
	private static int level;
	
	private static int smallR = 275;
	private static int smallC = 350;
	private static int midR = 455;
	private static int midC = 530;
	private static int bigR = 750;
	private static int bigC = 530;
	
	private static ArrayList<Integer> mines;
	private static ArrayList<Button3> buttons;
	
	//private static File saveFile;
	//private static boolean saveExists;
	private static String[] timeArray = new String[3];
	private static 	String[] dateArray = new String[3];
	
	private static TimeCounter timer;
	private static int resultTime;
	
	public Mysweeper3(){
		row = 9;					//create a small size minesweeper as default
		col = 9;
		size = row*col;
		numOfMines = 10;
		level = 1;
		
		mines = new ArrayList<Integer>(numOfMines);
		buttons = new ArrayList<Button3>(size);
		
		win = new JFrame("Mysweeper");
		buttonPanel = new JPanel(new GridLayout(row, col));		
		timer = new TimeCounter();
		
		setUpMines();
		createButtons();
		setUpMenu();		
		organizeWindow();
//		readSaved();
		
		win.setSize(smallR, smallC);
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		win.setVisible(true);
		win.setResizable(true);
		
		Dimension dim = win.getToolkit().getScreenSize();
		win.setLocation(dim.width/2 - win.getWidth()/2, dim.height/2 - win.getHeight()/2);		

	}
	
	public static boolean allMinesFlagged(){
		for(int i = 0, k; i < numOfMines; i++){
			k = mines.get(i);
			Button3 b = buttons.get(k);
			if(!b.isFlagged())
				return false;
		}
		return true;
	}
	
	public static void createButtons(){
		for(int i = 0; i < size; i++){
			boolean mined = false;
			if(lookUpMines(i))
				mined = true;
			
			Button3 b = new Button3(i, mined);
			buttons.add(i, b);
			buttonPanel.add(b);
		}
	}
	
	public static void displayGameOver(boolean gameWon){
	
		timer.end();
	
		String victory = "VICTORIOUS!";
		String defeat = "DEFEATED -.-";
		String result;

		resultTime = timer.getTime();
		
		if(gameWon){
			result = victory;
			// if(updateScore()){
				// result += "\nbtw, You set a new record time!";
			// }
		}			
		else
			result = defeat;
		
		Object[] options = {"Yes", "No (exit)"};
		int n = JOptionPane.showOptionDialog(win, result + "\nTime:                    " + 
		Integer.toString(timer.getTime()) + 
		" secs\nPlay again~?", "You are...", JOptionPane.YES_NO_OPTION, 
		JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
		
		
		
		if(n == JOptionPane.OK_OPTION){
			resetGame();					//restart the game in current mode	
		}	
		else{
			//System.out.println("going to dispose");
			win.dispose();					//exit the game
		}	
	}		

	public static int getHint(int pos){
		int num = 0;
		if(pos >= col){				//check top
			int top = pos - col;
			if(lookUpMines(top))
				num++;
			if((pos % col) != 0){
				int topLeft = top -1;
				if(lookUpMines(topLeft))
					num++;
			}
			if(((pos+1) % col) != 0){
				int topRight = top +1;
				if(lookUpMines(topRight))
					num++;
			}		
		}
		
		if((pos % col) != 0){		//check left
			int left = pos -1;
			if(lookUpMines(left))
				num++;
		}
		
		if(((pos+1) % col) != 0){			//check right
			int right = pos+1;
			if(lookUpMines(right)){
				num++;
			}
		}
		
		if(pos < (size - col)){		//check buttom
			int buttom = pos + col;
			if(lookUpMines(buttom))
				num++;
			if((pos % col) != 0){
				int buttomLeft = buttom -1;
				if(lookUpMines(buttomLeft))
					num++;
			}
			if(((pos+1)%col) != 0){
				int buttomRight = buttom+1;
				if(lookUpMines(buttomRight))
					num++;
			}
		}
		return num;
	}

	public static void increaseCount(boolean increment){
		if(increment)
			flagCount++;
		else
			flagCount--;
			
		countLabel.setText("Mines count: " + Integer.toString(flagCount));
	}
	
	public static boolean lookUpMines(int pos){
		for(int i = 0; i < numOfMines; i++){
			if(mines.get(i) == pos)
				return true;
		}
		return false;
	}
	
	public static void organizeWindow(){
		Container cp = win.getContentPane();
		cp.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.insets = new Insets(10,10,10,10);
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridheight = GridBagConstraints.RELATIVE;
		gbc.fill = GridBagConstraints.BOTH;
		
		cp.add(buttonPanel, gbc);
		
		countLabel = new JLabel();
		countLabel.setText("Mines count: " + Integer.toString(flagCount));
		
		JLabel timeLabel = new JLabel();
		timeLabel.setText("Time: ");
		
		JPanel pan = new JPanel();
		pan.setLayout(new GridBagLayout());

		gbc = new GridBagConstraints();
		gbc.insets = new Insets(0,0,5,5);		
		
		pan.add(countLabel, gbc);
	
		gbc = new GridBagConstraints();
		gbc.insets = new Insets(0,50,5,5);			
		
		pan.add(timeLabel, gbc);
		
		gbc = new GridBagConstraints();
		gbc.insets = new Insets(0,5,5,5);		
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		pan.add(timer, gbc);
		
		//cp.add(countLabel);
		//cp.add(timer, gbc);
		cp.add(pan);
	}
	
	
	public static void removeButtons(){
		for(int i = 0; i < size; i++){
			Button3 b = buttons.get(i);
			buttonPanel.remove(b);
		}
	}
	
	public static void resetGame(){
		
		resetTimer();
		
		setUpMines();
		removeButtons();
		createButtons();
		
		flagCount = numOfMines;
		countLabel.setText("Mines count: " + Integer.toString(flagCount));		
		
		buttonPanel.updateUI();		
	}
	
	public static void resetGameMode(int gameLevel){
		if(gameLevel == level)
			return;
		
		int winRow = smallR, winCol = smallC;
		
		if(gameLevel == 1){
			row = 9;
			col = 9;
			numOfMines = 10;
			winRow = smallR;
			winCol = smallC;
		}
		else if(gameLevel == 2){
			row = 16;
			col = 16;
			numOfMines = 40;
			winRow = midR;
			winCol = midC;
		}
		else if(gameLevel == 3){
			row = 16;
			col = 30;
			numOfMines = 99;
			winRow = bigR;
			winCol = bigC;
		}
		level = gameLevel;
		size = row*col;

		mines = new ArrayList<Integer>(numOfMines);
		buttons = new ArrayList<Button3>(size);			

		win.dispose();
		win = new JFrame("Mysweeper");		
		buttonPanel = new JPanel(new GridLayout(row, col));					
		timer = new TimeCounter();
	
		setUpMines();
		//removeButtons();		no need, win.dispose already destroys everything
		createButtons();
		setUpMenu();
		organizeWindow();

		if(level == 1)
			item3.setSelected(true);
		else if(level == 2)
			item4.setSelected(true);
		else if(level ==3)
			item5.setSelected(true);
		
		win.setSize(winRow, winCol);
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		win.setVisible(true);
		win.setResizable(false);
		
		Dimension dim = win.getToolkit().getScreenSize();
		win.setLocation(dim.width/2 - win.getWidth()/2, dim.height/2 - win.getHeight()/2);
		
		flagCount = numOfMines;
		countLabel.setText("Mines count: " + Integer.toString(flagCount));		
	
	}	
	
	public static void revealAnySurround(Button3 b){
		int pos = b.getPos();
		int top=0, left=0, right=0, buttom=0;
		
		if(pos >= col){
			top = pos - col;
			Button3 b2 = buttons.get(top);
			if(!b2.isAMine())
				b2.open();
		}
		
		if(((pos) % col) != 0){
			left = pos-1;
			Button3 b2 = buttons.get(left);
			if(!b2.isAMine())
				b2.open();
		}
		
		if(((pos+1) % col) != 0){			
			right = pos+1;
			Button3 b2 = buttons.get(right);
			if(!b2.isAMine())
				b2.open();
		}		

		if(pos < (size - col)){
			buttom = pos + col;		
			Button3 b2 = buttons.get(buttom);
			if(!b2.isAMine())
				b2.open();
		}


		if(pos >= col){
			if((pos % col) != 0){
				int topLeft = top -1;
				Button3 b2 = buttons.get(topLeft);
				if(!b2.isAMine())
					b2.open();
			}
			if(((pos+1)%col) != 0){
				int topRight = top+1;
				Button3 b2 = buttons.get(topRight);
				if(!b2.isAMine())
					b2.open();
			}
		}
		
		if(pos < (size - col)){
			if((pos % col) != 0){
				int buttomLeft = buttom -1;
				Button3 b2 = buttons.get(buttomLeft);
				if(!b2.isAMine())
					b2.open();
			}

			if(((pos+1)%col)!=0){
				int buttomRight = buttom+1;
				Button3 b2 = buttons.get(buttomRight);
				if(!b2.isAMine())
					b2.open();					
			}
		}	
		
	}

	
	public static void setUpMenu(){
		JMenuBar menuBar = new JMenuBar();						//create menu & items
		JMenu menu = new JMenu("Option");
		JMenuItem item = new JMenuItem("New Game");
		item.addActionListener(al);
		
		JMenuItem item6 = new JMenuItem("See Records");
		item6.addActionListener(al6);
		
		JMenu menu2 = new JMenu("About");
		JMenuItem item2 = new JMenuItem("the game");
		item2.addActionListener(al2);
		
		JMenu menu3 = new JMenu("Level");
		item3 = new JRadioButtonMenuItem("EZ");
		item3.addActionListener(al3);
		item4 = new JRadioButtonMenuItem("OKAY");
		item4.addActionListener(al4);
		item5 = new JRadioButtonMenuItem("FUN");
		item5.addActionListener(al5);
		
		ButtonGroup group = new ButtonGroup();
		group.add(item3);
		group.add(item4);
		group.add(item5);
		
		item3.setSelected(true);
		
		menu.add(item);
		menu.add(item6);
		menu2.add(item2);
		menu3.add(item3);
		menu3.add(item4);
		menu3.add(item5);
		
		menuBar.add(menu);
		menuBar.add(menu3);
		menuBar.add(menu2);
		win.setJMenuBar(menuBar);	
	
	}
	
	public static void setUpMines(){
		flagCount= numOfMines;
	
		for(int i=0; i < numOfMines; i++){
			mines.add(-1);
		}
		
		Random rand = new Random();
		for(int i=0, k; i < numOfMines; i++){
			do{
				k = rand.nextInt(size);
			}while(lookUpMines(k));
			mines.add(i, k);
		}
		
	}

//--------------------Counter Managing Methods	--------------------

	public static void runTimer(){
		if(!timer.running()){
				timer.begin();
		}
	}
	
	public static void resetTimer(){
		timer.end();
		timer.reset();
	}

/*
//--------------------Save File Methods	--------------------
	public static void writeDefaultSave(){
		PrintWriter out = null;

		try{
			out = new PrintWriter(new FileWriter(saveFile));
			
			String  s;
			for(int i = 0; i < 3; i++){
				out.println("2008");
				out.println("8");
				out.println("21");
			}
				
			for(int i = 0; i < 3; i++){
				out.println("999");
			}
			out.flush();
		
		}catch(IOException ex){
		}finally{
			if(out != null)
					out.close();
		}
	
	}

	public static void readSaved(){
		saveFile = new File("save.txt");
		
		try{
			if(saveFile.createNewFile())
				writeDefaultSave();
			
		}catch(IOException io){
		}
		
		BufferedReader in = null;
			
		try{
			in = new BufferedReader(new FileReader(saveFile));
			
			String  s;
			for(int i = 0; i < 3; i++){
				String g = "";
				for(int j = 0; j < 3; j++){
					if((s = in.readLine()) == null){
						break;
					}
					g+=s;
					if(j != 2)
						g+=".";
				}
				dateArray[i] = g;
			}
				
			for(int i = 0; i < 3; i++){
				if((s = in.readLine()) == null)
					break;
					
				timeArray[i] = s;
			}
				
					//System.out.println(s);
				
				
		}catch(IOException ex){
		}finally{
			try{
				if(in != null)
					in.close();
			}catch(IOException ex){
			}
		}
	}

	public static boolean updateScore(){

		readSaved();
		
		int a = Integer.parseInt(timeArray[0]);
		int b = Integer.parseInt(timeArray[1]);
		int c = Integer.parseInt(timeArray[2]);
		
		boolean recordSet = false;
		
		int line = -1;
		
		if(resultTime < a)
			line = 0;
		else if(resultTime < b)
			line = 1;
		else if(resultTime < c)
			line = 2;

		if(line != -1){
			recordSet = true;
			
			System.out.println(resultTime);
			timeArray[line] = Integer.toString(resultTime);

			Calendar calendar = Calendar.getInstance();
			String d =Integer.toString(calendar.get(calendar.YEAR));
			d += ".";
			d += Integer.toString(calendar.get(calendar.MONTH));
			d += ".";
			d += Integer.toString(calendar.get(calendar.DATE));					

			dateArray[line] = d;
		
			PrintWriter out = null;

			try{
				out = new PrintWriter(new FileWriter(saveFile));

				for(int i = 0; i < 3; i++){
					String whole = dateArray[i];
					String yr = whole.substring(0, 4);
					String mon = whole.substring(5, 6);
					String dat = whole.substring(7,9);
					out.println(yr);
					out.println(mon);
					out.println(dat);
				}
				
				for(int i = 0; i < 3; i++){
					out.println(timeArray[i]);
				}
				
				out.flush();
				
			}catch(IOException ex){
			}finally{
				if(out != null)
						out.close();
			}
			readSaved();
		}		
			
		return recordSet;
	}
*/	

//--------------------Anonymous Active Listener Classes--------------------

	static ActionListener al = new ActionListener(){
		public void actionPerformed(ActionEvent e){
			resetGame();
		}
	};
	
	static ActionListener al2 = new ActionListener(){
		public void actionPerformed(ActionEvent e){
			JFrame frame = new JFrame("About this game");
			
			JLabel label = new JLabel();

			ImageIcon iconAbout;
			java.net.URL imgURL = getClass().getResource("images/about.jpg");
			if(imgURL != null){
				iconAbout = new ImageIcon(imgURL);
				label.setIcon(iconAbout);
			}else{
				System.err.println("Couldn't find file: images/about.jpg");
			}
			
			JPanel jp = new JPanel();
			jp.add(label);
			
			frame.add(jp);
			frame.setSize(300,200);
			frame.setVisible(true);
			frame.setResizable(false);
			
			Dimension dim = frame.getToolkit().getScreenSize();
			frame.setLocation(dim.width/2 - frame.getWidth()/2, dim.height/2 - frame.getHeight()/2);			
		}
	};

	static ActionListener al3 = new ActionListener(){
		public void actionPerformed(ActionEvent e){
			resetGameMode(1);
		}
	};

	static ActionListener al4 = new ActionListener(){
		public void actionPerformed(ActionEvent e){
			resetGameMode(2);
		}
	};

	static ActionListener al5 = new ActionListener(){
		public void actionPerformed(ActionEvent e){
			resetGameMode(3);
		}
	};
	
	static ActionListener al6 = new ActionListener(){
		public void actionPerformed(ActionEvent e){
			
			JOptionPane.showMessageDialog(null, "This feature will be added later.", "sumimasen", JOptionPane.INFORMATION_MESSAGE);
/*			
			readSaved();

			JLabel lab = new JLabel();
			lab.setText("<html><font color = blue>Record times: </font></html>");
			lab.setHorizontalAlignment(JLabel.CENTER);
			
			JLabel lab1 = new JLabel();
			lab1.setText("1.               Date     " + dateArray[0] + "       " + timeArray[0] + " secs");
			lab1.setHorizontalAlignment(JLabel.CENTER);
			
			JLabel lab2 = new JLabel();
			lab2.setText("2.               Date     " + dateArray[1] + "       " + timeArray[1] + " secs" );
			lab2.setHorizontalAlignment(JLabel.CENTER);
			
			JLabel lab3 = new JLabel();
			lab3.setText("3.               Date     " + dateArray[2] + "       " + timeArray[2] + " secs" );
			lab3.setHorizontalAlignment(JLabel.CENTER);
			
			JFrame scoreBoard = new JFrame("Fastest Times");

			JPanel pan = new JPanel(new GridLayout(6, 0));
			pan.setBorder(BorderFactory.createLineBorder(Color.black));
			pan.add(lab, JLabel.CENTER_ALIGNMENT);
			pan.add(lab1);
			pan.add(lab2);
			pan.add(lab3);
			
			
			scoreBoard.add(pan);
			scoreBoard.setSize(300,300);
			scoreBoard.setVisible(true);
			scoreBoard.setResizable(false);
			
			Dimension dim = scoreBoard.getToolkit().getScreenSize();
			scoreBoard.setLocation(dim.width/2 - scoreBoard.getWidth()/2, dim.height/2 - scoreBoard.getHeight()/2);						
*/			
		}
	};
	
	public static void main(String[] args){
		new Mysweeper3();

	}	
}