package com;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class MainSystem extends JFrame {

	private JPanel contentPane;
	private JTextField nameField, chiField, engField, matField, sciField;
	private JCheckBox chiVIP, engVIP, matVIP, sciVIP;
	private JCheckBox chiAdj, engAdj, matAdj, sciAdj;
	private JTextArea outputTable, failArea, over80Area;
	private JLabel lblTime;
	private JButton btnAdd, btnClear, btnPrintFail, btnPrintRank, btnExit;

	private ArrayList<Student> studentList = new ArrayList<Student>();

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainSystem frame = new MainSystem();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainSystem() {
		setTitle("期末成績管理系統 - FlunkingSystem0115版");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1200, 850);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// --- 1. UI 元件初始化 ---
		JPanel panelInput = new JPanel();
		panelInput.setBorder(new TitledBorder(null, "學生資料輸入區", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelInput.setBounds(20, 20, 1140, 150);
		contentPane.add(panelInput);
		panelInput.setLayout(null);

		JLabel lblName = new JLabel("學生姓名"); lblName.setBounds(30, 30, 80, 20); panelInput.add(lblName);
		JLabel lblChi = new JLabel("國文"); lblChi.setBounds(180, 30, 80, 20); panelInput.add(lblChi);
		JLabel lblEng = new JLabel("英文"); lblEng.setBounds(330, 30, 80, 20); panelInput.add(lblEng);
		JLabel lblMat = new JLabel("數學"); lblMat.setBounds(480, 30, 80, 20); panelInput.add(lblMat);
		JLabel lblSci = new JLabel("自然"); lblSci.setBounds(630, 30, 80, 20); panelInput.add(lblSci);

		nameField = new JTextField(); nameField.setBounds(30, 60, 100, 25); panelInput.add(nameField);
		chiField = new JTextField(); chiField.setBounds(180, 60, 100, 25); panelInput.add(chiField);
		engField = new JTextField(); engField.setBounds(330, 60, 100, 25); panelInput.add(engField);
		matField = new JTextField(); matField.setBounds(480, 60, 100, 25); panelInput.add(matField);
		sciField = new JTextField(); sciField.setBounds(630, 60, 100, 25); panelInput.add(sciField);

		chiVIP = new JCheckBox("小老師x1.05"); chiVIP.setBounds(180, 100, 120, 25); panelInput.add(chiVIP);
		engVIP = new JCheckBox("小老師x1.05"); engVIP.setBounds(330, 100, 120, 25); panelInput.add(engVIP);
		matVIP = new JCheckBox("小老師x1.05"); matVIP.setBounds(480, 100, 120, 25); panelInput.add(matVIP);
		sciVIP = new JCheckBox("小老師x1.05"); sciVIP.setBounds(630, 100, 120, 25); panelInput.add(sciVIP);

		btnAdd = new JButton("輸入資料"); btnAdd.setBounds(850, 40, 120, 50); panelInput.add(btnAdd);
		btnClear = new JButton("清除全部"); btnClear.setBounds(1000, 40, 120, 50); panelInput.add(btnClear);

		JPanel panelAdj = new JPanel();
		panelAdj.setBorder(new TitledBorder(null, "2. 全域調分控制(開根號x10)", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelAdj.setBounds(20, 180, 1140, 60);
		contentPane.add(panelAdj);
		panelAdj.setLayout(null);
		
		chiAdj = new JCheckBox("國文"); chiAdj.setBounds(20, 20, 80, 25); panelAdj.add(chiAdj);
		engAdj = new JCheckBox("英文"); engAdj.setBounds(120, 20, 80, 25); panelAdj.add(engAdj);
		matAdj = new JCheckBox("數學"); matAdj.setBounds(220, 20, 80, 25); panelAdj.add(matAdj);
		sciAdj = new JCheckBox("自然"); sciAdj.setBounds(320, 20, 80, 25); panelAdj.add(sciAdj);

		outputTable = new JTextArea();
		outputTable.setFont(new Font("Monospaced", Font.PLAIN, 14));
		JScrollPane scrollTable = new JScrollPane(outputTable);
		scrollTable.setBounds(20, 250, 1140, 250);
		contentPane.add(scrollTable);

		failArea = new JTextArea();
		JScrollPane scrollFail = new JScrollPane(failArea);
		scrollFail.setBorder(new TitledBorder(null, "不及格名單(<60)", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		scrollFail.setBounds(20, 510, 560, 230);
		contentPane.add(scrollFail);

		over80Area = new JTextArea();
		JScrollPane scroll80 = new JScrollPane(over80Area);
		scroll80.setBorder(new TitledBorder(null, "各科優異名單(>80)", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		scroll80.setBounds(600, 510, 560, 230);
		contentPane.add(scroll80);

		lblTime = new JLabel("時間讀取中...");
		lblTime.setBounds(20, 760, 300, 20);
		contentPane.add(lblTime);

		// --- 2. Events 操作移到最底下 ---
		
		// 1000毫秒更新一次時間
		new Timer(1000, e -> lblTime.setText("目前時間：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))).start();

		// 清除按鈕：清空 List 與所有 UI 元件
		btnClear.addActionListener(e -> { 
			studentList.clear(); 
			nameField.setText(""); chiField.setText(""); engField.setText(""); matField.setText(""); sciField.setText("");
			chiVIP.setSelected(false); engVIP.setSelected(false); matVIP.setSelected(false); sciVIP.setSelected(false);
			updateDisplay(); 
		});
		
		btnPrintFail = new JButton("列印不及格"); btnPrintFail.setBounds(740, 760, 120, 30);
		btnPrintFail.addActionListener(e -> { try { failArea.print(); } catch (Exception ex) {} });
		contentPane.add(btnPrintFail);

		btnPrintRank = new JButton("列印名次表"); btnPrintRank.setBounds(880, 760, 120, 30);
		btnPrintRank.addActionListener(e -> { try { outputTable.print(); } catch (Exception ex) {} });
		contentPane.add(btnPrintRank);

		btnExit = new JButton("結束系統"); btnExit.setBounds(1040, 760, 120, 30);
		btnExit.addActionListener(e -> System.exit(0));
		contentPane.add(btnExit);
		
		// 調分即時監聽：點選 CheckBox 立即觸發刷新
		ActionListener adjListener = e -> updateDisplay();
		chiAdj.addActionListener(adjListener); engAdj.addActionListener(adjListener);
		matAdj.addActionListener(adjListener); sciAdj.addActionListener(adjListener);

		// 輸入資料按鈕：含 try-catch 安全檢查
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String nameS = nameField.getText();
					if(nameS.equals("")) {
						JOptionPane.showMessageDialog(null, "姓名不能為空！");
						return;
					}
					// 若空白則預設為0
					double c = Double.parseDouble(chiField.getText().equals("")?"0":chiField.getText());
					double n = Double.parseDouble(engField.getText().equals("")?"0":engField.getText());
					double m = Double.parseDouble(matField.getText().equals("")?"0":matField.getText());
					double s = Double.parseDouble(sciField.getText().equals("")?"0":sciField.getText());

					if(chiVIP.isSelected()) c *= 1.05;
					if(engVIP.isSelected()) n *= 1.05;
					if(matVIP.isSelected()) m *= 1.05;
					if(sciVIP.isSelected()) s *= 1.05;

					studentList.add(new Student(nameS, c, n, m, s));
					updateDisplay();
					
					// 輸入後自動清空並聚焦回姓名欄
					nameField.setText(""); chiField.setText(""); engField.setText(""); matField.setText(""); sciField.setText("");
					nameField.requestFocus();
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "請輸入有效的數字分數！");
				}
			}
		});
	}

	public void updateDisplay() {
		String header = "名次\t姓名\t國文\t英文\t數學\t自然\t平均\n";
		String line = "--------------------------------------------------------------------------------\n";
		outputTable.setText(header + line);
		failArea.setText(""); over80Area.setText("");

		if(studentList.isEmpty()) return;

		// 使用自定義計算的平均值進行排序
		Collections.sort(studentList, (s1, s2) -> Double.compare(calculateAvg(s2), calculateAvg(s1)));

		String chiF = "【國文】\n", engF = "【英文】\n", matF = "【數學】\n", sciF = "【自然】\n";
		String chiHigh = "【國文優異】\n", engHigh = "【英文優異】\n", matHigh = "【數學優異】\n", sciHigh = "【自然優異】\n";

		for(int i = 0; i < studentList.size(); i++) {
			Student s = studentList.get(i);
			s.setRank(i + 1);
			
			double c = chiAdj.isSelected() ? Math.sqrt(s.getChi()) * 10 : s.getChi();
			double n = engAdj.isSelected() ? Math.sqrt(s.getEng()) * 10 : s.getEng();
			double m = matAdj.isSelected() ? Math.sqrt(s.getMat()) * 10 : s.getMat();
			double sci = sciAdj.isSelected() ? Math.sqrt(s.getSci()) * 10 : s.getSci();
			double avg = (c + n + m + sci) / 4.0;

			outputTable.append(s.show(c, n, m, sci, avg));
			
			if(c < 60) chiF += " - " + s.getName() + " (" + String.format("%.1f", c) + ")\n";
			if(n < 60) engF += " - " + s.getName() + " (" + String.format("%.1f", n) + ")\n";
			if(m < 60) matF += " - " + s.getName() + " (" + String.format("%.1f", m) + ")\n";
			if(sci < 60) sciF += " - " + s.getName() + " (" + String.format("%.1f", sci) + ")\n";
			
			if(c > 80) chiHigh += " ★ " + s.getName() + " (" + String.format("%.1f", c) + ")\n";
			if(n > 80) engHigh += " ★ " + s.getName() + " (" + String.format("%.1f", n) + ")\n";
			if(m > 80) matHigh += " ★ " + s.getName() + " (" + String.format("%.1f", m) + ")\n";
			if(sci > 80) sciHigh += " ★ " + s.getName() + " (" + String.format("%.1f", sci) + ")\n";
		}
		failArea.setText("不及格名單：\n" + chiF + engF + matF + sciF);
		over80Area.setText("各科高分名單：\n" + chiHigh + engHigh + matHigh + sciHigh);
	}

	private double calculateAvg(Student s) {
		double c = chiAdj.isSelected() ? Math.sqrt(s.getChi()) * 10 : s.getChi();
		double n = engAdj.isSelected() ? Math.sqrt(s.getEng()) * 10 : s.getEng();
		double m = matAdj.isSelected() ? Math.sqrt(s.getMat()) * 10 : s.getMat();
		double sci = sciAdj.isSelected() ? Math.sqrt(s.getSci()) * 10 : s.getSci();
		return (c + n + m + sci) / 4.0;
	}
}