package io.github.tsecho.poketeams.eventlisteners;

import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import io.github.tsecho.poketeams.apis.PokeTeamsAPI;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.api.entity.living.player.Player;

public class CatchPokemonListener {

    @SubscribeEvent
    public void onCapture(CaptureEvent.SuccessfulCapture e) {
        PokeTeamsAPI role = new PokeTeamsAPI((Player) e.player);

        if(isLegend(e.getPokemon()))
            addLegend(role);
        else
            addCaught(role);
    }

    private void addCaught(PokeTeamsAPI role) {
        if(role.inTeam())
            role.addAmountCaught(1);
    }

    private void addLegend(PokeTeamsAPI role) {
        if(role.inTeam()) {
            role.addLegendsCaught(1);
            role.addAmountCaught(1);
        }
    }

    private boolean isLegend(EntityPixelmon pokemon) {
        return EnumSpecies.legendaries.contains(EnumSpecies.getFromName(pokemon.getName()).get().getPokemonName());
    }
}
