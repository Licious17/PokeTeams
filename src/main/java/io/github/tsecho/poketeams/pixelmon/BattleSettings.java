package io.github.tsecho.poketeams.pixelmon;

import com.pixelmonmod.pixelmon.battles.rules.BattleRules;
import com.pixelmonmod.pixelmon.battles.rules.clauses.BattleClause;
import com.pixelmonmod.pixelmon.battles.rules.clauses.BattleClauseRegistry;
import io.github.tsecho.poketeams.configuration.ConfigManager;

import java.util.ArrayList;
import java.util.List;

public class BattleSettings {

    private List<BattleClause> clauses;
    private BattleRules rules;

    public BattleSettings() {
        clauses = new ArrayList();
        rules = new BattleRules();
        setupRules();
        setupClauses();
    }

    private void setupRules() {
        if(ConfigManager.getConfNode("Battle-Settings", "Competitive", "Rules", "TurnTime").getInt() != 0)
            rules.turnTime = ConfigManager.getConfNode("Battle-Settings", "Competitive", "Rules", "TurnTime").getInt();

        if(ConfigManager.getConfNode("Battle-Settings", "Competitive", "Rules", "FullHeal").getBoolean())
            rules.fullHeal = ConfigManager.getConfNode("Battle-Settings", "Competitive", "Rules", "FullHeal").getBoolean();

        if(ConfigManager.getConfNode("Battle-Settings", "Competitive", "Rules", "LevelCap").getInt() != 0)
            rules.levelCap = ConfigManager.getConfNode("Battle-Settings", "Competitive", "Rules", "LevelCap").getInt();

        if(ConfigManager.getConfNode("Battle-Settings", "Competitive", "Rules", "RaiseToCap").getBoolean())
            rules.raiseToCap = ConfigManager.getConfNode("Battle-Settings", "Competitive", "Rules", "RaiseToCap").getBoolean();
    }

    private void setupClauses() {
        if(ConfigManager.getConfNode("Battle-Settings", "Competitive", "Clauses", "SleepClause").getBoolean())
            clauses.add(BattleClauseRegistry.getClauseRegistry().getClause(BattleClauseRegistry.SLEEP_CLAUSE));

        if(ConfigManager.getConfNode("Battle-Settings", "Competitive", "Clauses", "BagClause").getBoolean())
            clauses.add(BattleClauseRegistry.getClauseRegistry().getClause(BattleClauseRegistry.BAG_CLAUSE));

        if(ConfigManager.getConfNode("Battle-Settings", "Competitive", "Clauses", "ForfeitClause").getBoolean())
            clauses.add(BattleClauseRegistry.getClauseRegistry().getClause(BattleClauseRegistry.FORFEIT_CLAUSE));

        if(ConfigManager.getConfNode("Battle-Settings", "Competitive", "Clauses", "InverseBattle").getBoolean())
            clauses.add(BattleClauseRegistry.getClauseRegistry().getClause(BattleClauseRegistry.INVERSE_BATTLE));

        if(ConfigManager.getConfNode("Battle-Settings", "Competitive", "Clauses", "SkyBattle").getBoolean())
            clauses.add(BattleClauseRegistry.getClauseRegistry().getClause(BattleClauseRegistry.SKY_BATTLE));
    }

    /**
     *
     * @return setup BattleRules for queue battles based on configuration
     */
    public BattleRules getSettings() {
        return rules;
    }
}
