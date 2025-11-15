package com.jtprince.coordinateoffset.impl;

import com.jtprince.coordinateoffset.Offset;
import com.jtprince.coordinateoffset.adapter.OffsetLocation;
import org.bukkit.Location;
import org.bukkit.World;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class PaperOffsetLocation implements OffsetLocation {
    private final Location location;

    public PaperOffsetLocation(Location location) {
        this.location = location.clone();
    }

    @Override
    public @Nullable String getWorldName() {
        World world = location.getWorld();
        return world == null ? null : world.getName();
    }

    @Override
    public double getX() {
        return location.getX();
    }

    @Override
    public double getY() {
        return location.getY();
    }

    @Override
    public double getZ() {
        return location.getZ();
    }

    @Override
    public OffsetLocation apply(Offset offset) {
        Location clone = location.clone();
        clone.setX(clone.getX() - offset.x());
        clone.setZ(clone.getZ() - offset.z());
        return new PaperOffsetLocation(clone);
    }

    @Override
    public OffsetLocation unapply(Offset offset) {
        Location clone = location.clone();
        clone.setX(clone.getX() + offset.x());
        clone.setZ(clone.getZ() + offset.z());
        return new PaperOffsetLocation(clone);
    }

    @Override
    public Object getPlatformLocationObject() {
        return location.clone();
    }
}
