package me.starzebra.events;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PlayerMoveEvent extends Event {

    @Cancelable
    public static class Pre extends Event {}

    public static class Post extends Event {}
}
