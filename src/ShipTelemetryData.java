import processing.core.PVector;

public class ShipTelemetryData {
	
	public final float heading; // the ships current heading in degrees
	public final PVector trajectory; // the ships current direction and magnitude of movement
	public final PVector position; // the ships current position within the System
	
	public ShipTelemetryData(float heading, PVector trajectory, PVector position) {
		this.heading = heading;
		this.trajectory = trajectory;
		this.position = position;
	}
}