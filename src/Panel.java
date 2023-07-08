import processing.core.PShape;
import processing.core.PVector;

public class Panel {
	
	EVNgen pa;
	Slot[][] slots;
	String panelName;
	
	PVector panelTopLeft; // top left corner of the panel in the svg
	PVector panelBottomRight; // bottom right corner of the panel in the svg
	
	private Device[] devices;
	
	boolean isMouseOver = false;
	
	public Panel(EVNgen pApplet, PShape panelShape, PVector svgCenterCoords) {
		
		pa = pApplet;
		
		String panelDataString = panelShape.getName();
		String[] panelDataStrings = panelDataString.split("_");
		panelName = panelDataStrings[0];
		int nrOfRows = 0;
		int nrOfColumns = 0;
		int columnCounter = 0;
		
		// parse the data-string gotten from the name of the svg-panel-group to get the dimensions of the panel (rows and columns of slots in this panel)
		// in the svg the title of the group that holds the slots must conform to this:
		// - data-Items separated by underscore.
		// - rows will be counted from top to bottom
		// - columns will be counted from left to right
		// - the start of each row is indicated by "_r_" in the svg-id-string
		// - after each "_r_" there will be numbers "_1_2_n_" which corsepond to the named panel-svg children.
		//   a row that consists of the two svg-elements "slot1" and "slot344" will be indicated by the string "r_1_344" in the element-ID
		// - all rows must have the same number of slots in them.
		for (String s : panelDataStrings) {
			//println("panelData: " + s);
			if (s.equals("r")) {
				if (nrOfRows == 1) { // if it's the first row completed and we now look at the second one
					nrOfColumns = columnCounter;
				}
				if (nrOfRows > 1) { // if we look at the second or following rows, compare the previous column-size and warn if it's not matching the previously recorded column-size
					if (columnCounter != nrOfColumns) {
						pa.println("error in panelData: inconsistent number of columns per row! string: " + panelDataString);
					}
				}
				nrOfRows++;  // count the rows in this panel
				columnCounter = 0;
			} else {
				try {
					pa.parseInt(s);
					columnCounter++; // if after a row comes a valid number, increase the columncounter
				} catch (Exception e) {
					pa.println("couldnt parse int from string: " + s);
				}
			}
		}
		nrOfColumns = columnCounter;
		
		pa.println("panel parsed: " + panelName);
		pa.println("rows: " + nrOfRows);
		pa.println("cols: " + nrOfColumns);
		
		slots = new Slot[nrOfRows][nrOfColumns];
		
		int rI = 0; //row index for populating the array
		int cI = 0; //column index
		
		for (int i = 2; i < panelDataStrings.length; i++) {
			String s = panelDataStrings[i];
			
			if (s.equals("r")) { // if a new row starts, adjust the counters
				rI++;
				cI = 0;
			} else { // if no new row starts, fill the array with corresponding shape data
				String slotName = "slot" + s;
				PShape shape = panelShape.getChild(slotName);
				slots[rI][cI] = new Slot(pa, shape, rI, cI, slotName, this, svgCenterCoords);
				cI++;
			}
		}
		
		findPanelCorners();
	}
	
	
	// find panel top-left and bottom-right coordinates in the svg by looking at the slots
	public void findPanelCorners() {
		
		float minX = Float.MAX_VALUE;
		float minY = Float.MAX_VALUE;
		float maxX = -Float.MAX_VALUE;
		float maxY = -Float.MAX_VALUE;
		
		for (Slot[] sarr : slots) {
			for (Slot slot : sarr) {
				if (slot.slotTopLeft.x < minX) {
					minX = slot.slotTopLeft.x;
				}
				if (slot.slotTopLeft.y < minY) {
					minY = slot.slotTopLeft.y;
				}
				if (slot.slotBottomRight.x > maxX) {
					maxX = slot.slotBottomRight.x;
				}
				if (slot.slotBottomRight.y > maxY) {
					maxY = slot.slotBottomRight.y;
				}
			}
		}
		
		panelTopLeft = new PVector(minX, minY);
		panelBottomRight = new PVector(maxX, maxY);
	}
	
	
	// determine if the given coordinates are inside the panel
	public boolean mouseOver(int x, int y) {
		
		boolean nextIsMouseOver = determineMouseOver(x, y);
		
		if(nextIsMouseOver || isMouseOver) {
			for (Slot[] sarr : slots) {
				for (Slot slot : sarr) {
					slot.mouseOver(x, y);
				}
			}
		}
		
		isMouseOver = nextIsMouseOver;
		return isMouseOver;
	}
	
	
	private boolean determineMouseOver( int x, int y) {
		
		if (x > panelTopLeft.x-1 && x < panelBottomRight.x+1 && y > panelTopLeft.y-1 && y < panelBottomRight.y+1) {
			return true;
		} else {
			return false;
		}
	}
	
	
	// does the given device fit in this slot?
	public boolean doesDeviceFit(Device device, int r, int c) {
		
		int rows = device.rows;
		int cols = device.cols;
		
		if (r + rows > slots.length) {
			return false;
		}
		if (c + cols > slots[0].length) {
			return false;
		}
		
		for (int i = r; i < r + rows; i++) {
			for (int u = c; u < c + cols; u++) {
				if (!slots[i][u].isEmpty()) {
					return false;
				}
			}
		}
		return true;
	}



