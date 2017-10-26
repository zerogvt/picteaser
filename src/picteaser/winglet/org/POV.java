package picteaser.winglet.org;

import org.eclipse.swt.graphics.Point;

public class POV {
	public Point origin;
	public int height;
	public int width;
	public int screenheight;
	public int screenwidth;
	
	public POV(int originX, int originY, int height, int width, int screenheight, 
			int screenwidth)  {
		this.origin = new Point(originX, originY);
		this.height = height;
		this.width = width;
		this.screenheight = screenheight;
		this.screenwidth = screenwidth;
	}
	
	public POV(POV pov)  {
		this(pov.origin.x, pov.origin.y, pov.height,
				pov.width, pov.screenheight, pov.screenwidth);
	}
	
	public POV()  {
		this(0, 0, 0, 0, 0, 0);
	}
	/*
	public Point getOrigin() {
		return origin;
	}
	
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setOrigin(Point origin) {
		this.origin = origin;
	}

	public Point setOriginX (int x) {
		origin.x = x;
		return origin;
		
	}
	
	public Point setOriginY (int y) {
		origin.y = y;
		return origin;
	}

	public int getScreenheight() {
		return screenheight;
	}

	public void setScreenheight(int screenheight) {
		this.screenheight = screenheight;
	}

	public int getScreenwidth() {
		return screenwidth;
	}

	public void setScreenwidth(int screenwidth) {
		this.screenwidth = screenwidth;
	}
	
	*/

	
}
