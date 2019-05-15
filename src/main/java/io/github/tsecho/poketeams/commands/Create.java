package io.github.tsecho.poketeams.commands;

import io.github.tsecho.poketeams.apis.PokeTeamsAPI;
import io.github.tsecho.poketeams.configuration.ConfigManager;
import io.github.tsecho.poketeams.economy.EconManager;
import io.github.tsecho.poketeams.enums.Messages.ErrorMessages;
import io.github.tsecho.poketeams.enums.Messages.SuccessMessages;
import io.github.tsecho.poketeams.enums.Messages.TechnicalMessages;
import io.github.tsecho.poketeams.language.CensorCheck;
import io.github.tsecho.poketeams.language.Texts;
import io.github.tsecho.poketeams.PokeTeams;
import io.github.tsecho.poketeams.utilities.ErrorCheck;
import io.github.tsecho.poketeams.utilities.Permissions;
import io.github.tsecho.poketeams.utilities.Utils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;

public class Create implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

		String team = args.<String>getOne(Text.of("team")).get();

		if(!(src instanceof Player))
			return ErrorCheck.test(src, TechnicalMessages.NOT_PLAYER);

		PokeTeamsAPI role = new PokeTeamsAPI(src);
		CensorCheck check = new CensorCheck(team);

		if(role.inTeam())
			return ErrorCheck.test(src, ErrorMessages.ALREADY_IN_TEAM);
		if(Utils.teamExists(team))
			return ErrorCheck.test(src, ErrorMessages.AlREADY_EXISTS);
		if(check.failsCensor(false))
			return ErrorCheck.test(src, ErrorMessages.INNAPROPRIATE);

		Text confirmation = TechnicalMessages.CONFIRM.getText(src)
								.toBuilder()
								.onClick(TextActions.executeCallback(callback -> confirm(src, team)))
								.build();

		src.sendMessage(confirmation);

		return CommandResult.success();
	}

	private void confirm(CommandSource src, String team) {
		if(ConfigManager.getConfNode("Team-Settings", "Money", "Cost-Enabled").getBoolean()) {

			EconManager econ = new EconManager((Player) src);

			if(econ.isEnabled()) {

				if (econ.buyTeam().getResult() == ResultType.SUCCESS) {
					src.sendMessage(SuccessMessages.MONEY_WITHDRAW.getText(src));
					createTeam(team, src);
				} else {
					src.sendMessage(ErrorMessages.INSUFFICIENT_FUNDS.getText(src));
				}

			} else {
				src.sendMessage(Texts.of("&cERROR: Economy is not enabled. Please alert an admin or server owner"));
				PokeTeams.getLogger().error("Economy plugin is not installed. Please install one for player's to use");
			}

		} else {
			createTeam(team, src);
		}
	}

	private void createTeam(String team, CommandSource src) {
		ConfigManager.getStorNode("Teams", team, "Members", ((Player) src).getUniqueId().toString()).setValue("Owner");
		ConfigManager.getStorNode("Teams", team, "Record", "Wins").setValue(0);
		ConfigManager.getStorNode("Teams", team, "Record", "Losses").setValue(0);
		ConfigManager.getStorNode("Teams", team, "Stats", "Kills").setValue(0);
		ConfigManager.getStorNode("Teams", team, "Stats", "Caught").setValue(0);
		ConfigManager.getStorNode("Teams", team, "Stats", "Legends").setValue(0);
		ConfigManager.getStorNode("Teams", team, "Stats", "Bal").setValue(
				ConfigManager.getConfNode("Team-Settings", "Default-Team-Bal"));
		ConfigManager.save();

		src.sendMessage(SuccessMessages.CREATED_TEAM.getText(src));
	}
	
	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.CREATE)
				.arguments(GenericArguments.remainingJoinedStrings(Text.of("team")))
				.executor(new Create())
				.build();
	}
}
