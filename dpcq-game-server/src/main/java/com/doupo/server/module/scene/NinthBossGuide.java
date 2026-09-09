package com.doupo.server.module.scene;

import com.doupo.protocol.AttributeActionVO;
import com.doupo.protocol.AttributeVO;
import com.doupo.protocol.MpResp;
import com.doupo.protocol.PlayerFightGuildCallResp;
import com.doupo.protocol.SceneFightUnitInfoVo;
import com.doupo.protocol.SceneUnitVo;
import com.doupo.protocol.SceneUpdateVisibleResp;
import com.doupo.protocol.SkillContainerVO;
import com.doupo.server.module.combat.CombatSession;
import com.doupo.server.module.combat.CombatUnit;
import org.gaming.fakecmd.side.game.IPlayerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 第九关濒死教学，回包顺序取自第一次创角色.json idx 8207..8211。 */
public final class NinthBossGuide {
    private static final Logger LOGGER = LoggerFactory.getLogger(NinthBossGuide.class);

    private NinthBossGuide() {
    }

    public static void tryStart(IPlayerContext context, CombatSession session) {
        if (session.getNinthBossPlayerSnapshot() == null
                || !session.tryBeginNinthBossGuide()) {
            return;
        }
        CombatUnit player = session.getPlayer();
        // 复用本服角色快照，位置和战斗数值使用当前会话，不能复制抓包中的玩家ID。
        SceneUnitVo.Builder visible = session.getNinthBossPlayerSnapshot().toBuilder();
        visible.getBaseInfoVoBuilder().setX(player.getX()).setY(player.getY())
                .setZ(player.getZ()).setDir(player.getDir());
        SceneFightUnitInfoVo.Builder fight = visible.getFightInfoVoBuilder();
        for (int i = 0; i < fight.getAttributeListCount(); i++) {
            AttributeVO attr = fight.getAttributeList(i);
            double value;
            switch (attr.getType()) {
                case 101001:
                case 191001:
                case 191005: value = player.getAttack(); break;
                case 103001: value = player.getMaxHp(); break;
                case 103011: value = 1; break;
                case 103012: value = player.getMaxHp() - 1; break;
                case 103111: value = 0; break;
                default: continue;
            }
            fight.setAttributeList(i, attr.toBuilder().setValue(value));
        }
        // idx 8207 暂时移除主动技能，保留五个普攻供后续教学替换。
        SkillContainerVO.Builder skills = visible.getHeroVo().getSkillContainerVo()
                .toBuilder().clearSkillList();
        visible.getHeroVo().getSkillContainerVo().getSkillListList().stream()
                .filter(skill -> !skill.getActiveSkill()).forEach(skills::addSkillList);
        visible.getHeroVoBuilder().setSkillContainerVo(skills);
        context.write(50756, SceneUpdateVisibleResp.newBuilder()
                .setSnapshot(false).addVisibleList(visible).build(), 0);

        player.restoreFullHealth();
        context.write(50801, AttributeActionVO.newBuilder()
                .addAttrList(attribute(103011, player.getCurrentHp()))
                .addAttrList(attribute(103001, player.getMaxHp()))
                .setTargetId(player.getSceneUnitId()).build(), 0);
        context.write(77066, PlayerFightGuildCallResp.newBuilder()
                .setGuildGroupId(10043).build(), 0);
        session.addMp(-session.getMp());
        writeMp(context, session);
        session.addMp(80);
        writeMp(context, session);
        LOGGER.info("Ninth boss guide triggered: player={}, chapter={}, guide=10043, "
                        + "hpBefore=1, hpAfter={}, mp=80",
                context.getId(), session.getChapterId(), player.getCurrentHp());
    }

    private static AttributeVO attribute(int type, double value) {
        return AttributeVO.newBuilder().setType(type).setValue(value).build();
    }

    private static void writeMp(IPlayerContext context, CombatSession session) {
        context.write(50798, MpResp.newBuilder()
                .setId(session.getPlayer().getSceneUnitId()).setMp(session.getMp()).build(), 0);
    }
}
