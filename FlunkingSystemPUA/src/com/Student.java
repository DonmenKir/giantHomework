package com;

/**
 * Student 類別：負責資料儲存與核心邏輯運算
 */
public class Student {
	
	// ---------------------------------------------------------
	// 1. Field (屬性/欄位)：定義物件要儲存哪些原始資料
	// ---------------------------------------------------------
	private String name; 
	private double chi, eng, mat, sci;
	private int rank;

	// ---------------------------------------------------------
	// 2. Constructor (建構子)：初始化物件內容
	// ---------------------------------------------------------
	public Student(String name, double chi, double eng, double mat, double sci) {
		this.name = name;
		this.chi = chi;
		this.eng = eng;
		this.mat = mat;
		this.sci = sci;
	}

	// ---------------------------------------------------------
	// 3. Method (方法)：定義物件具備的行為
	// ---------------------------------------------------------
	
	// 行為 A：計算單科調分 (開根號x10)
	public double getAdjScore(double original, boolean isAdj) {
		return isAdj ? Math.sqrt(original) * 10 : original;
	}

	// 行為 B：計算平均分數 (供排序使用)
	public double calculateAverage(boolean cA, boolean eA, boolean mA, boolean sA) {
		double total = getAdjScore(chi, cA) + getAdjScore(eng, eA) + 
					   getAdjScore(mat, mA) + getAdjScore(sci, sA);
		return total / 4.0;
	}

	// 行為 C：產出中間欄位對齊字串 (關鍵對齊點：單個 \t)
	public String show(boolean cA, boolean eA, boolean mA, boolean sA) {
		double c = getAdjScore(chi, cA);
		double n = getAdjScore(eng, eA);
		double m = getAdjScore(mat, mA);
		double s = getAdjScore(sci, sA);
		double avg = (c + n + m + s) / 4.0;
		
		return rank + "\t" + name + "\t" + 
			   String.format("%.1f", c) + "\t" + 
			   String.format("%.1f", n) + "\t" + 
			   String.format("%.1f", m) + "\t" + 
			   String.format("%.1f", s) + "\t" + 
			   String.format("%.1f", avg) + "\n";
	}

	// 行為 D：檢查狀態並返回報告字串
	public String checkStatus(int subjectIdx, boolean isAdj, int type) {
		double[] scores = {chi, eng, mat, sci};
		double score = getAdjScore(scores[subjectIdx], isAdj);
		
		if (type == 1 && score < 60) {
			return " - " + name + " (" + String.format("%.1f", score) + ")\n";
		} else if (type == 2 && score > 80) {
			return " ★ " + name + " (" + String.format("%.1f", score) + ")\n";
		}
		return "";
	}

	// Getter & Setter
	public String getName() { return name; }
	public double getChi() { return chi; }
	public double getEng() { return eng; }
	public double getMat() { return mat; }
	public double getSci() { return sci; }
	public void setRank(int rank) { this.rank = rank; }
}