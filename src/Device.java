import processing.core.*;

import java.io.File;
import java.util.ArrayList;

public class Device implements  iBusDevice<BusData>{
	EVNgen pa;
	
	String deviceName; // name of the device from deviceDataFile
	PShape dgfx_front;
	PShape dgfx_back;
	String manufacturer; // manufacturer of the device from deviceDataFile
	int rows, cols; // number of rows and cols in a Cockpit.Panel that this device spans over. from deviceDataFile
	int price; // price of the device from deviceDataFile
	int mass; // mass of the device from deviceDataFile
	
	private Panel panel; // the panel that this device is assigned to
	private Slot[] slots; // the slots in that panel that the device spans over
	
	private PVector topLeft;
	private PVector bottomRight;

	private DeviceInteractiveElement[] interactiveElements;

	/// copy constructor
	@Deprecated // bad idea becaus you always forget stuff, plus, the interacive elements are not copied but referenced.
	public Device(Device d) {

		pa = d.pa;

		deviceName = d.deviceName;
		dgfx_front = d.dgfx_front;
		dgfx_back = d.dgfx_back;
		manufacturer = d.manufacturer;
		rows = d.rows;
		cols = d.cols;
		price = d.price;
		mass = d.mass;

		// create InteractiveElements from Graphics file
		parseDeviceGraphicsChildren(dgfx_front);

		// interactiveElements = d.interactiveElements;
		topLeft = d.topLeft;
		bottomRight = d.bottomRight;
	}


	public Device(EVNgen pApplet, File devDirectory) {
		
		pa = pApplet;
		
		deviceName = devDirectory.getName();
		pa.print("DeviceName: " + deviceName);
		String s = devDirectory.getAbsolutePath()+"/"+deviceName+".svg";
		pa.println(" at " + s);
		loadDeviceData( devDirectory.getAbsolutePath() );

		PShape graphicsShape = pa.loadShape( s );
		dgfx_front = graphicsShape.getChild("front");
		dgfx_back = graphicsShape.getChild("back");

		// create InteractiveElements from Graphics file
		parseDeviceGraphicsChildren(dgfx_front);
	}


	private void parseDeviceGraphicsChildren( PShape graphicsShape ) {
		ArrayList<PShape> interactive_children = new ArrayList<PShape>();

		for(int i = 0; i < graphicsShape.getChildCount(); i++ ) {
			PShape child = graphicsShape.getChild(i);

			// get the x/y coordinate of the PShape child inside the dgfx_front
			PVector[] minMaxXY = Utils.minMaxXY(child, null, null);

			if( child.getName().startsWith("interactive") ) {
				interactive_children.add( child );
				//dgfx_front.removeChild( i );
			}
		}
		interactiveElements = new DeviceInteractiveElement[interactive_children.size()];
		for( int i = 0; i < interactive_children.size(); i++ ) {
			String[] nameParts = interactive_children.get(i).getName().split("_");

			if( nameParts.length > 1 ) {

				if( nameParts[1].equals("toggle") ) {
					interactiveElements[i] = new DeviceToggleableElement( pa, this, interactive_children.get(i), topLeft, bottomRight);
				}
				else {
					interactiveElements[i] = new DeviceInteractiveElement( pa, this, interactive_children.get(i), topLeft, bottomRight);
				}
			}
		}
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
	
	
	
	public void drawDevice( boolean drawFront ) {
		
		pa.pushMatrix();
		pa.pushStyle();
		pa.shapeMode(pa.CORNERS);

		if( drawFront ) {

			/*
			pa.rectMode(pa.CORNERS);
			pa.noFill();
			pa.stroke(200,200,0);
			pa.rect(topLeft.x, topLeft.y, bottomRight.x, bottomRight.y);
			*/

			pa.shape(dgfx_front, topLeft.x, topLeft.y, bottomRight.x, bottomRight.y);

			for( DeviceInteractiveElement die : interactiveElements ) {
				die.toggleStateGraphics();
				//die.setState( (int)pa.random(6) );
			}


		} else {
			pa.shape(dgfx_back, topLeft.x, topLeft.y, bottomRight.x, bottomRight.y);
		}

		pa.popStyle();
		pa.popMatrix();
	}



	public void mouseOver(int x, int y) {
		for( DeviceInteractiveElement elem : interactiveElements ) {

			if( elem.isMouseOver(x,y) ) {
				elem.toggleStateGraphics();
			}
		}
	}


	public boolean mousePressed() {
		if( interactiveElements == null ) return false;

		for( DeviceInteractiveElement elem : interactiveElements ) {
			if( elem.mousePressed() ) {
				return true;
			}
		}
		return false;
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


	// get the row and column index of the top left slot that this device occupies, return two integers [col, row]
	public int[] getTopLeftSlotIndex() {

		if( this.slots == null ) return null;

		int[] topLeftSlotIndex = new int[2];

		topLeftSlotIndex[0] = slots[0].rowIndex;
		topLeftSlotIndex[1] = slots[0].colIndex;

		for( Slot s : slots ) {

			if( s.rowIndex < topLeftSlotIndex[0] ) topLeftSlotIndex[0] = s.rowIndex;
			if( s.colIndex < topLeftSlotIndex[1] ) topLeftSlotIndex[1] = s.colIndex;
		}

		return topLeftSlotIndex;
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

	public PVector getTopLeft() {
		return topLeft;
	}

	public PVector getBottomRight() {
		return bottomRight;
	}

	public void removeFromSlots() {

		slots = null;
	}

	@Override
	public int getIBusAdress() {
		return 0;
	}
}