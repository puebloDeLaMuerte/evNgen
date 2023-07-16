import processing.core.*;

public class EVNgen extends PApplet {
    
    
    Ship ship;
    System system;

    public EVData evData;
    
    public float deltaTime; // time spent since last frame in seconds;
    float lastDeltaTimeStamp;

    int tempDeviceSelector = 0;
    
    PShape cursor;

    boolean commandKeyDown = false;
    boolean controlKeyDown = false;
    boolean optionKeyDown = false;

    public void settings() {
        smooth();
        println("pixel density: " + displayDensity() );

        fullScreen();

        pixelDensity( displayDensity() );
        println("pixel density: " + displayDensity() );
    }


    public void setup() {
        
        evData = new EVData(this);
        evData.InitEvDataFiles(this, loadStrings("data/evData/shïp.csv"));
        evData.loadAllDevices();
        
        frameRate(500);
        system = new System(this);
        ship = new Ship(this,130);
        system.registerSysObject(ship, true);
        background(0);
        noCursor();

        cursor = loadShape("data/cursor_ni.svg");
        cursor.disableStyle();
    }


    public void draw() {

        ship.beginFrameTick();
        system.beginFrameTick();


        //if( frameCount % 20 == 0 ) println(frameRate);

        calculateDeltaTime();

        ship.processInput();
        ship.processPhysics();
        system.updateViewPosition();

        background(0);

        pushMatrix();
        translate(width/2, height/2);
        system.drawSystem();
        ship.evaluateMousePosition(mouseX-width/2,mouseY-height/2);
        ship.drawCockpit();
        popMatrix();

        pushStyle();
        noFill();
        stroke(200);
        strokeWeight(0.2f);
        shape(cursor, mouseX, mouseY);
        popStyle();

        /*
        // draw crosshair at screen center
        pushMatrix();
        pushStyle();
        strokeWeight(2);
        stroke(0, 255, 0);
        translate(width/2, height/2);
        line(-10, -10, 10, 10);
        line(-10, 10, 10, -10);
        popStyle();
        popMatrix();
        */
        
        //println(mouseX+"-"+mouseY);
        //println(width+"-"+height);
    }



    public void mousePressed() {
        ship.mousePressed();
    }

    public void keyPressed() {

        setInput(true);

        if ( (key == 'd' || key == 'D') && (keyCode == CONTROL) ) {
            println("Ctrl + D is pressed!");
        }

        if( key == 'd' && commandKeyDown ) {
            ship.getCockpit().toggleFrontBackView();
        }

        if( isModifierKeyDown() ) {
            return;
        }

        if( key == 'd') {
            // make a Copy of the Device at array pos tempDeviceSelector and apply it to the ship
            ship.ApplyDeviceToSlotUnderCursor( new Device(evData.devices[tempDeviceSelector]) );
        }
        if( key == 'D') {
            ship.RemoveDeviceFromSlotUnderCursor();
        }

        if( key == '^' ) {
            tempDeviceSelector++;
            tempDeviceSelector %= evData.devices.length;
        }
    }


    public void keyReleased() {
        setInput(false);
    }


    private boolean isModifierKeyDown() {
        return controlKeyDown || commandKeyDown || optionKeyDown;
    }


    void setInput(boolean toggle) {

        if( keyCode == 17 ) {
            controlKeyDown = toggle;
        }
        if( keyCode == 157 ) {
            commandKeyDown = toggle;
        }
        if( keyCode == 18 ) {
            optionKeyDown = toggle;
        }

        if ( key == CODED ) {
            if ( keyCode == UP ) {
                ship.setInput(ShipInput.ACCEL, toggle);
            }
            if ( keyCode == LEFT ) {
                ship.setInput(ShipInput.LEFT, toggle);
            }
            if ( keyCode == RIGHT ) {
                ship.setInput(ShipInput.RIGHT, toggle);
            }
        }
    }


    void calculateDeltaTime() {
        deltaTime = (millis() - lastDeltaTimeStamp) / 1000f;
        lastDeltaTimeStamp = millis();
    }
}
