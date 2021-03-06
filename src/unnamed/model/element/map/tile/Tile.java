package unnamed.model.element.map.tile;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import unnamed.controller.GameController;
import unnamed.model.PixelisedImage;
import unnamed.model.container.ElementContainer;
import unnamed.model.element.Element;
import unnamed.model.element.SelectableElement;
import unnamed.model.element.entity.Entity;
import unnamed.model.element.map.tile.behaviour.TileBehaviour;

public class Tile extends Element implements SelectableElement
{
	private static Tile EMPTY;

	public static final int TRANSITION_STEP_SPEED = 6;
	private static final int TRANSITION_OFFFSET = 100;
	private static final int AVERAGE_TRANSITION_ORIGIN = 800;

	public static final int Y_ENTITY_OFFSET = 29;

	public static final int TILE_WIDTH = 32;
	public static final int TILE_HEIGHT = 29;

	public static final int FLOATING_OFFSET = 8;
	private static final int Z_SPACE_BETWEEN_TILES = 10;

	private static TileImageRegistry registry;

	private Map<TileDirection, Tile> adjacents;

	private int column;
	private int row;

	private TileBiome biome;
	private TileBehaviour behaviour;

	private TileType type;

	private int spriteVariant;

	private boolean isSelected;

	private boolean isOccupied;

	private boolean seen;

	public static void init() throws SlickException
	{
		Tile.registry = new TileImageRegistry();
	}

	private Tile() throws SlickException
	{
		this(ElementContainer.getEmptyContainer());
	}

	public Tile(ElementContainer container) throws SlickException
	{
		this(0, 0, container);
	}

	public Tile(int column, int row, ElementContainer container) throws SlickException
	{
		super(Tile.getXValueFrom(column, row), Tile.getYValueFrom(row), Tile.getZValueFrom(row), container);

		this.setAdjacents(new HashMap<TileDirection, Tile>());

		this.column = column;
		this.row = row;

		this.setBiome(TileBiome.GRASS);

		this.type = TileType.FLAT;
		this.spriteVariant = this.type.getRandomVariant();

		this.isSelected = false;

		this.isOccupied = false;

		this.seen = false;
	}

	public int getColumn()
	{
		return this.column;
	}

	public int getRow()
	{
		return this.row;
	}

	public TileType getType()
	{
		return this.type;
	}

	public void setType(TileType type)
	{
		this.type = type;
		this.setSpriteVariant(type.getRandomVariant());
	}

	public TileBiome getBiome()
	{
		return this.biome;
	}

	public void setBiome(TileBiome biome) throws SlickException
	{
		this.biome = biome;

		try
		{
			if(this.behaviour != null)
			{
				this.behaviour.cleanUp();
			}

			this.setBehaviour(this.biome.getBehaviour().getConstructor(Tile.class).newInstance(this));

			this.informAllNeighboursOfChange();
		}
		catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
		{
			e.printStackTrace();
		}
	}

	public TileBehaviour getBehaviour()
	{
		return this.behaviour;
	}

	public void setBehaviour(TileBehaviour behaviour)
	{
		this.behaviour = behaviour;
	}

	private void informAllNeighboursOfChange() throws SlickException
	{
		for(Tile tile : this.getAdjacents())
		{
			tile.informNeighbourChange();
		}
	}

	public void informNeighbourChange() throws SlickException
	{
		this.behaviour.informNeighbourChange();
	}

	public Tile getAdjacentFrom(TileDirection direction)
	{
		return this.adjacents.get(direction);
	}

	public List<Tile> getAdjacents()
	{
		ArrayList<Tile> adjacents = new ArrayList<Tile>(this.adjacents.values());

		adjacents.removeIf(tile -> tile.isEmpty());

		return adjacents;
	}

	public void setAdjacents(Map<TileDirection, Tile> adjacents)
	{
		this.adjacents = adjacents;
	}

	@Override
	public void tickUpdate() throws SlickException
	{
		this.behaviour.tickUpdate();
	}

	@Override
	public void pressed()
	{
		this.behaviour.pressed();
	}

	@Override
	public void mouseLeft()
	{
		this.behaviour.mouseLeft();
	}

