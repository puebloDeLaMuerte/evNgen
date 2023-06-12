import processing.core.*;
//import processing.core.PVector;

import static java.lang.Math.PI;

public class Utils {
	public static float radToDeg(float radians) {
		
		return (float) (radians * ( 180f / PI ));
	}
	
	
	public static float degToRad(float degrees) {
		
		return (float) (degrees * ( PI / 180f ));
	}
	
	// return the point that's exactly in the middle between the lowest and hightes x and y of all vertices
	public static PVector centerCoordinates(PShape shape) {
		
		PVector[] mm = minMaxXY(shape);
		
		PApplet.println("minmax: max: " + mm[1].x+"/"+mm[1].y + " min: " + mm[0].x+"/"+mm[0].y);
		
		return new PVector( mm[0].x+((mm[1].x-mm[0].x)/2), mm[0].y+((mm[1].y-mm[0].y)/2) );
	}
	
	
	// return the smallest x, smallest y, hightest x, highest y of all vertex' coordinates
	public static PVector[] minMaxXY(PShape shape) {
		
		float maxX = -Float.MAX_VALUE;
		float minX =  Float.MAX_VALUE;
		float maxY = -Float.MAX_VALUE;
		float minY =  Float.MAX_VALUE;
		
		for ( int i = 0; i < shape.getVertexCount(); i++ ) {
			PVector v = shape.getVertex(i);
			if ( v.x > maxX ) maxX = v.x;
			if ( v.x < minX ) minX = v.x;
			if ( v.y > maxY ) maxY = v.y;
			if ( v.y < minY ) minY = v.y;
		}
		
		return new PVector[] {
				new PVector(minX,minY),
				new PVector(maxX,maxY)
		};
	}
}
