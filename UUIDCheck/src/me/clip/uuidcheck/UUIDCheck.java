package me.clip.uuidcheck;

import org.bukkit.plugin.java.JavaPlugin;

public class UUIDCheck extends JavaPlugin {

	private Commands commands = new Commands(this);
	
	@Override
	public void onEnable() {
		getCommand("cp").setExecutor(commands);
	}
	
	@Override
	public void onDisable() {	
	}
}