	public static float getXValueFrom(int column, int row)
	{
		if((row % 2) == 0)
		{
			return column * Tile.TILE_WIDTH;
		}
		else
		{
			return (column * Tile.TILE_WIDTH) + (Tile.TILE_WIDTH / 2);
		}
	}

	private static float getYRandomValueFrom(int row)
	{
		return Tile.getYValueFrom(row) + Tile.getRandomYOrigin();
	}

	public static float getYValueFrom(int row)
	{
		if(row != 0)
		{
			return ((row * Tile.TILE_HEIGHT) - (Tile.FLOATING_OFFSET * row)) + Tile.FLOATING_OFFSET;
		}
		else
		{
			return Tile.FLOATING_OFFSET;
		}
	}

	private static float getRandomYOrigin()
	{
		return ((GameController.getInstance().getRandom().nextBoolean() ? -1 : 1) * AVERAGE_TRANSITION_ORIGIN) + Tile.getRandomYOffset();
	}

	private static int getRandomYOffset()
	{
		return GameController.getInstance().getRandom().nextInt(TRANSITION_OFFFSET) - (TRANSITION_OFFFSET / 2);
	}

	private static int getZValueFrom(int row)
	{
		return row * Tile.Z_SPACE_BETWEEN_TILES;
	}

	public int getSpriteVariant()
	{
		return this.spriteVariant;
	}

	public void setSpriteVariant(int spriteVariant)
	{
		this.spriteVariant = spriteVariant;
	}

	public void informPresence(Entity entity)
	{
		this.isOccupied = true;
	}

	public void informDeparture(Entity entity)
	{
		this.isOccupied = false;
	}

	public boolean isOccupied()
	{
		return this.isOccupied;
	}

	public void book()
	{
		this.isOccupied = true;
	}

	public void unbook()
	{
		this.isOccupied = false;
	}

	@Override
	public void clickEvent()
	{
		this.behaviour.clickEvent();
	}

	@Override
	public boolean isSelected()
	{
		return this.isSelected;
	}

	@Override
	public void selectEvent()
	{
		this.setY(this.getY() - Tile.FLOATING_OFFSET);
		this.behaviour.selectEvent();
	}

	@Override
	public void deselectEvent()
	{
		this.setY(this.getY() + Tile.FLOATING_OFFSET);
		this.behaviour.deselectEvent();
	}

	@Override
	public void setSelected(boolean toSelect)
	{
		this.isSelected = toSelect;
	}

	@Override
	public Image getSprite() throws SlickException
	{
		PixelisedImage sprite = Tile.registry.get(Tile.registry.getImageNameFor(this.biome, this.type, this.spriteVariant));

		if(this.seen)
		{
			return sprite;
		}
		else
		{
			return sprite.madeTransparent();
		}
	}

	public static Tile getEmptyTile() throws SlickException
	{
		if(Tile.EMPTY == null)
		{
			Tile.EMPTY = new Tile() {

				@Override
				public boolean isEmpty()
				{
					return true;
				}
			};
		}

		return Tile.EMPTY;
	}

	public TileDirection directionOf(Tile adjacentTile)
	{
		for(Map.Entry<TileDirection, Tile> entry : this.adjacents.entrySet())
		{
			if(entry.getValue().equals(adjacentTile))
			{
				return entry.getKey();
			}
		}

		return TileDirection.NONE;
	}

	public boolean doTransitionStep()
	{
		if(this.seen)
		{
			float currentY = this.getY();
			float destinationY = Tile.getYValueFrom(this.getRow()) - (this.isSelected ? Tile.FLOATING_OFFSET : 0);

			if(currentY < destinationY)
			{
				this.setY(Math.min(currentY + TRANSITION_STEP_SPEED, destinationY));
			}
			else if(currentY > destinationY)
			{
				this.setY(Math.max(currentY - TRANSITION_STEP_SPEED, destinationY));
			}

			return this.getY() == destinationY;
		}
		else
		{
			return false;
		}
	}

	public boolean isSeen()
	{
		return this.seen;
	}

	public void seen()
	{
		this.seen = true;
		this.setY(Tile.getYRandomValueFrom(this.getRow()));
	}
}
