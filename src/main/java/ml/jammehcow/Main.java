package ml.jammehcow;

import ml.jammehcow.Config.Config;
import ml.jammehcow.Handlers.EventHandlers;
import ml.jammehcow.Handlers.MessageHandler;
//import ml.jammehcow.LavaPlayer.AudioManager;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.util.DiscordException;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static ml.jammehcow.Config.ConfigWrapper.getConfig;

/**
 * Author: jammehcow.
 * Date: 21/12/16.
 */

public class Main {
    // Sets logger to SLF4J with logback
    public static final Logger logger           = LoggerFactory.getLogger(Main.class);
    private static Config config                = getConfig();

    public static final String prefix           = config.prefix;
    public static final String YouTubeAPIKey    = config.youtubeAPIKey;

    public static boolean debug                 = false;
    public static IDiscordClient client;

    //public static AudioManager manager;

    public static void main(String[] args) throws DiscordException {
        if (config.token.equals("your_discord_bot_token")) throw new DiscordException("Your bot is using the placeholder token. You need to change the \"token\" field in the Hara config to your bot token.");

        List<String> argsList = Arrays.asList(args);

        if (argsList.contains("debug")) debug = true;

        Reflections reflections = new Reflections("sx.blah.discord.handle.impl.events");
        Set<Class<?extends Event>> subTypes = reflections.getSubTypesOf(Event.class);
        EventHandlers.events.addAll(subTypes);

        logger.info("Registered " + subTypes.size() + " events from Discord4J.");

        if (!argsList.contains("noclient")) {
            client = getClient();
            EventDispatcher dispatcher = client.getDispatcher();
            dispatcher.registerListener(new EventHandlers());
            dispatcher.registerListener(new MessageHandler());

            /*if (config.lavaplayer) {
                if (config.youtubeAPIKey.equals("your_YouTube_API_key")) {
                    throw new DiscordException("Your provided YouTube API key is the default key. \nCheck the config for instructions on obtaining one.");
                } else {
                    // stub
                }
            }*/
        }
    }

    private static IDiscordClient getClient() throws DiscordException {
        ClientBuilder clientBuilder = new ClientBuilder();
        clientBuilder.withToken(config.token);

        return clientBuilder.login();
    }
}
