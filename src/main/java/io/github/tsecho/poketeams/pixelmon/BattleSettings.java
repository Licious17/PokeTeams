package io.github.tsecho.poketeams.pixelmon;

import com.pixelmonmod.pixelmon.battles.rules.BattleRules;
import com.pixelmonmod.pixelmon.battles.rules.clauses.BattleClause;
import com.pixelmonmod.pixelmon.battles.rules.clauses.BattleClauseRegistry;

import java.util.ArrayList;
import java.util.List;

import static io.github.tsecho.poketeams.configuration.ConfigManager.getConfNode;
import static io.github.tsecho.poketeams.configuration.ConfigManager.getSettings;

public class BattleSettings {

    private List<BattleClause> clauses;
    private BattleRules rules;

    public BattleSettings() {
        clauses = new ArrayList<>();
        rules = new BattleRules();
        setupRules();
        setupClauses();
    }

    private void setupRules() {

        if(getSettings().battle.competitive.rules.turnTime != 0)
            rules.turnTime = getSettings().battle.competitive.rules.turnTime;

        if(getSettings().battle.competitive.rules.fullHeal)
            rules.fullHeal = getSettings().battle.competitive.rules.fullHeal;

        if(getSettings().battle.competitive.rules.levelCap != 0)
            rules.levelCap = getSettings().battle.competitive.rules.levelCap;

        if(getSettings().battle.competitive.rules.raiseToCap)
            rules.raiseToCap = getSettings().battle.competitive.rules.raiseToCap;
    }

    private void setupClauses() {
        getConfNode("Battle-Settings", "Competitive", "Clauses").getChildrenMap().entrySet().stream()
                .filter(node -> node.getValue().getBoolean())
                .map(node -> node.getKey().toString().toLowerCase())
                .forEach(clause -> clauses.add(BattleClauseRegistry.getClauseRegistry().getClause(clause)));
        rules.setNewClauses(clauses);
    }

    /**
     *
     * @return setup BattleRules for queue battles based on configuration
     */
    public BattleRules getRules() {
        return rules;
    }
}
