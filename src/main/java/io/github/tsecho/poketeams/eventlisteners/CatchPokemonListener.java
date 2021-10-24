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

        if(role.inTeam()) {
            role.addAmountCaught(1);
            if(EnumSpecies.legendaries.contains(e.getPokemon().getSpecies())) {
                role.addLegendsCaught(1);
            }
        }
    }
}
