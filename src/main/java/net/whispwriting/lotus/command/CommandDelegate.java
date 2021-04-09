package net.whispwriting.lotus.command;

import net.whispwriting.lotus.Lotus;
import net.whispwriting.lotus.command.commands.*;
import net.whispwriting.lotus.command.commands.audio_cmds.*;

public class CommandDelegate {

    public static void registerCommands(Lotus bot){
        bot.registerCommand(Lotus.prefix + "announce", new Announce());
        bot.registerCommand(Lotus.prefix + "create", new CreateAnnouncer());
        bot.registerCommand(Lotus.prefix + "auto", new CreateAnnounceChannel());
        bot.registerCommand(Lotus.prefix + "roles", new SendRoleMessage());
        bot.registerCommand(Lotus.prefix + "status", new SetPresence());
        bot.registerCommand(Lotus.prefix + "join", new JoinChannel());
        bot.registerCommand(Lotus.prefix + "play", new Play());
        bot.registerCommand(Lotus.prefix + "leave", new LeaveChannel());
        bot.registerCommand(Lotus.prefix + "clearqueue", new ClearQueue());
        bot.registerCommand(Lotus.prefix + "skip", new SkipTrack());
        bot.registerCommand(Lotus.prefix + "setvolume", new SetVolume());
        bot.registerCommand(Lotus.prefix + "playlist", new Playlist());
    }
}
