package com.jtprince.coordinateoffset.impl;

import com.jtprince.coordinateoffset.api.CoordinateOffsetAPIImpl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class PaperOffsetListener implements Listener {
    private final CoordinateOffsetAPIImpl api;

    PaperOffsetListener(CoordinateOffsetAPIImpl api) {
        this.api = api;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        api.ensureOffset(event.getPlayer());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        api.resetOffset(player);
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        api.resetOffset(event.getPlayer());
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.getFrom().getWorld() != event.getTo().getWorld()) {
            api.resetOffset(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        api.clearOffset(event.getPlayer());
    }
}
