import processing.core.PShape;
import java.io.File;

public class Device {
	Main pa;
	
	String deviceName; // name of the device from deviceDataFile
	PShape dgfx;
	
	String manufacturer; // manufacturer of the device from deviceDataFile
	int rows, cols; // number of rows and cols in a Cockpit.Panel that this device spans over. from deviceDataFile
	int price; // price of the device from deviceDataFile
	int mass; // mass of the device from deviceDataFile
	
	
	public Device(Main pApplet, File devDirectory) {
		
		pa = pApplet;
		
		deviceName = devDirectory.getName();
		pa.print("DeviceName: " + deviceName);
		String s = devDirectory.getAbsolutePath()+"/"+deviceName+".svg";
		pa.println(" at " + s);
		dgfx = pa.loadShape( s );
		loadDeviceData( devDirectory.getAbsolutePath() );
	}
	
	// load device data from json file
	public void loadDeviceData( String deviceDirectory ) {
	
		String[] lines = pa.loadStrings( deviceDirectory + "/deviceData.json" );
		
		for( String l : lines ) {
			if( l.startsWith("manufacturer") ) {
				manufacturer = l.split(":")[1].trim();
			}
			if( l.startsWith("rows") ) {
				rows = Integer.parseInt( l.split(":")[1].trim() );
			}
			if( l.startsWith("cols") ) {
				cols = Integer.parseInt( l.split(":")[1].trim() );
			}
			if( l.startsWith("price") ) {
				price = Integer.parseInt( l.split(":")[1].trim() );
			}
			if( l.startsWith("mass") ) {
				mass = Integer.parseInt( l.split(":")[1].trim() );
			}
		}
	}
}