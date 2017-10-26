package picteaser.winglet.org;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;


public class PicTeaser {
	static int nextimg = 0;
	static int times[];
	static Policy.FocusPolicy policy[];
	static String imgfile=null;
	private static Object o = new Object();
	static Point zoompoint = new Point(0,0);
	
	public static String setText(){
		return Integer.toString(o.hashCode());
	}
	
	
	public static void createScenario(ArrayList<String> imgs) {
		//shuffle images
		for (int i=0; i<imgs.size(); i++) {
			int j = Utils.getRandomMinMax(i,imgs.size()-1);
			String tmp = imgs.get(j);
			imgs.set(j, imgs.get(i));
			imgs.set(i, tmp);
			times[i] = Utils.getRandomMinMax(1000, 1000);
			policy[i] = Policy.FocusPolicy.values()[new Random().nextInt(Policy.FocusPolicy.values().length)];
		}
	}
	
	private static Image originalImage;
	

	
	public static void main (String [] args) {
		final Display display = new Display ();
		final Shell shell = new Shell (display);
		Monitor mon = Display.getDefault().getMonitors()[0]; // returns an array of monitors attached to device and 0 fetches first one.
		Rectangle screenrec = mon.getBounds();
		final int screen_w = screenrec.width;
		final int screen_h = screenrec.height;
		shell.setLayout(new FillLayout());
		final Canvas canvas = new Canvas (shell, SWT.NO_BACKGROUND | SWT.NONE);

		DirectoryDialog dialog = new DirectoryDialog (shell, SWT.OPEN);
		dialog.setText ("Select a directory");
		//String string = dialog.open ();
		String string = "C:\\sample_pics";
		File seldir = new File(string);
		final ArrayList<String> imagefiles = new ArrayList<String>();
		for (String fname : seldir.list() ) {
			if (Utils.isImage(fname)) {
				imagefiles.add(string + File.separator + fname);
			}
		}
		
		Scenario scenario1 = new Scenario(imagefiles, display, shell, canvas, false);
		scenario1.run();
		/*
		final int NUMIMGS = imagefiles.size();
		times = new int[NUMIMGS];
		policy = new Policy.FocusPolicy[NUMIMGS];
		createScenario(imagefiles);
		nextimg=0;
		display.timerExec(1000, new Runnable() {
			int rep=0;
			final int TOTAL_REPETITIONS = 200;
			int scale = rep -TOTAL_REPETITIONS/2;
			String nextimgpath;
		    public void run() {
		    	//Policy.FocusPolicy random_policy = Policy.FocusPolicy.values()[new Random().nextInt(Policy.FocusPolicy.values().length)];
		    	Policy.FocusPolicy random_policy=Policy.FocusPolicy.SCALE;
		    	//displayImage(imagefiles, shell, display, canvas, screen_h, screen_w, random_policy);
		    	if (rep==0 || rep%TOTAL_REPETITIONS==0) {
		    		nextimgpath=imagefiles.get(nextimg%NUMIMGS);
		    		if (originalImage!=null)
		    			originalImage.dispose();
		    		originalImage = Utils.loadImage(display, nextimgpath);
		    		zoompoint.x = Utils.getRandomMinMax(0, originalImage.getBounds().width-1);
		    		zoompoint.y = Utils.getRandomMinMax(0, originalImage.getBounds().height-1);
		    		nextimg++;
		    	}
		    	scale = (rep%TOTAL_REPETITIONS) - (TOTAL_REPETITIONS/2);
		    	rep++;
		    	
		    	Utils.displayImage(originalImage, shell, display, canvas, screen_h, screen_w, random_policy, 
		    			scale, zoompoint);
		    	canvas.redraw();
		    	int secstonext = Utils.getRandomMinMax(10, 10);
		    	System.out.println("next in " + secstonext );
		    	
		    	display.timerExec(secstonext, this);
		      }
		});
		*/
		shell.setMaximized(true);
		shell.setFullScreen(true);
		shell.open ();
		
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		
	}
	
	
	

}
