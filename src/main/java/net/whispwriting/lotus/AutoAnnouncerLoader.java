package net.whispwriting.lotus;

import net.dv8tion.jda.api.entities.TextChannel;
import net.whispwriting.lotus.util.Announcer;
import net.whispwriting.lotus.util.JsonFile;
import net.whispwriting.lotus.util.auto_announcer.Announcement;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutoAnnouncerLoader {

    public static Map<TextChannel, Announcer> loadAutoAnnouncers(){
        Lotus bot = Lotus.getInstance();
        Map<TextChannel, Announcer> announcerMap = new HashMap<>();
        File dir = new File("auto announcers");
        if (dir.exists() && dir.isDirectory()){
            File[] files = dir.listFiles();
            for (File file : files){
                String name = file.getName();
                name = name.substring(0, name.indexOf(".json"));
                System.out.println("Loading " + name);
                JsonFile jFile = new JsonFile(name, "auto announcers");
                TextChannel input = bot.getChannel(name);
                TextChannel output = bot.getChannel(jFile.getString("output"));
                Announcer announcer = new Announcer(jFile.getString("announcer"), output);
                announcerMap.put(input, announcer);
            }
        }
        return announcerMap;
    }

    public static List<Announcement> loadAutoMessages(){
        Lotus bot = Lotus.getInstance();
        List<Announcement> announcements = new ArrayList<>();
        File dir = new File("announcements");
        if (dir.exists() && dir.isDirectory()){
            File[] files = dir.listFiles();
            for (File file : files){
                String name = file.getName();
                name = name.substring(0, name.indexOf(".json"));
                System.out.println("Loading " + name);
                JsonFile jFile = new JsonFile(name, "announcements");
                String role = jFile.getString("role");
                int targetDay = jFile.getInt("targetDay");
                int targetHour = jFile.getInt("targetHour");
                int targetMin = jFile.getInt("targetMin");
                int targetSec = jFile.getInt("targetSec");
                String channelStr = jFile.getString("channel");
                String message = jFile.getString("message");
                TextChannel channel = bot.getChannel(channelStr);
                Announcement announcement = new Announcement(
                        name,
                        message,
                        role,
                        channel,
                        targetDay,
                        targetHour,
                        targetMin,
                        targetSec
                );
                announcements.add(announcement);
            }
        }
        return announcements;
    }
}
