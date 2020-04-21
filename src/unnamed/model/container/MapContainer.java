package unnamed.model.container;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import unnamed.controller.CameraController;
import unnamed.controller.GameController;
import unnamed.model.element.entity.Entity;
import unnamed.model.element.map.TileMap;
import unnamed.model.element.map.tile.Tile;
import unnamed.model.generator.MapGenerator;

public class MapContainer extends ElementContainer implements Serializable
{
	private static final long serialVersionUID = -3760401809828849717L;

	private static final int NUMBER_OF_COLUMNS = 75;
	private static final int NUMBER_OF_ROWS = 75;

	private boolean isMouseWheelActivated;

	private TileMap map;
	private Tile fountain;

	private List<Tile> transitionTiles;

	public MapContainer() throws SlickException
	{
		super();

		this.isMouseWheelActivated = false;

		this.map = new TileMap(MapContainer.NUMBER_OF_COLUMNS, MapContainer.NUMBER_OF_ROWS);

		this.fountain = Tile.getEmptyTile();

		this.transitionTiles = new ArrayList<Tile>();
	}

	@Override
	public void init() throws SlickException
	{
		MapGenerator generator = new MapGenerator(MapContainer.NUMBER_OF_COLUMNS, MapContainer.NUMBER_OF_ROWS);

		this.map.addAll(generator.generateMap(this));
		this.transitionTiles.addAll(this.map);

		this.fountain = generator.getFountain();

		for(Tile tile : this.map)
		{
			this.addElement(tile);
		}

		this.addElement(new Entity(this.fountain, this));
	}

	@Override
	public void tickUpdate() throws SlickException
	{
		if(!this.transitionTiles.isEmpty())
		{
			this.transitionAllTiles();
		}

		super.tickUpdate();
	}

	private void transitionAllTiles() throws SlickException
	{
		for(int i = 0; i < this.transitionTiles.size(); i++)
		{
			Tile tile = this.transitionTiles.get(i);

			if(!tile.isSeen() && GameController.getInstance().getCameraController().isVisible(tile))
			{
				tile.seen();
			}

			if(tile.doTransitionStep())
			{
				this.transitionTiles.remove(i);
				i--;
			}
		}
	}

	@Override
	public int getHeight()
	{
		return ((MapContainer.NUMBER_OF_ROWS * Tile.TILE_HEIGHT) - (Tile.FLOATING_OFFSET * MapContainer.NUMBER_OF_ROWS)) + Tile.TILE_HEIGHT + Tile.FLOATING_OFFSET;
	}

	@Override
	public int getWidth()
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
	public void keyPressed(int key, char c) throws SlickException
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
				GameController.getInstance().goToPauseMenu();
				break;
		}
	}

	@Override
	public void mouseDragged(float oldx, float oldy, float newx, float newy)
	{
		if(this.isMouseWheelActivated)
		{
			GameController.getInstance().getCameraController().mouseWheelDragged(oldx, oldy, newx, newy);
		}
	}

	@Override
	public void wheelPressedAt(float x, float y)
	{
		this.isMouseWheelActivated = true;
	}

	@Override
	public void wheelReleasedAt(float x, float y)
	{
		this.isMouseWheelActivated = false;
	}

	@Override
	public void rightClickAt(float x, float y) throws SlickException
	{
		if(this.getSelected() instanceof Entity)
		{
			((Entity) this.getSelected()).moveTo(this.getTopTileAt(x, y));
		}
	}

	private Tile getTopTileAt(float x, float y) throws SlickException
	{
		List<Tile> tiles = new ArrayList<Tile>(this.map);
		tiles.sort((left, right) -> Integer.compare(left.getZ(), right.getZ()));
		Tile topTile = this.getTopElementAtIn(x, y, tiles);

		return topTile == null ? Tile.getEmptyTile() : topTile;
	}

	@Override
	public void enter()
	{
		GameController.getInstance().getCameraController().zoom(10);
	}
}
