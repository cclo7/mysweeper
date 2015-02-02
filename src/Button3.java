import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Button3 extends JButton{

	ImageIcon iconDefault = createImageIcon("images/blank.jpg");
	ImageIcon iconQ = createImageIcon("images/question.jpg");
	ImageIcon iconF = createImageIcon("images/flag.jpg");
	ImageIcon iconM = createImageIcon("images/mine2.jpg");
	ImageIcon icon0 = createImageIcon("images/zero.jpg");
	ImageIcon icon1 = createImageIcon("images/one.jpg");
	ImageIcon icon2 = createImageIcon("images/two.jpg");
	ImageIcon icon3 = createImageIcon("images/three.jpg");
	ImageIcon icon4 = createImageIcon("images/four.jpg");
	ImageIcon icon5 = createImageIcon("images/five.jpg");
	ImageIcon icon6 = createImageIcon("images/six.jpg");
	ImageIcon icon7 = createImageIcon("images/seven.jpg");
	
	private Font font;
	private boolean hasMine;
	private boolean flagged;
	private boolean opened;
	private boolean questioned;
	private int label;
	private int hint;

	public Button3(int pos, boolean mined){
		
		label = pos;
		hasMine = mined;
		flagged = false;
		opened = false;
		questioned = false;
		hint = Mysweeper3.getHint(label);

		font = new Font("SansSerif", Font.BOLD,8);
		setFont(font);
		
		setIcon(iconDefault);
		
		addMouseListener(ml);
	}
	
	private ImageIcon createImageIcon(String path){
		java.net.URL imgURL = getClass().getResource(path);
		if(imgURL != null){
			return new ImageIcon(imgURL);
		}else{
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
	
	private void displayHint(){
		if(hint == 0)
			setIcon(icon0);
		else if(hint == 1)
			setIcon(icon1);
		else if(hint == 2)
			setIcon(icon2);
		else if(hint == 3)
			setIcon(icon3);
		else if(hint == 4)
			setIcon(icon4);
		else if(hint == 5)
			setIcon(icon5);
		else if(hint == 6)
			setIcon(icon6);
		else if(hint == 7)
			setIcon(icon7);			
	}
	
	public int getPos(){
		return label;
	}

	public boolean isAMine(){
		return hasMine;
	}
	
	public boolean isFlagged(){
		return flagged;
	}
	
	public void open(){
		if(opened)			//if already opened, do nothing
			return;
		else{
			opened = true;
			if(hint == 0){
				displayHint();
				Mysweeper3.revealAnySurround(this);
			}	
			else
				displayHint();			//only display this button's hint
		}
	}
	
	public void respond(int num){
		if(!opened){
			if(num ==1){			//if left mouse button pressed
				if(!flagged){
					if(hasMine){
						setIcon(iconM);
						Mysweeper3.displayGameOver(false);			//display game over
					}
					else if(hint > 0){			//if hint > 0, opens up this button
						displayHint();
						opened = true;
					}
					else if(hint == 0){			//if hint = 0, opens up this button, and reveal surrounding areas
						displayHint();
						opened = true;
						Mysweeper3.revealAnySurround(this);
					}
				}	
			}
			else if(num == 3){		//if right mouse button pressed
				if(!flagged && !questioned){		//if nothing has been done to this button yet
					flagged = true;
					Mysweeper3.increaseCount(false);		//decrease mine count;
					setIcon(iconF);
					if(Mysweeper3.allMinesFlagged())
						Mysweeper3.displayGameOver(true);		//display victory
				}
				else if(questioned){		//if already question marked
					setIcon(iconDefault);
					questioned = false;
				}
				else if(flagged){			//if already flagged
					flagged = false;
					questioned = true;
					setIcon(iconQ);
					Mysweeper3.increaseCount(true);		//increase mine count
				}
			}
		}
	}
	
	MouseListener ml = new MouseListener(){
		public void mousePressed(MouseEvent e){
			Mysweeper3.runTimer();
				
			switch(e.getButton()){
				case MouseEvent.BUTTON1:
					respond(1);
					break;
				case MouseEvent.BUTTON3:
					respond(3);
					break;
			}
			
		}
		public void mouseClicked(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}		
		public void mouseReleased(MouseEvent e){}		
		
	};

}