package io.github.tsecho.poketeams.eventlisteners;

import io.github.tsecho.poketeams.apis.PokeTeamsAPI;
import io.github.tsecho.poketeams.configuration.ConfigManager;
import io.github.tsecho.poketeams.language.ChatUtils;
import io.github.tsecho.poketeams.language.Texts;
import io.github.tsecho.poketeams.utilities.Permissions;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.text.Text;

public class ChatListener implements EventListener<MessageChannelEvent.Chat>{

	private MessageChannelEvent.Chat e;
	private Player player;
	private String message, prefix, chatColor;
	private Text newMessage, staffMessage;
	private PokeTeamsAPI role;
	
	@Override
	public void handle(MessageChannelEvent.Chat e) throws Exception {
		this.e = e;
		if(isPlayer() && inGroupChat())
			modifyMessage();
	}
	
	private void modifyMessage() {

		prefix = ConfigManager.getConfNode("Chat-Settings", "Prefix").getString();
		chatColor = ConfigManager.getConfNode("Chat-Settings", "Chat-Color").getString();
		message = Texts.getString(e.getRawMessage());

		newMessage = Texts.of((prefix + chatColor + message), player);
		staffMessage = Texts.of(ConfigManager.getConfNode("Chat-Settings", "SocialSpy-Message").getString() + message, player);

		sendMessages();
		e.setCancelled(true);
	}

	private void sendMessages() {
		for(Player members : Sponge.getServer().getOnlinePlayers()) {

			if(inTeam(members))
				members.sendMessage(newMessage);
			else if(isStaff(members) && !ChatUtils.inSocialSpyOff(members.getName()))
				members.sendMessage(staffMessage);
		}

		if(ConfigManager.getConfNode("Chat-Settings", "Console-SocialSpy").getBoolean())
			Sponge.getServer().getConsole().sendMessage(newMessage);
	}
	
	private boolean inTeam(Player members) {
		return !ConfigManager.getStorNode("Teams", role.getTeam(), "Members", members.getUniqueId().toString()).isVirtual();
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
		return role.inTeam() && ChatUtils.inChat(player.getName());
	}
}
