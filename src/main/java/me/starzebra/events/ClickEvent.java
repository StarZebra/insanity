package me.starzebra.events;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ClickEvent extends Event{

    @Cancelable
    public static class LeftClickEvent extends ClickEvent{}
}


