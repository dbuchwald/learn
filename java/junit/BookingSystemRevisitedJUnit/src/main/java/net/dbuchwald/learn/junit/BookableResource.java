package net.dbuchwald.learn.junit;

import java.util.Set;

/**
 * Created by dawidbuchwald on 18.02.2017.
 */
public interface BookableResource {
    String getId();

    int getCapacity();

    Set<Equipment> getEquipment();
}
