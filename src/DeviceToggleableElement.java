import processing.core.PShape;
import processing.core.PVector;

public class DeviceToggleableElement extends DeviceInteractiveElement {


    private PVector localTopLeft;
    private PVector localBottomRight;

    public DeviceToggleableElement(EVNgen pa, Device d, PShape elemGfx, PVector deviceTopLeft, PVector deviceBottomRight) {
        super(pa, d, elemGfx, deviceTopLeft, deviceBottomRight);

        PVector[] tlbr = Utils.minMaxXY(elemGfx, null, null);
        localTopLeft     = tlbr[0];
        localBottomRight = tlbr[1];
    }


    public boolean isMouseOver(int x, int y) {

        boolean mover = (x > topLeft().x && x < bottomRight().x && y > topLeft().y && y < bottomRight().y);

        // is the mouse over this element and was the mouse clicked this frame
        if( mover && pa.mousePressed && pa.mouseButton == pa.LEFT ) {

            // toggle the state
            state = (state + 1) % numStates;
        }

        return mover;
    }

    public boolean mousePressed() {
        return false;
    }

    private PVector topLeft() {
      return PVector.add(localTopLeft, device.getTopLeft());
    }

    private PVector bottomRight() {
      return PVector.add(localBottomRight, device.getBottomRight());
    }
}
