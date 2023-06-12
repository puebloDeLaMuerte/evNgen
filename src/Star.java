import processing.core.PApplet;
import processing.core.PGraphics;

public class Star {
	
	PApplet pa;
	float xPos, yPos, zPos;
	float intensity;
	
	public Star(PApplet pApplet) {
		pa = pApplet;
		
		intensity = pa.random(0.5f, 1f);
		xPos = pa.random((float) -pa.width / 2, (float) pa.width / 2);
		//println("x: " + xPos);
		yPos = pa.random((float) -pa.height / 2, (float) pa.height / 2);
		//println("y: " + yPos);
		zPos = 1 / pa.random(0.001f, 1);
	}
	
	
	public void processDeltaPosition(float deltaX, float deltaY) {
		xPos += deltaX * zPos;
		yPos += deltaY * zPos;
		
		if (xPos < -pa.width / 2) xPos = pa.width / 2;
		if (xPos > pa.width / 2) xPos = (-pa.width / 2);
		if (yPos < -pa.height / 2) yPos = (pa.height / 2);
		if (yPos > pa.height / 2) yPos = (-pa.height / 2);
	}
	
	public void drawStar() {
		pa.stroke(255, 255 * intensity);
		pa.point(xPos, yPos);
	}
}
