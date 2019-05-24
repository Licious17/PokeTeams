package io.github.tsecho.poketeams.economy;

import io.github.tsecho.poketeams.apis.PokeTeamsAPI;
import io.github.tsecho.poketeams.enums.messages.SuccessMessage;
import org.spongepowered.api.Sponge;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import static io.github.tsecho.poketeams.configuration.ConfigManager.getSettings;
import static io.github.tsecho.poketeams.configuration.ConfigManager.getStorNode;

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
		getStorNode("Teams").getChildrenMap().forEach((key, value) -> {

			team = key.toString();
			teamBal = getStorNode("Teams", team, "Stats", "Bal").getInt();
			cost = getSettings().team.money.tax;

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
		getStorNode("Teams", team, "Members").getChildrenMap().entrySet().stream()
				.map(key -> key.getKey().toString())
				.forEach(membber -> Sponge.getServer().getPlayer(membber).ifPresent(p -> p.sendMessage(SuccessMessage.TAX_TAKEN.getText(p))));
		new PokeTeamsAPI(team, true).addBal(-cost);
	}

	
	private static void sendDeletion() {
		getStorNode("Teams", team, "Members").getChildrenMap().entrySet().stream()
				.map(key -> key.getKey().toString())
				.forEach(member -> Sponge.getServer().getPlayer(member).ifPresent(p -> p.sendMessage(SuccessMessage.DISBANDED.getText(p))));
		new PokeTeamsAPI(team, true).deleteTeam();
	}
	
	private static void sendWarning() {
		getStorNode("Teams", team, "Members").getChildrenMap().entrySet().stream()
				.map(key -> key.getKey().toString())
				.forEach(member -> Sponge.getServer().getPlayer(member).ifPresent(p -> p.sendMessage(SuccessMessage.TAX_WARNING.getText(p))));
	}
	
	private static boolean cantPay() {
		return teamBal < cost;
	}
	
	private static boolean isTaxTime() {
		LocalTime now = Instant.now().atZone(ZoneId.systemDefault()).toLocalTime().truncatedTo(ChronoUnit.MINUTES);
		return getSettings().team.money.taxTimes.stream()
				.filter(i -> now.equals(LocalTime.of(Integer.valueOf(i.split(":")[0]), Integer.valueOf(i.split(":")[1]))))
				.collect(Collectors.toList()).size() > 0;

	}
	
	private static boolean isTaxingEnabled() {
		return getSettings().team.money.taxEnabled;
	}
}
