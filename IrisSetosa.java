package Part1;

public class IrisSetosa extends Iris{

	private double sepalLen, sepalWid;
	private double petalLen, petalWid;
	private String type = "Iris-setosa";
	private String label;

	public IrisSetosa(String[] data) {
		this.sepalLen = Double.parseDouble(data[0]);
		this.sepalWid = Double.parseDouble(data[1]);
		this.petalLen = Double.parseDouble(data[2]);
		this.petalWid = Double.parseDouble(data[3]);
		this.label = data[4];
	}

	public double getSepalLen() {
		return sepalLen;
	}

	public double getSepalWid() {
		return sepalWid;
	}

	public double getPetalWid() {
		return petalWid;
	}

	public double getPetalLen() {
		return petalLen;
	}

	public String getType() {
		return type;
	}

	public String getLabel() {
		return this.label;
	}

	public String toString() {
		return "Classed as: " + type + " Is Actually: " + label + "  " + sepalLen + "  " + sepalWid + "  " + petalLen + "  " + petalWid;
	}
}
