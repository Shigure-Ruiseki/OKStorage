package ruiseki.okstorage.api.wrapper;

import net.minecraft.entity.Entity;

/**
 * An upgrade that can apply special behavior to the storage container
 * and/or entities nearby.
 */
public interface IEntityApplicable {

    /**
     * Called when the upgrade should affect entities near the storage container.
     *
     * @param nearbyEntity The nearby entity that the upgrade can affect
     */
    default void applyToNearbyEntity(Entity nearbyEntity) {}
}
