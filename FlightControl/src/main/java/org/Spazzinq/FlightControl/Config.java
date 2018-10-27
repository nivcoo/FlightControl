package org.Spazzinq.FlightControl;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;

import java.io.File;
import java.io.IOException;
import java.util.*;

class Config {
	private FlightControl pl;
	private FileConfiguration c;
    private PluginManager pm;
    private File dTrailF;
	private FileConfiguration dTrailC;

	static boolean is13, useCombat, cancelFall, vanishBypass, flightTrail, actionBar;
	static Sound eSound, dSound;
	static String dFlight, eFlight, permDenied;
    static HashMap<String, AbstractMap.SimpleEntry<List<String>, List<String>>> categories = new HashMap<>();
	static ArrayList<String> trailPrefs = new ArrayList<>();
	static ArrayList<String> worlds = new ArrayList<>();
	static HashMap<String, List<String>> regions = new HashMap<>();

	Config(FlightControl i) {
		pl = i;
		pm = pl.getServer().getPluginManager();
        is13 = pl.getServer().getVersion().contains("1.13");
		dTrailF = new File(pl.getDataFolder(), "disabled_trail.yml");
	}

	void loadConfig() {
	    pl.saveDefaultConfig();
		pl.reloadConfig();
		c = pl.getConfig();
		useCombat = c.getBoolean("disable_flight_in_combat");
		cancelFall = c.getBoolean("disable_fall_damage");
		vanishBypass = c.getBoolean("vanish_bypass");
		flightTrail = c.getBoolean("flight_trail");
		actionBar = c.getBoolean("messages.actionbar");
		dFlight = c.getString("messages.disable_flight");
		eFlight = c.getString("messages.enable_flight");
		permDenied = c.getString("messages.permission_denied");
        trailPrefs = worlds = new ArrayList<>(); regions = new HashMap<>(); categories = new HashMap<>();
        loadSounds(); loadTrailPrefs(); loadWorlds();
		if (pm.isPluginEnabled("WorldGuard")) loadRegions();
		if (pm.isPluginEnabled("Factions")) loadFacCategories();

        for (World w : Bukkit.getWorlds()) { String name = w.getName(); if (pm.getPermission("flightcontrol.autofly." + name) == null)
            pm.addPermission(new Permission("flightcontrol.autofly." + name, PermissionDefault.FALSE)); }
	}

	private void loadWorlds() {
		if (c.getStringList("disable_flight_worlds") != null && !c.getStringList("disable_flight_worlds").isEmpty())
		    for (String world : c.getStringList("disable_flight_worlds")) { if (Bukkit.getWorld(world) != null) worlds.add(world); }
		else pl.getServer().getLogger().warning("disable_flight_worlds is invalid!");
	}

	private void loadRegions() {
		if (c.isConfigurationSection("disable_flight_regions")) {
			Set<String> worlds = c.getConfigurationSection("disable_flight_regions").getKeys(false);
			if (worlds != null && !worlds.isEmpty()) {
				for (String world : worlds) {
					if (world != null && !world.isEmpty() && Bukkit.getWorld(world) != null) {
						List<String> worldRegions = c.getStringList("disable_flight_regions." + world);
						for (String region : worldRegions) {
							if (region != null && !region.isEmpty() && pl.regions.hasRegion(world, region)) {
								regions.put(world, worldRegions);
								if (pm.getPermission("flightcontrol.autofly." + world + "." + region) == null)
								    pm.addPermission(new Permission("flightcontrol.autofly." + world + "." + region, PermissionDefault.FALSE));
							}
						}
					}
				}
			} else pl.getServer().getLogger().warning("disable_flight_regions is invalid!");
		}
	}

	private void loadFacCategories() {
		if (c.isConfigurationSection("factions")) {
		    // Local categories
			Set<String> lCategories = c.getConfigurationSection("factions").getKeys(false);
			if (lCategories != null && !lCategories.isEmpty()) {
				for (String category : lCategories) {
					if (pm.getPermission("flightcontrol.factions." + category) == null) pm.addPermission(new Permission("flightcontrol.factions." + category, PermissionDefault.FALSE));
					Set<String> eAndD = c.getConfigurationSection("factions." + category).getKeys(false);
					if (eAndD != null && !eAndD.isEmpty() && (eAndD.contains("auto_flight") || eAndD.contains("disable_flight"))) {
						categories.put(category, new AbstractMap.SimpleEntry<>(
						        typeValidator(c.getStringList("factions." + category + "." + "auto_flight")),
                                typeValidator(c.getStringList("factions." + category + "." + "disable_flight"))));
					}
				}
			}
		}
	}

    private void loadSounds() {
	    String e = c.getString("sounds.enable_flight.value").toUpperCase().replaceAll("\\.", "_"),
                d = c.getString("sounds.disable_flight.value").toUpperCase().replaceAll("\\.", "_");
        if (Sound.is(e)) eSound = new Sound(e, (float) c.getDouble("sounds.enable_flight.volume"), (float) c.getDouble("sounds.enable_flight.pitch")); else eSound = null;
        if (Sound.is(d)) dSound = new Sound(d, (float) c.getDouble("sounds.disable_flight.volume"), (float) c.getDouble("sounds.disable_flight.pitch")); else dSound = null;
    }

	// Per-player disabled trails
	private void loadTrailPrefs() {
		if (!dTrailF.exists()) { try { //noinspection ResultOfMethodCallIgnored
            dTrailF.createNewFile(); } catch (IOException e) { e.printStackTrace(); } }
		dTrailC = YamlConfiguration.loadConfiguration(dTrailF);
		
		if (dTrailC.isList("disabled_trail")) {
			if (dTrailC.getStringList("disabled_trail") != null && !dTrailC.getStringList("disabled_trail").isEmpty()) {
				for (String uuid : dTrailC.getStringList("disabled_trail")) {
					try { if (pl.getServer().getPlayer(UUID.fromString(uuid)) != null || pl.getServer().getOfflinePlayer(UUID.fromString(uuid)) != null) trailPrefs.add(uuid); } catch (IllegalArgumentException ignored) { }
				}
			}
		} else dTrailC.createSection("disabled_trail");
	}

	private List<String> typeValidator(List<String> types) {
		if (types != null && !types.isEmpty()) {
			ArrayList<String> toRemove = new ArrayList<>();
			for (String type : types) {
				if (type == null || type.isEmpty()
						|| !(type.contains("OWN") || type.contains("ALLY") || type.contains("TRUCE")
								|| type.contains("NEUTRAL") || type.contains("ENEMY") || type.contains("WARZONE")
								|| type.contains("SAFEZONE") || type.contains("WILDERNESS"))) toRemove.add(type);
			}
			types.removeAll(toRemove);
		}
		return types;
	}

	// Saves personal trail preferences
	void disable() {
        dTrailC.set("disabled_trail", (trailPrefs != null && !trailPrefs.isEmpty()) ? trailPrefs : null);
        try { dTrailC.save(dTrailF); } catch (IOException e) { e.printStackTrace(); }
    }
}