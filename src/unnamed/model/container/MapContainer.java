package unnamed.model.container;

import java.util.List;

import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import unnamed.controller.CameraController;
import unnamed.controller.GameController;
import unnamed.model.element.map.DesertTile;
import unnamed.model.element.map.GrassTile;
import unnamed.model.element.map.Tile;
import unnamed.model.element.map.WaterTile;
import unnamed.model.generator.MapGenerator;

public class MapContainer extends ElementContainer
{

	private static final int NUMBER_OF_ROWS = 75;
	private static final int NUMBER_OF_COLUMNS = 75;

	private boolean isMouseWheelActivated;

	public MapContainer()
	{
		super();
		this.isMouseWheelActivated = false;
	}

	@Override
	public void init() throws SlickException
	{
		GrassTile.init();
		WaterTile.init();
		DesertTile.init();

		MapGenerator gen = new MapGenerator(NUMBER_OF_ROWS, NUMBER_OF_COLUMNS);
		List<Tile> tiles = gen.generateMap(this);

		for(Tile tile : tiles)
		{
			this.addElement(tile);
		}
	}

	public int getMapHeight()
	{
		return (NUMBER_OF_ROWS * Tile.TILE_HEIGHT) - (Tile.FLOATING_OFFSET * NUMBER_OF_ROWS) + Tile.TILE_HEIGHT + Tile.FLOATING_OFFSET;
	}

	public int getMapWidth()
	{
		return (MapContainer.NUMBER_OF_COLUMNS * Tile.TILE_WIDTH) + (Tile.TILE_WIDTH / 2);
	}

	@Override
	public void mouseWheelMoved(int change)
	{
		if(change < 0)
		{
			GameController.getInstance().getCameraController().unzoom();
		}
		else
		{
			GameController.getInstance().getCameraController().zoom();
		}
	}

	@Override
	public void keyReleased(int key, char c)
	{
		CameraController cam = GameController.getInstance().getCameraController();

		switch(key)
		{
			case Input.KEY_UP:
				cam.stopMovingUp();
				break;
			case Input.KEY_DOWN:
				cam.stopMovingDown();
				break;
			case Input.KEY_LEFT:
				cam.stopMovingLeft();
				break;
			case Input.KEY_RIGHT:
				cam.stopMovingRight();
				break;
		}
	}

	@Override
	public void keyPressed(int key, char c)
	{
		CameraController cam = GameController.getInstance().getCameraController();

		switch(key)
		{
			case Input.KEY_UP:
				cam.movingUp();
				break;
			case Input.KEY_DOWN:
				cam.movingDown();
				break;
			case Input.KEY_LEFT:
				cam.movingLeft();
				break;
			case Input.KEY_RIGHT:
				cam.movingRight();
				break;
			case Input.KEY_ESCAPE:
				GameController.getInstance().goToMainMenu();
				break;
		}
	}

	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy)
	{
		if(this.isMouseWheelActivated)
		{
			GameController.getInstance().getCameraController().mouseWheelDragged(oldx, oldy, newx, newy);
		}
	}

	@Override
	public void wheelPressedAt(int x, int y)
	{
		this.isMouseWheelActivated = true;
	}

	@Override
	public void wheelReleasedAt(int x, int y)
	{
		this.isMouseWheelActivated = false;
	}
}
