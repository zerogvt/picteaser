package picteaser.winglet.org;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.imgscalr.Scalr;

public final class Utils {
	
	//private ctor
	private Utils(){
	}
	
	static ImageData convertToSWT(BufferedImage bufferedImage) {
		if (bufferedImage.getColorModel() instanceof DirectColorModel) {
			DirectColorModel colorModel = (DirectColorModel)bufferedImage.getColorModel();
			PaletteData palette = new PaletteData(colorModel.getRedMask(), colorModel.getGreenMask(), colorModel.getBlueMask());
			ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(), colorModel.getPixelSize(), palette);
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					int rgb = bufferedImage.getRGB(x, y);
					int pixel = palette.getPixel(new RGB((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF)); 
					data.setPixel(x, y, pixel);
					if (colorModel.hasAlpha()) {
						data.setAlpha(x, y, (rgb >> 24) & 0xFF);
					}
				}
			}
			return data;		
		} else if (bufferedImage.getColorModel() instanceof IndexColorModel) {
			IndexColorModel colorModel = (IndexColorModel)bufferedImage.getColorModel();
			int size = colorModel.getMapSize();
			byte[] reds = new byte[size];
			byte[] greens = new byte[size];
			byte[] blues = new byte[size];
			colorModel.getReds(reds);
			colorModel.getGreens(greens);
			colorModel.getBlues(blues);
			RGB[] rgbs = new RGB[size];
			for (int i = 0; i < rgbs.length; i++) {
				rgbs[i] = new RGB(reds[i] & 0xFF, greens[i] & 0xFF, blues[i] & 0xFF);
			}
			PaletteData palette = new PaletteData(rgbs);
			ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(), colorModel.getPixelSize(), palette);
			data.transparentPixel = colorModel.getTransparentPixel();
			WritableRaster raster = bufferedImage.getRaster();
			int[] pixelArray = new int[1];
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					raster.getPixel(x, y, pixelArray);
					data.setPixel(x, y, pixelArray[0]);
				}
			}
			return data;
		}
		return null;
	}
	
	static BufferedImage convertToAWT(ImageData data) {
		ColorModel colorModel = null;
		PaletteData palette = data.palette;
		if (palette.isDirect) {
			colorModel = new DirectColorModel(data.depth, palette.redMask, palette.greenMask, palette.blueMask);
			BufferedImage bufferedImage = new BufferedImage(colorModel, colorModel.createCompatibleWritableRaster(data.width, data.height), false, null);
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					int pixel = data.getPixel(x, y);
					RGB rgb = palette.getRGB(pixel);
					bufferedImage.setRGB(x, y,  rgb.red << 16 | rgb.green << 8 | rgb.blue);
				}
			}
			return bufferedImage;
		} else {
			RGB[] rgbs = palette.getRGBs();
			byte[] red = new byte[rgbs.length];
			byte[] green = new byte[rgbs.length];
			byte[] blue = new byte[rgbs.length];
			for (int i = 0; i < rgbs.length; i++) {
				RGB rgb = rgbs[i];
				red[i] = (byte)rgb.red;
				green[i] = (byte)rgb.green;
				blue[i] = (byte)rgb.blue;
			}
			if (data.transparentPixel != -1) {
				colorModel = new IndexColorModel(data.depth, rgbs.length, red, green, blue, data.transparentPixel);
			} else {
				colorModel = new IndexColorModel(data.depth, rgbs.length, red, green, blue);
			}		
			BufferedImage bufferedImage = new BufferedImage(colorModel, colorModel.createCompatibleWritableRaster(data.width, data.height), false, null);
			WritableRaster raster = bufferedImage.getRaster();
			int[] pixelArray = new int[1];
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					int pixel = data.getPixel(x, y);
					pixelArray[0] = pixel;
					raster.setPixel(x, y, pixelArray);
				}
			}
			return bufferedImage;
		}
	}


	public static int getRandomMinMax(int min, int max) {
		return (int) (min + (Math.random() * ((max-min) + 1)));
	}
	
	public static boolean isImage (String filepath) {
		// array of supported extensions (use a List if you prefer)
	    final String[] EXTENSIONS = new String[]{
	        "jpg","jpeg","gif", "png", "bmp" // and other formats you need
	    };
	    for (final String ext : EXTENSIONS) {
            if (filepath.endsWith("." + ext)) {
                return (true);
            }
        }
        return (false);
	}
	
	
	public static Image loadImage(Display display, String imgfile ){
		if (imgfile==null) 
			return null;
		File f = new File(imgfile);
		if ( !f.exists() ) {
			//TODO log warning
			return null;
		}
		return new Image (display, imgfile);
	}
	
	
	public static void displayImage(Image orig, Shell shell, Display display, Canvas canvas, 
			int screenHeight, int screenWidth, Policy.FocusPolicy focusPolicy, int scale, int percX, int percY ) {
		Image dispImage = null;
		System.out.println("policy " + focusPolicy );
		System.out.println("scale " + scale );
		POV currPOV = new POV(0,0, orig.getBounds().height, orig.getBounds().width, 
				screenHeight, screenWidth);
		//POV newPOV = Policy.getNewPOV(currPOV, focusPolicy);
		
		POV newPOV = Policy.computeNewPOV(currPOV, scale, percX, percY);

		BufferedImage awtImg = Scalr.resize(Utils.convertToAWT(orig.getImageData()), Scalr.Method.QUALITY, 
				newPOV.width, newPOV.height);
		dispImage = new Image(display, Utils.convertToSWT(awtImg));
		if (dispImage != null) {
			final Image image = dispImage;
			final Point vieworigin = newPOV.origin;
			final Canvas canvasref = canvas;
			final Display dis = display;
			canvas.addPaintListener ( new PaintListener () {
			public void paintControl (PaintEvent e) {
				Rectangle client = canvasref.getClientArea ();
				e.gc.setBackground(dis.getSystemColor(SWT.COLOR_BLACK));
				e.gc.fillRectangle(0, 0, client.width, client.height );
				e.gc.drawImage (image, vieworigin.x, vieworigin.y);
				System.out.println("drawed image");
				canvasref.removePaintListener(this); //TODO - stupid way must find sth more elegant
			}
			});
		}
	}
	
	
}
