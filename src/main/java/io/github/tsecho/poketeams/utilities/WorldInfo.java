package io.github.tsecho.poketeams.utilities;

import com.flowpowered.math.vector.Vector3d;
import io.github.tsecho.poketeams.configuration.ConfigManager;
import org.spongepowered.api.Sponge;

import java.util.UUID;

public class WorldInfo {
	
	private static UUID WORLD_UUID;
	private static Vector3d posA, posB;
	
	public static void init() {
		WORLD_UUID = Sponge.getServer().getDefaultWorld().get().getUniqueId();
	}

	public static void refreshPos() {
		double x1 = ConfigManager.getConfNode("Battle-Settings", "Arena", "LocationA", "X").getDouble();
		double y1 = ConfigManager.getConfNode("Battle-Settings", "Arena", "LocationA", "Y").getDouble();
		double z1 = ConfigManager.getConfNode("Battle-Settings", "Arena", "LocationA", "Z").getDouble();
		double x2 = ConfigManager.getConfNode("Battle-Settings", "Arena", "LocationB", "X").getDouble();
		double y2 = ConfigManager.getConfNode("Battle-Settings", "Arena", "LocationB", "Y").getDouble();
		double z2 = ConfigManager.getConfNode("Battle-Settings", "Arena", "LocationB", "Z").getDouble();

		posA = new Vector3d(x1,y1,z1);
		posB = new Vector3d(x2,y2,z2);
	}

	public static Vector3d getPosA() {
		return posA;
	}

	public static Vector3d getPosB() {
		return posB;
	}

	public static UUID getWorldUUID() {
		return WORLD_UUID;
	}

	public static boolean useArena() {
		return ConfigManager.getConfNode("Battle-Settings", "Arena", "Enabled").getBoolean();
	}
}
