package net.whispwriting.lotus.util.auto_announcer;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.whispwriting.lotus.Lotus;
import net.whispwriting.lotus.util.JsonFile;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Announcement {

    private final String name, role;
    private final int targetDay, targetHour, targetMin, targetSec;
    private final Message message;
    private final String messageText;
    private final TextChannel channel;
    private final TaskExecutor executor;
    private Lotus bot = Lotus.getInstance();

    public Announcement(String name, String messageText, String role, TextChannel channel, int targetDay, int targetHour,
                        int targetMin, int targetSec) {
        MessageBuilder builder = new MessageBuilder();
        builder.allowMentions(Message.MentionType.USER, Message.MentionType.ROLE);
        builder.mentionRoles(role);
        String roleFormatted = "<@&" + role + ">";
        builder.setContent(roleFormatted + " " + messageText);

        this.message = builder.build();
        this.channel = channel;
        this.name = name;
        this.role = role;
        this.targetDay = targetDay;
        this.targetHour = targetHour;
        this.targetMin = targetMin;
        this.targetSec = targetSec;
        this.messageText = messageText;

        executor = new TaskExecutor();
        executor.startExecutionAt(targetDay, targetHour, targetMin, targetSec);
    }

    private void send(){
        bot.sendMessage(message, channel);
    }

    public void save() throws IOException {
        JsonFile file = new JsonFile(name, "announcements");
        file.set("role", role);
        file.set("targetDay", targetDay);
        file.set("targetHour", targetHour);
        file.set("targetMin", targetMin);
        file.set("targetSec", targetSec);
        file.set("channel", channel.getName());
        file.set("message", messageText);
        file.save();
    }

    public void terminate(){
        executor.stop();
    }

    private class TaskExecutor {
        private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        private volatile boolean isStopIssued;
        private volatile boolean firstRun = true;

        public void startExecutionAt(int targetDay, int targetHour, int targetMin, int targetSec)
        {
            Runnable task = new Runnable(){

                @Override
                public void run()
                {
                    send();
                    try {
                        Thread.sleep(5000);
                        startExecutionAt(targetDay, targetHour, targetMin, targetSec);
                    } catch (InterruptedException e) {
                        System.err.println("Failed to delay auto message execution call: " + e.getMessage());
                        e.printStackTrace();
                    }
                }

            };
            long delay = computeNextDelay(targetDay, targetHour, targetMin, targetSec);
            executorService.schedule(task, delay, TimeUnit.SECONDS);
        }

        private long computeNextDelay(int targetDay, int targetHour, int targetMin, int targetSec)
        {
            LocalDateTime localNow = LocalDateTime.now();
            int currentDay = localNow.getDayOfWeek().getValue();
            int difference = targetDay - currentDay;
            ZoneId currentZone = ZoneId.systemDefault();
            ZonedDateTime zonedNow = ZonedDateTime.of(localNow, currentZone);
            ZonedDateTime zonedNextTarget = zonedNow.withHour(targetHour).withMinute(targetMin).withSecond(targetSec);
            if(zonedNow.compareTo(zonedNextTarget) > 0)
                if (firstRun) {
                    firstRun = false;
                    if (difference > 0)
                        zonedNextTarget = zonedNextTarget.plusDays(difference);
                    else
                        zonedNextTarget = zonedNextTarget.minusDays(difference);
                }else{
                    zonedNextTarget = zonedNextTarget.plusDays(7);
                }

            Duration duration = Duration.between(zonedNow, zonedNextTarget);
            return duration.getSeconds();
        }

        public void stop()
        {
            executorService.shutdownNow();
        }
    }
}
