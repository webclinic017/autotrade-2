package service;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import entity.Zone;
import systemenum.SystemEnum;

public class ZoneColorInfoService {

private volatile static ZoneColorInfoService instance; 
	
	private Hashtable<String,Zone> sceZoneColors; //only working zone 
	private Hashtable<String,Zone> volZoneColors; //only working zone 
	
    private ZoneColorInfoService ()  {
    	
    	this.sceZoneColors = new Hashtable<String,Zone>();
    	this.volZoneColors = new Hashtable<String,Zone>();
    } 
    
	public static ZoneColorInfoService getInstance() {  
    	if (instance == null) {  
    		synchronized (ZoneColorInfoService.class) {  
    			if (instance == null) {  
    				instance = new ZoneColorInfoService();  
    			}	  
    		}  
    	}  
    	return instance;  
    }

	public void reloadSceZoneColorsByNewZoneListWithDefaultColor(ArrayList<Zone> zoneList) {
		
		if (!getSceZoneColors().isEmpty()) {
			getSceZoneColors().clear();
		}
		
		for(Zone zone : zoneList) {
			getSceZoneColors().put(zone.getZone(),zone);
		}
	}
	
	public void reloadVolZoneColorsByNewZoneListWithDefaultColor(ArrayList<Zone> zoneList) {
		
		if (!getVolZoneColors().isEmpty()) {
			getVolZoneColors().clear();
		}
		
		for(Zone zone : zoneList) {
			getVolZoneColors().put(zone.getZone(),zone);
		}
	}
	
	public void updateZoneColorByTimer() {

		if (getSceZoneColors().isEmpty() && getVolZoneColors().isEmpty()) {
			return;
		}
		
        try {
        	
			Robot rb = null;
			rb = new Robot();
			Toolkit tk = Toolkit.getDefaultToolkit();
			Dimension di = tk.getScreenSize();
			Rectangle rec = new Rectangle(0, 0, di.width, di.height);
			BufferedImage bi = rb.createScreenCapture(rec);

			int[] rgb = new int[3];
			
			Enumeration<Zone> e = getSceZoneColors().elements();
			while( e. hasMoreElements() ){
				
				Zone zone = e.nextElement();
				
				int pixel = bi.getRGB(zone.getxCoord(), zone.getyCoord());
				rgb[0] = (pixel & 0xff0000) >> 16;
				rgb[1] = (pixel & 0xff00) >> 8;
				rgb[2] = (pixel & 0xff);
				
				zone.setColor(tool.Util.getColorEnumByColorRGB(rgb[0], rgb[1], rgb[2]));
			}
			
			e = getVolZoneColors().elements();
			while( e. hasMoreElements() ){
				
				Zone zone = e.nextElement();
				
				int pixel = bi.getRGB(zone.getxCoord(), zone.getyCoord());
				rgb[0] = (pixel & 0xff0000) >> 16;
				rgb[1] = (pixel & 0xff00) >> 8;
				rgb[2] = (pixel & 0xff);
				
				zone.setColor(tool.Util.getColorEnumByColorRGB(rgb[0], rgb[1], rgb[2]));
			}
			
			return;
			
		} catch (AWTException e) {
			e.printStackTrace();
		}
        
	}
	
	public Enum<SystemEnum.Color> getColorBySceZone(String zone) {
		
		Zone z = getSceZoneColors().get(zone);
		
		return z.getColor();
	}
	
	public Enum<SystemEnum.Color> getColorByVolZone(String zone) {
		
		Zone z = getVolZoneColors().get(zone);
		
		return z.getColor();
	}

	public Hashtable<String,Zone> getSceZoneColors() {
		return sceZoneColors;
	}

	public void setSceZoneColors(Hashtable<String,Zone> sceZoneColors) {
		this.sceZoneColors = sceZoneColors;
	}

	public Hashtable<String,Zone> getVolZoneColors() {
		return volZoneColors;
	}

	public void setVolZoneColors(Hashtable<String,Zone> volZoneColors) {
		this.volZoneColors = volZoneColors;
	}

	
}