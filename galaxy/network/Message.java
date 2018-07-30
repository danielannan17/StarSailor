package galaxy.network;

import java.nio.ByteBuffer;

/**
 * a message object that can be serialised and sent over the network
 * 
 * 
 *
 */
public class Message {

	public static final int SET_OWNERSHIP = 0, REQUEST_OWNERSHIP = 1, SEED = 2, PLAYER_NUMBER = 3, PLANET_OWNERSHIP = 4, GET_SHIPS = 5, SET_SHIPS = 6;
	private int messageID, starID, planetID, ownedBy, battling, playerNumber, numOfPlanets, numOfFighters, numOfCarriers, numOfCommand;
	private int[] planetOwnership;
	private long seed;
	private static ByteBuffer buffer;

	/**
	 * creates a new message for setting the ownership of a planet or starting a
	 * battle
	 * 
	 * @param starID
	 *            the id of the star this message is about
	 * @param planetID
	 *            the id of the planet this message is about
	 * @param ownedBy
	 *            the id of the player this planet is now owned by
	 * @param battling
	 *            whether this planet is currently battling
	 */
	public static Message createSetOwnership(int starID, int planetID, int ownedBy, int battling) {
		Message m = new Message();
		m.setMessageID(SET_OWNERSHIP);
		m.setStarID(starID);
		m.setPlanetID(planetID);
		m.setOwnedBy(ownedBy);
		m.setBattling(battling);
		return m;
	}

	/**
	 * creates a new message for requesting the player number of who owns the
	 * planets in a system
	 * 
	 * @param starID
	 *            the id of the star being checked
	 * @param numOfPlanets
	 *            the number of planets the star has
	 * @param playerNumber
	 *            the player requesting the information
	 */
	public static Message createRequestOwnership(int starID, int playerNumber) {
		Message m = new Message();
		m.setMessageID(REQUEST_OWNERSHIP);
		m.setStarID(starID);
		m.setPlayerNumber(playerNumber);
		return m;
	}

	/**
	 * creates a new message used for sending the player their player number
	 * 
	 * @param playerNumber
	 *            the number of the player
	 */
	public static Message createPlayerNumber(int playerNumber) {
		Message m = new Message();
		m.setMessageID(PLAYER_NUMBER);
		m.setPlayerNumber(playerNumber);
		return m;
	}

	/**
	 * creates a message used for sending the seed to players
	 * 
	 * @param seed
	 *            the seed of the game
	 */
	public static Message createSeed(long seed) {
		Message m = new Message();
		m.setMessageID(SEED);
		m.setSeed(seed);
		return m;
	}

	/**
	 * creates a message used for sending the player the ownership of each
	 * planet in a system
	 * 
	 * @param planetOwnership
	 *            the array of ints representing who owns which planet, should
	 *            always be the same size
	 */
	public static Message createPlanetOwnership(int starID, int[] planetOwnership) {
		Message m = new Message();
		m.setMessageID(PLANET_OWNERSHIP);
		m.setPlanetOwnership(planetOwnership);
		m.setStarID(starID);
		return m;
	}
	
	public static Message createGetShips(int starID, int planetID){
		Message m = new Message();
		m.setMessageID(GET_SHIPS);
		m.setStarID(starID);
		m.setPlanetID(planetID);
		return m;
	}
	
	public static Message createSetShips(int starID, int planetID, int numOfFighters, int numOfCarriers, int numOfCommand){
		Message m = new Message();
		m.setMessageID(SET_SHIPS);
		m.setStarID(starID);
		m.setPlanetID(planetID);
		m.setNumOfFighters(numOfFighters);
		m.setNumOfCarriers(numOfCarriers);
		m.setNumOfCommand(numOfCommand);
		return m;
	}

	/**
	 * serializes this message into a byte array
	 * 
	 * @return the byte array that represents this message
	 */
	public byte[] serialize() {
		byte[] message = null;
		switch (messageID) {
		case SET_OWNERSHIP:
			message = new byte[5];
			message[0] = (byte) messageID;
			message[1] = (byte) starID;
			message[2] = (byte) planetID;
			message[3] = (byte) ownedBy;
			message[4] = (byte) battling;
			break;
		case REQUEST_OWNERSHIP:
			message = new byte[4];
			message[0] = (byte) messageID;
			message[1] = (byte) starID;
			message[2] = (byte) playerNumber;
			break;
		case SEED:
			message = new byte[9];
			message[0] = (byte) messageID;
			byte[] b = longToBytes(seed);
			for (int i = 1; i < message.length; i++) {
				message[i] = b[i - 1];
			}
			break;
		case PLAYER_NUMBER:
			message = new byte[2];
			message[0] = (byte) messageID;
			message[1] = (byte) playerNumber;
			break;
		case PLANET_OWNERSHIP:
			message = new byte[planetOwnership.length + 2];
			message[0] = (byte) messageID;
			message[1] = (byte) starID;
			for (int i = 2; i < planetOwnership.length + 2; i++) {
				message[i] = (byte) planetOwnership[i - 2];
			}
			break;
		case GET_SHIPS:
			message = new byte[4];
			message[0] = (byte) messageID;
			message[1] = (byte) starID;
			message[2] = (byte) planetID;
			break;
		case SET_SHIPS:
			message = new byte[6];
			message[0] = (byte) messageID;
			message[1] = (byte) starID;
			message[2] = (byte) planetID;
			message[3] = (byte) numOfFighters;
			message[4] = (byte) numOfCarriers;
			message[5] = (byte) numOfCommand;
			break;
		}
		return message;
	}

