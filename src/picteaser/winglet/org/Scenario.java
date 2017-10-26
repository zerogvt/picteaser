package picteaser.winglet.org;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

public class Scenario implements Runnable{

	int	nextimg=0;
	int rep=0;
	int TOTAL_REPETITIONS = 2;
	int scale = rep -TOTAL_REPETITIONS/2;
	String nextimgpath;
	final ArrayList<String> imagefiles;
	final int NUMIMGS;
	private static Image originalImage;
	private Display display;
	private Shell shell;
	private Canvas canvas;
	final int screen_w;
	final int screen_h;
	boolean repeat=false;
	
	public Scenario(ArrayList<String> images, Display disp, Shell sh, Canvas can, boolean forever){
		imagefiles = images; 
		repeat = forever;
		NUMIMGS = images.size();
		display = disp;
		shell = sh; //new Shell (display);
		canvas = can; //new Canvas (shell, SWT.NO_BACKGROUND | SWT.NONE);
		Monitor mon = Display.getDefault().getMonitors()[0]; // returns an array of monitors attached to device and 0 fetches first one.
		Rectangle screenrec = mon.getBounds();
		screen_w = screenrec.width;
		screen_h = screenrec.height;
	}
	
	
	
	@Override
    public void run() {
    	//playOne();
		playTwo();
    }
	
	
	
	private void playOne(){
		int newx=0, newy=0;
		//Policy.FocusPolicy random_policy = Policy.FocusPolicy.values()[new Random().nextInt(Policy.FocusPolicy.values().length)];
    	Policy.FocusPolicy random_policy=Policy.FocusPolicy.SCALE;
    	//displayImage(imagefiles, shell, display, canvas, screen_h, screen_w, random_policy);
    	if (rep==0 || rep%TOTAL_REPETITIONS==0) {
    		if (!repeat) {
    			if (nextimg==NUMIMGS) {
    				shell.dispose();
    				return;
    			}
    		}
    		nextimgpath=imagefiles.get(nextimg%NUMIMGS);
    		if (originalImage!=null)
    			originalImage.dispose();
    		originalImage = Utils.loadImage(display, nextimgpath);
    		newx = Utils.getRandomMinMax(0, 100);
    		newy = Utils.getRandomMinMax(0, 100);
    		nextimg++;
    	}
    	scale = (rep%TOTAL_REPETITIONS) - (TOTAL_REPETITIONS/2);
    	rep++;
    	
    	Utils.displayImage(originalImage, shell, display, canvas, screen_h, screen_w, random_policy, 
    			scale, newx, newy);
    	canvas.redraw();
    	int secstonext = Utils.getRandomMinMax(1000, 1000);
    	System.out.println("next in " + secstonext );
    	
    	display.timerExec(secstonext, this);
	}

	
	private void playTwo(){
		List<Integer>  scalearray = new ArrayList<Integer>(); 
		for (int i=0; i<TOTAL_REPETITIONS; i++){
			scalearray.add(100-(int)((i+1)*100/TOTAL_REPETITIONS));
		}
		int newx=0, newy=0;
		//Policy.FocusPolicy random_policy = Policy.FocusPolicy.values()[new Random().nextInt(Policy.FocusPolicy.values().length)];
    	Policy.FocusPolicy random_policy=Policy.FocusPolicy.SCALE;
    	//displayImage(imagefiles, shell, display, canvas, screen_h, screen_w, random_policy);
		int w=0,h=0,nw=0,nh=0;
    	if (rep==0 || rep%TOTAL_REPETITIONS==0) {
    		if (!repeat) {
    			if (nextimg==NUMIMGS) {
    				shell.dispose();
    				return;
    			}
    		}
    		nextimgpath=imagefiles.get(nextimg%NUMIMGS);
    		
    		if (originalImage!=null)
    			originalImage.dispose();
    		originalImage = Utils.loadImage(display, nextimgpath);
    		
    		//0% is at 0 point on x,y axis - 100% at max x,y
    		//with 0 set at the minimum that ensures filling all the screen
    		newx = Utils.getRandomMinMax(0,100);
    		newy = Utils.getRandomMinMax(0,100);
    		nextimg++;
    		System.out.println(scale + " " +w + " " + h + " " + nw + " "+ nh + " "+ screen_w + " "+ screen_h  + " z:" + newx + " " + newy);
    	}
    	
    	scale = scalearray.get(rep%TOTAL_REPETITIONS);
    	rep++;
    	
    	Utils.displayImage(originalImage, shell, display, canvas, screen_h, screen_w, random_policy, 
    			scale, newx, newy);
    	canvas.redraw();
    	
    	int msecstonext = 1000; //Utils.getRandomMinMax(1000, 1000);
    	System.out.println("next in " + msecstonext );
    	
    	display.timerExec(msecstonext, this);
	}
}
