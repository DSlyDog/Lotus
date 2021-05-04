package net.whispwriting.lotus.events;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.whispwriting.lotus.Lotus;

import java.time.LocalDateTime;

public class RaidNotify {

    public static void start(){
        Thread raidNotify = new Thread(new Runnable() {
            @Override
            public void run() {
                while (Lotus.running){
                    LocalDateTime current = LocalDateTime.now();
                    if ((current.getDayOfWeek().name().equals("TUESDAY") || current.getDayOfWeek().name().equals("THURSDAY"))
                            && current.getHour() == 19 && current.getMinute() == 30 && current.getSecond() == 0){
                        TextChannel channel = Lotus.getChannel("announcements");
                        MessageBuilder builder = new MessageBuilder();
                        builder.allowMentions(Message.MentionType.USER, Message.MentionType.ROLE);
                        builder.mentionRoles("819115150069858334");
                        builder.setContent("<@&819115150069858334> A raid run will start in approximately two hours " +
                                "(9pm EST). If you plan to come, please be ready for an invite in approximately " +
                                "ninety minutes.");
                        Message message = builder.build();
                        Lotus.sendMessage(message, channel);
                    }

                    if ((current.getDayOfWeek().name().equals("TUESDAY") || current.getDayOfWeek().name().equals("THURSDAY"))
                            && current.getHour() == 21 && current.getMinute() == 0 && current.getSecond() == 0){
                        TextChannel channel = Lotus.getChannel("announcements");
                        MessageBuilder builder = new MessageBuilder();
                        builder.allowMentions(Message.MentionType.USER, Message.MentionType.ROLE);
                        builder.mentionRoles("819115150069858334");
                        builder.setContent("<@&819115150069858334> Invites are now going out.");
                        Message message = builder.build();
                        Lotus.sendMessage(message, channel);
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        raidNotify.start();
    }
}
