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
		
		PVector[] mm = minMaxXY(shape,null,null);
		
		PApplet.println("minmax: max: " + mm[1].x+"/"+mm[1].y + " min: " + mm[0].x+"/"+mm[0].y);
		
		return new PVector( mm[0].x+((mm[1].x-mm[0].x)/2), mm[0].y+((mm[1].y-mm[0].y)/2) );
	}
	
	
	// return the smallest x, smallest y, hightest x, highest y of all vertex' coordinates
	public static PVector[] minMaxXY(PShape shape, PVector prevMin, PVector prevMax) {

		PVector max = new PVector();
		PVector min = new PVector();

		/*
		float maxX = -Float.MAX_VALUE;
		float minX =  Float.MAX_VALUE;
		float maxY = -Float.MAX_VALUE;
		float minY =  Float.MAX_VALUE;
		*/

		// like above, but only use MAX_VALUE if the given prevMin/Max is null

		max.x = prevMax == null ? -Float.MAX_VALUE : prevMax.x;
		min.x = prevMin == null ?  Float.MAX_VALUE : prevMin.x;
		max.y = prevMax == null ? -Float.MAX_VALUE : prevMax.y;
		min.y = prevMin == null ?  Float.MAX_VALUE : prevMin.y;

		// if the shape is of type PATH or GEOMETRY
		if ( shape.getFamily() == PShape.PATH || shape.getFamily() == PShape.GEOMETRY ) {

			for ( int i = 0; i < shape.getVertexCount(); i++ ) {
				PVector v = shape.getVertex(i);
				if ( v.x > max.x ) max.x = v.x;
				if ( v.x < min.x ) min.x = v.x;
				if ( v.y > max.y ) max.y = v.y;
				if ( v.y < min.y ) min.y = v.y;
			}
		}
		else {
			// if the shape is of type GROUP
			for ( int i = 0; i < shape.getChildCount(); i++ ) {
				PShape child = shape.getChild(i);
				PVector[] mm = minMaxXY(child, min, max);
				if ( mm[0].x < min.x ) min.x = mm[0].x;
				if ( mm[0].y < min.y ) min.y = mm[0].y;
				if ( mm[1].x > max.x ) max.x = mm[1].x;
				if ( mm[1].y > max.y ) max.y = mm[1].y;
			}
		}


		return new PVector[] {
				min,
				max
		};
	}

}