	// add a device to the panel at row r and column c spanning over the number of rows and columns that the device needs
	public void addDeviceAt(Device device, int r, int c) {

		int rows = device.rows;
		int cols = device.cols;

		if(!doesDeviceFit(device, r, c) ) {
			pa.println("device doesn't fit here!");
			return;
		}

		// set panel and slots for device:
		// also register the device with the slots

		device.setPanel(this);

		for (int i = r; i < r + rows; i++) {
			for (int u = c; u < c + cols; u++) {
				device.addSlot(slots[i][u]);
				slots[i][u].addDevice(device);
			}
		}

		// register Device with this panel:

		if( devices == null ) devices = new Device[0];
		Device[] tmpD = new Device[devices.length+1];
		for( int i = 0; i < devices.length; i++ ) {
			tmpD[i] = devices[i];
		}
		tmpD[devices.length] = device;
		devices = tmpD;
	}


	// remove a device from the panel at row r and column c. remove references to this panel and all slots from device.
	public void removeDevice(Device device) {
		/*
		if(!doesDeviceFit(device, i, j) ) {
			pa.println("device doesn't fit here!");
			return;
		}
		*/

		int[] topLeftSlotIndex = device.getTopLeftSlotIndex();
		if( topLeftSlotIndex == null ) {
			return;
		}

		int i = topLeftSlotIndex[0];
		int j = topLeftSlotIndex[1];

		for (int r = i; r < i + device.rows; r++) {
			for (int c = j; c < j + device.cols; c++) {
				slots[r][c].device = null;
			}
		}

		// remove the device from the list of devices:
		for ( int i2 = 0; i2 < devices.length; i2++ ) {
			if( devices[i2] == device ) {
				devices[i2] = null;
			}
		}

		// copy the content of devices to a new array without the null values:

		if( devices.length == 1 ) {
			devices = null;
		}
		else {

			Device[] tmpD = new Device[devices.length-1];
			int i4 = 0;
			for( int i3 = 0; i3 < devices.length; i3++ ) {
				if( devices[i3] != null ) {
					tmpD[i4] = devices[i3];
					i4++;
				}
			}
			devices = tmpD;
		}

		device.unsetPanel();
	}



	public void drawPanel(boolean drawFront) {

		for( Slot[] sarr : slots ) {
			for( Slot slot : sarr ) {
				slot.drawSlot();
			}
		}

		if( devices != null ) {

			for( Device d : devices ) {
				d.drawDevice(drawFront);
			}
		}

		// debug: use pa.rect() to draw the panel:
		if( isMouseOver ) {
			pa.noFill();
			pa.stroke(255,20);
			pa.rect(panelTopLeft.x, panelTopLeft.y, panelBottomRight.x-panelTopLeft.x, panelBottomRight.y-panelTopLeft.y);
		}


	}

}
