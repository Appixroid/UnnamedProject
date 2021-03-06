package unnamed.model.element.menu;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import unnamed.model.PixelisedImage;
import unnamed.model.container.ElementContainer;
import unnamed.model.element.Element;

public abstract class MenuElement extends Element
{
	private static final MenuElement EMPTY = new EmptyMenuElement();

	public MenuElement(float x, float y, int z, ElementContainer container)
	{
		super(x, y, z, container);
	}

	public MenuElement(ElementContainer container)
	{
		super(container);
	}

	@Override
	public void pressed()
	{
	}

	@Override
	public void mouseLeft()
	{
	}

	public static MenuElement getEmptyMenuElement()
	{
		return MenuElement.EMPTY;
	}

	private static class EmptyMenuElement extends MenuElement
	{
		public EmptyMenuElement()
		{
			super(ElementContainer.getEmptyContainer());
		}

		@Override
		public Image getSprite() throws SlickException
		{
			return PixelisedImage.getEmptyPixelisedImage();
		}

		@Override
		public void tickUpdate()
		{
		}

		@Override
		public void pressed()
		{
		}

		@Override
		public void mouseLeft()
		{
		}

		@Override
		public boolean isEmpty()
		{
			return true;
		}

		@Override
		public void clickEvent()
		{

		}
	}
}
