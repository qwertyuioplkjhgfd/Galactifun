package io.github.addoncommunity.galactifun.api.aliens;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.AccessLevel;
import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpellCastEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import io.github.addoncommunity.galactifun.Galactifun;
import me.mrCookieSlime.Slimefun.cscorelib2.data.PersistentDataAPI;

public final class AlienManager implements Listener, Runnable {

    @Getter
    private final NamespacedKey key;
    @Getter
    private final NamespacedKey bossKey;
    @Getter(AccessLevel.PACKAGE)
    private final Map<LivingEntity, BossBar> bossInstances = new HashMap<>();
    private final Map<String, Alien<?>> aliens = new HashMap<>();

    public AlienManager(Galactifun galactifun) {
        galactifun.registerListener(this);
        galactifun.scheduleRepeatingSync(this, galactifun.getConfig().getInt("aliens.tick-interval", 1, 20));

        this.key = new NamespacedKey(galactifun, "alien");
        this.bossKey = Galactifun.instance().getKey("galactifun_boss");
    }

    void register(Alien<?> alien) {
        this.aliens.put(alien.id(), alien);
    }

    @Nullable
    public Alien<?> Alien(@Nonnull String id) {
        return this.aliens.get(id);
    }

    @Nullable
    public Alien<?> Alien(@Nonnull Entity entity) {
        String id = PersistentDataAPI.getString(entity, this.key);
        return id == null ? null : Alien(id);
    }

    @Nonnull
    public Collection<Alien<?>> Aliens() {
        return Collections.unmodifiableCollection(this.aliens.values());
    }

    @Override
    public void run() {
        for (Alien<?> alien : this.aliens.values()) {
            alien.onUniqueTick();
        }

        for (World world : Bukkit.getWorlds()) {
            for (LivingEntity entity : world.getLivingEntities()) {
                Alien<?> alien = Alien(entity);
                if (alien != null) {
                    alien.onMobTick(alien.clazz().cast(entity));
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onAlienTar(@Nonnull EntityTargetEvent e) {
        Alien<?> alien = Alien(e.getEntity());
        if (alien != null) {
            alien.onTarget(e);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onAlienInteract(@Nonnull PlayerInteractEntityEvent e) {
        Alien<?> alien = Alien(e.getRightClicked());
        if (alien != null) {
            alien.onInteract(e);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onAlienHit(@Nonnull EntityDamageByEntityEvent e) {
        Alien<?> alien = Alien(e.getEntity());
        if (alien != null) {
            alien.onHit(e);
        }
        alien = Alien(e.getDamager());
        if (alien != null) {
            alien.onAttack(e);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onAlienDie(@Nonnull EntityDeathEvent e) {
        Alien<?> alien = Alien(e.getEntity());
        if (alien != null) {
            alien.onDeath(e);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onAlienCombust(@Nonnull EntityCombustEvent e) {
        Alien<?> alien = Alien(e.getEntity());
        if (alien != null) {
            e.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onAlienCastSpell(@Nonnull EntitySpellCastEvent e) {
        Alien<?> alien = Alien(e.getEntity());
        if (alien != null) {
            alien.onCastSpell(e);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onAlienDamage(@Nonnull EntityDamageEvent e) {
        Alien<?> alien = Alien(e.getEntity());
        if (alien != null) {
            alien.onDamage(e);
        }
    }

}
