package com.modishmonkee.arsenalofextinction.entity;

import com.modishmonkee.arsenalofextinction.ArsenalOfExtinction;
import com.modishmonkee.arsenalofextinction.entity.custom.NukeEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@EventBusSubscriber(modid = ArsenalOfExtinction.MOD_ID)
public class NukeManager {

    private static final List<NukeEntity> activeNukes = new ArrayList<>();
    private static final Map<Runnable, Integer> scheduledTasks = new LinkedHashMap<>();

    public static void register(NukeEntity nuke) {
        activeNukes.add(nuke);
    }

    public static void scheduleTask(int delayTicks, Runnable task) {
        scheduledTasks.put(task, delayTicks + 1);
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        activeNukes.removeIf(NukeEntity::isRemoved);

        Iterator<Map.Entry<Runnable, Integer>> it = scheduledTasks.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Runnable, Integer> entry = it.next();
            int remaining = entry.getValue() - 1;
            if (remaining <= 0) {
                entry.getKey().run();
                it.remove();
            } else {
                entry.setValue(remaining);
            }
        }
    }
}
