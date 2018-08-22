package Part1;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.lang.Math;

public class IrisReaderMain {

	private ArrayList<Iris> trainingSet = new ArrayList<Iris>();
	private static ArrayList<Iris> testingSet = new ArrayList<Iris>();
	private static int count = 0;
	double sepalLenRange;
	double sepalWidRange;
	double petalLenRange;
	double petalWidRange;

	public IrisReaderMain() {
	}


	/**
	 * Read the training data into an ArrayList, classifying each as a type of Iris.
	 * */
	private void TrainingSet() {
		File training = new File("COMP307_ASSN1_Part1/part1/iris-training.txt");
		try {
			BufferedReader trainSet = new BufferedReader(new FileReader(training));
			while(trainSet.ready()) {
				Iris line = trainingID(trainSet.readLine());
				if(line != null) {trainingSet.add(line);}
			}
		trainSet.close();
		findRanges();
		}
		catch(IOException e) {System.out.println("File read error: " + e);}
	}

	/**
	 * Read a line form the training file and parse it into a type of Iris
	 * */
	private Iris trainingID(String readline) {
		String[] data = readline.split("  ");
		if(data.length == 1) {
			return null;
		}
		else if(data[4].equals("Iris-setosa")) {
			return new IrisSetosa(data);
		}
		else if(data[4].equals("Iris-versicolor")) {
			return new IrisVersicolor(data);
		}
		else if(data[4].equals("Iris-virginica")) {
			return new IrisVirginica(data);
		}
		return null;
	}

	/**
	 * Read the test data and check it against the K nearest neighbours. find K by
	 * */
	private void TestSet() {
		File testData = new File("COMP307_ASSN1_Part1/part1/iris-test.txt");
		try {
			BufferedReader testSet = new BufferedReader(new FileReader(testData));
			//Set K
			int K = 3;
			while(testSet.ready()) {
				Iris line = kNNIris(testSet.readLine(), K);
				if(line != null) {
					testingSet.add(line);
				}
			}
			testSet.close();
		}
		catch(IOException e) {System.out.println("File read error: " + e);}
	}

	/**
	 * Split the test set and find the K nearest neighbours for each instance in the test set
	 * */
	private Iris kNNIris(String line, int k) {
		HashMap<Double, String> nearestK = new HashMap<Double, String>();
		String[] data = line.split("  ");
		if(data.length <= 4) {return null;}
		Double largestN = 0.0;
		for(Iris i : trainingSet) {
			Double distanceM = distanceMeasure(Double.parseDouble(data[0]), Double.parseDouble(data[1]),
								Double.parseDouble(data[2]), Double.parseDouble(data[3]), i);
			if(nearestK.size() < k) {
				if(Double.compare(distanceM, largestN) > 0 || largestN == 0) {
					largestN = distanceM;
				}
				nearestK.put(distanceM, i.getLabel());
			}
			else {
				Set<Double> keys = nearestK.keySet();
				if(Double.compare(distanceM, largestN) < 0) {
					nearestK.remove(largestN);
					nearestK.put(distanceM, i.getLabel());
					double max = 0;
					for(Double d : keys) {
						if(Double.compare(d, max) > 0|| max == 0) {
							max = d;
						}
					}
					largestN = max;
				}
			}
		}
		Set<Double> keys = nearestK.keySet();
		int setosaCount = 0;
		int versicolorCount = 0;
		int virginicaCount = 0;
		for(Double d : keys) {
			if(nearestK.get(d).equals("Iris-setosa")) {
				setosaCount ++;
			}
			else if(nearestK.get(d).equals("Iris-versicolor")) {
				versicolorCount ++;
			}
			else if(nearestK.get(d).equals("Iris-virginica")) {
				virginicaCount ++;
			}
		}
		if(setosaCount > versicolorCount && setosaCount > virginicaCount) {
			return parseIris(data, "Setosa");
		}
		else if(versicolorCount > setosaCount && versicolorCount > virginicaCount) {
			return parseIris(data, "Versicolor");
		}
		else if(virginicaCount > versicolorCount && virginicaCount > setosaCount) {
			return parseIris(data, "Virginica");
		}
		else {
			return kNNIris(line, k - 1);
		}
	}

	private Iris parseIris(String[] data, String type) {
		if(type.equals("Setosa")) {
			count ++;
			return new IrisSetosa(data);
		}
		else if(type.equals("Versicolor")) {
			count ++;
			return new IrisVersicolor(data);
		}
		else if(type.equals("Virginica")) {
			count ++;
			return new IrisVirginica(data);
		}
		return null; // Should never get to this state
	}

	private double distanceMeasure(double sepalLen, double sepalWid, double petalLen, double petalWid, Iris other) {
		double sepalLenD = Math.pow(sepalLen - other.getSepalLen(), 2) / Math.pow(sepalLenRange, 2);
		double sepalWidD = Math.pow(sepalWid - other.getSepalWid(), 2) / Math.pow(sepalWidRange, 2);
		double petalLenD = Math.pow(petalLen - other.getPetalLen(), 2) / Math.pow(petalLenRange, 2);
		double petalWidD = Math.pow(petalWid - other.getPetalWid(), 2) / Math.pow(petalWidRange, 2);

		return Math.sqrt(sepalLenD + sepalWidD + petalLenD + petalWidD);
	}

	public void findRanges() {
		double sepalLenMax = 0, sepalLenMin = 0;
		double sepalWidMax = 0, sepalWidMin = 0;
		double petalLenMax = 0, petalLenMin = 0;
		double petalWidMax = 0, petalWidMin = 0;

		for(Iris i: trainingSet) {
			if(i.getSepalLen() > sepalLenMax) {
				sepalLenMax = i.getSepalLen();
			}
			else if(i.getSepalLen() < sepalLenMin) {
				sepalLenMin = i.getSepalLen();
			}
			if(i.getSepalWid() > sepalWidMax) {
				sepalWidMax = i.getSepalWid();
			}
			else if(i.getSepalWid() < sepalWidMin) {
				sepalWidMin = i.getSepalWid();
			}
			if(i.getPetalLen() > petalLenMax) {
				petalLenMax = i.getPetalLen();
			}
			else if(i.getPetalLen() < petalLenMin) {
				petalLenMin = i.getPetalLen();
			}
			if(i.getPetalWid() > petalWidMax) {
				petalWidMax = i.getPetalWid();
			}
			else if(i.getPetalWid() < petalWidMin) {
				petalWidMin = i.getPetalWid();
			}
		}

		this.sepalLenRange = sepalLenMax - sepalLenMin;
		this.sepalWidRange = sepalWidMax - sepalWidMin;
		this.petalLenRange = petalLenMax - petalLenMin;
		this.petalWidRange = petalWidMax - petalWidMin;

	}


	/**
	 * Check the accuracy of the test set against what each iris actually is.
	 * */
	private static void accuracyChecker() {
		Integer accuracy = 0;
		Integer total = 0;
		for(Iris i : testingSet) {
			total ++;
			if(i.getLabel().equals(i.getType())) {
				accuracy ++;
			}
		}
		if(count != testingSet.size()) {
			System.out.println("Error!: " + count + " items were input, " + testingSet.size() + " Were classified");
		}
		double percentage = (accuracy.doubleValue() / total.doubleValue()) * 100;

		System.out.println(accuracy + " / " + total + " Correctly classified, " + percentage + "% total accuracy");
	}

	public static void main(String[] args) {
		IrisReaderMain reader = new IrisReaderMain();
		reader.TrainingSet();
		reader.TestSet();
		for(Iris i : testingSet) {
			System.out.println(i.toString());
		}
		accuracyChecker();
	}

}
