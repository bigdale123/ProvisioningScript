
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class provisiongui extends JFrame{
	private static provision p = new provision();
	public static void main(String[] args) {
		// Create Frame
		JFrame frame = new JFrame("Provisioning Script");

		// set close operation for exit button
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// set the look and feel the same as the system
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
		}

		JPanel panel = mainPanel();

		// add panel to frame
		frame.add(panel);
		frame.setSize(560,540);
		frame.setVisible(true);
	}

	public static JPanel mainPanel(){
		JPanel panel = new JPanel();
		panel.setLayout(null);

		JTextArea result = new JTextArea();
		result.setEditable(false);
		result.setBounds(0,0,400,500);
		result.setFont(new Font("Arial",0,12));
		result.setBackground(new Color(255,255,255));
		panel.add(result);

		JLabel l = new JLabel("Provisioning Script");
		l.setBounds(405,0,150,40);
		l.setHorizontalAlignment(JLabel.CENTER);
		l.setFont(new Font("Arial",Font.BOLD,14));
		panel.add(l);

		JButton devices = new JButton("Setup Devices");
		devices.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				result.setText(null);
				result.append("Checking Devices... \n");
				provision.getDevices();
				if(provision.devicesConnectedList()[0]==null) {
					result.append("Found 0 Devices connected. \n");
				}
				else {
					result.append("Found "+String.valueOf(provision.devicesConnected())+" Devices connected. \n");
				}

				if(provision.devicesConnected()>0&&provision.devicesConnectedList()[0]!=null) {
					result.append("Continuing with setup... \n");
					provision.adbSetup();
					result.append("Setup Complete. Press Provision button to continue. \n");

				}
				else {
					result.append("No devices connected, troubleshoot and try again. \n");
				}
			}
		});
		devices.setBounds(405,55,150,50);
		panel.add(devices);

		JButton provisioning = new JButton("Provision!");
		provisioning.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//result.append("Attempting to install apk's... \n");
				provision.apkInstall();
				//result.append("Task Completed! Attempting to install boot animation... \n");
				provision.bootAnimation();
				provision.changeSettings();
				provision.reboot();
				result.append("Provisioning completed!\n");
			}
		});
		provisioning.setBounds(405,105,150,50);
		panel.add(provisioning);


		JButton newbatch = new JButton("Start New Batch");
		newbatch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				result.setText(null);
				provision.newBatch();
			}
		});
		newbatch.setBounds(405,155,150,50);
	    panel.add(newbatch);

		return panel;
	}
}
