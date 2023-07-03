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
			p.drawPanel();
		}
		pa.popMatrix();
		pa.popStyle();
	}
	
	
	public void evaluateMousePosition( int mXc, int mYc ) {
		for( Panel p : panels ) {
			p.mouseOver(mXc, mYc);
		}
	}
	
	public PVector getDisplayOffset()
	{
		return new PVector(0,-160);
	}
	
	
	public void ApplyDeviceFromSlotUnderCursor(Device device) {
		
		pa.println("applying device: " + device.deviceName);
		
		// find the panel and slot that the mouse is over
		for( Panel p : panels) {
			if( p.isMouseOver ) {
				
				// iterate over the two dimensional slots array
				for( int i = 0; i < p.slots.length; i++ ) {
					for( int j = 0; j < p.slots[i].length; j++ ) {
						
						// check if the mouse is over the slot
						if( p.slots[i][j].isMouseOver ) {
							
							Slot slot = p.slots[i][j];
							
							// check if the slot is empty
							if( slot.device == null ) {
								
								// check if the device dimensions exceed the slot dimensions if device is to be fit at i j
								if( device.rows + i > p.slots.length || device.cols + j > p.slots[i].length ) {
									pa.println("device dimensions exceed slot dimensions at this position: " + i + " " + j + "");
									return;
								}
								
								// check if the slots that would be used up by the device are empty
								for( int k = 0; k < device.rows; k++ ) {
									for( int l = 0; l < device.cols; l++ ) {
										if( p.slots[i+k][j+l].device != null ) {
											pa.println("device cannot be placed here, because the slots are not empty");
											return;
										}
									}
								}
								
								// assign the device to the slot
								//slot.device = device;
								p.addDeviceAt(device, i, j);
							}
							/*else {
								p.removeDeviceAt(slot.device,i, j);
								//slot.device = null; // remove exitsting device from the slot
							}*/
						}
					}
				}
			}
		}
	}

	public void RemoveDeviceFromSlotUnderCursor() {

		// find the panel and slot that the mouse is over
		for( Panel p : panels) {
			if( p.isMouseOver ) {

				// iterate over the two dimensional slots array
				for( int i = 0; i < p.slots.length; i++ ) {
					for( int j = 0; j < p.slots[i].length; j++ ) {

						// check if the mouse is over the slot
						if( p.slots[i][j].isMouseOver ) {

							Slot slot = p.slots[i][j];

							if( slot.device != null ) {
								p.removeDevice(slot.device);
							}
						}
					}
				}
			}
		}
	}
}


