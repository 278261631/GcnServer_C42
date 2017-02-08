package test;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;

public class RobotTest {

	public static void main(String[] args) {
		Robot bot = null;
		try {
			bot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bot.mouseMove(0, 1000);
		bot.mousePress(InputEvent.BUTTON1_MASK);
		bot.delay(3000);
		
		bot.mousePress(InputEvent.BUTTON1_MASK);
		
//		bot.keyPress(keycode);


	}
}
