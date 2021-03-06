package unnamed.controller;

import org.newdawn.slick.SlickException;

import unnamed.model.element.Element;
import unnamed.view.Camera;

public class CameraController
{
	private Camera camera;

	public CameraController(Camera camera)
	{
		this.camera = camera;
	}

	public void movingUp()
	{
		this.camera.movingUp();
	}

	public void movingDown()
	{
		this.camera.movingDown();
	}

	public void movingLeft()
	{
		this.camera.movingLeft();
	}

	public void movingRight()
	{
		this.camera.movingRight();
	}

	public void stopMovingUp()
	{
		this.camera.stopMovingUp();
	}

	public void stopMovingDown()
	{
		this.camera.stopMovingDown();
	}

	public void stopMovingLeft()
	{
		this.camera.stopMovingLeft();
	}

	public void stopMovingRight()
	{
		this.camera.stopMovingRight();
	}

	public void zoom()
	{
		this.camera.zoom();
	}

	public void unzoom()
	{
		this.camera.unzoom();
	}

	public void reset()
	{
		this.camera.setToOrigin();
	}

	public void mouseWheelDragged(float oldx, float oldy, float newx, float newy)
	{
		this.camera.moveFromPointTo(oldx, oldy, newx, newy);
	}

	public void zoom(int times)
	{
		for(int i = 0; i < times; i++)
		{
			this.zoom();
		}
	}
	
	public boolean isVisible(Element elem) throws SlickException
	{
		return this.camera.isVisible(elem);
	}
}
