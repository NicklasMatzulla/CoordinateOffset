package com.jtprince.coordinateoffset.impl;

import com.jtprince.coordinateoffset.Offset;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

@NullMarked
public final class OffsetManager {
    private static final int DEFAULT_RANDOM_BOUND_BLOCKS = 8192;

    private final Map<UUID, Map<String, Offset>> offsets = new ConcurrentHashMap<>();

    public Offset getOrCreateOffset(PaperOffsetPlayer player) {
        String worldName = worldName(player.getPlayer());
        return offsets.computeIfAbsent(player.getUuid(), uuid -> new ConcurrentHashMap<>())
            .computeIfAbsent(worldName, ignored -> randomOffset(player.getPlayer()));
    }

    public void setOffset(PaperOffsetPlayer player, Offset offset) {
        String worldName = worldName(player.getPlayer());
        offsets.computeIfAbsent(player.getUuid(), uuid -> new ConcurrentHashMap<>())
            .put(worldName, offset);
    }

    public void resetOffset(PaperOffsetPlayer player) {
        String worldName = worldName(player.getPlayer());
        offsets.computeIfAbsent(player.getUuid(), uuid -> new ConcurrentHashMap<>())
            .put(worldName, randomOffset(player.getPlayer()));
    }

    public void clearOffset(PaperOffsetPlayer player) {
        offsets.remove(player.getUuid());
    }

    public void clearAll() {
        offsets.clear();
    }

    private Offset randomOffset(Player player) {
        World world = player.getWorld();
        int bound = DEFAULT_RANDOM_BOUND_BLOCKS;
        int x = ThreadLocalRandom.current().nextInt(-bound, bound + 1);
        int z = ThreadLocalRandom.current().nextInt(-bound, bound + 1);
        if (world.getEnvironment() == World.Environment.NETHER) {
            return Offset.align(x, z, Offset.ALIGN_OVERWORLD);
        }
        return Offset.align(x, z);
    }

    private String worldName(Player player) {
        World world = player.getWorld();
        if (world == null) {
            throw new IllegalStateException("Player world is not available.");
        }
        return world.getName();
    }
}
