package com.jtprince.coordinateoffset.api;

import com.jtprince.coordinateoffset.Offset;
import com.jtprince.coordinateoffset.adapter.OffsetLocation;
import com.jtprince.coordinateoffset.adapter.OffsetPlayer;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

/**
 * API for the CoordinateOffset plugin.
 *
 * <p>External plugins may get a singleton instance of this API via {@link CoordinateOffset#api()}.</p>
 */
@NullMarked
public interface CoordinateOffsetAPI {
    /**
     * Get the currently active coordinate {@link Offset} for a player.
     *
     * <p>Offsets are <b>subtracted</b> from a player's real coordinates to determine the coordinates they see.
     * As an example, a player might have a coordinate offset of <code>(128, 128)</code>. This would mean that the
     * player standing at <code>(128, 128)</code> sees that they are standing at <code>(0, 0)</code> in the "F3"
     * menu.</p> Similarly, if the player moves to the world's origin <code>(0, 0)</code>, they would see in "F3" that
     * they are standing at <code>(-128, -128)</code>.
     *
     * <p>This Offset is subject to change, for example if the Player changes worlds.</p>
     *
     * @param player A player currently logged in to the server. Use {@link #adaptPlayer(Object)} to convert
     *               a platform-specific instance (such as a Bukkit <code>Player</code>) to an {@link OffsetPlayer}, or
     *               {@link #getPlayer(UUID)} to get a player by their UUID.
     * @return The coordinate Offset this player sees, or <code>Offset.ZERO</code> if the player has no offset.
     */
    Offset getOffset(OffsetPlayer player);

    /**
     * Get an {@link OffsetPlayer} instance for a player currently connected to the server by their UUID.
     *
     * @param playerUuid The UUID of a player currently connected to the server.
     * @return An OffsetPlayer instance for the player, or null if no matching player is connected.
     */
    @Nullable OffsetPlayer getPlayer(UUID playerUuid);

    /**
     * Adapt a platform-specific player object (such as a Bukkit <code>Player</code>) into an {@link OffsetPlayer}.
     *
     * @param platformPlayerObject A platform-specific player object. The exact type depends on the platform adapter
     *                             in use. For example, on a Paper server, this would be an instance of
     *                             <code>org.bukkit.entity.Player</code>.
     * @return An OffsetPlayer instance for the player.
     * @throws ClassCastException if the provided object is not of the expected type for the running platform.
     */
    OffsetPlayer adaptPlayer(Object platformPlayerObject) throws ClassCastException;

    /**
     * Adapt a platform-specific location object (such as a Bukkit <code>Location</code>) into an
     * {@link OffsetLocation}.
     *
     * @param platformLocationObject A platform-specific location object. The exact type depends on the platform adapter
     *                               in use. For example, on a Paper server, this would be an instance of
     *                               <code>org.bukkit.Location</code>.
     * @return An OffsetLocation instance for the location.
     * @throws ClassCastException if the provided object is not of the expected type for the running platform.
     */
    OffsetLocation adaptLocation(Object platformLocationObject) throws ClassCastException;

    /**
     * Set the active coordinate {@link Offset} for a player.
     *
     * <p>This allows API consumers to override the automatically generated offset for a player. The provided offset
     * will immediately take effect for the player if they are online.</p>
     *
     * @param player The player whose offset should be changed.
     * @param offset The new offset value to apply.
     */
    void setOffset(OffsetPlayer player, Offset offset);

    /**
     * Clear any stored offset for a player, causing the default implementation to generate a new offset the next time
     * it is required.
     *
     * @param player The player whose offset should be cleared.
     */
    void clearOffset(OffsetPlayer player);
}
