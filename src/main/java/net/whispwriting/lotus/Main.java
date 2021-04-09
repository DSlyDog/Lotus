package net.whispwriting.lotus;

import net.whispwriting.lotus.command.CommandDelegate;

import javax.security.auth.login.LoginException;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {

    private static boolean listen = true;

    public static void main(String[] args){

        if (args.length < 1){
            System.err.println("You must provide the bot token");
            return;
        }

        Lotus bot = new Lotus();
        try{
            bot.init(args[0]);
        }catch (LoginException e){
            System.err.println("Failed to connect to the Discord bot");
            e.printStackTrace();
            return;
        }

        CommandDelegate.registerCommands(bot);

        listen(bot);
    }

    public static void listen(Lotus bot){
        Scanner sc = new Scanner(System.in);
        while (listen){
            String line = sc.nextLine();
            onCommandLineCmd(line, bot);
        }
    }

    public static void onCommandLineCmd(String cmd, Lotus bot){
        switch (cmd){
            case "stop":
                System.out.println("Shutting down bot...");
                listen = false;
                bot.stop();
                break;
            case "time":
                LocalDateTime time = LocalDateTime.now();
                System.out.println(time.getHour());
                break;
            default:
                System.out.println("Command not found");
                break;
        }
    }
}
