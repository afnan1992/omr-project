package com.omr.app.userinterface;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;



public class CustomCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 7770331524612924812L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		
		
		if(value instanceof JLabel)
		{
			JLabel temp=(JLabel)value;
			if(temp.getText().contains("success"))
			{
				temp.setIcon(new ImageIcon(this.getClass().getResource("green.png")));
	            temp.setOpaque(true);
	              
			}
			else
			{
				temp.setIcon(new ImageIcon(this.getClass().getResource("circle_red.png")));
				temp.setOpaque(true);
			}
			return(JLabel)temp;
		}
		else

			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
					row, column);
	}

}

class MyModel extends javax.swing.table.DefaultTableModel{
	 
 
	private static final long serialVersionUID = 7707351771626333098L;

	@Override
	public void addRow(Object[] rowData) {
		// TODO Auto-generated method stub
		super.addRow(rowData);
	}
	public Class getColumnClass(int columnIndex) {
        if(columnIndex == 2)
        	return getValueAt(0, columnIndex).getClass();
 
        else
        	return super.getColumnClass(columnIndex);
 
    }

 
}
