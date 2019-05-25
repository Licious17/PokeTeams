package io.github.tsecho.poketeams.apis;

import io.github.tsecho.poketeams.enums.ChatTypes;
import io.github.tsecho.poketeams.enums.Ranks;
import io.github.tsecho.poketeams.language.ChatUtils;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.github.tsecho.poketeams.configuration.ConfigManager.*;

public class PokeTeamsAPI {

	protected String role, team;
	protected String uuid;
	protected int players;

	/**
	 *
	 * Accepts a CommandSource (cast if needed) and supplies methods about this user's team
	 * If {@link PokeTeamsAPI#inTeam()} returns false every method <b>will return it's default value</b>
	 *
	 * @param user that you want to process
	 */
	public PokeTeamsAPI(CommandSource user) {
		Player player = (Player) user;
		players = 0;
		uuid = player.getUniqueId().toString();

		//loop through teams
		for (Map.Entry<Object, ? extends CommentedConfigurationNode> teams : getStorNode("Teams").getChildrenMap().entrySet()) {

			String teamTemp = teams.getKey().toString();

			if(!getStorNode("Teams", teamTemp, "Members", uuid).isVirtual()) {
				team = teamTemp;
				role = getStorNode("Teams", teamTemp, "Members", uuid).getString();
				players = getStorNode("Teams", teamTemp, "Members").getChildrenMap().entrySet().size();
				break;
			}
		}
	}

	/**
	 *
	 * Accepts a team name to try and get information for a team
	 * Check {@link PokeTeamsAPI#teamExists()} to see if you are getting valid values back
	 * Even without it being a valid team, methods will return default values
	 *
	 * @param name of a team/user that you want to process
	 * @param isTeam needs to be true to setup a team and false if this is a user
	 */
	public PokeTeamsAPI(String name, boolean isTeam) {
		if(isTeam) {
			this.team = !getStorNode("Teams", name).isVirtual() ? name : null;
			if(team != null)
				players = getStorNode("Teams", name, "Members").getChildrenMap().entrySet().size();
			else
				players = 0;

		} else {
			uuid = Sponge.getServiceManager().provide(UserStorageService.class).get().get(name).get().getUniqueId().toString();
			players = 0;

			for (Map.Entry<Object, ? extends CommentedConfigurationNode> teams : getStorNode("Teams").getChildrenMap().entrySet()) {

				String teamTemp = teams.getKey().toString();

				if(!getStorNode("Teams", teamTemp, "Members", uuid).isVirtual()) {
					team = teamTemp;
					role = getStorNode("Teams", teamTemp, "Members", uuid).getString();
					players = getStorNode("Teams", teamTemp, "Members").getChildrenMap().entrySet().size();
					break;
				}
			}
		}
	}

	/**
	 * Note: These players may not be online so check the optional first
	 * @return a list of all Users on this team
	 */
	public List<User> getAllMembers() {
		UserStorageService service = Sponge.getServiceManager().provide(UserStorageService.class).get();

		if(!teamExists())
			return new ArrayList<>();

		return getStorNode("Teams", team, "Members").getChildrenMap().entrySet().stream()
				.map(key -> key.getKey().toString())
				.map(uuid -> service.get(UUID.fromString(uuid)).get())
				.collect(Collectors.toList());
	}

	/**
	 *
	 * @return a {@code boolean} value depending if the user has a team or not.
	 */
	public boolean inTeam() {
		return team != null;
	}

	/**
	 *
	 * @return a {@code boolean} value depending if a team exists
	 */
	public boolean teamExists() {
		return inTeam();
	}

	/**
	 * Deletes this object's team. Will make the rest of the methods return default values
	 */
	public void deleteTeam() {
		if(teamExists()) {
			getStorNode("Teams", team).setValue(null);
			role = team = null;
			players = 0;
		}
	}

	/**
	 * 
	 * @param newRole the int value of the new role you want the player to have
	 * @see PokeTeamsAPI#getPlace()
	 */
	public void setRole(int newRole) {
		if(inTeam()) {
			role = Ranks.getEnum(newRole).getName();
			getStorNode("Teams", team, "Members", uuid).setValue(role);
			save();
		}
	}

	/**
	 *
	 * @param player to add to a team
	 * @param rank of the player to set at
	 */
	public void addTeamMember(CommandSource player, String rank) {
		if(teamExists()) {
			getStorNode("Teams", team, "Members", ((Player) player).getUniqueId().toString()).setValue(rank);
			save();
		}
	}

