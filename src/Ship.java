import processing.core.*;

class Ship implements iSysObject, iBusDevice<ShipTelemetryData> {
	
	EVNgen pa;
	private String name;
	
	private float turnSpeed = 120;
	private float accelerationForce;
	
	private float heading; // the ships current heading in degrees;
	private PVector trajectory = new PVector(0,0);
	private PVector position = new PVector(0,0);
	
	private boolean shouldTurnLeft;
	private boolean shouldTurnRight;
	private boolean shouldAccelerate;
	
	private PShape graphics;
	private PShape thrust;
	
	private Cockpit cockpit;
	
	public Ship(EVNgen pApplet, int dataID) {
		
		pa = pApplet;
		
		pa.println("creating ship");
		name = pa.evData.shipData.getValue(dataID,"Name");
		pa.println( "name: " + name);
		accelerationForce = pa.parseFloat(pa.evData.shipData.getValue(dataID,"Acceleration"));
		pa.println( "Acceleration: " + accelerationForce);
		turnSpeed = pa.parseFloat(pa.evData.shipData.getValue(dataID,"Turn Rate"));
		PApplet.println( "TurnSpeed: " + turnSpeed);
		heading = 0;
		graphics = pa.loadShape("data/"+name+"_sog.svg");
		//graphics = loadShape("Shuttlecraft_sog.svg");
		//graphics = loadShape("argosy_cc_plain_r.svg");
		thrust = graphics.findChild("thrust");
		thrust.setVisible(false);
		
		cockpit = new Cockpit(pa, this);
	}
	
	
	void setInput(ShipInput inputType, boolean inputToggle) {
		
		switch (inputType) {
			case ACCEL:
				shouldAccelerate = inputToggle;
				break;
			case LEFT:
				shouldTurnLeft = inputToggle;
				break;
			case RIGHT:
				shouldTurnRight = inputToggle;
				break;
		}
	}
	
	
	void processAcceleration() {
		
		PVector accel = new PVector( pa.cos( Utils.degToRad(heading)), pa.sin(Utils.degToRad(heading)) );
		accel.mult( (accelerationForce / 20) * pa.deltaTime );
		trajectory = PVector.add(trajectory,accel);
	}
	
	
	void processRotation(boolean clockwise) {
		
		int direction = 1;
		if( !clockwise ) {
			direction = -1;
		}
		heading += 2.2 * turnSpeed * pa.deltaTime * direction;
	}
	
	void processPhysics() {
		
		position = PVector.add(position,PVector.mult(trajectory,pa.deltaTime));
	}
	
	
	void processInput() {
		
		if( shouldTurnLeft ) {
			processRotation(false);
		}
		else if( shouldTurnRight ) {
			processRotation(true);
		}
		
		if( shouldAccelerate ) {
			processAcceleration();
		}
	}
	
	void drawCockpit() {
		cockpit.drawCockpit();
	}
	
	
	void evaluateMousePosition( int mXc, int mYc ) {
		cockpit.evaluateMousePosition( mXc, mYc );
	}
	
	
	public void drawObject() {
		
		pa.pushStyle();
		pa.pushMatrix();
		pa.rotate( Utils.degToRad(heading) );
		//scale(0.1);
		//scale(1.8);
		pa.shapeMode(pa.CENTER);
		pa.shape(graphics,0,0);
		
		//thrust.scale(random(0.8,1.2));
		if( shouldAccelerate ) {
			thrust.setVisible(true);
		}
		else {
			
			thrust.setVisible(false);
		}
		pa.popMatrix();
		pa.popStyle();
		
	}
	
	public PVector getPosition() {
		return position;
	}
	
	
	public PVector getDisplayOffset() {
		return cockpit.getDisplayOffset();
	}
	
	
	public int getIBusAdress() {
		return 1; // the ship itself is always on adress 1
	}
	
	public void ApplyDeviceToSlotUnderCursor(Device device) {
		
		cockpit.ApplyDeviceFromSlotUnderCursor(device);
	}

	public void RemoveDeviceFromSlotUnderCursor() {
		cockpit.RemoveDeviceFromSlotUnderCursor();
	}

	public void mousePressed() {
		cockpit.mousePressed();
	}
}


enum ShipInput {
	
	ACCEL,
	LEFT,
	RIGHT
}