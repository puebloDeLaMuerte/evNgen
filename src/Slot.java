import processing.core.PShape;
import processing.core.PVector;

public class Slot {
	
	EVNgen pa;
	
	PShape sgfx;
	int rowIndex, colIndex;
	String name;
	Panel panel;
	
	Device device;
	
	boolean isMouseOver = false;
	
	PVector slotSize;
	PVector slotHalfSize;
	PVector slotTopLeft; // this slot's top left corner coordinates
	PVector slotBottomRight; // this slot's bottom right corner coordinates
	PVector centerRelativePos; // this slot's position relative to the center of the svg (which is probably always going to be in the center of the screen, too)
	
	public Slot(EVNgen pApplet, PShape gfx, int r, int c, String n, Panel p, PVector svgCenterCoords) {
		
		pa = pApplet;
		
		sgfx = gfx;
		rowIndex = r;
		colIndex = c;
		name = n;
		panel = p;
		pa.println("Slot: " + p.panelName + ".r" + r + ".c" + c + "." + n);
		
		PVector svgCoordinatesOfCenterOfSlot = Utils.centerCoordinates(sgfx);
		centerRelativePos = PVector.sub(svgCoordinatesOfCenterOfSlot, svgCenterCoords);
		
		PVector[] minmax = Utils.minMaxXY(sgfx, null, null );
		
		slotSize = new PVector(minmax[1].x - minmax[0].x, minmax[1].y - minmax[0].y); // calculate width and height of slot
		slotHalfSize = new PVector((minmax[1].x - minmax[0].x) / 2, (minmax[1].y - minmax[0].y) / 2);
		
		slotTopLeft = new PVector(centerRelativePos.x - slotHalfSize.x, centerRelativePos.y - slotHalfSize.y);
		slotBottomRight = new PVector(centerRelativePos.x + slotHalfSize.x, centerRelativePos.y + slotHalfSize.y);
		
		sgfx.translate(-minmax[0].x - slotSize.x / 2, -minmax[0].y - slotSize.y / 2);  // move the svg-child back to (0,0) minus half it's size (because somehow just calling shapeMode(CENTER) before drawing them doesn't work - dunno why, this is just a fix for this. TODO: fund out why it doesn't work to have coherent drawing behaviour all around (this diry fix no good in that respect)
		//sgfx.translate(-minmax[0].x, -minmax[0].y); // should just be like this, but shapeMode(CENTER) doesnt work when drawing somehow:
		
		pa.println("slot-center, center-relative: " + centerRelativePos);
	}
	
	
	public void drawSlot() {
		
		pa.pushStyle();
		pa.pushMatrix();
		
		pa.translate(centerRelativePos.x, centerRelativePos.y);
		
		if( device == null ) {   // if there's no decvice in the slot, draw a generic slot-cover
			sgfx.disableStyle();
			pa.fill(0);
			if (isMouseOver) pa.fill(30);
			//cockpit.setTexture(tex);
			pa.stroke(120);
			pa.strokeWeight(0.3f);
			
			// translate(random(3),random(3)); // this is a fun effect - maybe use it for hyperdrive, or slot-selection...
			pa.shapeMode(pa.CENTER);
			pa.shape(sgfx, 0, 0);
		}
		else {
			
			// apply pa.translate() to device.dgfx here, so that it is drawn at the correct position relative to the slot
		//	pa.translate(slotHalfSize.x * (device.cols-1),slotHalfSize.y * (device.rows-1));
		//	pa.shape(device.dgfx, 0, 0, slotSize.x * device.cols, slotSize.y * device.rows);
		}
    
    /*
    if( isMouseOver ) {
      rectMode(CENTER);
      noFill();
      stroke(0,0,255);
      rect(0,0,slotSize.x,slotSize.y);
    }
    */
		
		pa.popMatrix();
		pa.popStyle();
	}
	
	// determine if the given Coordinates are inside this slot
	public boolean mouseOver(int mXc, int mYc) {
		
		if (mXc > slotTopLeft.x) {
			if (mXc < slotBottomRight.x) {
				if (mYc > slotTopLeft.y) {
					if (mYc < slotBottomRight.y) {
						isMouseOver = true;
						if( device != null) {
							device.mouseOver(mXc, mYc);
						}
						return true;
					}
				}
			}
		}
		isMouseOver = false;
		return false;
	}


	public boolean mousePressed() {
		if( device != null) {
			if( device.mousePressed() ) return true;
		}
		return false;
	}

	/*
	public void removeRevice(Device d ) {
		d.removeFromSlots();
		device = null;
	}
	*/

	public void addDevice(Device device) {
		//device.addSlot(this);
		//device.setPanel(panel);
		this.device = device;

	}
	
	public boolean isEmpty() {
		if (device == null) return true;
		return false;
	}
}
