import processing.core.PShape;
import processing.core.PVector;
import java.util.ArrayList;


// an interactive element that is part of a device (e.g. a button, a switch, a knob, a slider, a display, etc.)
// interactive elements have a varying number of states (e.g. a button has a pressed and a released state, a switch has an on and an off state, a knob has a varying number of states depending on the number of positions it can be turned to, a slider has a varying number of states depending on the number of positions it can be moved to, a display has a varying number of states depending on the number of characters it can display, etc.)
// interactive elements have a position and extension on that device.
// interactive elements are parsed from the svg-file of the device that is handed to them in the form of a PShape object.
public class DeviceInteractiveElement extends EVNgenObject {

    EVNgen pa;
    Device device;
    private PShape elementGraphic;
    protected String elementName;
    private PShape[] stateGraphics;
    protected int numStates;
    protected int state;
    /*
    private PVector topLeft;
    private PVector bottomRight;
    */
    public DeviceInteractiveElement( EVNgen pa, Device d, PShape elemGfx, PVector topLeft, PVector bottomRight) {

        this.pa = pa;
        this.device = d;
        //this.topLeft = topLeft;
        //this.bottomRight = bottomRight;

        // parse the interactive element from the device graphics children, extracting the graphics for the different states of the element, their graphics and numbers.

        elementGraphic = elemGfx;
        elementGraphic.setVisible(true);

        String[][] childNameParts = new String[elementGraphic.getChildCount()][];
        ArrayList<PShape> stateGraphics = new ArrayList<PShape>();
        ArrayList<String> stateNames = new ArrayList<String>();

        for( int i = 0; i < elementGraphic.getChildCount(); i++ ) {
            PShape child = elementGraphic.getChild(i);
            childNameParts[i] = child.getName().split("_");

            if( childNameParts[i][0].equals("state") ) {
                stateGraphics.add( child );
                stateNames.add( childNameParts[i][1] );
            }
        }

        this.stateGraphics = stateGraphics.toArray(new PShape[stateGraphics.size()]);
        this.numStates = stateGraphics.size();
        this.state = 0;
    }


    // escalate this object to an instance of a subclass of DeviceInteractiveElement




    public void setState(int state) {
        this.state = state;
    }

    /*
    public PVector getTopLeft() {
        return this.topLeft;
    }

    public PVector getBottomRight() {
        return this.bottomRight;
    }
    */

    public boolean isMouseOver(int x, int y) {
        return false;
    }

    public boolean mousePressed() {
        return false;
    }

    public int getState() {
        return this.state;
    }

    //toggle statGraphics visible
    public void toggleStateGraphics() {

        // iterate over all deviceGraphics and turn all of them off except for the one that matches the current state
        for( int i = 0; i < stateGraphics.length; i++ ) {
            if( i == state ) {
                stateGraphics[i].setVisible(true);
            } else {
                stateGraphics[i].setVisible(false);
            }
        }
    }

    @Override
    void beginFrameTick() {

    }

    @Override
    void endFrameTick() {

    }
}












