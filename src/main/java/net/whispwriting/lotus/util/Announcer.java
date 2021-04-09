package net.whispwriting.lotus.util;

import net.dv8tion.jda.api.entities.TextChannel;
import net.whispwriting.lotus.command.commands.Announce;

public class Announcer {

    private String name;
    private TextChannel output;

    public Announcer(String name, TextChannel output){
        this.name = name;
        this.output = output;
    }

    public String getName(){
        return name;
    }

    public TextChannel getOutput(){
        return output;
    }
}
