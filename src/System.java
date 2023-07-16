import java.util.ArrayList;
import processing.core.*;


public class System extends EVNgenObject {
	
	
	EVNgen pa;
	Stars stars;
	
	ArrayList<iSysObject> objects = new ArrayList<iSysObject>();
	iSysObject focalObject = null;
	PVector lastFocalPosition;
	
	public System(EVNgen pApplet) {
		pa = pApplet;
		stars = new Stars( pa,1000);
		registerSysObject( new Celestial(pa,0,0,400), false) ;
	}
	
	
	public void registerSysObject(iSysObject o, boolean setAsFocus) {
		
		objects.add(o);
		pa.println("registered SysOBject. total count: " + objects.size() );
		
		if( setAsFocus ) {
			focalObject = o;
			lastFocalPosition = o.getPosition();
		}
	}
	
	
	public void updateViewPosition() {
		
		PVector delta = PVector.sub(focalObject.getPosition(),lastFocalPosition);
		lastFocalPosition = focalObject.getPosition();
		
		stars.updateViewPosition(delta.x,delta.y);
	}
	
	
	public void drawSystem() {
		
		stars.drawStars();
		
		pa.g.pushMatrix();
		pa.g.translate(focalObject.getDisplayOffset().x, focalObject.getDisplayOffset().y);
		pa.g.translate(-focalObject.getPosition().x, -focalObject.getPosition().y);
		for( iSysObject o : objects ) {
			pa.g.translate(o.getPosition().x,o.getPosition().y);
			o.drawObject();
		}
		pa.g.popMatrix();
	}
	
	
}