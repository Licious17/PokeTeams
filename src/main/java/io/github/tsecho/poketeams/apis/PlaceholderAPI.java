package io.github.tsecho.poketeams.apis;

import io.github.tsecho.poketeams.PokeTeams;
import io.github.tsecho.poketeams.configuration.ConfigManager;
import io.github.tsecho.poketeams.language.Texts;
import lombok.Getter;
import me.rojo8399.placeholderapi.Listening;
import me.rojo8399.placeholderapi.Placeholder;
import me.rojo8399.placeholderapi.PlaceholderService;
import me.rojo8399.placeholderapi.Source;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;

@Listening
public class PlaceholderAPI {

	@Getter private PlaceholderService service;
	@Getter private boolean enabled = false;
	private static PlaceholderAPI INSTANCE;

	private PlaceholderAPI() {}

	public static PlaceholderAPI getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new PlaceholderAPI();

			if(Sponge.getPluginManager().getPlugin("placeholderapi").isPresent()) {
				INSTANCE.service = Sponge.getServiceManager().provideUnchecked(PlaceholderService.class);
				INSTANCE.load();
			}
		}

		return INSTANCE;
	}

	private void load() {

		enabled = true;
		INSTANCE.service.loadAll(INSTANCE, PokeTeams.getInstance()).stream().map(builder -> {

			switch(builder.getId()) {
				case "teamname":
					return builder.description("Player's team name");
				case "teamwins":
					return builder.description("Player's team's wins");
				case "teamlosses":
					return builder.description("Player's team's losses");
				case "teamkills":
					return builder.description("Player's team's kills");
				case "teamratio":
					return builder.description("Player's team's win/loss ratio");
				case "teamcaught":
					return builder.description("Player's team's total caught pokemon");
				case "teamtag":
					return builder.description("Player's team's custom tag");
				case "teamcaughtlegend":
					return builder.description("Player's team's total caught legendary pokemon");
				case "teambal":
					return builder.description("Player's team's bank balance");
				case "formattedteamtag":
					return builder.description("Player's team's tag with formatting options in config");
				case "teamrating":
					return builder.description("Player's team's total rating");
				case "teamalliance":
					return builder.description("Player's team's alliance name");
			}

			return builder;

		}).map(builder -> builder.plugin(PokeTeams.getInstance()).author("TSEcho").version("4.0.0")).forEach(builder -> {

			try {
				builder.buildAndRegister();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
	}

	/**
	 * This will try to use PAPI first but will fallback if needed
	 * @return a text with all replaced service
	 */
	public Text replace(String s, CommandSource src) {
		if(enabled)
			return service.replaceSourcePlaceholders(Texts.of(s), src);
		else
			return Texts.of(s
				.replaceAll("_", "")
				.replaceAll("%player%", src.getName())
				.replaceAll("%teamname%", getTeam(src)
				.replaceAll("%teamtag%", getTag(src)
				.replaceAll("%teamwins%", getWins(src)
				.replaceAll("%teamkills%" , getKills(src))
				.replaceAll("%teamlosses%", getLosses(src)
				.replaceAll("%teamratio%", getRatio(src)
				.replaceAll("%teamcaught%", getCaught(src)
				.replaceAll("%teamcaughtlegend%", getLegendCaught(src)
				.replaceAll("%teamrating%", getRating(src))
				.replaceAll("%formattedteamtag%", getFormattedTeamTag(src))
				.replaceAll("%teamalliance%", getAlliance(src))
				.replaceAll("%teambal%", getBalance(src))))))))));
	}

	/**
	 * This will use a the string as the replacement
	 * @return a text with all replaced service
	 */
	public Text replace(String s, String src, boolean isTeam) {
		PokeTeamsAPI role = new PokeTeamsAPI(src, isTeam);
		return Texts.of(s
				.replaceAll("_", "")
				.replaceAll("%player%", src)
				.replaceAll("%teamname%", role.getTeam())
				.replaceAll("%teamtag%", role.getTag())
				.replaceAll("%teamcaughtlegend%", String.valueOf(role.getLegends()))
				.replaceAll("%teamalliance%", new AllianceAPI(role).getAlliance())
				.replaceAll("%teamwins%", String.valueOf(role.getWins()))
				.replaceAll("%teamkills%" , String.valueOf(role.getKills()))
				.replaceAll("%teamlosses%", String.valueOf(role.getLosses()))
				.replaceAll("%teamratio%", String.valueOf(role.getRatio()))
				.replaceAll("%teamcaught%", String.valueOf(role.getCaught()))
				.replaceAll("%formattedteamtag%", role.getFormattedTeamTag())
				.replaceAll("%teamrating%", String.valueOf(role.getRating()))
				.replaceAll("%teambal%", String.valueOf(role.getBal())));
	}

	@Placeholder(id = "teamname")
	public String getTeam(@Source CommandSource src) {
		PokeTeamsAPI role = new PokeTeamsAPI(src);
		if(role.inTeam())
			return role.getTeam();
		else
			return Texts.getString(replace(ConfigManager.getConfNode("Placeholder-Settings", "Default-TeamName").getString(), src));
	}

	@Placeholder(id = "teamtag")
	public String getTag(@Source CommandSource src) {
		return new PokeTeamsAPI(src).getTag();
	}

	@Placeholder(id = "teamwins")
	public String getWins(@Source CommandSource src) {
		return String.valueOf(new PokeTeamsAPI(src).getWins());
	}

	@Placeholder(id = "teamlosses")
	public String getLosses(@Source CommandSource src) {
		return String.valueOf(new PokeTeamsAPI(src).getLosses());
	}

	@Placeholder(id = "teamratio")
	public String getRatio(@Source CommandSource src) {
		return new PokeTeamsAPI(src).getRatio() + "%";
	}

	@Placeholder(id = "teamkills")
	public String getKills(@Source CommandSource src) {
		return String.valueOf(new PokeTeamsAPI(src).getKills());
	}

	@Placeholder(id = "teamcaught")
	public String getCaught(@Source CommandSource src) {
		return String.valueOf(new PokeTeamsAPI(src).getCaught());
	}

	@Placeholder(id = "teamcaughtlegend")
	public String getLegendCaught(@Source CommandSource src) {
		return String.valueOf(new PokeTeamsAPI(src).getLegends());
	}

	@Placeholder(id = "teambal")
	public String getBalance(@Source CommandSource src) {
		return String.valueOf(new PokeTeamsAPI(src).getBal());
	}

	@Placeholder(id = "formattedteamtag")
	public String getFormattedTeamTag(@Source CommandSource src) {
		return String.valueOf(new PokeTeamsAPI(src).getFormattedTeamTag());
	}

	@Placeholder(id = "teamrating")
	public String getRating(@Source CommandSource src) {
		return String.valueOf(new PokeTeamsAPI(src).getRating());
	}

	@Placeholder(id = "teamalliance")
	public String getAlliance(@Source CommandSource src) {
		return String.valueOf(new AllianceAPI(new PokeTeamsAPI(src)).getAlliance());
	}
}
