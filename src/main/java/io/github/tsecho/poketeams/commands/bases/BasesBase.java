package io.github.tsecho.poketeams.commands.bases;

import com.flowpowered.math.vector.Vector3d;
import io.github.tsecho.poketeams.apis.PokeTeamsAPI;
import io.github.tsecho.poketeams.configuration.ConfigManager;
import io.github.tsecho.poketeams.enums.Messages.ErrorMessages;
import io.github.tsecho.poketeams.enums.Messages.SuccessMessages;
import io.github.tsecho.poketeams.enums.Messages.TechnicalMessages;
import io.github.tsecho.poketeams.utilities.ErrorCheck;
import io.github.tsecho.poketeams.utilities.Permissions;
import io.github.tsecho.poketeams.utilities.WorldInfo;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;

import java.util.UUID;

public class BasesBase implements CommandExecutor {

	private UUID world;
	private PokeTeamsAPI role;
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

		if (!(src instanceof Player))
			return ErrorCheck.test(src, TechnicalMessages.NOT_PLAYER);

		role = new PokeTeamsAPI(src);

		if(!role.inTeam())
			return ErrorCheck.test(src, ErrorMessages.NOT_IN_TEAM);
		if(!role.hasBase())
			return ErrorCheck.test(src, ErrorMessages.NO_BASE);
		if(!role.canTeleport())
			return ErrorCheck.test(src, ErrorMessages.INSUFFICIENT_RANK);

		if(ConfigManager.getStorNode("Teams", role.getTeam(), "Location", "World").isVirtual())
			world = WorldInfo.getWorldUUID();
		else
			world = UUID.fromString(ConfigManager.getStorNode("Teams", role.getTeam(), "Location", "World").getString());

		src.sendMessage(SuccessMessages.TELEPORTED.getText(src));
		((Player) src).transferToWorld(world, new Vector3d(role.getX(), role.getY(), role.getZ()));

		return CommandResult.success();
	}
	
	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.BASE_TELEPORT)
				.child(Set.build(), "set")
				.executor(new BasesBase())
				.build();
	}
}
