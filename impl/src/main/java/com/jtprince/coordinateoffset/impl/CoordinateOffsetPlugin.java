package com.jtprince.coordinateoffset.impl;

import com.jtprince.coordinateoffset.api.CoordinateOffsetAPIImpl;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public final class CoordinateOffsetPlugin extends JavaPlugin {
    private CoordinateOffsetAPIImpl api;
    private PaperOffsetListener listener;

    @Override
    public void onEnable() {
        api = new CoordinateOffsetAPIImpl(this, new OffsetManager());
        CoordinateOffsetAPIImpl.set(api);

        listener = new PaperOffsetListener(api);
        getServer().getPluginManager().registerEvents(listener, this);
    }

    @Override
    public void onDisable() {
        if (listener != null) {
            HandlerList.unregisterAll(listener);
            listener = null;
        }
        if (api != null) {
            api.shutdown();
            api = null;
        }
    }
}
