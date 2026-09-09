package com.doupo.server.module.scene;

import com.doupo.protocol.AllHeroLevelInfoResp;
import com.doupo.protocol.AlchemyNewExpChangeResp;
import com.doupo.protocol.ChangeSceneFinishBReq;
import com.doupo.protocol.GuidanceMainMapMonsterEnterReq;
import com.doupo.protocol.HeroSkillSkillUpdateResp;
import com.doupo.protocol.IntegerAndIntegerPairEntry;
import com.doupo.protocol.LotteryDrawReq;
import com.doupo.protocol.LotteryDrawResp;
import com.doupo.protocol.LotteryType;
import com.doupo.protocol.MainMapChapterInfoResp;
import com.doupo.protocol.PackInfoResp;
import com.doupo.protocol.PackUpdateResp;
import com.doupo.protocol.PackUpdateVo;
import com.doupo.protocol.PlayerCreateReq;
import com.doupo.protocol.PlayerFightForceResp;
import com.doupo.protocol.PlayerGuideSaveResp;
import com.doupo.protocol.SceneUnitVo;
import com.doupo.protocol.SceneUpdateVisibleResp;
import com.doupo.protocol.SyncNonSceneHeroVoUpdateResp;
import com.doupo.protocol.TaskPhase;
import com.doupo.protocol.TaskUpdateResp;
import com.doupo.protocol.UpdateItem;
import com.doupo.server.module.combat.CombatSession;
import com.doupo.server.module.combat.CombatSessionRegistry;
import com.doupo.server.module.system.PlayerCreateHandler;
import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageV3;
import java.util.ArrayList;
import java.util.List;
import org.gaming.fakecmd.side.game.IPlayerContext;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class Chapter9TestCheckpointTest {
    @Test
    public void normalCreationStillStartsAtChapterOne() {
        SceneHandler handler = new SceneHandler();
        RecordingContext c = new RecordingContext(100000009001L);
        create(handler, c);
        assertEquals(10100101, c.last(MainMapChapterInfoResp.class).getMainMapChapterId());
        assertEquals(0, c.last(PlayerGuideSaveResp.class).getGuideGroupAndIdPairListCount());
    }

    @Test
    public void checkpointRestoresCapturedProgressBeforeInitEnd() {
        SceneHandler handler = checkpointHandler();
        RecordingContext c = new RecordingContext(100000009002L);
        create(handler, c);
        MainMapChapterInfoResp chapter = c.last(MainMapChapterInfoResp.class);
        assertEquals(10200401, chapter.getMainMapChapterId());
        assertEquals(10200305, chapter.getHistoryTopId());
        assertEquals(0, chapter.getNextChallengeId());
        assertEquals(7, c.last(AllHeroLevelInfoResp.class).getHeroVoList(0).getLevel());
        assertEquals(25778, c.last(PlayerFightForceResp.class).getPlayerFightForce(), 0.001);
        assertEquals(10, c.last(AlchemyNewExpChangeResp.class).getExp());
        assertEquals(29, c.itemCount(100200));
        assertEquals(200023, c.last(TaskUpdateResp.class).getTaskVos(0).getTaskId().getTaskResourceId());
        assertEquals(6, c.last(HeroSkillSkillUpdateResp.class).getPlayerSkillUpdatesCount());
        assertTrue(c.ids.indexOf(50455) > 0);
        for (IntegerAndIntegerPairEntry guide : c.last(PlayerGuideSaveResp.class).getGuideGroupAndIdPairListList()) {
            assertNotEquals(10043, guide.getKey());
        }
        assertTrue(c.ids.indexOf(50353) > c.ids.lastIndexOf(61951));
        handler.drawNewFightSkill(c, LotteryDrawReq.newBuilder()
                .setLotteryType(LotteryType.LOTTERY_TYPE_NEW_FIGHT_SKILL).build());
        assertFalse(c.last(LotteryDrawResp.class).getFailure());
        assertEquals(9, c.itemCount(100200));
        assertEquals(TaskPhase.FINISHED, c.last(TaskUpdateResp.class).getTaskVos(0).getTaskPhase());
        create(handler, c);
        assertEquals(29, c.itemCount(100200));
    }

    @Test
    public void loadedHeroAndBossSessionUseSameCheckpointStats() {
        SceneHandler handler = checkpointHandler();
        RecordingContext c = new RecordingContext(100000009003L);
        create(handler, c);
        SceneUpdateVisibleResp loaded = handler.finishSceneLoadB(c,
                ChangeSceneFinishBReq.newBuilder().setMapId(2).build());
        SceneUnitVo hero = loaded.getVisibleList(0);
        assertEquals(7, hero.getHeroVo().getCrossHeroShortInfo().getLevel());
        assertEquals(25778, hero.getHeroVo().getFightPower(), 0.001);
        assertEquals(47938.8, attr(hero, 103001), 0.001);
        assertEquals(47938.8, attr(hero, 103011), 0.001);
        assertEquals(1846.54656, attr(hero, 101001), 0.001);
        assertEquals(728, attr(hero, 102001), 0.001);
        assertEquals(1214, attr(hero, 101002), 0.001);
        assertEquals(495, attr(hero, 102002), 0.001);
        assertEquals(30362, attr(hero, 103002), 0.001);
        assertEquals(7, c.last(SyncNonSceneHeroVoUpdateResp.class).getVoList(0)
                .getHeroVo().getCrossHeroShortInfo().getLevel());
        assertFalse(c.last(SyncNonSceneHeroVoUpdateResp.class).getVoList(0).getHeroVo().getClientDriven());
        handler.enterGuidanceMainMapMonster(c, GuidanceMainMapMonsterEnterReq.newBuilder()
                .setChapterId(10200405).setZ(23).build());
        CombatSession session = CombatSessionRegistry.get(c.id);
        assertEquals(47938.8, session.getPlayer().getMaxHp(), 0.001);
        assertFalse(session.isNinthBossGuideStarted());
        assertEquals(7, session.getNinthBossPlayerSnapshot().getHeroVo().getCrossHeroShortInfo().getLevel());
        handler.resetTutorialPlayer(c.id);
        assertNull(CombatSessionRegistry.get(c.id));
    }

    private static SceneHandler checkpointHandler() {
        SceneHandler handler = new SceneHandler();
        try {
            java.lang.reflect.Field enabled = SceneHandler.class.getDeclaredField("chapter9TestEnabled");
            enabled.setAccessible(true);
            enabled.set(handler, true);
        } catch (ReflectiveOperationException e) { throw new AssertionError(e); }
        return handler;
    }

    private static void create(SceneHandler handler, RecordingContext c) {
        new PlayerCreateHandler(handler).createPlayer(c,
                PlayerCreateReq.newBuilder().setName("第9关测试").build());
    }

    private static double attr(SceneUnitVo hero, int type) {
        return hero.getFightInfoVo().getAttributeListList().stream()
                .filter(a -> a.getType() == type).findFirst().get().getValue();
    }

    private static final class RecordingContext implements IPlayerContext {
        final long id;
        final List<Integer> ids = new ArrayList<>();
        final List<GeneratedMessageV3> messages = new ArrayList<>();
        RecordingContext(long id) { this.id = id; }
        <T> T last(Class<T> type) {
            for (int i = messages.size() - 1; i >= 0; i--) {
                if (type.isInstance(messages.get(i))) return type.cast(messages.get(i));
            }
            throw new AssertionError("Missing " + type);
        }
        int itemCount(int key) {
            for (int i = messages.size() - 1; i >= 0; i--) {
                GeneratedMessageV3 m = messages.get(i);
                List<PackUpdateVo> packs = m instanceof PackInfoResp ? ((PackInfoResp) m).getPacksList()
                        : m instanceof PackUpdateResp ? ((PackUpdateResp) m).getPacksList() : new ArrayList<>();
                for (PackUpdateVo pack : packs) for (UpdateItem item : pack.getUpdateItemsList()) {
                    if (item.getPackItem().getKey() == key) return item.getPackItem().getSize();
                }
            }
            return 0;
        }
        public long getId() { return id; }
        public int getServerZone() { return 1; }
        public int getCurrMsgId() { return 0; }
        public void setCurrMsgId(int id) { }
        public boolean isLogin() { return true; }
        public void write(int cmd, GeneratedMessageV3 m, int request) { ids.add(cmd); messages.add(m); }
        public void write(int cmd, ByteString m, int request) { ids.add(cmd); messages.add(null); }
    }
}
