package io.github.tsecho.poketeams.eventlisteners;

import io.github.tsecho.poketeams.apis.AllianceAPI;
import io.github.tsecho.poketeams.apis.PokeTeamsAPI;
import io.github.tsecho.poketeams.configuration.ConfigManager;
import io.github.tsecho.poketeams.enums.ChatTypes;
import io.github.tsecho.poketeams.language.ChatUtils;
import io.github.tsecho.poketeams.language.Texts;
import io.github.tsecho.poketeams.utilities.Permissions;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.text.Text;

import static io.github.tsecho.poketeams.enums.ChatTypes.ALLIANCE;
import static io.github.tsecho.poketeams.enums.ChatTypes.TEAM;

public class ChatListener implements EventListener<MessageChannelEvent.Chat>{

	private MessageChannelEvent.Chat e;
	private Player player;
	private String message, prefix, chatColor;
	private Text newMessage, staffMessage;
	private PokeTeamsAPI role;
	private ChatTypes chatType;
	private AllianceAPI ogAlliance;
	
	@Override
	public void handle(MessageChannelEvent.Chat e) {
		this.e = e;

		if(isPlayer()) {

			this.chatType = ChatUtils.getChatType(player.getName());
			this.message = Texts.getString(e.getRawMessage());
			this.staffMessage = Texts.of(ConfigManager.getConfNode("Chat-Settings", "SocialSpy-Message").getString() + message, player);

			if(inGroupChat()) {
				modifyTeam();
			} else if(chatType == ALLIANCE) {
				modifyAlliance();
			}
		}
	}
	
	private void modifyTeam() {
		prefix = ConfigManager.getConfNode("Chat-Settings", "Prefix").getString();
		chatColor = ConfigManager.getConfNode("Chat-Settings", "Chat-Color").getString();
		newMessage = Texts.of((prefix + chatColor + message), player);
		sendMessages();
	}

	private void modifyAlliance() {
		prefix = ConfigManager.getConfNode("Ally-Settings", "Chat-Settings", "Prefix").getString();
		chatColor = ConfigManager.getConfNode("Ally-Settings", "Chat-Settings", "Chat-Color").getString();
		newMessage = Texts.of((prefix + chatColor + message), player);
		ogAlliance = new AllianceAPI(role);
		sendMessages();
	}

	private void sendMessages() {
		for(Player members : Sponge.getServer().getOnlinePlayers()) {

			if((inTeam(members) && chatType == TEAM) || (inAlliance(members) && chatType == ALLIANCE))
				members.sendMessage(newMessage);
			else if(isStaff(members) && !ChatUtils.inSocialSpyOff(members.getName()))
				members.sendMessage(staffMessage);
		}

		if(ConfigManager.getConfNode("Chat-Settings", "Console-SocialSpy").getBoolean())
			Sponge.getServer().getConsole().sendMessage(newMessage);
		e.setCancelled(true);
	}
	
	private boolean inTeam(Player members) {
		return !ConfigManager.getStorNode("Teams", role.getTeam(), "Members", members.getUniqueId().toString()).isVirtual();
	}

	private boolean inAlliance(Player members) {
		AllianceAPI alliance = new AllianceAPI(new PokeTeamsAPI(members));
		return alliance.inAlliance() && alliance.getAlliance().equals(ogAlliance.getAlliance());
	}
	
	private boolean isStaff(Player members) {
		return ConfigManager.getConfNode("Chat-Settings", "Players-SocialSpy").getBoolean() && members.hasPermission(Permissions.SOCIAL_SPY);
	}
	
	private boolean isPlayer() {
		if(e.getSource() instanceof Player) {
			player = (Player) e.getSource();
			role = new PokeTeamsAPI(player);
			return true;
		}
		return false;
	}
	
	private boolean inGroupChat() {
		return role.inTeam() && ChatUtils.getChatType(player.getName()) == ChatTypes.TEAM;
	}
}
