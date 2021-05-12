package net.whispwriting.lotus.events;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.IEventManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EventManager implements IEventManager {

    @Override
    public void register(@NotNull Object listener) {

    }

    @Override
    public void unregister(@NotNull Object listener) {

    }

    @Override
    public void handle(@NotNull GenericEvent event) {
        
    }

    @NotNull
    @Override
    public List<Object> getRegisteredListeners() {
        return null;
    }
}
