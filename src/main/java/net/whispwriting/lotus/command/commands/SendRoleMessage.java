package net.whispwriting.lotus.command.commands;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.*;
import net.whispwriting.lotus.Lotus;
import net.whispwriting.lotus.command.Command;
import net.whispwriting.lotus.util.JsonFile;

import java.io.IOException;
import java.util.List;

public class SendRoleMessage implements Command {
    @Override
    public void onCommand(Member sender, String label, String[] args, List<Message.Attachment> attachments, TextChannel channel) {
        boolean permission = false;
        for (Role role : sender.getRoles()){
            if (role.getName().equals("Co Owner") || role.getName().equals("Jodas' Bitch"))
                permission = true;
        }

        if (!permission){
            Lotus.sendMessage("You do not have permission to use that command.", channel);
            return;
        }

        MessageBuilder builder = new MessageBuilder();
        builder.allowMentions(Message.MentionType.USER, Message.MentionType.ROLE);
        builder.mentionRoles("819115150069858334", "829716177642323979");
        builder.setContent("Here are the currently available roles: \n\n" +
                "<@&819115150069858334>  -  Anyone with this role will be pinged when a raid run is starting \n" +
                "To acquire this role, react to this message with :star: \n\n" + "<@&829716177642323979>  -  " +
                "Anyone with this role will be pinged when any general announcements are made. To acquire this role, " +
                "react with :mega:");
        Message message = builder.build();
        JsonFile file = new JsonFile("role message", "messages");
        file.set("messageText", message.getContentRaw());
        try {
            file.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
        TextChannel roleChannel = Lotus.getChannel("roles");

        String id = roleChannel.getLatestMessageId();
        MessageHistory history = roleChannel.getHistory();
        history.retrievePast(100).complete();
        Message lastMessage = history.getMessageById(id);
        JsonFile messageFile = new JsonFile("role message", "messages");
        String messageText = messageFile.getString("messageText");
        if (messageText.contains(lastMessage.getContentRaw())){
            lastMessage.editMessage(builder.build()).queue();
            lastMessage.addReaction("\uD83D\uDCE3").queue();
            Lotus.sendMessage("Roles message updated.", channel);
        }else{
            Lotus.sendMessage("The roles message failed to update.", channel);
        }
    }
}
