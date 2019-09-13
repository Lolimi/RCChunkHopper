package lolimi.chunkHoppers.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import org.bukkit.Bukkit;

public class ChDataHandler {
	
	private static File offlineSoldFile = new File(Main.getPlugin().getDataFolder().getPath() + File.separator + "offlineSold.yml");
	
	public static void setupOfflineSold(String uuid) {
		String name = Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName();
		try {
			if(!offlineSoldFile.exists())
				offlineSoldFile.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(offlineSoldFile, true));
			if(offlineSoldFile.length()!=0)
				bw.newLine();
			bw.append(name+","+0+","+uuid);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void setOfflineSold(String uuid, double sold, boolean gotOnline) {
		try {
			List<String> fileContent = new ArrayList<>(Files.readAllLines(offlineSoldFile.toPath(), StandardCharsets.UTF_8));
			for (int i = 0; i < fileContent.size(); i++) {
			    if (fileContent.get(i).contains(uuid)) {
			    	String[] toChange = fileContent.get(i).split(",");
			    	if(!gotOnline)
			    		toChange[1] = Double.parseDouble(toChange[1]) + sold + "";
			    	else
			    		toChange[1] = "0";
			    	String changed = "";
			    	for(int j = 0; j<toChange.length; j++) {
			    		changed = changed.concat(toChange[j]+",");
			    	}
			        fileContent.set(i, changed);
			        break;
			    }
			}
			Files.write(offlineSoldFile.toPath(), fileContent, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static double getOfflineSold(String uuid) {
		try {
			if(!offlineSoldFile.exists()) {
				try {
					offlineSoldFile.createNewFile();
				} catch (IOException e) {
				}
			}
			Scanner s = new Scanner(offlineSoldFile);
			String data;
			String[] ch;
			while(s.hasNext()) {
				data = s.next();
				if(data.contains(uuid)) {
					ch = data.split(",");
					s.close();
					return Double.parseDouble(ch[1]);
				}
			}s.close();
			return -1;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (NumberFormatException f) {
			f.printStackTrace();
		}
		return -1;
	}
	
}
