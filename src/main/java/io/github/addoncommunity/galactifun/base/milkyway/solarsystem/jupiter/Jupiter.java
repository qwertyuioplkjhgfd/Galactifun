package io.github.addoncommunity.galactifun.base.milkyway.solarsystem.jupiter;

import io.github.addoncommunity.galactifun.api.universe.CelestialBody;
import io.github.addoncommunity.galactifun.api.universe.attributes.DayCycle;
import io.github.addoncommunity.galactifun.api.universe.attributes.Gravity;
import io.github.addoncommunity.galactifun.api.universe.attributes.Orbit;
import io.github.addoncommunity.galactifun.api.universe.attributes.atmosphere.Atmosphere;
import io.github.addoncommunity.galactifun.api.universe.attributes.atmosphere.AtmosphereBuilder;
import io.github.addoncommunity.galactifun.api.universe.type.CelestialType;
import io.github.addoncommunity.galactifun.core.util.ItemChoice;
import org.bukkit.Material;

import javax.annotation.Nonnull;

public class Jupiter extends CelestialBody {

    public Jupiter(@Nonnull CelestialBody... moons) {
        super("Jupiter", new Orbit(778_340_821L), CelestialType.GAS_GIANT, new ItemChoice(Material.RED_DYE), moons);
    }

    @Nonnull
    @Override
    protected DayCycle createDayCycle() {
        return new DayCycle(10);
    }

    @Nonnull
    @Override
    protected Atmosphere createAtmosphere() {
        return new AtmosphereBuilder().addStorm().addThunder().enableFire().enableWeather().build();
    }

    @Nonnull
    @Override
    protected Gravity createGravity() {
        return new Gravity(24.79);
    }

    @Override
    protected long createSurfaceArea() {
        return 0;
    }
}
