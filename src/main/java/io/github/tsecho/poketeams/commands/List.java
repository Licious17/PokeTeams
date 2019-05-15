package io.github.tsecho.poketeams.commands;

import io.github.tsecho.poketeams.apis.InfoBuilderAPI;
import io.github.tsecho.poketeams.configuration.ConfigManager;
import io.github.tsecho.poketeams.utilities.Permissions;
import io.github.tsecho.poketeams.language.Texts;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.Map;

public class List implements CommandExecutor {

	private CommandSource src;
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

		this.src = src;
		
		PaginationList.builder()
		.title(Text.of(TextColors.YELLOW, "Teams List"))
		.contents(addTeams())
		.padding(Text.of(TextColors.AQUA, "="))
		.sendTo(src);
		
		return CommandResult.success();
	}
	
	private ArrayList<Text> addTeams() {
		
		ArrayList<Text> teams = new ArrayList();
		int i = 1;
		
		for (Map.Entry<Object, ? extends CommentedConfigurationNode> teamLoop : ConfigManager
				.getStorNode("Teams").getChildrenMap().entrySet()) {
			
			Text team = Text.of(TextColors.YELLOW, i + ".) " + teamLoop.getKey().toString())
					.toBuilder()
					.onHover(TextActions.showText(Texts.of("&b&oClick me to view information about the " + teamLoop.getKey().toString() + " team")))
					.onClick(TextActions.executeCallback(run -> messageInfo(teamLoop.getKey().toString())))
					.build();
			teams.add(team);

			i++;
		}
		
		return teams;
	}
	
	private void messageInfo(String teamString) {
		InfoBuilderAPI.builder(teamString, (Player) src)
				.addName()
				.addRating()
				.addWinLoss()
				.addKills()
				.addCaught()
				.addLegendCaught()
				.addBank()
				.addPlayerList()
				.send();
	}
	
	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.LIST)
				.executor(new List())
				.build();
	}
}