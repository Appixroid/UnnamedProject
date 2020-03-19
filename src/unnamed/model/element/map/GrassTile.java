package unnamed.model.element.map;

import org.newdawn.slick.SlickException;

import unnamed.model.PixelisedImage;
import unnamed.model.container.ElementContainer;

public class GrassTile extends Tile
{
	private static TileImageRegistry grassRegistry;

	public static void init() throws SlickException
	{
		GrassTile.grassRegistry = new TileImageRegistry("grass");
	}

	public GrassTile(int column, int row, TileType type, ElementContainer container)
	{
		super(column, row, type, container);
	}

	@Override
	public PixelisedImage getSprite()
	{
		return GrassTile.grassRegistry.get(GrassTile.grassRegistry.getImageNameFor(this.getType(), this.getSpriteVariant()));
	}
}
