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
        ConfigManager.getConfNode("Battle-Settings", "Competitive", "Clauses").getChildrenMap().entrySet().stream()
                .filter(node -> node.getValue().getBoolean())
                .map(node -> node.getKey().toString().toLowerCase())
                .forEach(clause -> clauses.add(BattleClauseRegistry.getClauseRegistry().getClause(clause)));
    }

    /**
     *
     * @return setup BattleRules for queue battles based on configuration
     */
    public BattleRules getSettings() {
        return rules;
    }
}
