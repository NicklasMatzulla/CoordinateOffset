package com.jtprince.coordinateoffset.api;

import com.jtprince.coordinateoffset.Offset;
import com.jtprince.coordinateoffset.adapter.OffsetLocation;
import com.jtprince.coordinateoffset.adapter.OffsetPlayer;
import com.jtprince.coordinateoffset.impl.OffsetManager;
import com.jtprince.coordinateoffset.impl.PaperOffsetLocation;
import com.jtprince.coordinateoffset.impl.PaperOffsetPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

@NullMarked
public final class CoordinateOffsetAPIImpl implements CoordinateOffsetAPI {
    private static final String BYPASS_PERMISSION = "coordinateoffset.bypass";

    private final JavaPlugin plugin;
    private final OffsetManager offsetManager;

    public CoordinateOffsetAPIImpl(JavaPlugin plugin, OffsetManager offsetManager) {
        this.plugin = plugin;
        this.offsetManager = offsetManager;
    }

    @Override
    public Offset getOffset(OffsetPlayer player) {
        PaperOffsetPlayer paperPlayer = ensurePaperPlayer(player);
        if (paperPlayer.hasPermission(BYPASS_PERMISSION)) {
            return Offset.ZERO;
        }
        return offsetManager.getOrCreateOffset(paperPlayer);
    }

    @Override
    public @Nullable OffsetPlayer getPlayer(UUID playerUuid) {
        Player player = plugin.getServer().getPlayer(playerUuid);
        if (player == null || !player.isOnline()) {
            return null;
        }
        return new PaperOffsetPlayer(player);
    }

    @Override
    public OffsetPlayer adaptPlayer(Object platformPlayerObject) throws ClassCastException {
        if (!(platformPlayerObject instanceof Player player)) {
            if (platformPlayerObject == null) {
                throw new ClassCastException("Expected a Paper Player instance but got null");
            }
            throw new ClassCastException("Expected a Paper Player instance but got " + platformPlayerObject.getClass());
        }
        return new PaperOffsetPlayer(player);
    }

    @Override
    public OffsetLocation adaptLocation(Object platformLocationObject) throws ClassCastException {
        if (!(platformLocationObject instanceof Location location)) {
            if (platformLocationObject == null) {
                throw new ClassCastException("Expected a Paper Location instance but got null");
            }
            throw new ClassCastException("Expected a Paper Location instance but got " + platformLocationObject.getClass());
        }
        return new PaperOffsetLocation(location);
    }

    @Override
    public void setOffset(OffsetPlayer player, Offset offset) {
        PaperOffsetPlayer paperPlayer = ensurePaperPlayer(player);
        offsetManager.setOffset(paperPlayer, offset);
    }

    @Override
    public void clearOffset(OffsetPlayer player) {
        PaperOffsetPlayer paperPlayer = ensurePaperPlayer(player);
        offsetManager.clearOffset(paperPlayer);
    }

    public void ensureOffset(Player player) {
        offsetManager.getOrCreateOffset(new PaperOffsetPlayer(player));
    }

    public void resetOffset(Player player) {
        offsetManager.resetOffset(new PaperOffsetPlayer(player));
    }

    public void clearOffset(Player player) {
        offsetManager.clearOffset(new PaperOffsetPlayer(player));
    }

    private PaperOffsetPlayer ensurePaperPlayer(OffsetPlayer player) {
        if (player instanceof PaperOffsetPlayer paper) {
            return paper;
        }
        Object platform = player.getPlatformPlayerObject();
        if (platform instanceof Player bukkitPlayer) {
            return new PaperOffsetPlayer(bukkitPlayer);
        }
        throw new IllegalArgumentException("OffsetPlayer is not backed by a Paper player instance.");
    }

    public void shutdown() {
        offsetManager.clearAll();
    }

    public static void set(CoordinateOffsetAPI api) {
        CoordinateOffset.set(api);
    }
}
