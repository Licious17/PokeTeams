package io.github.tsecho.poketeams.utilities;

import io.github.tsecho.poketeams.PokeTeams;
import io.github.tsecho.poketeams.economy.Taxes;
import io.github.tsecho.poketeams.pixelmon.QueueManager;
import org.spongepowered.api.scheduler.Task;

import java.util.concurrent.TimeUnit;

import static io.github.tsecho.poketeams.configuration.ConfigManager.getSettings;

public class Tasks {

	public Tasks() {
		startQueue();
		startTax();
		startWarn();
	}

	private void startQueue() {
		Task.builder()
				.name(PokeTeams.ID + "-Queue-Timer")
				.delay(5, TimeUnit.SECONDS)
				.async()
				.interval(getSettings().battle.queueTimer, TimeUnit.SECONDS)
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
