import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TimeCounter extends JLabel implements ActionListener{

	private Timer timer;
	private long timePassed;
	private int currentTime;
	
	public TimeCounter(){
		currentTime = 0;
	
		timer = new Timer(1000, this);
		setText(Integer.toString(currentTime));
		timer.setInitialDelay(100);
	}
	
	public void begin(){
		timer.start();
	}

	public void end(){
		timer.stop();
		
	}
		
	public int getTime(){
		return currentTime;
	}
		
	public void reset(){
		currentTime = 0;	
		setText(Integer.toString(currentTime));		
//		timer.restart();
	}
	
	public boolean running(){
		return timer.isRunning();
	}
	
	public void actionPerformed(ActionEvent e){
		if(timer.isRunning()){
			if(currentTime < 999)
				currentTime++;
			setText(Integer.toString(currentTime));
		}
	}	
}