	/**
	 * deserializes a message object
	 * 
	 * @param b
	 *            the byte array to deserialize
	 * @return a message object
	 */
	public static Message deSerialize(byte[] b) {
		Message m = null;
		switch (b[0]) {
		case PLANET_OWNERSHIP:
			int starID = (int) b[1];
			int[] planets = new int[b.length - 2];
			for (int i = 2; i < b.length; i++) {
				planets[i - 2] = (int) b[i];
			}
			m = createPlanetOwnership(starID, planets);
			break;
		case SET_OWNERSHIP:
			m = createSetOwnership(b[1], b[2], b[3], b[4]);
			break;
		case REQUEST_OWNERSHIP:
			m = createRequestOwnership(b[1], b[2]);
			break;
		case SEED:
			byte[] b1 = new byte[b.length - 1];
			for (int i = 1; i < b.length; i++) {
				b1[i - 1] = (byte) b[i];
			}
			m = createSeed(bytesToLong(b1));
			break;
		case PLAYER_NUMBER:
			m = createPlayerNumber(b[1]);
			break;
		case GET_SHIPS:
			m = createGetShips(b[1], b[2]);
			break;
		case SET_SHIPS:
			m = createSetShips(b[1], b[2], b[3], b[4], b[5]);
			break;
		}
		return m;
	}

	/**
	 * gets the star id this message is about
	 * 
	 * @return the star id
	 */
	public int getStarID() {
		return starID;
	}

	/**
	 * sets the star id of this message
	 * 
	 * @param starID
	 *            the new star id
	 */
	public void setStarID(int starID) {
		this.starID = starID;
	}

	/**
	 * gets the planet id of this message
	 * 
	 * @return the planet id
	 */
	public int getPlanetID() {
		return planetID;
	}

	/**
	 * sets the planet id
	 * 
	 * @param planetID
	 *            the new planet id
	 */
	public void setPlanetID(int planetID) {
		this.planetID = planetID;
	}

	/**
	 * gets the player that owns the planet this message is about
	 * 
	 * @return the player number who owns the planet
	 */
	public int getOwnedBy() {
		return ownedBy;
	}

	/**
	 * sets who owns the planet in this message
	 * 
	 * @param ownedBy
	 *            the person who owns the planet
	 */
	public void setOwnedBy(int ownedBy) {
		this.ownedBy = ownedBy;
	}

	/**
	 * gets if this planet is being battled
	 * 
	 * @return the player who is battling this planet
	 */
	public int getBattling() {
		return battling;
	}

	/**
	 * sets who is battling this planet, if no one is it should be 0
	 * 
	 * @param battling
	 *            the new value for battling
	 */
	public void setBattling(int battling) {
		this.battling = battling;
	}

	/**
	 * gets the player number from a message
	 * 
	 * @return the player number
	 */
	public int getPlayerNumber() {
		return playerNumber;
	}

	/**
	 * sets the player number
	 * 
	 * @param playerNumber
	 *            the new player number
	 */
	public void setPlayerNumber(int playerNumber) {
		this.playerNumber = playerNumber;
	}

	/**
	 * gets the seed from this message
	 * 
	 * @return the seed
	 */
	public long getSeed() {
		return seed;
	}

	/**
	 * sets the seed for this message
	 * 
	 * @param seed
	 *            the new seed
	 */
	public void setSeed(long seed) {
		this.seed = seed;
	}

	/**
	 * gets the number of planets in this message
	 * 
	 * @return the number of planets
	 */
	public int getNumOfPlanets() {
		return numOfPlanets;
	}

	/**
	 * sets the number of planets in this message
	 * 
	 * @param numOfPlanets
	 *            the number of planets
	 */
	public void setNumOfPlanets(int numOfPlanets) {
		this.numOfPlanets = numOfPlanets;
	}

	/**
	 * gets the message id
	 * 
	 * @return the message id
	 */
	public int getMessageID() {
		return messageID;
	}

	/**
	 * sets the message id
	 * 
	 * @param messageID
	 *            the new message id
	 */
	public void setMessageID(int messageID) {
		this.messageID = messageID;
	}

	/**
	 * method for converting a long into a byte array
	 * 
	 * @param x
	 *            the long to convert
	 * @return the byte array
	 */
	public static byte[] longToBytes(long x) {
		buffer = ByteBuffer.allocate(Long.BYTES);
		buffer.putLong(x);
		return buffer.array();
	}

	/**
	 * converts a byte array into a long
	 * 
	 * @param bytes
	 *            the bytes to convert
	 * @return a long
	 */
	public static long bytesToLong(byte[] bytes) {
		buffer = ByteBuffer.allocate(Long.BYTES);
		buffer.put(bytes);
		buffer.flip();
		return buffer.getLong();
	}

	/**
	 * gets the array of planets
	 * 
	 * @return an array of ints representing who owns which planets
	 */
	public int[] getPlanetOwnership() {
		return planetOwnership;
	}

	/**
	 * sets the array of planet ownership
	 * 
	 * @param planetOwnership
	 *            the new array
	 */
	public void setPlanetOwnership(int[] planetOwnership) {
		this.planetOwnership = planetOwnership;
	}

	public int getNumOfFighters() {
		return numOfFighters;
	}

	public void setNumOfFighters(int numOfFighters) {
		this.numOfFighters = numOfFighters;
	}

	public int getNumOfCarriers() {
		return numOfCarriers;
	}

	public void setNumOfCarriers(int numOfCarriers) {
		this.numOfCarriers = numOfCarriers;
	}

	public int getNumOfCommand() {
		return numOfCommand;
	}

	public void setNumOfCommand(int numOfCommand) {
		this.numOfCommand = numOfCommand;
	}

}