	/**
	 *
	 * @param playerName to add to a team
	 * @param rank of the player to set at
	 */
	public void addTeamMember(String playerName, String rank) {
		if (teamExists()) {
			String playerUUID = Sponge.getServiceManager().provide(UserStorageService.class).get().get(playerName).get().getUniqueId().toString();
			getStorNode("Teams", team, "Members", playerUUID).setValue(rank);
			save();
		}
	}

    /**
     * Kicks a player form this team
     * @param playerName to kick from the team
     */
	public void kickPlayer(String playerName) {
        String playerUUID = Sponge.getServiceManager().provide(UserStorageService.class).get().get(playerName).get().getUniqueId().toString();
        getStorNode("Teams", team, "Members", playerUUID).setValue(null);
        save();
        ChatUtils.setChat(playerName, ChatTypes.PUBLIC);
        players -= 1;
    }

	/**
	 * Automatically returns false if the team does not exist
	 * @param amount to take from the account
	 * @return true if the transaction was a success
	 */
	public boolean takeBal(int amount) {
		if(teamExists()) {
			if(getBal() - amount >= 0) {
				getStorNode("Teams", team, "Stats", "Bal").setValue(getBal() - amount);
				save();
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param amount to add to the losses of a team
	 * @see PokeTeamsAPI#getLosses()
	 */
	public void addLoss(int amount) {
		if(teamExists()) {
			getStorNode("Teams", team, "Record", "Losses").setValue(getLosses() + amount);
			save();
		}
	}

	/**
	 * 
	 * @param amount to add to the wins of a team
	 * @see PokeTeamsAPI#getWins()
	 */
	public void addWin(int amount) {
		if(teamExists()) {
			getStorNode("Teams", team, "Record", "Wins").setValue(getWins() + amount);
			save();
		}
	}
	
	/**
	 *
	 * @param amount to add to the amount of pokemon caught per a team
	 * @see PokeTeamsAPI#getCaught()
	 */
	public void addAmountCaught(int amount) {
		if(teamExists()) {
			getStorNode("Teams", team, "Stats", "Caught").setValue(getCaught() + amount);
			save();
		}
	}

	/**
	 * 
	 * @param amount to add to the bank per a team
	 * @see PokeTeamsAPI#getBal()
	 */
	public void addBal(int amount) {
		if(teamExists()) {
			getStorNode("Teams", team, "Stats", "Bal").setValue(getBal() + amount);
			save();
		}
	}
	
	/**
	 * 
	 * @param amount to add to the amount of pokemon killed per a team
	 * @see PokeTeamsAPI#getKills()
	 */
	public void addAmountKilled(int amount) {
		if(teamExists()) {
			getStorNode("Teams", team, "Stats", "Kills").setValue(getKills() + amount);
			save();
		}
	}
	
	/**
	 * 
	 * @param amount to add to the amount of legendary pokemon caught per a team
	 * @see PokeTeamsAPI#getLegends()
	 */
	public void addLegendsCaught(int amount) {
		if(teamExists()) {
			getStorNode("Teams", team, "Stats", "Legends").setValue(getLegends() + amount);
			save();
		}
	}
	
	/**
	 * 
	 * @param tag to set as the team's new tag
	 * @see PokeTeamsAPI#getTag()
	 */
	public void setTag(String tag) {
		if(teamExists()) {
			getStorNode("Teams", team, "Tag").setValue(tag);
			save();
		}
	}

	/**
	 *
	 * @param x Can be retrieved with {@link PokeTeamsAPI#getX()}
	 * @param y Can be retrieved with {@link PokeTeamsAPI#getY()}
	 * @param z Can be retrieved with {@link PokeTeamsAPI#getZ()}
	 * @param world to use for the base (can use default world if wanted)
	 */
	public void setBase(double x, double y, double z, World world) {
		if(teamExists()) {
			getStorNode("Teams", team, "Location", "X").setValue(x);
			getStorNode("Teams", team, "Location", "Y").setValue(y);
			getStorNode("Teams", team, "Location", "Z").setValue(z);
			getStorNode("Teams", team, "Location", "World").setValue(world.getUniqueId().toString());
			save();
		}
	}

	/**
	 * @return true if the team exists and changing was successful
	 * @param newName to set the team as
	 */
	public boolean setTeamName(String newName) {
		if(teamExists()) {
			Map<Object, ? extends CommentedConfigurationNode> teamCopy = getStorNode("Teams", team).getChildrenMap();
			getStorNode("Teams", team).setValue(teamCopy);
			getStorNode("Teams", newName).setValue(null);
			save();
			team = newName;
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @return {@code int} value representing the user's rank
	 */
	public int getPlace() {
		return role != null ? Ranks.getEnum(role).getHierarchyPlace() : 0;
	}
	
	/**
	 * 
	 * @return The user's rank as a string
	 */
	public String getRank() {
		return role != null ? role : "";
	}
	
	/**
	 * 
	 * @return The user's the user's rank as translated by the language file
	 */
	public String getTranslatedRank() {
		return role != null ? Ranks.getEnum(getRank()).getTranslatedName() : "";
	}

    /**
     *
     * @return {@code true} if the user has any alliance permission in config based on their role
     */
    public boolean canAllianceCommands() {
        return team != null && getConfNode("Team-Settings", "Roles", role, "Alliance-Commands").getBoolean();
    }

	/**
	 * 
	 * @return {@code true} if the user has the delete permission in config based on their role
	 */
	public boolean canDelete() {
		return team != null && getConfNode("Team-Settings", "Roles", role, "Delete").getBoolean();
	}
	
	/**
	 * 
	 * @return {@code true} if the user has the kick permission in config based on their role
	 */
	public boolean canKick() {
		return team != null && getConfNode("Team-Settings", "Roles", role, "Kick").getBoolean();
	}
	
	/**
	 * 
	 * @return {@code true} if the user has the promote permission in config based on their role
	 */
	public boolean canPromote() {
		return team != null && getConfNode("Team-Settings", "Roles", role, "Promote").getBoolean();
	}
	
	/**
	 * 
	 * @return {@code true} if the user has the demote permission in config based on their role
	 */
	public boolean canDemote() {
		return team != null && getConfNode("Team-Settings", "Roles", role, "Demote").getBoolean();
	}
	
	/**
	 * 
	 * @return {@code true} if the user has the invite permission in config based on their role
	 */
	public boolean canInvite() {
		return team != null && getConfNode("Team-Settings", "Roles", role, "Invite").getBoolean();
	}
	
	/**
	 * 
	 * @return {@code true} if the user has the base-set permission in config based on their role
	 */
	public boolean canSetBase() {
		return role != null && getConfNode("Team-Settings", "Roles", role, "Base-Set").getBoolean();
	}
		
	/**
	 * 
	 * @return {@code true} if the user has the base-teleport permission in config based on their role
	 */
	public boolean canTeleport() {
		return role != null && getConfNode("Team-Settings", "Roles", role, "Base-Teleport").getBoolean();
	}
	
	/**
	 * 
	 * @return {@code true} if the user has the tag-set permission in config based on their role
	 */
	public boolean canSetTag() {
		return role != null && getConfNode("Team-Settings", "Roles", role, "Tag-Set").getBoolean();
	}
	
	/**
	 * 
	 * @return {@code true} if the user has the bank-add permission in config based on their role
	 */
	public boolean canAddBank() {
		return role != null && getConfNode("Team-Settings", "Roles", role, "Bank-Add").getBoolean();
	}

	/**
	 *
	 * @return {@code true} if the user has the bank-take permission in config based on their role
	 */
	public boolean canTakeBank() {
		return role != null && getConfNode("Team-Settings", "Roles", role, "Bank-Take").getBoolean();
	}

	/**
	 *
	 * @return {@code true} if the user has the queue-join permission in config based on their role
	 */
	public boolean canJoinQueue() {
		return role != null && getConfNode("Team-Settings", "Roles", role, "Queue-Join").getBoolean();
	}
	
	/**
	 * 
	 * @return {@code true} if the user has a base location saved in the config
	 */
	public boolean hasBase() {
		return !getStorNode("Teams", team, "Location").isVirtual();
	}

	/**
	 * 
	 * @return {@code double} value of the X coordinate of the user's base  
	 */
	public double getX() {
		return team != null ? getStorNode("Teams", team, "Location", "X").getDouble() : 0.0;
	}
	
	/**
	 * 
	 * @return {@code double} value of the Y coordinate of the user's base  
	 */
	public double getY() {
		return team != null ? getStorNode("Teams", team, "Location", "Y").getDouble() : 0.0;
	}

	/**
	 * 
	 * @return {@code double} value of the Z coordinate of the user's base  
	 */
	public double getZ() {
		return team != null ? getStorNode("Teams", team, "Location", "Z").getDouble() : 0.0;
	}
	
	/**
	 * 
	 * @return {@code int} value of the the team's win record
	 */
	public int getWins() {
		return team != null ? getStorNode("Teams", team, "Record", "Wins").getInt() : 0;
	}
	
	/**
	 * 
	 * @return {@code int} value of the the team's loss record
	 */
	public int getLosses() {
		return team != null ? getStorNode("Teams", team, "Record", "Losses").getInt() : 0;
	}
	
	/**
	 * 
	 * @return {@code int} value of the the team's win/loss record
	 */
	public int getRatio() {
		if(getLosses() != 0)
			return (int) (((double) getWins() / ((double) getWins() + (double) getLosses())) * 100.0);
		else if(getWins() != 0)
			return 100;
		else
			return 0;
	}
	
	/**
	 * 
	 * @return {@code int} value of the the team's catching record
	 */
	public int getCaught() {
		return team != null ? getStorNode("Teams", team, "Stats", "Caught").getInt() : 0;
	}
	
	/**
	 * 
	 * @return {@code int} value of the the team's pokemon fainting record
	 */
	public int getKills() {
		return team != null ? getStorNode("Teams", team, "Stats", "Kills").getInt() : 0;
	}

	/**
	 * 
	 * @return {@code int} value of the the team's legendary catch record
	 */
	public int getLegends() {
		return team != null ? getStorNode("Teams", team, "Stats", "Legends").getInt() : 0;
	}
	
	/**
	 * 
	 * @return {@code int} value of how many members there are in this team
	 */
	public int getMemberTotal() {
		return team != null ? players : 0;
	}
	
	/**
	 * 
	 * @return {@code int} value of how many members there can be in a team 
	 */
	public int getMaxPlayers() {
		return getSettings().team.maxMembers;
	}
	
	/**
	 * 
	 * @return {@code String} name of the team 
	 */
	public String getTeam() {
		return teamExists() ? team : getSettings().placeholders.defaultTeamName;
	}
	
	/**
	 * 
	 * @return {@code String} representation of the team's tag. Will return the default if no tag is set
	 */
	public String getTag() {
		if(!getStorNode("Teams", team, "Tag").isVirtual())
			return getStorNode("Teams", team, "Tag").getString();
		return getSettings().placeholders.defaultTeamTag;
	}

	/**
	 * If the team does not have a tag, the default values will be used without formatting
	 * @return {@code String} representation of a player's tag with formatting from config
	 */
	public String getFormattedTeamTag() {
		if(!getStorNode("Teams", team, "Tag").isVirtual())
			return getSettings().placeholders.formattedTeamTag.replace("%teamtag%", getTag());
		return getSettings().placeholders.defaultTeamTag;
	}
	
	/**
	 * 
	 * @return {@code int} value of the team's current balance
	 * 
	 */
	public int getBal() { 
		return team != null ? getStorNode("Teams", team, "Stats", "Bal").getInt() : 0;
	}

	/**
	 *
	 * @return the rating of a team based on several settings and records
	 */
	public double getRating() {
		double highCaught = getSettings().leaderboard.caughtAverage;
		double highLegend = getSettings().leaderboard.legendsAverage;
		double highKills = getSettings().leaderboard.killsAverage;
		double highBal = getSettings().leaderboard.balAverage;
		double highRecord = getSettings().leaderboard.recordAverage;

		double totCaught = highCaught >= getCaught() ? ((double) getCaught() / highCaught) * 100.0 : 100.0;
		double totLegend = highLegend >= getLegends() ? ((double) getLegends() / highLegend) * 100.0 : 100.0;
		double totKills = highKills >= getKills() ? ((double) getKills() / highKills) * 100.0 : 100.0;
		double totBal = highBal >= getBal() ? ((double) getBal() / highBal) * 100.0 : 100.0;
		double totRecord = highRecord >= getRatio() ? ((double) getRatio() / highRecord) * 100.0 : 100.0;
		return (totCaught + totLegend + totKills + totBal + totRecord) / 5.0;
	}
}