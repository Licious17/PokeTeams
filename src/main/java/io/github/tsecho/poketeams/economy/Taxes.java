package io.github.tsecho.poketeams.economy;

import io.github.tsecho.poketeams.apis.PokeTeamsAPI;
import io.github.tsecho.poketeams.enums.Messages.SuccessMessages;
import io.github.tsecho.poketeams.configuration.ConfigManager;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.util.TypeTokens;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.stream.Collectors;

public class Taxes {
	
	private static String team;
	private static int teamBal, cost;
	
	public static void tax() {		
		if(isTaxingEnabled() && isTaxTime()) {
			loopTeams(true);
		}
	}
	
	public static void warn() {
		if(isTaxingEnabled()) {
			loopTeams(false);
		}
	}
	
	private static void loopTeams(boolean tax) {
		ConfigManager.getStorNode("Teams").getChildrenMap().forEach((key, value) -> {

			team = key.toString();
			teamBal = ConfigManager.getStorNode("Teams", team, "Stats", "Bal").getInt();
			cost = ConfigManager.getConfNode("Team-Settings", "Money", "Tax").getInt();

			if (cantPay())
				if (tax)
					sendDeletion();
				else
					sendWarning();
			else if (tax)
				takeMoney();
		});
	}

	private static void takeMoney() {
		ConfigManager.getStorNode("Teams", team, "Members").getChildrenMap().entrySet().stream()
				.map(key -> key.getKey().toString())
				.forEach(membber -> Sponge.getServer().getPlayer(membber).ifPresent(p -> p.sendMessage(SuccessMessages.TAX_TAKEN.getText(p))));
		new PokeTeamsAPI(team, true).addBal(-1 * cost);
	}

	
	private static void sendDeletion() {
		ConfigManager.getStorNode("Teams", team, "Members").getChildrenMap().entrySet().stream()
				.map(key -> key.getKey().toString())
				.forEach(member -> Sponge.getServer().getPlayer(member).ifPresent(p -> p.sendMessage(SuccessMessages.DISBANDED.getText(p))));
		new PokeTeamsAPI(team, true).deleteTeam();
	}
	
	private static void sendWarning() {
		ConfigManager.getStorNode("Teams", team, "Members").getChildrenMap().entrySet().stream()
				.map(key -> key.getKey().toString())
				.forEach(membber -> Sponge.getServer().getPlayer(membber).ifPresent(p -> p.sendMessage(SuccessMessages.TAX_WARNING.getText(p))));
	}
	
	private static boolean cantPay() {
		return teamBal < cost;
	}
	
	private static boolean isTaxTime() {
		try {
			LocalTime now = Instant.now().atZone(ZoneId.systemDefault()).toLocalTime().truncatedTo(ChronoUnit.MINUTES);

			return ConfigManager.getConfNode("Team-Settings", "Money", "Tax-Timer").getList(TypeTokens.STRING_TOKEN).stream()
					.filter(i -> now.equals(LocalTime.of(Integer.valueOf(i.split(":")[0]), Integer.valueOf(i.split(":")[1]))))
					.collect(Collectors.toList()).size() > 0;

		} catch (ObjectMappingException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static boolean isTaxingEnabled() {
		return ConfigManager.getConfNode("Team-Settings", "Money", "Tax-Enabled").getBoolean();
	}
}
