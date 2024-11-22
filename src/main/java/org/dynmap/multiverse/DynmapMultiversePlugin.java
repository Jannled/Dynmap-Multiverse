package org.dynmap.multiverse;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.MarkerIcon;
import org.dynmap.markers.MarkerSet;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.utils.AnchorManager;

/**
 * A bridge to display Multiverse Anchors in Dynmap
 */
public class DynmapMultiversePlugin extends JavaPlugin implements Listener 
{
	private static Logger log;

	// Dynmap
	DynmapAPI pluginDynmap = null;
	private MarkerSet markerSet;
	private MarkerIcon iconAnchor;

	// Multiverse
	MultiverseCore pluginMultiverse = null;

	@Override
	public void onEnable() 
	{
		// Init routine
		Bukkit.getPluginManager().registerEvents(this, this);
		log = this.getLogger();

		getHooks();
		loadAllAnchors();
	}

	public void getHooks()
	{
		// Get the Dynmap Hooks
		PluginManager pm = getServer().getPluginManager();
		Plugin dynmap_plugin = pm.getPlugin("dynmap");
		if(dynmap_plugin == null)
			log.severe("Unable to find dynmap plugin!");

		pluginDynmap = (DynmapAPI) dynmap_plugin;

		markerSet = pluginDynmap.getMarkerAPI().createMarkerSet("multiverse_anchors", "Multiverse Anchors", null, false);
		iconAnchor = pluginDynmap.getMarkerAPI().getMarkerIcon(MarkerIcon.SIGN);

		// Get the Multiverse Hooks
		Plugin mv_plugin = pm.getPlugin("Multiverse-Core");
		if(mv_plugin == null)
			log.severe("Unable to find Multiverse-Core plugin!");

		pluginMultiverse = (MultiverseCore) mv_plugin;
	}

	public void loadAllAnchors()
	{
		AnchorManager am = pluginMultiverse.getAnchorManager();

		for(String aTitle : am.getAllAnchors())
		{
			Location anchor = am.getAnchorLocation(aTitle);

			addAnchor(aTitle, anchor.getWorld().getName(), anchor.getX(), anchor.getY(), anchor.getZ());
		}
	}

	public void addAnchor(String name, String world, double x, double y, double z)
	{
		markerSet.createMarker("mv_" + name, name, world, x, y, z, iconAnchor, false);
	}

	@Override
	public void onDisable()
	{
		super.onDisable();
		markerSet.deleteMarkerSet();
	}
}