package net.whispwriting.lotus;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.whispwriting.lotus.command.Command;
import net.whispwriting.lotus.command.CommandHandler;
import net.whispwriting.lotus.events.MessageEvent;
import net.whispwriting.lotus.events.RaidNotify;
import net.whispwriting.lotus.events.ReactionEvent;
import net.whispwriting.lotus.util.Announcer;

import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.Map;

public class Lotus {

    private static JDA jda;
    public static String prefix = "lo!";
    private CommandHandler handler = new CommandHandler();
    private MessageEvent messageEvent = new MessageEvent();
    private ReactionEvent reactionEvent = new ReactionEvent();
    public static Map<TextChannel, Announcer> announcerChannels;
    public static boolean running;
    public static String avatar;

    public static JDA getJDA() {
        return jda;
    }

    public void init(String token) throws LoginException {
        running = true;
        Thread postLoad = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                announcerChannels = AutoAnnouncerLoader.loadAutoAnnouncers();
                System.out.println("Auto announcers loaded");
            }
        });
        postLoad.start();
        jda = JDABuilder.createDefault(token)
                .setChunkingFilter(ChunkingFilter.ALL)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setEnabledIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                        GatewayIntent.DIRECT_MESSAGE_TYPING, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_BANS,
                        GatewayIntent.GUILD_EMOJIS, GatewayIntent.GUILD_INVITES, GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGE_REACTIONS)
                .enableCache(CacheFlag.VOICE_STATE)
                .build();
        jda.getPresence().setActivity(Activity.playing("World of Warcraft"));
        jda.addEventListener(handler);
        jda.addEventListener(messageEvent);
        jda.addEventListener(reactionEvent);
        avatar = jda.getSelfUser().getAvatarUrl();
        RaidNotify.start();
    }

    public static void setPresence(Activity activity){
        jda.getPresence().setActivity(activity);
    }

    public void registerCommand(String label, Command command){
        handler.registerCommand(label, command);
    }

    public void stop(){
        running = false;
        jda.shutdownNow();
    }

    public static void sendMessage(Message message, TextChannel channel){
        channel.sendTyping().queue();
        try{
            Thread.sleep(100);
            channel.sendMessage(message).queue();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public static void sendMessage(MessageBuilder builder, TextChannel channel){
        sendMessage(builder.build(), channel);
    }

    public static TextChannel getChannel(String name){
        Guild guild = jda.getGuilds().get(0);
        try {
            return guild.getTextChannelsByName(name, true).get(0);
        }catch(IndexOutOfBoundsException e){
            System.err.println("No channel not found: " + name);
            return null;
        }
    }

    public static List<TextChannel> getChannels(){
        return jda.getTextChannels();
    }

    public static void sendMessage(String message, TextChannel channel){
        MessageBuilder builder = new MessageBuilder();
        builder.setContent(message);
        sendMessage(builder, channel);
    }
}
