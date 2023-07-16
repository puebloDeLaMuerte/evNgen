import processing.core.*;

import java.util.ArrayList;

public class Cockpit {
	
	EVNgen pa;
	private PShape cgfx;
	
	private ArrayList<Panel> panels = new ArrayList<Panel>(); // all panels that this cockpit has
	private Device[] devices; // all devices that are loaded to the cockpit
	
	// get the first device from evData and assign it to
	//public void
	
	
	PVector anchor;
	PShape anchorObj;
	private boolean viewFront = true;


	//PImage tex = loadImage("tx/carbon-fiber.jpg");
	//textureMode(NORMAL);
	
	public Cockpit(EVNgen pApplet, Ship ship) {
		
		pa = pApplet;
		
		cgfx = pa.loadShape("data/shuttlecraft_cpt_bw_p_txt_plain.svg");
		
		PShape centerShape = cgfx.getChild("center");
		PVector centerCoordinates = Utils.centerCoordinates(centerShape);
		// println("CENTER COORDINATES OF COCKPIT SVG: " + centerCoordinates); // used for calculating panel-on-screen-positions
		
		
		pa.println("loading cockpit panels from svg");
		
		for( PShape s : cgfx.getChildren() ) { // parse panels from cockpit-svg
			if( s.getName().startsWith("panel") ) {
				panels.add(new Panel(pa, s, centerCoordinates));
			}
		}
		for(int i = cgfx.getChildCount()-1; i > 0 ; i-- ) { // remove the PShape-svgChildren from cockpit svgPshape, to avoid drawing them double (and off screen)
			if( cgfx.getChild(i).getName().startsWith("panel") ) {
				cgfx.removeChild(i);
			}
		}
		
		anchorObj = cgfx.findChild("anchor");
		//anchorObj.setVisible(false);
		anchor = anchorObj.getVertex(0);
		
		pa.println("anchor: " + anchorObj + " child count: " + anchorObj.getChildCount() + " vertexCount: " + anchorObj.getVertexCount());
		for( int i = 0; i < anchorObj.getVertexCount(); i++ ){
			PVector v = anchorObj.getVertex(i);
			pa.println("anchorVertex: x " + v.x + " y " + v.y);
		}
		pa.println("cockpit-graphics size: " + cgfx.width + "/" + cgfx.height);
		
		
	}
	
	
	
	
	
	public void drawCockpit() {
		
		pa.pushStyle();
		pa.pushMatrix();
		pa.shapeMode(pa.CENTER);
		//anchorObj.setVisible( !anchorObj.isVisible() );
		//scale(0.2);
		cgfx.disableStyle();
		pa.fill(0);
		//cockpit.setTexture(tex);
		pa.stroke(120);
		pa.strokeWeight(0.3f);
		pa.shape(cgfx, 0, 0);
		
		pa.popMatrix();
		pa.popStyle();
		
		pa.pushStyle();
		pa.pushMatrix();
		pa.shapeMode(pa.CENTER);
		//translate(-width/2,-height/2);
		//translate(-960,-540);
		
		/*
		// draw crosshair at cockpit center
		pa.pushStyle();
		pa.stroke(255,0,0);
		pa.line(-10,0,10,0);
		pa.line(0,-10,0,10);
		pa.popStyle();
		*/
		
		for( Panel p : panels ) {
			p.drawPanel(viewFront);
		}
		pa.popMatrix();
		pa.popStyle();
	}
	
	
	public void evaluateMousePosition( int mXc, int mYc ) {
		for( Panel p : panels ) {
			p.mouseOver(mXc, mYc);
		}
	}


	public boolean mousePressed() {
		for( Panel p : panels ) {
			if( p.mousePressed() ) return true;
		}
		return false;
	}
	
	public PVector getDisplayOffset()
	{
		return new PVector(0,-160);
	}
	
	
	public void ApplyDeviceFromSlotUnderCursor(Device device) {
		
		pa.println("applying device: " + device.deviceName);
		
		// find the panel and slot that the mouse is over
		Panel p = getPanelUnderCursor();
		Slot slot = getSlotUnderCursor();

		if( p == null || slot == null ) {
			pa.println("no panel or slot under cursor");
			return;
		}

		// check if the slot is empty
		if( slot.device != null) {
			return;
		}

		if( !p.doesDeviceFit(device, slot.rowIndex, slot.colIndex) ) {
			pa.println("device dimensions exceed slot dimensions or slot is full at this position: " + slot.rowIndex + " " + slot.colIndex + "");
			return;
		}

		// assign the device to the slot

		p.addDeviceAt(device, slot.rowIndex, slot.colIndex);

	}

	public void RemoveDeviceFromSlotUnderCursor() {

		Slot slot = getSlotUnderCursor();
		if( slot == null ) return;

		if( slot.device != null ) {
			slot.panel.removeDevice(slot.device);
		}
	}


	public boolean toggleFrontBackView() {
		viewFront = !viewFront;
		return viewFront;
	}

	private Panel getPanelUnderCursor() {
		for( Panel p : panels ) {
			if( p.isMouseOver ) {
				return p;
			}
		}
		return null;
	}

	private Slot getSlotUnderCursor() {

		Panel p = getPanelUnderCursor();
		if( p == null ) {
			return null;
		}

		for( int i = 0; i < p.slots.length; i++ ) {
			for( int j = 0; j < p.slots[i].length; j++ ) {
				if( p.slots[i][j].isMouseOver ) {
					return p.slots[i][j];
				}
			}
		}

		return null;
	}
}


