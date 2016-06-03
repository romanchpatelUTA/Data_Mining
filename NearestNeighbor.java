//Reference Quoted: https://github.com/nsadawi/- findMajorityClass

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;




public class NearestNeighbor {
	
	public List<String[]> input(String name){
		String filename = name;
		List<String[]> linewisevalue = new ArrayList<String[]>();
		String[] arrayOfRow = null;
		String[] arrayOfRowSplitted;
		String filevalues = "";
		try{
		FileInputStream in = new FileInputStream(new File(filename));
	    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	    
	    String eachrow = "";
		while((eachrow = reader.readLine()) != null) {
			//System.out.println(eachrow + "\n");
			filevalues += eachrow + "\n";
			}
		arrayOfRow = filevalues.split("\n");
		for(int i=0; i<arrayOfRow.length; i++){
			arrayOfRowSplitted = arrayOfRow[i].split(",");
			linewisevalue.add(arrayOfRowSplitted);
		}		
		reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
					
		return linewisevalue;
	} 	
	
	public static void main(String[] args){
		NearestNeighbor c = new NearestNeighbor();
		List<String[]>inputvalues  = c.input("trainDataXY200.txt");
		int kvalue=5;
		List<String[]>testdata = c.input("testDataX200.txt");
		int count=0;
		
		for(int k=0; k<testdata.get(0).length;k++){
			
			List<Lable> list = new ArrayList<Lable>();
			for(int i=0; i<inputvalues.get(0).length;i++){
				double dis = 0;
				for(int j=1;j<inputvalues.size();j++){
					dis += Math.pow(Double.parseDouble(inputvalues.get(j)[i]) - Double.parseDouble(testdata.get(j-1)[k]), 2) ;
				}
				
				double distance = Math.sqrt(dis);
				list.add(new Lable(distance,String.valueOf(inputvalues.get(0)[i])));
			}
		
			//System.out.println(list.get(k).distance+","+list.get(k).classLable +"  : "+(++count));
			Collections.sort(list, new DistanceComparator());
			String[] array = new String[kvalue];
			for(int i = 0; i < kvalue; i++){
				//System.out.println(list.get(i).classLable+ "," + list.get(i).distance);
				array[i] = list.get(i).classLable;
			}
			
			String finalclass = finalLable(array);
			System.out.println("Pitcure "+ (k+1)+": "+finalclass);
		}
		 
	}

	static class Lable {
		double distance;
		String classLable;
		public Lable(double distance, String classLable){
			this.classLable = classLable;
			this.distance = distance;	    	    
		}
	}
	
	static class DistanceComparator implements Comparator<Lable> {
		@Override
		public int compare(Lable x, Lable y) {
			return x.distance < y.distance ? -1 : x.distance == y.distance ? 0 : 1;
		}
	}
	
	private static String finalLable(String[] array)
	{
		Set<String> temphash = new HashSet<String>(Arrays.asList(array));
		String[] onetimevalue = temphash.toArray(new String[0]);
		int[] c = new int[onetimevalue.length];   
		for (int i = 0; i < onetimevalue.length; i++) {
			for (int j = 0; j < array.length; j++) {
				if(array[j].equals(onetimevalue[i])){
					c[i]++;
				}
			}        
		}

		
		int highest = c[0];
		for (int flag = 1; flag < c.length; flag++) {
			if (c[flag] > highest) {
				highest = c[flag];
			}
		}
		int revise = 0;
		for (int flag = 0; flag < c.length; flag++) {
			if (c[flag] == highest) {
				revise++;
			}
		}
		int index = -1;
		if(revise==1){
			for (int flag = 0; flag < c.length; flag++) {
				if (c[flag] == highest) {
					index = flag;
					break;
				}
			}
			
			return onetimevalue[index];
		} else{
			int[] a = new int[revise];
			int b = 0;
			for (int flag = 0; flag < c.length; flag++) {
				if (c[flag] == highest) {
					a[b] = flag;
					b++; 
				}
			}

			   
//			Random number = new Random();        
//			int indexval = number.nextInt(a.length);
//			//System.out.println("random index: "+indexval);
//			int indexV = a[indexval];
			return onetimevalue[0];
		}

	}
}
