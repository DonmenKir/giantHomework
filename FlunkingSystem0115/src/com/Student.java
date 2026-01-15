package com;

public class Student {
	private String name;
	private double chi, eng, mat, sci;
	private int rank;

	public Student(String name, double chi, double eng, double mat, double sci) {
		super();
		this.name = name;
		this.chi = chi;
		this.eng = eng;
		this.mat = mat;
		this.sci = sci;
	}

	public String getName() { return name; }
	public double getChi() { return chi; }
	public double getEng() { return eng; }
	public double getMat() { return mat; }
	public double getSci() { return sci; }
	public int getRank() { return rank; }
	public void setRank(int rank) { this.rank = rank; }

	// 接收計算後的數值進行顯示
	public String show(double c, double e, double m, double s, double avg) {
		return rank + "\t" + name + "\t" + 
			   String.format("%.1f", c) + "\t" + 
			   String.format("%.1f", e) + "\t" + 
			   String.format("%.1f", m) + "\t" + 
			   String.format("%.1f", s) + "\t" + 
			   String.format("%.1f", avg) + "\n";
	}
}