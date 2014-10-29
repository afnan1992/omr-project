package com.omr.app.userinterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class ManualSerialNumberFrame extends JDialog {

	private static final long serialVersionUID = -6193266438847567014L;
	private JPanel contentPane;
	private String code;
	private JTextField SerialNumberTextField;
	JPanel EnterSerialNumberPanel;
	JLabel ImageLabel;
	int iterator;

	/**
	 * Launch the application.
	 */


	/**
	 * Create the frame.
	 */
	public ManualSerialNumberFrame(JFrame parent,String imagePath) {
		 super(parent, "About Dialog", true);
		code = null;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 812, 441);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		iterator=0;

		ImageIcon icon=new ImageIcon(imagePath);
		java.awt.Image image = icon.getImage();
		image = createImage(new FilteredImageSource(image.getSource(),
				new CropImageFilter(5, 5, (int) (icon.getIconWidth()*0.65), (int) (icon.getIconHeight()*0.17))));
		icon.setImage(image);
		contentPane.setLayout(null);
		ImageLabel=new JLabel(icon);
		ImageLabel.setBounds(5, 0, 786, 343);
		contentPane.add(ImageLabel);
//		this.setVisible(true);
		EnterSerialNumberPanel = new JPanel();
		EnterSerialNumberPanel.setBorder(new TitledBorder(null, "Enter Serial Number", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		EnterSerialNumberPanel.setBounds(5, 348, 786, 55);
		contentPane.add(EnterSerialNumberPanel);
		EnterSerialNumberPanel.setLayout(null);

		JLabel SerialNumberLabel = new JLabel("Serial Number:");
		SerialNumberLabel.setBounds(10, 30, 82, 14);
		EnterSerialNumberPanel.add(SerialNumberLabel);

		SerialNumberTextField = new JTextField();
		SerialNumberTextField.setBounds(91, 27, 249, 20);
		EnterSerialNumberPanel.add(SerialNumberTextField);
		SerialNumberTextField.setColumns(10);

		JButton SubmitSerialNumber = new JButton("Submit");
		SubmitSerialNumber.setBounds(350, 26, 89, 23);
		EnterSerialNumberPanel.add(SubmitSerialNumber);


		SubmitSerialNumber.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				code = SerialNumberTextField.getText();
				dispose();
			}
		});



	}
	public String getCode(){
		return code;
	}
}