package io.github.tsecho.poketeams.eventlisteners;

import com.pixelmonmod.pixelmon.api.events.BeatWildPixelmonEvent;
import io.github.tsecho.poketeams.apis.PokeTeamsAPI;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.api.entity.living.player.Player;

public class WildBattleListener {

    @SubscribeEvent
    public void onBattle(BeatWildPixelmonEvent e) {
        PokeTeamsAPI role = new PokeTeamsAPI((Player) e.player);

        if(role.inTeam())
            role.addAmountKilled(1);
    }
}
