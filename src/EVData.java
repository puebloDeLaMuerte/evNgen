import java.io.File;
import java.util.ArrayList;

public class EVData {
	
	//private String[]
	
	EVNgen pa;
	
	public EVData(EVNgen pApplet) {
		pa = pApplet;
	}
	
	
	public EVDataFile shipData; // Data File for all the ships
	
	public void InitEvDataFiles(EVNgen pa, String[] lines) {
		
		shipData = new EVDataFile(pa, lines);
	}
	
	
	public Device[] devices; // array of all devices in the game in their unassigned form.
	
	public void loadAllDevices() {
		
		ArrayList<Device> tempDevices = new ArrayList<Device>();
		
		String devicesPath = "data/devices";
		File file = new File( devicesPath );
		
		String[] directories = file.list();
		
		for( String d : directories ) {
			File df = new File( devicesPath +"/"+ d );
			
			if( df.exists() && df.isDirectory() ) {
				
				pa.println( "loading Device: " + df.getAbsolutePath() );
				tempDevices.add( new Device(pa,df) );
			}
		}
		
		devices = tempDevices.toArray(new Device[tempDevices.size()]);
	}
}