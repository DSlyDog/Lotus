package net.whispwriting.lotus;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.*;
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
import net.whispwriting.lotus.util.auto_announcer.Announcement;

import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.Map;

public class Lotus {

    private JDA jda;
    private String prefix = "lo!";
    private CommandHandler handler = new CommandHandler();
    private MessageEvent messageEvent = new MessageEvent();
    private ReactionEvent reactionEvent = new ReactionEvent();
    private Map<TextChannel, Announcer> announcerChannels;
    private List<Announcement> announcements;
    private boolean running;
    private String avatar;

    private static Lotus instance;

    private Lotus(){}

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
                announcements = AutoAnnouncerLoader.loadAutoMessages();
                Main.getLogger().info("Assets loaded");
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
        //RaidNotify.start();
    }

    public JDA getJDA() {
        return jda;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getAvatar() {
        return avatar;
    }

    public Map<TextChannel, Announcer> getAnnouncerChannels() {
        return announcerChannels;
    }

    public void setPresence(Activity activity){
        jda.getPresence().setActivity(activity);
    }

    public void registerCommand(String label, Command command){
        handler.registerCommand(label, command);
    }

    public void stop(){
        running = false;
        jda.shutdownNow();
        for (Announcement announcement : announcements){
            announcement.terminate();
        }
    }

    public void sendMessage(Message message, TextChannel channel){
        channel.sendTyping().queue();
        try{
            Thread.sleep(100);
            channel.sendMessage(message).queue();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void sendMessage(MessageBuilder builder, TextChannel channel){
        sendMessage(builder.build(), channel);
    }

    public TextChannel getChannel(String name){
        Guild guild = jda.getGuilds().get(0);
        try {
            return guild.getTextChannelsByName(name, true).get(0);
        }catch(IndexOutOfBoundsException e){
            return null;
        }
    }

    public Role getRole(String name){
        Guild guild = jda.getGuilds().get(0);
        try{
            return guild.getRolesByName(name, true).get(0);
        }catch(IndexOutOfBoundsException e){
            return null;
        }
    }

    public List<TextChannel> getChannels(){
        return jda.getTextChannels();
    }

    public void sendMessage(String message, TextChannel channel){
        MessageBuilder builder = new MessageBuilder();
        builder.setContent(message);
        sendMessage(builder, channel);
    }

    public static Lotus getInstance(){
        if (instance == null)
            instance = new Lotus();

        return instance;
    }
}
