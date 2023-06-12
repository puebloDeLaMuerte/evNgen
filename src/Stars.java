import processing.core.*;

import static java.lang.Math.random;

public class Stars {
	
	PApplet pa;
	
	int numberOfStars;
	float viewPosX, viewPosY;
	
	Star[] stars;
	
	public Stars( PApplet pApplet, int numberOfStars ) {
		
		pa = pApplet;
		this.numberOfStars = numberOfStars;
		
		stars = new Star[numberOfStars];
		for( int i = 0; i < numberOfStars; i++ ) {
			stars[i] = new Star(pa);
		}
	}
	
	
	public void updateViewPosition(float deltaX, float deltaY) {
		deltaX *= 0.2;
		deltaY *= 0.2;
		for( Star star : stars ) {
			star.processDeltaPosition(-deltaX,-deltaY);
		}
	}
	
	
	public void drawStars() {
		
		pa.g.pushStyle();
		for( int i = 0; i < numberOfStars; i++ ) {
			stars[i].drawStar();
		}
		pa.g.popStyle();
	}
	
}


