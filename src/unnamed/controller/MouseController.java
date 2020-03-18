package unnamed.controller;

import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;

public class MouseController implements MouseListener
{

	@Override
	public void inputEnded()
	{

	}

	@Override
	public void inputStarted()
	{

	}

	@Override
	public boolean isAcceptingInput()
	{
		return true;
	}

	@Override
	public void setInput(Input arg0)
	{

	}

	@Override
	public void mouseClicked(int button, int x, int y, int clickCount)
	{

	}

	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy)
	{
		GameController.getInstance().mouseDragged(oldx, oldy, newx, newy);
	}

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy)
	{

	}

	@Override
	public void mousePressed(int button, int x, int y)
	{
		if(button == Input.MOUSE_LEFT_BUTTON)
		{
			GameController.getInstance().pressedAt(x, y);
		}
	}

	@Override
	public void mouseReleased(int button, int x, int y)
	{
		if(button == Input.MOUSE_LEFT_BUTTON)
		{
			GameController.getInstance().clickAt(x, y);
		}
	}

	@Override
	public void mouseWheelMoved(int change)
	{
		GameController.getInstance().mouseWheelMoved(change);
	}

}