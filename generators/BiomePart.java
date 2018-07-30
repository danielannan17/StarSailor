package generators;

/**
 * a part of a biome
 * 
 * 
 *
 */
public class BiomePart {

	private Block block;
	private float start, end, chance;

	/**
	 * creates a new biome part
	 * 
	 * @param block
	 *            the block this part uses
	 * @param start
	 *            the start of this part
	 * @param end
	 *            the end of this part
	 * @param chance
	 *            the chance of this part occuring
	 */
	public BiomePart(Block block, float start, float end, float chance) {
		setBlock(block);
		setStart(start);
		setEnd(end);
		setChance(chance);
	}

	/**
	 * gets the block this part uses
	 * 
	 * @return the block
	 */
	public Block getBlock() {
		return block;
	}

	/**
	 * sets the block this part uses
	 * 
	 * @param block
	 *            the new block this part uses
	 */
	public void setBlock(Block block) {
		this.block = block;
	}

	/**
	 * gets the start of this part
	 * 
	 * @return the start float for this part
	 */
	public float getStart() {
		return start;
	}

	/**
	 * sets the start of this part
	 * 
	 * @param start
	 *            the new start
	 */
	public void setStart(float start) {
		this.start = start;
	}

	/**
	 * gets the end of this part
	 * 
	 * @return the end of this part
	 */
	public float getEnd() {
		return end;
	}

	/**
	 * sets the ned of this part
	 * 
	 * @param end
	 *            the new end
	 */
	public void setEnd(float end) {
		this.end = end;
	}

	/**
	 * gets the chance this part will show up
	 * 
	 * @return the chance this part will show
	 */
	public float getChance() {
		return chance;
	}

	/**
	 * sets the chance this part will show
	 * 
	 * @param chance
	 *            the new chance
	 */
	public void setChance(float chance) {
		this.chance = chance;
	}

}
