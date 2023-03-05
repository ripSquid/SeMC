package net.fabricmc.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ExampleMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("modid");

	public static String address = null;


	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		File configFile = new File(FabricLoader.getInstance().getConfigDir() + "/SeMC.cfg");

		if ( !configFile.exists() ) {
			try {
				configFile.createNewFile();
				FileWriter fw = new FileWriter(configFile);
				fw.write("http://127.0.0.1:38745");
				fw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		String ipAddress = null;

		try {
			Scanner fr = new Scanner(configFile);
			ipAddress = fr.nextLine();
			fr.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}

		address = ipAddress;

		ServerTickEvents.END_SERVER_TICK.register(new ServerTickListener());
	}
}
