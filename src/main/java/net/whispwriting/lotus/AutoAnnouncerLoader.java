package net.whispwriting.lotus;

import net.dv8tion.jda.api.entities.TextChannel;
import net.whispwriting.lotus.util.Announcer;
import net.whispwriting.lotus.util.JsonFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AutoAnnouncerLoader {

    public static Map<TextChannel, Announcer> loadAutoAnnouncers(){
        Map<TextChannel, Announcer> announcerMap = new HashMap<>();
        File dir = new File("auto announcers");
        if (dir.exists() && dir.isDirectory()){
            File[] files = dir.listFiles();
            for (File file : files){
                String name = file.getName();
                name = name.substring(0, name.indexOf(".json"));
                System.out.println("Loading " + name);
                JsonFile jFile = new JsonFile(name, "auto announcers");
                TextChannel input = Lotus.getChannel(name);
                TextChannel output = Lotus.getChannel(jFile.getString("output"));
                Announcer announcer = new Announcer(jFile.getString("announcer"), output);
                announcerMap.put(input, announcer);
            }
        }
        return announcerMap;
    }
}
