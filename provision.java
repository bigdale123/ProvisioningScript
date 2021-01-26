
import java.io.*;
import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class provision{
	private static String[] devices = new String[1];

	public static void main(String[] args) {
		getDevices();
		for (String str: devices) {
			System.out.println(str);
		}
		adbSetup();
		//apkInstall();
		//bootAnimation();
		changeSettings();
		reboot();
	}
	public static void getDevices() {
		devices = new String[1];
		try {
			Process process = null;
			if(System.getProperty("os.name").equals("Linux")) {
				process = Runtime.getRuntime().exec(System.getProperty("user.dir")+"/adb_linux/adb devices");
			}
			else {
				process = Runtime.getRuntime().exec(System.getProperty("user.dir")+"\\adb_windows\\adb.exe devices");
			}

			Scanner scanner = new Scanner(process.getInputStream());
			while(scanner.hasNext()) {
				String str = scanner.nextLine();
				if(str.contains("device")&&str.contains("List")==false) {
					str = str.replace("\u0009device", "");
					//System.out.println(str);
					devices = Arrays.copyOf(devices,devices.length+1);
					devices[devices.length-1]=str;
					devices = Arrays.stream(devices).filter(Objects::nonNull).toArray(String[]::new);
				}
			}

		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static int devicesConnected() {
		return devices.length;
	}
	public static String[] devicesConnectedList() {
		return devices;
	}
	public static void adbSetup() {
		// Roots and remounts all device filesystems.
		for(String device:devices) {
			try {
				Process process = null;
				if(System.getProperty("os.name").equals("Linux")) {
					process = Runtime.getRuntime().exec(System.getProperty("user.dir")+"/adb_linux/adb -s "+device+" root");
				}
				else {
					process = Runtime.getRuntime().exec(System.getProperty("user.dir")+"\\adb_windows\\adb.exe -s "+device+" root");
				}
				Scanner scanner = new Scanner(process.getInputStream());
				while(scanner.hasNext()){
					System.out.println(scanner.nextLine());
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			devices = new String[1];
			getDevices();
			try {
				Process process = null;
				if(System.getProperty("os.name").equals("Linux")) {
					process = Runtime.getRuntime().exec(System.getProperty("user.dir")+"/adb_linux/adb -s "+device+" remount");
				}
				else {
					process = Runtime.getRuntime().exec(System.getProperty("user.dir")+"\\adb_windows\\adb.exe -s "+device+" remount");
				}
				Scanner scanner = new Scanner(process.getInputStream());
				while(scanner.hasNext()){
					System.out.println(scanner.nextLine());
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
	public static void apkInstall() {
		String[] apks = new String[1];
		File directoryPath;
		//System.out.println(System.getProperty("os.name"));
		if(System.getProperty("os.name").equals("Linux")) {
			directoryPath = new File(System.getProperty("user.dir")+"/apksforinstall");
		}
		else {
			directoryPath = new File(System.getProperty("user.dir")+"\\apksforinstall");
		}
		System.out.println(directoryPath);

		apks = directoryPath.list();
		for(int i = 0;i<apks.length;i++) {
			if(System.getProperty("os.name").equals("Linux")) {
				apks[i]=directoryPath+"/"+apks[i];
			}
			else {
				apks[i]=directoryPath+"\\"+apks[i];
			}
		}
		for(String apk:apks) {
			System.out.println(apk);
		}
		for(String device:devices) {
			for(String apk:apks) {
				if(apk.toLowerCase().contains("onsign")) {
					try {
						Process process = null;
						if(System.getProperty("os.name").equals("Linux")) {
							process = Runtime.getRuntime().exec(System.getProperty("user.dir")+"/adb_linux/adb -s "+device+" uninstall tv.onsign");
						}
						else {
							process = Runtime.getRuntime().exec(System.getProperty("user.dir")+"\\adb_windows\\adb.exe -s "+device+" uninstall tv.onsign");
						}
						Scanner scanner = new Scanner(process.getInputStream());
						while(scanner.hasNext()){
							System.out.println(scanner.nextLine());
						}
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
				try {
					Process process = null;
					if(System.getProperty("os.name").equals("Linux")) {
						process = Runtime.getRuntime().exec(System.getProperty("user.dir")+"/adb_linux/adb -s "+device+" install "+apk);
					}
					else {
						process = Runtime.getRuntime().exec(System.getProperty("user.dir")+"\\adb_windows\\adb.exe -s "+device+" install "+apk);
					}
					Scanner scanner = new Scanner(process.getInputStream());
					while(scanner.hasNext()){
						System.out.println(scanner.nextLine());
					}
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static void bootAnimation() {
			for(String device:devices) {
				try {
					Process process = null;

					//
					// Make the Temp Directory for pushing files
					//
					if(System.getProperty("os.name").equals("Linux")) {
						process = Runtime.getRuntime().exec(System.getProperty("user.dir")+"/adb_linux/adb -s "+device+" shell \"mkdir /data/temp_boot\"");
					}
					else {
						process = Runtime.getRuntime().exec(System.getProperty("user.dir")+"\\adb_windows\\adb.exe -s "+device+" shell \"mkdir /data/temp_boot\"");
					}
					Scanner scanner = new Scanner(process.getInputStream());
					while(scanner.hasNext()){
						System.out.println(scanner.nextLine());
					}
					//
					// Push the boot animation file to the temp directory
					//
					if(System.getProperty("os.name").equals("Linux")) {
						process = Runtime.getRuntime().exec(System.getProperty("user.dir")+"/adb_linux/adb -s "+device+" push "+System.getProperty("user.dir")+"/bootanimation/bootanimation.zip /data/temp_boot/bootanimation.zip");
					}
					else {
						process = Runtime.getRuntime().exec(System.getProperty("user.dir")+"\\adb_windows\\adb.exe -s "+device+" push "+System.getProperty("user.dir")+"\\bootanimation\\bootanimation.zip /data/temp_boot/bootanimation.zip");
					}
					scanner.close();
					scanner = new Scanner(process.getInputStream());
					while(scanner.hasNext()) {
						System.out.println(scanner.nextLine());
					}
					//
					// Push the com.inovatica.android.blackboxtv_preferences.xml file
					//
					if(System.getProperty("os.name").equals("Linux")) {
						process = Runtime.getRuntime().exec(System.getProperty("user.dir")+"/adb_linux/adb -s "+device+" push "+System.getProperty("user.dir")+"/bootanimation/com.inovatica.android.blackboxtv_preferences.xml /data/temp_boot/com.inovatica.android.blackboxtv_preferences.xml");
					}
					else {
						process = Runtime.getRuntime().exec(System.getProperty("user.dir")+"\\adb_windows\\adb.exe -s "+device+" push "+System.getProperty("user.dir")+"\\bootanimation\\com.inovatica.android.blackboxtv_preferences.xml /data/temp_boot/com.inovatica.android.blackboxtv_preferences.xml");
					}
					scanner.close();
					scanner = new Scanner(process.getInputStream());
					while(scanner.hasNext()) {
						System.out.println(scanner.nextLine());
					}
					//
					// Push the com.example.android.blackboxtv_preferences.xml file to the temp_boot directory
					//
					if(System.getProperty("os.name").equals("Linux")) {
						process = Runtime.getRuntime().exec(System.getProperty("user.dir")+"/adb_linux/adb -s "+device+" push "+System.getProperty("user.dir")+"/bootanimation/com.example.android.blackboxtv_preferences.xml /data/temp_boot/com.example.android.blackboxtv_preferences.xml");
					}
					else {
						process = Runtime.getRuntime().exec(System.getProperty("user.dir")+"\\adb_windows\\adb.exe -s "+device+" push "+System.getProperty("user.dir")+"\\bootanimation\\com.example.android.blackboxtv_preferences.xml /data/temp_boot/com.example.android.blackboxtv_preferences.xml");
					}
					scanner.close();
					scanner = new Scanner(process.getInputStream());
					while(scanner.hasNext()) {
						System.out.println(scanner.nextLine());
					}
					//
					// Copy the boot animation to /data/local
					//
					if(System.getProperty("os.name").equals("Linux")) {
						process = Runtime.getRuntime().exec(System.getProperty("user.dir")+"/adb_linux/adb -s "+device+" shell \"cp /data/temp_boot/bootanimation.zip /data/local\"");
					}
					else {
						process = Runtime.getRuntime().exec(System.getProperty("user.dir")+"\\adb_windows\\adb.exe -s "+device+" shell \"cp /data/temp_boot/bootanimation.zip /data/local\"");
					}
					scanner.close();
					scanner = new Scanner(process.getInputStream());
					while(scanner.hasNext()) {
						System.out.println(scanner.nextLine());
					}
					//
					// Copy the boot animation to /system/media
					//
					if(System.getProperty("os.name").equals("Linux")) {
						process = Runtime.getRuntime().exec(System.getProperty("user.dir")+"/adb_linux/adb -s "+device+" shell \"cp /data/temp_boot/bootanimation.zip /system/media\"");
					}
					else {
						process = Runtime.getRuntime().exec(System.getProperty("user.dir")+"\\adb_windows\\adb.exe -s "+device+" shell \"cp /data/temp_boot/bootanimation.zip /system/media\"");
					}
					scanner.close();
					scanner = new Scanner(process.getInputStream());
					while(scanner.hasNext()) {
						System.out.println(scanner.nextLine());
					}
					//
					// copy the com.inovatica file to /data/data/com.inovatica..../shared_prefs
					//
					if(System.getProperty("os.name").equals("Linux")) {
						process = Runtime.getRuntime().exec(System.getProperty("user.dir")+"/adb_linux/adb -s "+device+" shell \"cp /data/temp_boot/com.inovatica.android.blackboxtv_preferences.xml /data/data/com.inovatica.android.blackboxtv/shared_prefs/com.inovatica.android.blackboxtv_preferences.xml\"");
					}
					else {
						process = Runtime.getRuntime().exec(System.getProperty("user.dir")+"\\adb_windows\\adb.exe -s "+device+" shell \"cp /data/temp_boot/com.inovatica.android.blackboxtv_preferences.xml /data/data/com.inovatica.android.blackboxtv/shared_prefs/com.inovatica.android.blackboxtv_preferences.xml\"");
					}
					scanner.close();
					scanner = new Scanner(process.getInputStream());
					while(scanner.hasNext()) {
						System.out.println(scanner.nextLine());
					}
					scanner.close();
					//
					// copy the com.example file to /data/data/com.example..../shared_prefs folder
					//
					if(System.getProperty("os.name").equals("Linux")) {
						process = Runtime.getRuntime().exec(System.getProperty("user.dir")+"/adb_linux/adb -s "+device+" shell \"cp /data/temp_boot/com.example.android.blackboxtv_preferences.xml /data/data/com.example.android.blackboxtv/shared_prefs/com.example.android.blackboxtv_preferences.xml\"");
					}
					else {
						process = Runtime.getRuntime().exec(System.getProperty("user.dir")+"\\adb_windows\\adb.exe -s "+device+" shell \"cp /data/temp_boot/com.example.android.blackboxtv_preferences.xml /data/data/com.example.android.blackboxtv/shared_prefs/com.example.android.blackboxtv_preferences.xml\"");
					}
					scanner.close();
					scanner = new Scanner(process.getInputStream());
					while(scanner.hasNext()) {
						System.out.println(scanner.nextLine());
					}
					scanner.close();

				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
	}
	public static void changeSettings() {
		for(String device:devices) {
			try {
				Process process = null;
				//
				// Set timezone to America/Chicago
				//
				if(System.getProperty("os.name").equals("Linux")) {
					process = Runtime.getRuntime().exec(System.getProperty("user.dir")+"/adb_linux/adb -s "+device+" shell \"setprop persist.sys.timezone \"America/Chicago\"\"");
				}
				else {
					process = Runtime.getRuntime().exec(System.getProperty("user.dir")+"\\adb_windows\\adb.exe -s "+device+" shell \"setprop persist.sys.timezone \"America/Chicago\"\"");
				}
				Scanner scanner = new Scanner(process.getInputStream());
				while(scanner.hasNext()){
					System.out.println(scanner.nextLine());
				}
				scanner.close();
				//
				// Set Player time to current time on device
				//
				LocalDateTime current_date_time = LocalDateTime.now();
				DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyMMdd.HHmmss");
				String formatted_date_time = current_date_time.format(format);
				// System.out.println(formatted_date_time);
				if(System.getProperty("os.name").equals("Linux")) {
					process = Runtime.getRuntime().exec(System.getProperty("user.dir")+"/adb_linux/adb -s "+device+" shell \"su 0 toolbox date -s "+formatted_date_time+"\"");
				}
				else {
					process = Runtime.getRuntime().exec(System.getProperty("user.dir")+"\\adb_windows\\adb.exe -s "+device+" shell \"su 0 toolbox date -s "+formatted_date_time+"\"");
				}
				scanner = new Scanner(process.getInputStream());
				while(scanner.hasNext()){
					System.out.println(scanner.nextLine());
				}
				scanner.close();
				if(System.getProperty("os.name").equals("Linux")) {
					process = Runtime.getRuntime().exec(System.getProperty("user.dir")+"/adb_linux/adb -s "+device+" shell \"setprop persist.sys.language \"English\"\"");
				}
				else {
					process = Runtime.getRuntime().exec(System.getProperty("user.dir")+"\\adb_windows\\adb.exe -s "+device+" shell \"setprop persist.sys.language \"English\"\"");
				}
				scanner = new Scanner(process.getInputStream());
				while(scanner.hasNext()){
					System.out.println(scanner.nextLine());
				}
				scanner.close();
				if(System.getProperty("os.name").equals("Linux")) {
					process = Runtime.getRuntime().exec(System.getProperty("user.dir")+"/adb_linux/adb -s "+device+" shell settings put system time_12_24 12");
				}
				else {
					process = Runtime.getRuntime().exec(System.getProperty("user.dir")+"\\adb_windows\\adb.exe -s "+device+" shell settings put system time_12_24 12");
				}
				scanner = new Scanner(process.getInputStream());
				while(scanner.hasNext()){
					System.out.println(scanner.nextLine());
				}
				scanner.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void reboot() {
		for(String device:devices) {
			try {
				Process process = null;
				if(System.getProperty("os.name").equals("Linux")) {
					process = Runtime.getRuntime().exec(System.getProperty("user.dir")+"/adb_linux/adb -s "+device+" reboot");
				}
				else {
					process = Runtime.getRuntime().exec(System.getProperty("user.dir")+"\\adb_windows\\adb.exe -s "+device+" reboot");
				}
				Scanner scanner = new Scanner(process.getInputStream());
				while(scanner.hasNext()){
					System.out.println(scanner.nextLine());
				}
				scanner.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void newBatch() {
		devices = new String[1];
	}

}
