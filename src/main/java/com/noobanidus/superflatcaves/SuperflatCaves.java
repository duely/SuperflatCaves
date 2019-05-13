package com.noobanidus.superflatcaves;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
@Mod(modid = SuperflatCaves.MODID, name = SuperflatCaves.MODNAME, version = SuperflatCaves.VERSION, dependencies = SuperflatCaves.DEPENDS)
@SuppressWarnings("WeakerAccess")
public class SuperflatCaves {
    public static final String MODID = "superflatcaves";
    public static final String MODNAME = "SuperflatCaves";
    public static final String VERSION = "GRADLE:VERSION";
    public static final String DEPENDS = "after:voidislandcontrol;";

    public final static Logger LOG = LogManager.getLogger(MODID);

    @Mod.Instance(SuperflatCaves.MODID)
    public static SuperflatCaves instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOG.info("Replacing FLAT world generator with delightful, cave-filled generator!");
        WorldTypeFlat.createFlatWorld();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        if (SuperflatConfig.SPAWN_IN_CAVES) {
            try {
                DimensionManager.unregisterDimension(0);
                DimensionManager.registerDimension(0, DimensionType.register("OVERWORLD_CAVES", "", 0, WorldProviderCaves.class, true));
            } catch (Exception e) {
                LOG.error("Unable to replace overworld provider with overworld_caves provider. Void spawning will now be problematic.");
                e.printStackTrace();
            }
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }

    @Mod.EventHandler
    public void onServerStarting (FMLServerStartingEvent event) {
        World world = DimensionManager.getWorld(0);
        world.setSeaLevel(23);
    }

    @Mod.EventHandler
    public void loadComplete(FMLLoadCompleteEvent event) {
        if (Loader.isModLoaded("voidislandcontrol")) {
            LOG.info("Allowing Void Island Control into the tendril-like folds of caves...");
            Handler.replaceVoid();
        }
    }

    @Config(modid=MODID)
    public static class SuperflatConfig {
        @Config.Comment("Set to false to prevent caves from generating above 128 (Vanilla Default")
        @Config.Name("Allow Higher Caves")
        public static boolean HIGHER_CAVES = true;

        @Config.Comment("Set to whatever value between 1-256 you wish caves to stop at")
        @Config.Name("Cave Height Limit")
        @Config.RangeInt(min=1, max=256)
        public static int CAVE_HEIGHT = 256;

        @Config.Comment("Set to false to prevent replacement of the World Provider. This means that stone-filled worlds will have their spawn point above the world. Set to true to replace, meaning a suitable cave will be found.")
        @Config.Name("Replace Provider For Spawning-In-Caves")
        public static boolean SPAWN_IN_CAVES = true;

        @Config.Comment("Number of times to attempt to find a spawn location.")
        @Config.Name("Spawn Location Attempts")
        public static int ATTEMPTS = 50;

        @Config.Comment("The average ground level the world provider should return for the overworld")
        @Config.Name("Average ground level")
        public static int AVERAGE_GROUND_LEVEL = 23;

        @Config.Comment("Set to true to override the celestial angle and prevent the day/night cycle from having a visual")
        @Config.Name("Override Celestial Angle")
        public static boolean OVERRIDE_CELESTIAL_ANGLE = true;

        @Config.Comment("Set to the value you wish the sun/moon to be at. 0f = midnight")
        @Config.Name("Celestial Angle Value")
        public static float CELESTIAL_ANGLE = 0.0f;

        @Config.Comment("Set to false to prevent the sky from being coloured")
        @Config.Name("Sky Coloured")
        public static boolean SKY_COLOURED = false;

        @Config.Comment("Set to true to override x/y shows fog from returning default and using the value specified below")
        @Config.Name("Override Fog Value")
        public static boolean OVERRIDE_FOG = true;

        @Config.Comment("Set to true or false to determine if all coordinates show or don't show fog")
        @Config.Name("Fog Value (Overriden)")
        public static boolean FOG_VALUE = false;
    }
}
