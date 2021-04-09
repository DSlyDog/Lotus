package net.whispwriting.lotus.command;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandHandler extends ListenerAdapter {

    private Map<String, Command> commandMap = new HashMap<>();

    public void registerCommand(String label, Command command){
        commandMap.put(label, command);
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (!event.getAuthor().isBot()){
            String[] message = event.getMessage().getContentRaw().split(" (?=([^\"]*\"[^\"]*\")*[^\"]*$)");

            if (commandMap.containsKey(message[0])){
                Member member = event.getMember();
                String label = message[0];
                List<Message.Attachment> attachments = event.getMessage().getAttachments();
                TextChannel channel = event.getChannel();
                String[] finalMessage = new String[message.length-1];

                for (int i=1; i<message.length; i++){
                    if (message[i].startsWith("\""))
                        message[i] = message[i].substring(1);
                    if (message[i].endsWith("\""))
                        message[i] = message[i].substring(0, message[i].length()-1);

                    finalMessage[i-1] = message[i];
                }

                commandMap.get(message[0]).onCommand(member, label, finalMessage, attachments, channel);
            }
        }
    }
}
