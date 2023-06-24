import processing.core.*;

import java.io.File;

public class Device {
	EVNgen pa;
	
	String deviceName; // name of the device from deviceDataFile
	PShape dgfx;
	String manufacturer; // manufacturer of the device from deviceDataFile
	int rows, cols; // number of rows and cols in a Cockpit.Panel that this device spans over. from deviceDataFile
	int price; // price of the device from deviceDataFile
	int mass; // mass of the device from deviceDataFile
	
	private Panel panel; // the panel that this device is assigned to
	private Slot[] slots; // the slots in that panel that the device spans over
	
	private PVector topLeft;
	private PVector bottomRight;


	/// copy constructor
	public Device(Device d) {

		pa = d.pa;

		deviceName = d.deviceName;
		dgfx = d.dgfx;
		manufacturer = d.manufacturer;
		rows = d.rows;
		cols = d.cols;
		price = d.price;
		mass = d.mass;
	}


	public Device(EVNgen pApplet, File devDirectory) {
		
		pa = pApplet;
		
		deviceName = devDirectory.getName();
		pa.print("DeviceName: " + deviceName);
		String s = devDirectory.getAbsolutePath()+"/"+deviceName+".svg";
		pa.println(" at " + s);
		dgfx = pa.loadShape( s );
		loadDeviceData( devDirectory.getAbsolutePath() );
	}
	
	// load device data from json file
	public void loadDeviceData( String deviceDirectory ) {
	
		String[] lines = pa.loadStrings( deviceDirectory + "/deviceData.json" );
		
		for( String l : lines ) {
			
			l = l.trim();
			l = l.replace(",", "");
			
			if( l.startsWith("\"manufacturer") ) {
				manufacturer = l.split(":")[1].trim();
			}
			if( l.startsWith("\"rows") ) {
				rows = Integer.parseInt( l.split(":")[1].trim() );
			}
			if( l.startsWith("\"cols") ) {
				cols = Integer.parseInt( l.split(":")[1].trim() );
			}
			if( l.startsWith("\"price") ) {
				price = Integer.parseInt( l.split(":")[1].trim() );
			}
			if( l.startsWith("\"mass") ) {
				mass = Integer.parseInt( l.split(":")[1].trim() );
			}
		}
	}
	
	
	
	public void drawDevice() {
		
		pa.pushMatrix();
		pa.pushStyle();;
		
		pa.shapeMode(pa.CORNERS);
		pa.shape( dgfx, topLeft.x, topLeft.y, bottomRight.x, bottomRight.y);
		
		pa.popStyle();
		pa.popMatrix();
	}
	
	
	public void setPanel( Panel p ) {
		panel = p;
	}
	
	
	public void unsetPanel() {
		slots = null;
		panel = null;
		topLeft = null;
		bottomRight = null;
	}
	
	// copy slots[] to new array of size+1 and add it to the slots[]
	public void addSlot( Slot s ) {
		
		if( slots == null ) slots = new Slot[0];
		
		Slot[] tempSlots = new Slot[slots.length+1];
		
		for( int i = 0; i < slots.length; i++ ) {
			tempSlots[i] = slots[i];
		}
		
		tempSlots[slots.length] = s;
		
		slots = tempSlots;
		findTopLeftBottomRight();
	}
	
	
	
	// iterate over all slots and find leftmost, rightmost, topmost and bottommost
	public void findTopLeftBottomRight() {
		
		float leftmost = Float.MAX_VALUE;
		float rightmost = -Float.MAX_VALUE;
		float topmost = Float.MAX_VALUE;
		float bottommost = -Float.MAX_VALUE;
		
		for( Slot s : slots ) {
			
			if( s.slotTopLeft.x < leftmost ) leftmost = s.slotTopLeft.x;
			if( s.slotBottomRight.x > rightmost ) rightmost = s.slotBottomRight.x;
			if( s.slotTopLeft.y < topmost ) topmost = s.slotTopLeft.y;
			if( s.slotBottomRight.y > bottommost ) bottommost = s.slotBottomRight.y;
		}
		
		topLeft = new PVector(leftmost, topmost);
		bottomRight = new PVector(rightmost, bottommost);
	}
	
}