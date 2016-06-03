import java.util.*;
import java.io.*;
import java.nio.file.*;

public class Centroid {
	
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
			
			//String filename = "trainDataXY.txt";
			Centroid c = new Centroid();
			List<String[]>inputvalues  = c.input("trainDataXY200.txt");
			List<String[]> testdata  = c.input("testDataX200.txt");
			
			List<double[]> newTrainData = new ArrayList<>();
			int num = 1;
			int r=0;
			for(int i=0; i<inputvalues.size() ;i++){
				double[] resultlist = new double[inputvalues.get(0).length];
				double toavg=0;
				r=0;
				for(int j=0; j<inputvalues.get(0).length; j++)
				{	
					if(num<9)
					{
					toavg= toavg+ Integer.parseInt(inputvalues.get(i)[j]);	
					num++;
					//System.out.print(toavg);
					}
					else
					{
						toavg= toavg+ Double.parseDouble(inputvalues.get(i)[j]);	
						resultlist[r] = (toavg/9);
						num = 1;
						toavg = 0;
						r++;
					}
					
					
				}
				newTrainData.add(resultlist);
				// add result to array list
				
			}
			
			
		//	diff += Math.pow(image.pixels[j] - testdata[j - 1], 2);
			
			
			int column = 0;
			for (int j=0; j<testdata.get(0).length;j++){
				
				List<Lable> list = new ArrayList<Lable>();
				
				for (int i = 0; i < newTrainData.get(0).length; i++) {	
					double diff = 0;
					for (int k = 1; k < newTrainData.size(); k++) {
						//System.out.println(newTrainData.get(k)[i]);
						diff += Math.pow(Double.parseDouble(testdata.get(k-1)[j]) -newTrainData.get(k)[i], 2);
					}
				
					double eu_distance = Math.sqrt(diff);
				
					list.add(new Lable(eu_distance,String.valueOf(newTrainData.get(0)[i])));
				
					
					Collections.sort(list, new DistanceComparator());
				}
			
				int ans = (int)Double.parseDouble(list.get(0).classLabel);
				System.out.println("column "+ (++column) +" : " + ans);
		}
			
	}

		
		// simple comparator class used to compare results via distances
		static class DistanceComparator implements Comparator<Lable> {
				@Override
				public int compare(Lable a, Lable b) {
					return a.eu_distance < b.eu_distance ? -1 : a.eu_distance == b.eu_distance ? 0 : 1;
				}
		}
		
		
		static class Lable {
			double eu_distance;
			String classLabel;

			public Lable(double eu_distance, String classLabel) {
				this.classLabel = classLabel;
				this.eu_distance = eu_distance;
			}
		}
}

