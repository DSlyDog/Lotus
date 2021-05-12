package net.whispwriting.lotus.command;

import net.whispwriting.lotus.Lotus;
import net.whispwriting.lotus.command.commands.*;
import net.whispwriting.lotus.command.commands.audio_cmds.*;

public class CommandDelegate {

    public static void registerCommands(Lotus bot){
        bot.registerCommand(bot.getPrefix() + "announce", new Announce());
        bot.registerCommand(bot.getPrefix() + "create", new CreateAnnouncer());
        bot.registerCommand(bot.getPrefix() + "auto", new CreateAnnounceChannel());
        bot.registerCommand(bot.getPrefix() + "roles", new SendRoleMessage());
        bot.registerCommand(bot.getPrefix() + "status", new SetPresence());
        bot.registerCommand(bot.getPrefix() + "join", new JoinChannel());
        bot.registerCommand(bot.getPrefix() + "play", new Play());
        bot.registerCommand(bot.getPrefix() + "leave", new LeaveChannel());
        bot.registerCommand(bot.getPrefix() + "clearqueue", new ClearQueue());
        bot.registerCommand(bot.getPrefix() + "skip", new SkipTrack());
        bot.registerCommand(bot.getPrefix() + "setvolume", new SetVolume());
        bot.registerCommand(bot.getPrefix() + "playlist", new Playlist());
        bot.registerCommand(bot.getPrefix() + "notify", new Notify());
    }
}
