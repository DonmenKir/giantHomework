package com;

import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import javax.swing.*;
import javax.swing.border.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.Timer;

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
		setTitle("期末成績管理系統 - FlunkingSystem (PM PUA Gemini版)");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1200, 850);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		initUI();   // 初始化介面元件
		initEvents(); // 初始化事件監聽 (置於程式碼最下方)
	}

	private void initUI() {
		// 學生資料輸入區
		JPanel panelInput = new JPanel();
		panelInput.setBorder(new TitledBorder(null, "學生資料輸入區", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelInput.setBounds(20, 20, 1140, 150);
		panelInput.setLayout(null);
		contentPane.add(panelInput);

		JLabel lblName = new JLabel("姓名/學號"); lblName.setBounds(30, 30, 120, 20); panelInput.add(lblName);
		nameField = new JTextField(); nameField.setBounds(30, 60, 100, 25); panelInput.add(nameField);

		JLabel lblChi = new JLabel("國文"); lblChi.setBounds(180, 30, 80, 20); panelInput.add(lblChi);
		chiField = new JTextField(); chiField.setBounds(180, 60, 100, 25); panelInput.add(chiField);
		chiVIP = new JCheckBox("小老師x1.05"); chiVIP.setBounds(180, 100, 120, 25); panelInput.add(chiVIP);

		JLabel lblEng = new JLabel("英文"); lblEng.setBounds(330, 30, 80, 20); panelInput.add(lblEng);
		engField = new JTextField(); engField.setBounds(330, 60, 100, 25); panelInput.add(engField);
		engVIP = new JCheckBox("小老師x1.05"); engVIP.setBounds(330, 100, 120, 25); panelInput.add(engVIP);

		JLabel lblMat = new JLabel("數學"); lblMat.setBounds(480, 30, 80, 20); panelInput.add(lblMat);
		matField = new JTextField(); matField.setBounds(480, 60, 100, 25); panelInput.add(matField);
		matVIP = new JCheckBox("小老師x1.05"); matVIP.setBounds(480, 100, 120, 25); panelInput.add(matVIP);

		JLabel lblSci = new JLabel("自然"); lblSci.setBounds(630, 30, 80, 20); panelInput.add(lblSci);
		sciField = new JTextField(); sciField.setBounds(630, 60, 100, 25); panelInput.add(sciField);
		sciVIP = new JCheckBox("小老師x1.05"); sciVIP.setBounds(630, 100, 120, 25); panelInput.add(sciVIP);

		btnAdd = new JButton("輸入資料"); btnAdd.setBounds(850, 40, 120, 50); panelInput.add(btnAdd);
		btnClear = new JButton("清除全部"); btnClear.setBounds(1000, 40, 120, 50); panelInput.add(btnClear);

		// 全域調分控制區
		JPanel panelAdj = new JPanel();
		panelAdj.setBorder(new TitledBorder(null, "2. 全域調分控制(開根號x10)", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelAdj.setBounds(20, 180, 1140, 60);
		panelAdj.setLayout(null);
		contentPane.add(panelAdj);

		chiAdj = new JCheckBox("國文"); chiAdj.setBounds(20, 20, 80, 25); panelAdj.add(chiAdj);
		engAdj = new JCheckBox("英文"); engAdj.setBounds(120, 20, 80, 25); panelAdj.add(engAdj);
		matAdj = new JCheckBox("數學"); matAdj.setBounds(220, 20, 80, 25); panelAdj.add(matAdj);
		sciAdj = new JCheckBox("自然"); sciAdj.setBounds(320, 20, 80, 25); panelAdj.add(sciAdj);

		// 成績顯示區
		outputTable = new JTextArea();
		outputTable.setFont(new Font("Monospaced", Font.PLAIN, 14));
		JScrollPane scrollTable = new JScrollPane(outputTable);
		scrollTable.setBounds(20, 250, 1140, 250);
		contentPane.add(scrollTable);

		// 不及格名單區
		failArea = new JTextArea();
		JScrollPane scrollFail = new JScrollPane(failArea);
		scrollFail.setBorder(new TitledBorder(null, "不及格名單(<60)", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		scrollFail.setBounds(20, 510, 560, 230);
		contentPane.add(scrollFail);

		// 優異名單區
		over80Area = new JTextArea();
		JScrollPane scroll80 = new JScrollPane(over80Area);
		scroll80.setBorder(new TitledBorder(null, "各科優異名單(>80)", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		scroll80.setBounds(600, 510, 560, 230);
		contentPane.add(scroll80);

		// 時間與按鈕區
		lblTime = new JLabel("目前時間：讀取中...");
		lblTime.setBounds(20, 760, 300, 20);
		contentPane.add(lblTime);

		btnPrintFail = new JButton("列印不及格"); btnPrintFail.setBounds(740, 760, 120, 30); contentPane.add(btnPrintFail);
		btnPrintRank = new JButton("列印名次表"); btnPrintRank.setBounds(880, 760, 120, 30); contentPane.add(btnPrintRank);
		btnExit = new JButton("結束系統"); btnExit.setBounds(1040, 760, 120, 30); contentPane.add(btnExit);
	}

	public void updateDisplay() {
		// 如果清單已空，徹底清除區域並返回
		if(studentList.isEmpty()) {
			outputTable.setText(""); failArea.setText(""); over80Area.setText("");
			return;
		}

		// 表頭回歸單個 \t 對齊
		String header = "名次\t姓名\t國文\t英文\t數學\t自然\t平均\n";
		String line = "--------------------------------------------------------------------------------\n";
		outputTable.setText(header + line);

		boolean cA = chiAdj.isSelected(); boolean eA = engAdj.isSelected();
		boolean mA = matAdj.isSelected(); boolean sA = sciAdj.isSelected();

		Collections.sort(studentList, (s1, s2) -> Double.compare(
			s2.calculateAverage(cA, eA, mA, sA), 
			s1.calculateAverage(cA, eA, mA, sA)
		));

		String[] fReport = {"【國文不及格】\n", "【英文不及格】\n", "【數學不及格】\n", "【自然不及格】\n"};
		String[] hReport = {"【國文優異】\n", "【英文優異】\n", "【數學優異】\n", "【自然優異】\n"};

		for(int i = 0; i < studentList.size(); i++) {
			Student s = studentList.get(i);
			s.setRank(i + 1);
			outputTable.append(s.show(cA, eA, mA, sA));
			for(int sub = 0; sub < 4; sub++) {
				boolean curA = (sub==0?cA : (sub==1?eA : (sub==2?mA : sA)));
				fReport[sub] += s.checkStatus(sub, curA, 1);
				hReport[sub] += s.checkStatus(sub, curA, 2);
			}
		}
		failArea.setText("各科不及格：\n" + fReport[0] + fReport[1] + fReport[2] + fReport[3]);
		over80Area.setText("各科高分：\n" + hReport[0] + hReport[1] + hReport[2] + hReport[3]);
	}

	// =========================================================
	// --- 4. 核心事件處理區 (Events) 集中置底 ---
	// =========================================================
	
	private void initEvents() {
		
		// [Event: 日期時間計時器]
		Timer timer = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
				lblTime.setText("目前時間：" + time);
			}
		});
		timer.start();
		/* [老師範例對照]：
		Timer timer = new Timer(1000, new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		        lblNewLabel_Date.setText("目前時間：" + time);
		    }
		});
		timer.start();
		*/

		// [Event: 輸入資料]
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(nameField.getText().equals("")) { JOptionPane.showMessageDialog(null, "姓名不能為空！"); return; }
					double c = Double.parseDouble(chiField.getText().isEmpty()?"0":chiField.getText());
					double n = Double.parseDouble(engField.getText().isEmpty()?"0":engField.getText());
					double m = Double.parseDouble(matField.getText().isEmpty()?"0":matField.getText());
					double s = Double.parseDouble(sciField.getText().isEmpty()?"0":sciField.getText());

					if(chiVIP.isSelected()) c *= 1.05;
					if(engVIP.isSelected()) n *= 1.05;
					if(matVIP.isSelected()) m *= 1.05;
					if(sciVIP.isSelected()) s *= 1.05;

					studentList.add(new Student(nameField.getText(), c, n, m, s));
					updateDisplay();
					
					nameField.setText(""); chiField.setText(""); engField.setText("");
					matField.setText(""); sciField.setText("");
					chiVIP.setSelected(false); engVIP.setSelected(false);
					matVIP.setSelected(false); sciVIP.setSelected(false);
					nameField.requestFocus();
				} catch (Exception ex) { JOptionPane.showMessageDialog(null, "請輸入有效的數字分數！"); }
			}
		});

		// [Event: 清除全部]
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				studentList.clear();
				nameField.setText(""); chiField.setText(""); engField.setText("");
				matField.setText(""); sciField.setText("");
				chiVIP.setSelected(false); engVIP.setSelected(false);
				matVIP.setSelected(false); sciVIP.setSelected(false);
				updateDisplay(); // 徹底清空 TextArea
				nameField.requestFocus();
			}
		});
		/* [老師範例對照]：
		btnClear.addActionListener(e -> {
			txtRulerQty.setText("0"); // 原為 0，本系統改為 "" 完全清空
			txtPenQty.setText("0");
			chkMember.setSelected(false);
			textArea.setText("");
		});
		*/

		// [Event: 調分 CheckBox 即時更新監聽]
		ActionListener adjListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) { updateDisplay(); }
		};
		chiAdj.addActionListener(adjListener);
		engAdj.addActionListener(adjListener);
		matAdj.addActionListener(adjListener);
		sciAdj.addActionListener(adjListener);

		// [Event: 列印功能]
		btnPrintRank.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Font oFont = outputTable.getFont();
					outputTable.setFont(new Font("Monospaced", Font.PLAIN, 10)); // 列印縮小防止截斷
					outputTable.print(); 
					outputTable.setFont(oFont);
				} catch (PrinterException e1) { e1.printStackTrace(); }
			}
		});
		/* [老師範例對照]：
		btnPrint.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try { textArea.print(); } catch (PrinterException e1) { e1.printStackTrace(); }
			}
		});
		*/

		btnPrintFail.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try { failArea.print(); } catch (PrinterException e1) { e1.printStackTrace(); }
			}
		});

		// [Event: 結束系統]
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		/* [老師範例對照]：
		System.exit(0);
		*/
	}
}