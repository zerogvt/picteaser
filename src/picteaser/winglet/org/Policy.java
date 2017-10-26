package picteaser.winglet.org;

import org.eclipse.swt.graphics.Point;

public class Policy {
	public enum FocusPolicy { SCALE, NO_SCALE, CRAZY_SCALE };

	public static POV getNewPOV(POV currPOV, Policy.FocusPolicy fp ) {
		switch (fp) {
		case SCALE: return computeSCALE(currPOV);
		case NO_SCALE: return computeNO_SCALE(currPOV);
		case CRAZY_SCALE: return computeCRAZY_SCALE(currPOV);
		default: return currPOV;
		}
		
	}
	
	private static POV computeSCALE(POV inPOV) {
		POV res = new POV(inPOV);
		float scale = 0;
		if ( res.height > res.screenheight ) {
			scale = 100 * res.screenheight / res.height ;   
		}
		if (res.width > res.screenwidth) {
			float scale2 = 100 * res.screenwidth / res.width;
			if (scale2 < scale) scale = scale2;
		}
		if ((int) scale != 0) {
			res.height = (int)(((double)scale/100.0) * res.height);
			res.width = (int)(((double)scale/100.0) * res.width); 
		}
		res.origin.x = (int)(res.screenwidth - res.width)/2;
		res.origin.y = (int)(res.screenheight - res.height)/2;

		return res;
	}
	
	private static POV computeNO_SCALE(POV inPOV) {
		POV res = new POV(inPOV);
		if ( res.height > res.screenheight ) {
			res.origin.y = - Utils.getRandomMinMax(0, res.height - res.screenheight);
		}else {
			res.origin.y = (int)(res.screenheight - res.height)/2;
		}
		if ( res.width > res.screenwidth ) {
			res.origin.x = - Utils.getRandomMinMax(0, res.width - res.screenwidth);
		}else {
			res.origin.x = (int)(res.screenwidth - res.width)/2;
		}
		return res;
	}
	
	private static POV computeCRAZY_SCALE(POV inPOV) {
		POV res = new POV(inPOV);
		int cscale = Utils.getRandomMinMax(0,300);
		res.height = (int)( res.height + res.height * cscale/100);
		res.width = (int) ( res.width + res.width * cscale/100);
		if ( res.height > res.screenheight ) {
			res.origin.y = - Utils.getRandomMinMax(0, res.height - res.screenheight);
		}else {
			res.origin.y = (int)(res.screenheight - res.height)/2;
		}
		if ( res.width > res.screenwidth ) {
			res.origin.x = - Utils.getRandomMinMax(0, res.width - res.screenwidth);
		} else {
			res.origin.x = (int)(res.screenwidth - res.width)/2;
		}
		return res;
	}
	
	
	public static POV computeNewPOV(POV inPOV, int scale, int zoompointPercentageX, int zoompointPercentageY) {
		POV res = new POV(inPOV);
		if (zoompointPercentageX<0 || zoompointPercentageX>100)
			zoompointPercentageX=0;
		if (zoompointPercentageY<0 || zoompointPercentageY>100)
			zoompointPercentageY=0;
		
		res.height = (int)( res.height + res.height * scale/100);
		res.width = (int) ( res.width + res.width * scale/100);
		if (res.height > inPOV.screenheight)
			res.origin.y =  - (res.height-inPOV.screenheight) * zoompointPercentageY/100;
		else
			res.origin.y = (int)(inPOV.screenheight - res.height) /2;
			
		if (res.width > inPOV.screenheight)
			res.origin.x =  - (res.width-inPOV.screenwidth) * zoompointPercentageX/100;
		else
			res.origin.x = (int)(inPOV.screenwidth - res.width) /2;
		/*
		if ( res.height > res.screenheight ) {
			if (zoompoint.y >=0  && zoompoint.y<=res.height - res.screenheight)
			res.origin.y = - PicTeaser.getRandomMinMax(0, res.height - res.screenheight);
		}else {
			res.origin.y = (int)(res.screenheight - res.height)/2;
		}
		if ( res.width > res.screenwidth ) {
			res.origin.x = - PicTeaser.getRandomMinMax(0, res.width - res.screenwidth);
		} else {
			res.origin.x = (int)(res.screenwidth - res.width)/2;
		}
		
		
		
		if ( res.height > res.screenheight ) {
			res.origin.y = - PicTeaser.getRandomMinMax(0, res.height - res.screenheight);
		}else {
			res.origin.y = (int)(res.screenheight - res.height)/2;
		}
		if ( res.width > res.screenwidth ) {
			res.origin.x = - PicTeaser.getRandomMinMax(0, res.width - res.screenwidth);
		} else {
			res.origin.x = (int)(res.screenwidth - res.width)/2;
		}
		*/
		return res;
	}
}
