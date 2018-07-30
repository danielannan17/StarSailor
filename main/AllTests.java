package main;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import database.DBConnectTest;
import entities.Vector2DTest;
import galaxy.GalaxyTest;
import galaxy.PlanetTest;
import galaxy.StarTest;
import galaxy.battle.testing.BattleTest;
import galaxy.battle.testing.CarrierTest;
import galaxy.battle.testing.EntityTest;
import galaxy.battle.testing.LongRangeLaserTest;
import gameAI.battle.battle_testing.AILogisticsTest;
import gameAI.battle.battle_testing.BattleAITest;
import gameAI.battle.battle_testing.BattleOrderTest;
import gameAI.battle.battle_testing.ShipDescriptionTest;
import gameAI.battle.battle_testing.SquadAITest;
import gameAI.battle.battle_testing.SquadFormationTest;
import gameLogic.PlayerStatsTest;
import generators.BiomeTest;
import generators.PerlinNoiseGeneratorTest;
import generators.PlanetGeneratorTest;
import handlers.MathHandlerTest;
import handlers.ResourceHandlerTest;
import handlers.SoundHandlerTest;
import instantBattle.PhysicsEngineTest;
import instantBattle.ShipBehaviourTest;
import instantBattle.testing.AvailableSkillTest;
import instantBattle.testing.BattleControllerTest;
import instantBattle.testing.InstantBattleTest;
import menu.BattleStartTest;
import menu.MainMenuTest;
import menu.OptionsMenuTest;
import menu.PlayMenuTest;
import messaging.PopUpTest;
import networking.InitMessageTest;
import networking.KeyMoveTest;
import networking.KeyValTest;
import networking.StartMessageTest;

@RunWith(Suite.class)
@SuiteClasses({
				AvailableSkillTest.class,
				BattleControllerTest.class,
				InstantBattleTest.class,
                GalaxyTest.class,
                 PlayerStatsTest.class,
                PlanetTest.class,
                StarTest.class,
                PerlinNoiseGeneratorTest.class,
                PlanetGeneratorTest.class,
                DBConnectTest.class,
                PopUpTest.class,
                BiomeTest.class,
                MainMenuTest.class,
                PlayMenuTest.class,
                OptionsMenuTest.class,
                MathHandlerTest.class,
                SoundHandlerTest.class,
                ResourceHandlerTest.class,
                BattleTest.class,
                CarrierTest.class,
                EntityTest.class,
                LongRangeLaserTest.class,
                AILogisticsTest.class,
                BattleAITest.class,
                BattleOrderTest.class,
                ShipDescriptionTest.class,
                SquadAITest.class,
                SquadFormationTest.class,
                BattleStartTest.class,
                InitMessageTest.class,
                KeyMoveTest.class,
                KeyValTest.class,
                StartMessageTest.class,
                PhysicsEngineTest.class,
                Vector2DTest.class,
                ShipBehaviourTest.class})

public class AllTests {

}
