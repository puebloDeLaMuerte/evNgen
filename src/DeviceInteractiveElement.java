import processing.core.PShape;
import processing.core.PVector;
import java.util.ArrayList;


// an interactive element that is part of a device (e.g. a button, a switch, a knob, a slider, a display, etc.)
// interactive elements have a varying number of states (e.g. a button has a pressed and a released state, a switch has an on and an off state, a knob has a varying number of states depending on the number of positions it can be turned to, a slider has a varying number of states depending on the number of positions it can be moved to, a display has a varying number of states depending on the number of characters it can display, etc.)
// interactive elements have a position and extension on that device.
// interactive elements are parsed from the svg-file of the device that is handed to them in the form of a PShape object.
public class DeviceInteractiveElement {

    private PShape elementGraphic;
    private PShape[] stateGraphics;
    private String elementName;
    private int numStates;
    private int state;
    private PVector topLeft;
    private PVector bottomRight;

    public DeviceInteractiveElement(PShape elementGraphics ) {

        // parse the interactive element from the device graphics children, extracting  the graphics for the different states of the element, their graphics and numbers.

        String[][] childNameParts = new String[elementGraphics.getChildCount()][];
        ArrayList<PShape> stateGraphics = new ArrayList<PShape>();
        ArrayList<String> stateNames = new ArrayList<String>();

        for( int i = 0; i < elementGraphics.getChildCount(); i++ ) {
            PShape child = elementGraphics.getChild(i);
            childNameParts[i] = child.getName().split("_");

            if( childNameParts[i][0].equals("state") ) {
                stateGraphics.add( child );
                stateNames.add( childNameParts[i][1] );
            }
        }


        this.state = 0;

        this.topLeft = new PVector(0, 0);
        this.bottomRight = new PVector(0, 0);
    }



    public void setState(int state) {
        this.state = state;
    }

    public PVector getTopLeft() {
        return this.topLeft;
    }

    public PVector getBottomRight() {
        return this.bottomRight;
    }

    public int getState() {
        return this.state;
    }

    public void draw() {
        // draw the current state of the interactive element
    }




}
