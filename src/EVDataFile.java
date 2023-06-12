import processing.core.PApplet;

import java.util.HashMap;

public class EVDataFile {
	
	Main pa;
	private HashMap<String, Integer> fields;
	private String[][] data;
	
	
	public EVDataFile(Main pApplet, String[] fileStrings) {
		
		pa = pApplet;
		
		String[] lines =fileStrings;
		String[] fieldStrings = lines[0].split(",");
		int l = fieldStrings.length;
		data = new String[lines.length-1][l];
		
		fields = new HashMap<String, Integer>(l);
		for( int i = 0; i < fieldStrings.length; i++ ) {    //// populate fields HashTable
			fields.put(fieldStrings[i],i);
		}
		
		for ( int i = 1; i < lines.length; i++ ) {     //// populate data[][]
			String[] dataPoints = lines[i].split(",");
			for ( int u = 0; u < dataPoints.length; u++ ) {
				data[i-1][u] = dataPoints[u];
				pa.print(" "+dataPoints[u]);
			}
			pa.println();
		}
	}
	
	
	public String getValue(int id, String fieldName) {
		
		if (fields.containsKey(fieldName)) {
			
			for( int d = 0; d < data.length; d++ ) {
				//println( "idLookup: " + data[d][0] );
				if( data[d][0].equals(""+id) ) {
					return data[d][fields.get(fieldName)];
				}
			}
			return "no data for id: " + id;
		} else return "no data for field name: " + fieldName;
	}
}