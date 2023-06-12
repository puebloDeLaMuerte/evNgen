import processing.core.*;

public class Main extends PApplet {
    
    
    public static void main(String[] args) {
        PApplet.main("Main");
    }

    
    Ship ship;
    System system;

    public EVData evData;
    
    public float deltaTime; // time spent since last frame in seconds;
    float lastDeltaTimeStamp;
    
    
    PShape cursor;
    
    public void settings() {
        smooth();
        println("pixel density: " + displayDensity() );

        fullScreen(); // fullscreen on the second display
        //fullScreen();
        pixelDensity( displayDensity() );
        println("pixel density: " + displayDensity() );

        evData = new EVData(this);
        evData.InitEvDataFiles(this, loadStrings("data/evData/shïp.csv"));
    }


    public void setup() {

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



    public void keyPressed() {
        setInput(true);
    }


    public void keyReleased() {
        setInput(false);
    }

    void setInput(boolean toggle) {

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