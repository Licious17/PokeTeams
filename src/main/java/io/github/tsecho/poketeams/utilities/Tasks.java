package io.github.tsecho.poketeams.utilities;

import java.util.concurrent.TimeUnit;

import io.github.tsecho.poketeams.economy.Taxes;
import io.github.tsecho.poketeams.configuration.ConfigManager;
import org.spongepowered.api.scheduler.Task;

import io.github.tsecho.poketeams.PokeTeams;
import io.github.tsecho.poketeams.pixelmon.QueueManager;

public class Tasks {

	public Tasks() {
		startQueue();
		startTax();
		startWarn();
	}

	private void startQueue() {
		Task.builder()
				.name(PokeTeams.ID + "-queue-Timer")
				.delay(5, TimeUnit.SECONDS)
				.async()
				.interval(ConfigManager.getConfNode("Battle-Settings", "queue-Timer").getInt(), TimeUnit.SECONDS)
				.execute(QueueManager::choosePlayers)
				.submit(PokeTeams.getInstance());
	}

	private void startTax() {
		Task.builder()
				.name(PokeTeams.ID + "-Tax-Timer")
				.async()
				.delay(5, TimeUnit.SECONDS)
				.interval(1, TimeUnit.MINUTES)
				.execute(Taxes::tax)
				.submit(PokeTeams.getInstance());
	}

	private void startWarn() {
		Task.builder()
				.name(PokeTeams.ID + "-Warn-Timer")
				.async()
				.delay(5, TimeUnit.SECONDS)
				.interval(1, TimeUnit.HOURS)
				.execute(Taxes::warn)
				.submit(PokeTeams.getInstance());
	}
}
