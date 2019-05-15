package io.github.tsecho.poketeams.pixelmon;

import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import io.github.tsecho.poketeams.apis.PokeTeamsAPI;
import org.spongepowered.api.entity.living.player.Player;

public class CatchingManager {

	private CaptureEvent.SuccessfulCapture e;
	private Player player;
	private PokeTeamsAPI role;

	public CatchingManager(CaptureEvent.SuccessfulCapture e) {
		this.e = e;
		player = (Player) e.player;
		role = new PokeTeamsAPI(player);
		
		if(isLegend())
			addLegend();
		else
			addCaught();
	}
	
	private void addCaught() {	
		if(role.inTeam())
			role.addAmountCaught(1);
	}
	
	private void addLegend() {
		if(role.inTeam()) {
			role.addLegendsCaught(1);
			role.addAmountCaught(1);
		}
	}
	
	private boolean isLegend() {
		return EnumSpecies.legendaries.contains(EnumSpecies.getFromName(e.getPokemon().getName()).get().getPokemonName());
	}
}
