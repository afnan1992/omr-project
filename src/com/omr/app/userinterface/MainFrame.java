package com.omr.app.userinterface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import au.com.bytecode.opencsv.CSVWriter;
import config.Config;
import java.awt.Color;



public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField BrowseFolderTextField;
	private JTable QrDecodeTable;
	BufferedImage sourceImage;
	JLabel sourceImageLabel,RedLabel,GreenLabel;
	JFileChooser chooser;	
	JMenuItem openMenu;
	File[] files;
	DefaultTableModel QrCodeTableModel;
	JRadioButton NoRadioButton;
	JRadioButton YesRadioButton;
	File folder;
	public ProgressDialog dialog;
	JButton BrowseFolderButton,ScanButton;
	private JTextField RedTextField;
	private JTextField GreenTextField;
	private JTextField BlueTextFiled;
	private JTextField ThresholdTextField;
	private JTextField PercentageTextField;
	private JTextField ServerURL;
	CSVWriter writer;
	private JPanel panel_4;
	private JPanel panel_5;
	private JPanel panel_6;
	private JLabel lblNewLabel_1;
	private JPanel panel_7;
	private JComboBox ComboBox;

	/**
	 * Launch the application.
	 * @throws IOException 
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/
	public void AddRowToTable(String ScanSheetName,String DecodedString,String SuccessOrFailure )
	{
		QrCodeTableModel.addRow(new Object[]{ScanSheetName,DecodedString, new JLabel(SuccessOrFailure)});
		QrDecodeTable.getCellRect(QrDecodeTable.getRowCount() - 1, 0, true);
		writer.writeNext(new String[] {ScanSheetName,DecodedString,SuccessOrFailure});
		
	
	}
	public void closeExcel() throws IOException{
		writer.close();
	};
	public void SetProgressBarValue(int value)
	{
		dialog.SetProgressBar(value);
	}
	public boolean getCancelButtonStatus()
	{
		return dialog.GetCancelButtonStatus();
	}
	
    public void setDirectoryFolder(File folder)
    {
    	this.folder=folder;
    }
    public File getDirectoryFolder()
    {
    	return folder;
    }
    public JButton getBrowseButton()
    {
    	return BrowseFolderButton;
    }
    public JButton getStartScanButton()
    {
    	return ScanButton;
    }
	public void doNothing() {
		ScanButton.setEnabled(false);
	}
	public void doCancel() {
		ScanButton.setEnabled(true);
	}
	public JButton getCancel(){
		return dialog.getCancelButton();
	}
	public boolean isChecked(){
		if( YesRadioButton.isSelected() )
		return true;
		return false;
	}

	/**
	 * Create the frame.
	 * @param session 
	 * @throws IOException 
	 */
	public MainFrame(String session) throws IOException {
		dialog = new ProgressDialog();
		final JTabbedPane tabs=new JTabbedPane();
		JPanel OmrContainer=new JPanel();
		OmrContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
		OmrContainer.setLayout(new BoxLayout(OmrContainer,BoxLayout.Y_AXIS));
		setMinimumSize(new Dimension(600, 450));
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\dell\\Pictures\\TeleTaleem\\TeleTaleem-50x50.jpg"));
		setTitle("Scan Sheets ");
		writer = new CSVWriter(new FileWriter(session+"/ScanProcessOverveiw.csv"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 50, 600, 655);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		QrCodeTableModel=new MyModel();
		
		String header[] = new String[] {"Sheet Name", "Serial Number", "Status" };		
		
		QrCodeTableModel.setColumnIdentifiers(header);
		QrDecodeTable = new JTable();
		QrDecodeTable.setModel(QrCodeTableModel);
		QrDecodeTable.getColumnModel().getColumn(0).setPreferredWidth(80);
		QrDecodeTable.getColumnModel().getColumn(1).setPreferredWidth(145);
		QrDecodeTable.getColumnModel().getColumn(2).setPreferredWidth(55);
		QrDecodeTable.setDefaultRenderer(JLabel.class, new CustomCellRenderer());
		
		panel_6 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_6.getLayout();
		flowLayout_1.setHgap(0);
		flowLayout_1.setVgap(0);
		panel_6.setBackground(new Color(204, 255, 153));
		OmrContainer.add(panel_6);
		
		lblNewLabel_1 = new JLabel();
		lblNewLabel_1.setIcon(new ImageIcon( getClass().getResource("logo.png")));
		panel_6.add(lblNewLabel_1);
		
		
		JPanel BrowseFolderPanel = new JPanel();
		BrowseFolderPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Select Folder", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		OmrContainer.add(BrowseFolderPanel);
		BrowseFolderPanel.setLayout(new BoxLayout(BrowseFolderPanel, BoxLayout.Y_AXIS));
		
		JPanel panel_3 = new JPanel();
		BrowseFolderPanel.add(panel_3);
		panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.X_AXIS));
		
		JLabel lblNewLabel = new JLabel("Folder :     ");
		panel_3.add(lblNewLabel);
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		
		BrowseFolderTextField = new JTextField();
		panel_3.add(BrowseFolderTextField);
		
		BrowseFolderButton = new JButton("Browse");
		//BrowseFolderButton.setPreferredSize(new Dimension(100,40));
		BrowseFolderButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		panel_7 = new JPanel();
		panel_3.add(panel_7);
		panel_3.add(BrowseFolderButton);
		
		
		JPanel panel_2 = new JPanel();
		BrowseFolderPanel.add(panel_2);
		
		ButtonGroup group=new ButtonGroup();
		JLabel SerialNumberLabel = new JLabel("Do you want to manually enter Serial Number?");
		panel_2.add(SerialNumberLabel);
		YesRadioButton = new JRadioButton("Yes");
		panel_2.add(YesRadioButton);
		group.add(YesRadioButton);
		
		NoRadioButton = new JRadioButton("No");
		panel_2.add(NoRadioButton);
		NoRadioButton.setSelected(true);
		group.add(NoRadioButton);
		QrDecodeTable.setBounds(16, 203, 492, 16);
		
		JPanel panel_8 = new JPanel();
		
		panel_8.setLayout(new BoxLayout(panel_8, BoxLayout.X_AXIS));
		JLabel StorageMediumLabel=new JLabel("Please tell where you would like to store the results: ");
		panel_8.add(StorageMediumLabel);
		final DefaultComboBoxModel<String> StorageModal=new DefaultComboBoxModel<>();
		StorageModal.addElement("local");
		StorageModal.addElement("live");
		ComboBox=new JComboBox<>(StorageModal);
		ComboBox.setMaximumSize(new Dimension(50, 75));
		ComboBox.setSelectedIndex(0);
		ComboBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String data=(String) ComboBox.getItemAt(ComboBox.getSelectedIndex());
				
			}
		});
		panel_8.add(ComboBox);
		BrowseFolderPanel.add(panel_8);
		
		JScrollPane scrollPane = new JScrollPane(QrDecodeTable);
		OmrContainer.add(scrollPane);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		/*panel_4 = new JPanel();
		OmrContainer.add(panel_4);
		panel_4.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		
		JPanel panel = new JPanel();
		panel_4.add(panel);
		panel.setBorder(new TitledBorder(null, "Optical Mark Recognition Grey Scale", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		
		RedLabel = new JLabel("Red  ");
		panel.add(RedLabel);
		
		RedTextField = new JTextField();
		panel.add(RedTextField);
		RedTextField.setColumns(5);
		RedTextField.setText("233");
		
		GreenLabel = new JLabel("  Green  ");
		panel.add(GreenLabel);
		
		GreenTextField = new JTextField();
		panel.add(GreenTextField);
		GreenTextField.setColumns(5);
		GreenTextField.setText("233");
		
		JLabel BlueLabel = new JLabel("  Blue  ");
		panel.add(BlueLabel);
		
		BlueTextFiled = new JTextField();
		panel.add(BlueTextFiled);
		BlueTextFiled.setColumns(5);
		BlueTextFiled.setText("233");
		
		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		panel_4.add(panel_1);
		panel_1.setBorder(new TitledBorder(null, "Threshold Settings", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JLabel ThresholdLabel = new JLabel("Threshold");
		panel_1.add(ThresholdLabel);
		
		ThresholdTextField = new JTextField();
		panel_1.add(ThresholdTextField);
		ThresholdTextField.setColumns(5);
		ThresholdTextField.setText("210");
		
		JLabel PercentageLabel = new JLabel("Percentage");
		panel_1.add(PercentageLabel);
		
		PercentageTextField = new JTextField();
		panel_1.add(PercentageTextField);
		PercentageTextField.setColumns(5);
		PercentageTextField.setText("50");*/
		
		panel_5 = new JPanel();
		OmrContainer.add(panel_5);
		
		ScanButton = new JButton("Start Scan");
		ScanButton.setPreferredSize(new Dimension(240,30));
		panel_5.add(ScanButton);
		ScanButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		
		tabs.addTab("Scanner",OmrContainer);
		tabs.addTab("Advance Options",CreateAdvanceOptionsPanel());
		tabs.addChangeListener(new ChangeListener() {
		    public void stateChanged(ChangeEvent e) {
		        if(tabs.getSelectedIndex()==1)
		        {
		        	
		        	String response = JOptionPane.showInputDialog(getParent(),
		                    "Please enter the admin password?",
		                    "Adminstrator",
		                    JOptionPane.INFORMATION_MESSAGE);
		        	if(response==null)
		        	{
		        		tabs.setSelectedIndex(0);
			        	  Object[] options = {"OK"};
			        	    int n = JOptionPane.showOptionDialog(getParent(),
			        	                   "You don't have access to adminstrator privileges","Adminstrator",
			        	                   JOptionPane.PLAIN_MESSAGE,
			        	                   JOptionPane.QUESTION_MESSAGE,
			        	                   null,
			        	                   options,
			        	                   options[0]);
		        	}
		        	else if(response.contains("stackoverflow"))
		        	{
		        	tabs.setSelectedIndex(1);
		        	}
		        	else 
		        	{
		        	  tabs.setSelectedIndex(0);
		        	  Object[] options = {"OK"};
		        	    int n = JOptionPane.showOptionDialog(getParent(),
		        	                   "Invalid Password","Adminstrator",
		        	                   JOptionPane.PLAIN_MESSAGE,
		        	                   JOptionPane.QUESTION_MESSAGE,
		        	                   null,
		        	                   options,
		        	                   options[0]);
		        	}
		        }
		    }
		});
		contentPane.add(tabs);
			
	}
	
	public JPanel CreateAdvanceOptionsPanel()
	{
		
		JPanel panel=new JPanel();
		FlowLayout layout=(FlowLayout)panel.getLayout();
		
		panel_4=new JPanel();
		panel_4.setBorder(new TitledBorder(null, "Optical Mark Recognition Grey Scale", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_4.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		
		RedLabel = new JLabel("Red  ");
		panel_4.add(RedLabel);
		
		RedTextField = new JTextField();
		panel_4.add(RedTextField);
		RedTextField.setColumns(5);
		RedTextField.setText("233");
		
		GreenLabel = new JLabel("  Green  ");
		panel_4.add(GreenLabel);
		
		GreenTextField = new JTextField();
		panel_4.add(GreenTextField);
		GreenTextField.setColumns(5);
		GreenTextField.setText("233");
		
		JLabel BlueLabel = new JLabel("  Blue  ");
		panel_4.add(BlueLabel);
		
		BlueTextFiled = new JTextField();
		panel_4.add(BlueTextFiled);
		BlueTextFiled.setColumns(5);
		BlueTextFiled.setText("233");
		
		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		panel.add(panel_1);
		panel_1.setBorder(new TitledBorder(null, "Threshold Settings", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JLabel ThresholdLabel = new JLabel("Threshold");
		panel_1.add(ThresholdLabel);
		
		ThresholdTextField = new JTextField();
		panel_1.add(ThresholdTextField);
		ThresholdTextField.setColumns(5);
		ThresholdTextField.setText("210");
		
		JLabel PercentageLabel = new JLabel("Percentage");
		panel_1.add(PercentageLabel);
		
		PercentageTextField = new JTextField();
		panel_1.add(PercentageTextField);
		PercentageTextField.setColumns(5);
		PercentageTextField.setText("50");
		panel.add(panel_1);
		panel.add(panel_4);
		JPanel panel_9=new JPanel();
		JLabel ServerLabel=new JLabel("Live Server URL:");
		
		ServerURL=new JTextField();
		ServerURL.setColumns(15);
		
		panel_9.add(ServerLabel);
		panel_9.add(ServerURL);		
		panel.add(panel_9);
		return panel;
		
		
	   
	}
	
	public File[] getImageFiles()
	{
		return this.files;
	}
	public JButton getCancelButton(){
		return dialog.getCancelButton();
	}
	public String getCodeFromUser(String iconPath){
		ManualSerialNumberFrame frame=new ManualSerialNumberFrame(this,iconPath);
		frame.setVisible(true);
		return frame.getCode();
	}

	public String getR() {
		return RedTextField.getText();
	}

	public void setRedTextField(JTextField redTextField) {
		RedTextField = redTextField;
	}

	public String getG() {
		return GreenTextField.getText();
	}

	public void setGreenTextField(JTextField greenTextField) {
		GreenTextField = greenTextField;
	}

	public String getB() {
		return BlueTextFiled.getText();
	}

	public void setBlueTextFiled(JTextField blueTextFiled) {
		BlueTextFiled = blueTextFiled;
	}

	public JTextField getThresholdTextField() {
		return ThresholdTextField;
	}

	public void setThresholdTextField(JTextField thresholdTextField) {
		ThresholdTextField = thresholdTextField;
	}

	public JTextField getPercentageTextField() {
		return PercentageTextField;
	}

	public void setPercentageTextField(JTextField percentageTextField) {
		PercentageTextField = percentageTextField;
	}

	public void SetNumerator(int numerator)
    {
    	dialog.SetNumeratorValue(numerator);
    }
    
    public void SetTotalCounter(int counter)
    {
    	dialog.SetTotalCounterValue(counter);
    }
	public void setFolderPath(String string) {
		BrowseFolderTextField.setText(string);
		
	}
	public void closeFiles() throws IOException {
		writer.close();
		
	}
	public void showDialog() {	
		dialog.setVisible(true);
	}
	public void updateConfig() {
		//Config.markth  = Integer.parseInt( ThresholdTextField.getText() );
		Config.percent  = Integer.parseInt( PercentageTextField.getText() );
		Config.cR  = Integer.parseInt( RedTextField.getText() );
		Config.cG  = Integer.parseInt( GreenTextField.getText() );
		Config.cB  = Integer.parseInt( BlueTextFiled.getText() );
		
	}
}


