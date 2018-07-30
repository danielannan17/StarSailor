package instantBattle.testing;

import static org.junit.Assert.*;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import entities.AvailableSkill;
import entities.skills.*;
import main.Main;

public class AvailableSkillTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Main.projectileCache = new HashMap<String,BufferedImage[][]>();
		Main.cache = new HashMap<String,Image[]>();
	AvailableSkill.initialise();
		
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before


	@After
	public void tearDown() throws Exception {
	}

	
	@Test
	public void testSkillSpritesStored() {
		assertTrue(Main.projectileCache.containsKey("basic"));
		assertTrue(Main.projectileCache.containsKey("eBasic"));
		assertTrue(Main.projectileCache.containsKey("lightningOrb"));
		assertTrue(Main.projectileCache.containsKey("eLightningOrb"));
		assertTrue(Main.projectileCache.containsKey("penetrate"));
		assertTrue(Main.projectileCache.containsKey("ePenetrate"));
	}

	@Test
	public void testGetSkills() {
		assertArrayEquals(new AvailableSkill[]{AvailableSkill.DashSkill,AvailableSkill.LightningOrbSkill,
				AvailableSkill.PenetrateSkill, AvailableSkill.BasicAttackSkill, AvailableSkill.FlashSkill,
				AvailableSkill.ArmourSkill},AvailableSkill.getSkills(true, true, true));
		
		assertArrayEquals(new AvailableSkill[]{AvailableSkill.LightningOrbSkill,
			AvailableSkill.PenetrateSkill, AvailableSkill.BasicAttackSkill,
			AvailableSkill.ArmourSkill}, AvailableSkill.getSkills(true, true, false));
	
	assertArrayEquals(new AvailableSkill[]{AvailableSkill.DashSkill, AvailableSkill.LightningOrbSkill,
			AvailableSkill.PenetrateSkill, AvailableSkill.BasicAttackSkill, AvailableSkill.FlashSkill},
			AvailableSkill.getSkills(true, false, true));
	
	assertArrayEquals(new AvailableSkill[]{AvailableSkill.LightningOrbSkill,
			AvailableSkill.PenetrateSkill, AvailableSkill.BasicAttackSkill},
			AvailableSkill.getSkills(true, false, false));
	
	assertArrayEquals(new AvailableSkill[]{AvailableSkill.DashSkill, AvailableSkill.FlashSkill,
			AvailableSkill.ArmourSkill}, AvailableSkill.getSkills(false, true, true));
	
	assertArrayEquals(new AvailableSkill[]{AvailableSkill.ArmourSkill}, AvailableSkill.getSkills(false, true, false));
	
	assertArrayEquals(new AvailableSkill[]{AvailableSkill.DashSkill, AvailableSkill.FlashSkill},
			AvailableSkill.getSkills(false, false, true));
	
	assertArrayEquals(new AvailableSkill[]{},AvailableSkill.getSkills(false, false, false));
	}

	@Test
	public void testGetSkillIcon() {
		assertEquals(AvailableSkill.DashSkill.getSkillIcon(), Dash.getSkillIcon());
		assertEquals(AvailableSkill.FlashSkill.getSkillIcon(), Flash.getSkillIcon());
		assertEquals(AvailableSkill.LightningOrbSkill.getSkillIcon(), LightningOrb.getSkillIcon());
		assertEquals(AvailableSkill.PenetrateSkill.getSkillIcon(), Penetrate.getSkillIcon());
		assertEquals(AvailableSkill.BasicAttackSkill.getSkillIcon(), BasicAttack.getSkillIcon());
		assertEquals(AvailableSkill.ArmourSkill.getSkillIcon(), Armour.getSkillIcon());
		//TODO Figure out how to reach the null
	}

	@Test
	public void testGetSkillInt() {
			assertEquals(new  LightningOrb(),AvailableSkill.getSkill(2));
		assertEquals(new Dash(), AvailableSkill.getSkill(1));
		assertEquals(new  Flash(),AvailableSkill.getSkill(5));
		assertEquals(new Penetrate(), AvailableSkill.getSkill(3));
		assertEquals(new BasicAttack(), AvailableSkill.getSkill(4));
		assertEquals(new Armour(), AvailableSkill.getSkill(6));
		assertEquals(null, AvailableSkill.getSkill(7), null);
		
	}

	@Test
	public void testGetSkill() {
		assertEquals( new  Dash(), AvailableSkill.DashSkill.getSkill());
		assertEquals( new  Flash(),AvailableSkill.FlashSkill.getSkill());
		assertEquals(new LightningOrb(), AvailableSkill.LightningOrbSkill.getSkill());
		assertEquals(new Penetrate(), AvailableSkill.PenetrateSkill.getSkill());
		assertEquals(new BasicAttack(), AvailableSkill.BasicAttackSkill.getSkill());
		assertEquals(new Armour(), AvailableSkill.ArmourSkill.getSkill());
	}
	
	@Test
	public void testGetSkillProjectile() {
		
				for (int i = 0; i < AvailableSkill.getSkillProjectile(2, true)[0].length;i++) {
					assertEquals(new LightningOrb().getProjectileIcon(true)[0][i],AvailableSkill.getSkillProjectile(2, true)[0][i]);
				}
		assertArrayEquals(new LightningOrb().getProjectileIcon(false) ,AvailableSkill.getSkillProjectile(2, false));
		assertTrue(null == AvailableSkill.getSkillProjectile(1, true));
		assertTrue(new Penetrate().getProjectileIcon(true) == AvailableSkill.getSkillProjectile(3, true));
		assertTrue(new Penetrate().getProjectileIcon(false) == AvailableSkill.getSkillProjectile(3, false));
		assertTrue(new BasicAttack().getProjectileIcon(true) == AvailableSkill.getSkillProjectile(4, true));
		assertTrue(new BasicAttack().getProjectileIcon(false) == AvailableSkill.getSkillProjectile(4, false));
	}


	@Test
	public void testGetSkillDescription() {
		assertEquals("Gives a speed boost in the direction of movement.", AvailableSkill.getSkillDescription(1));
		assertEquals("Shoots a lightning orb that deals damage overtime to enemys touching it", AvailableSkill.getSkillDescription(2));
		assertEquals("Shoots a projectile that passes through " + Penetrate.penetration + " enemies.", AvailableSkill.getSkillDescription(3));
		assertEquals("Shoots a basic projectile.", AvailableSkill.getSkillDescription(4));
		assertEquals("Teleports you a set distance in the direction you are facing.", AvailableSkill.getSkillDescription(5));
		assertEquals("Gives you " + (int) Armour.value + " armour.", AvailableSkill.getSkillDescription(6));
		
	}

	@Test
	public void testGetSkillID() {
		assertSame(1, AvailableSkill.DashSkill.getSkillID());
		assertSame(2, AvailableSkill.LightningOrbSkill.getSkillID());
		assertSame(3, AvailableSkill.PenetrateSkill.getSkillID());
		assertSame(4, AvailableSkill.BasicAttackSkill.getSkillID());
		assertSame(5, AvailableSkill.FlashSkill.getSkillID());
		assertSame(6, AvailableSkill.ArmourSkill.getSkillID());
	
	}



}
