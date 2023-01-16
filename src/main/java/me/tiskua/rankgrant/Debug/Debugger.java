package me.tiskua.rankgrant.Debug;

import me.tiskua.rankgrant.main.Files;
import me.tiskua.rankgrant.utils.Util;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;

import java.io.FileNotFoundException;
import java.util.*;

public class Debugger {

    private final HashMap<String, ArrayList<Object>> errors = new HashMap<>();
    private String checkingGUI;

    public void debugMessage(CommandSender sender) {
        if(errors.isEmpty()) {
            sender.sendMessage(Util.format("&aNo errors have been detected!"));
            return;
        }
        sender.sendMessage(Util.format("&c=====RankGrant Debugger====="));
        for(Map.Entry<String, ArrayList<Object>> entry : errors.entrySet()) {
            String errorType = entry.getKey();
            ArrayList<Object> errorInfo = entry.getValue();

            sender.sendMessage(Util.format("&4" + errorType + ":"));
            sender.sendMessage(Util.format("&c  What is causing the error:"));

            switch (errorType) {
                case "Sound":
                    sender.sendMessage(Util.format("&7   Sound: " + errorInfo.get(0)));
                    sender.sendMessage(Util.format("&7   Volume: " + errorInfo.get(1)));
                    sender.sendMessage(Util.format("&7   Pitch: " + errorInfo.get(2)));
                    sender.sendMessage(Util.format("&7   Line: " + getConfigErrorLine(errorInfo.get(0).toString())));
                    break;
                case "Invalid Slot":
                    sender.sendMessage(Util.format("&7   GUI: " + errorInfo.get(0)));
                    sender.sendMessage(Util.format("&7   Slot: " + errorInfo.get(1)));
                    sender.sendMessage(Util.format("&7   Line: " + getConfigErrorLine(errorInfo.get(1).toString())));
                    break;
                case "Invalid Item":
                    sender.sendMessage(Util.format("&7   GUI: " + errorInfo.get(0)));
                    sender.sendMessage(Util.format("&7   Item: " + errorInfo.get(1)));
                    sender.sendMessage(Util.format("&7   Line: " + getConfigErrorLine(errorInfo.get(1).toString())));
                    break;
                case "Date To String":
                    sender.sendMessage(Util.format("&7   Cannot Convert " + errorInfo.get(0) + " to a valid Date!"));
                    break;
            }
        }
        sender.sendMessage(Util.format("&c======================"));
    }


    public Integer getConfigErrorLine(String error) {
        try {
            Scanner myReader = new Scanner(Files.configfile);
            int line = 0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                line += 1;
                if (data.contains(error))
                    return line;

            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean checkSound() {
        try {
            Files.config.getDouble("Sound_settings.volume");
            Files.config.getDouble("Sound_settings.pitch");
            Sound.valueOf(Files.config.getString("Sound_settings.sound"));
            return true;
        } catch(IllegalArgumentException e) {
            addError("Sound", new ArrayList<>(Arrays.asList(
                    Files.config.getString("Sound_settings.sound"),
                    Files.config.getDouble("Sound_settings.pitch"),
                    Files.config.getDouble("Sound_settings.volume"))));
            return false;
        }
    }

    public void addError(String errorType, ArrayList<Object> info) {
        errors.put(errorType, info);
    }

    public void setCheckingGUI(String gui) {
        checkingGUI = gui;
    }
    public String getCheckingGUI() {
        return checkingGUI;
    }
}
