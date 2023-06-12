import processing.core.*;

public class Celestial implements iSysObject{
	
	Main pa;
	private PVector position;
	private float size;
	
	public Celestial( Main pApplet, float x, float y, float size) {
		
		pa = pApplet;
		position = new PVector(x,y);
		this.size = size;
	}
	
	
	public PVector getPosition() {
		return position;
	}
	
	@Override
	public PVector getDisplayOffset() {
		return new PVector(0,0,0);
	}
	
	
	public void drawObject() {
		
		pa.pushStyle();
		pa.rectMode(pa.CENTER);
		pa.fill(0);
		pa.stroke( pa.color( 242, 229, 161) ); // hex: #F2E5A1
		//fill(123,222,11);
		//rect(10,10,10,10);
		pa.ellipse(0,0,size,size );
		pa.popStyle();
	}
}
