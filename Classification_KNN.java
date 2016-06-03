import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * @author romanchpatel
 *
 */
public class Classification_KNN {

	static String trainDataFile = "trainDataXY200.txt";
	static String testDataFile = "testDataX200.txt";
	//static String resultFile = "resultFile.txt";
	static String line = "";
	static String newLineSplitter = "\\r?\\n";
	static String commaSplitter = ",";

	static double[][] trainDataInstances = null;
	static double[][] testDataInstances = null;

	static String fileAsString = "";

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		
		// Value of K
		int k = 5;
		
		int count = 1;
		
		//List of class lables
		List<String> classLabels = new ArrayList<>();

		testDataInstances = CreateMatrix(testDataFile);
		trainDataInstances = CreateMatrix(trainDataFile); // create matrix containing all the data
		
		
		for (int i = 0; i < testDataInstances.length; i++) {
			//System.out.println(GetClassLabel(testDataInstances[i], k));
		    classLabels.add(GetClassLabel(testDataInstances[i], k));
		}
		
		System.out.println("column :  classlabel");
		for (String string : classLabels) {
			System.out.println("column " + count + " : "+string);
			count++;
		}
	}

	/**
	 * @param testData
	 * @param k
	 * @throws IOException
	 */
	private static String GetClassLabel(double[] testData,int k) throws IOException {

		
		// list to save city data
		List<Image> ImageList = new ArrayList<Image>();
		
		// list to save distance result
		List<Result> resultList = new ArrayList<Result>();

		// add image data to image list
		for (int i = 1; i < trainDataInstances.length; i++) {
			ImageList.add(new Image(trainDataInstances[i], String.valueOf((int)trainDataInstances[i][0])));
		}

		for (Image image : ImageList) {
			double difference = 0.0;
			for (int j = 1; j < image.pixels.length; j++) {
				difference += Math.pow(image.pixels[j] - testData[j - 1], 2);
			}
			
			double distance = Math.sqrt(difference);
			
			resultList.add(new Result(distance, image.imageLabel));
		}

		Collections.sort(resultList, new DistanceComparator());
		
		String[] ss = new String[k];
		
		for (int i = 0; i < k; i++) {
			ss[i] = resultList.get(i).imageLabel;
		}
		
		
		String majClass = FindMaximumOccurance(ss);
		
		return majClass;
	}

	/**
	 * @param array
	 * @return
	 */
	private static String FindMaximumOccurance(String[] array) {
		
		// add the String array to a HashSet to get unique String values
		Set<String> h = new HashSet<String>(Arrays.asList(array));
		
		// convert the HashSet back to array
		String[] uniqueValues = h.toArray(new String[0]);
		
		// counts for unique strings
		int[] counts = new int[uniqueValues.length];
		
		// loop thru unique strings and count how many times they appear in
		// origianl array
		for (int i = 0; i < uniqueValues.length; i++) {
			for (int j = 0; j < array.length; j++) {
				if (array[j].equals(uniqueValues[i])) {
					counts[i]++;
				}
			}
		}


		int max = counts[0];
		for (int counter = 1; counter < counts.length; counter++) {
			if (counts[counter] > max) {
				max = counts[counter];
			}
		}
		

		// how many times max appears
		// we know that max will appear at least once in counts
		// so the value of freq will be 1 at minimum after this loop
		int freq = 0;
		for (int counter = 0; counter < counts.length; counter++) {
			if (counts[counter] == max) {
				freq++;
			}
		}

		// index of most freq value if we have only one mode
		int index = -1;
		if (freq == 1) {
			for (int counter = 0; counter < counts.length; counter++) {
				if (counts[counter] == max) {
					index = counter;
					break;
				}
			}
		
			return uniqueValues[index];
		} else {// we have multiple modes
			int[] ix = new int[freq];// array of indices of modes
		
			int ixi = 0;
			for (int counter = 0; counter < counts.length; counter++) {
				if (counts[counter] == max) {
					ix[ixi] = counter;// save index of each max count value
					ixi++; // increase index of ix array
				}
			}


			// now choose one at random
			Random generator = new Random();
			// get random number 0 <= rIndex < size of ix
			int rIndex = generator.nextInt(ix.length);
			
			//System.out.println("random index: " + rIndex);
			
			int nIndex = ix[rIndex];
			// return unique value at that index
			return uniqueValues[0];
		}

	}

	/**
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	private static double[][] CreateMatrix(String fileName) throws IOException {
		double[][] instance = null;
		fileAsString = "";
		int rowCount = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			while ((line = reader.readLine()) != null) {
				fileAsString += line + "\n";
			}

			// split the file to get array containing rows.
			String splitValues[] = fileAsString.split(newLineSplitter);

			int rowLength = splitValues.length; // getting the row length
			int colLength = splitValues[0].split(commaSplitter).length; // getting
																		// the
																		// column
																		// length

			instance = new double[colLength][rowLength];

			for (String sl : splitValues) {
				String splitedLine[] = sl.split(",");
				for (int j = 0; j < colLength; j++) {
					// System.out.println(" row = " + rowCount + " column = " +
					// j + " = " + splitedLine[j]);
					instance[j][rowCount] = Double.parseDouble(splitedLine[j].trim());
				}
				rowCount++;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return instance;

	}

	// simple class to model trainDataInstances (features + class)
	static class Image {
		double[] pixels;
		String imageLabel;

		public Image(double[] pixels, String imageLabel) {
			this.imageLabel = imageLabel;
			this.pixels = pixels; 
		}
	}

	// simple class to model results (distance + class)
	static class Result {
		double pixelValue;
		String imageLabel;

		public Result(double pixelValue, String imageLabel) {
			this.imageLabel = imageLabel;
			this.pixelValue = pixelValue;
		}
	}

	// simple comparator class used to compare results via distances
	static class DistanceComparator implements Comparator<Result> {
		@Override
		public int compare(Result a, Result b) {
			return a.pixelValue < b.pixelValue ? -1 : a.pixelValue == b.pixelValue ? 0 : 1;
		}
	}
}
