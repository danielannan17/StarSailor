package generators;

import java.util.Random;

/**
 * random name generator used for planets names
 * 
 * 
 *
 */
public class NameGenerator {

	private static Random random = new Random();

	private static String namePart[] = { "a", "aa", "ab", "aber", "ac", "acc", "ack", "afon", "an", "and", "ant", "ar",
			"ard", "ash", "ast", "at", "avon", "auch", "auchter", "axe", "ay", "ba", "bal", "ball", "balla", "bally",
			"be", "beck", "ben", "bein", "berg", "berry", "bex", "bi", "bin", "bio", "boo", "bost", "bourne", "burn",
			"can", "cap", "ce", "chrom", "chron", "cor", "da", "di", "dict", "dis", "dom", "dor", "e", "ed", "en",
			"ent", "er", "ess", "eth", "ex", "fa", "fid", "flect", "flor", "fleur", "for", "fract", "faa", "faas",
			"fab", "fahg", "fal", "fer", "gas", "gen", "geo", "giga", "glu", "gre", "gyn", "hect", "helic", "helio",
			"hemi", "hex", "ho", "hut", "hydr", "hyp", "i", "ian", "in", "ine", "io", "ist", "ize", "ja", "jak", "ject",
			"jel", "jil", "jud", "juven", "ka", "kaa", "kel", "kes", "kilo", "kine", "ku", "la", "lab", "leuk", "lex",
			"lia", "lib", "lif", "lite", "loc", "loqu", "luc", "luk", "lumin", "lun", "macro", "magn", "mal", "max",
			"mi", "mis", "na", "nab", "nal", "narr", "nat", "nav", "necro", "neg", "neo", "nephr", "neur", "o", "oct",
			"ocu", "ol", "omni", "oo", "or", "oxi", "pale", "pan", "para", "per", "phag", "phi", "phil", "plas", "poli",
			"pos", "pyr", "qa", "quad", "quart", "quin", "que", "qui", "quil", "ra", "raa", "rab", "ran", "rix", "rol",
			"ry", "san", "shil", "so", "spec", "sul", "tat", "th", "ti", "tin", "tec", "tro", "u", "um", "umb", "up",
			"ur", "ut", "va", "vac", "ve", "ven", "ver", "vok", "wa", "war", "wes", "whel", "wick", "win", "wor",
			"worth", "xan", "xeb", "xen", "xer", "xi", "xor", "xy", "ya", "yag", "yav", "yel", "yen", "yor", "za",
			"zahn", "ze", "zen", "zi" };

	/**
	 * generates a new name
	 * 
	 * @param permutations
	 *            the number of wordparts used to make this name
	 * @return a procedurally generated name
	 */
	public static String generateName(int permutations) {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < permutations; i++) {
			s.append(namePart[random.nextInt(namePart.length)]);
		}
		return s.toString();
	}

	/**
	 * generates a new name
	 * 
	 * @param permutations
	 *            the number of word parts to use
	 * @param seed
	 *            the seed to give the random generator
	 * @return a new name
	 */
	public static String generateName(int permutations, long seed) {
		setSeed(seed);
		return generateName(permutations);
	}

	/**
	 * sets the seed for this name generator
	 * 
	 * @param seed
	 *            the new seed
	 */
	public static void setSeed(long seed) {
		random.setSeed(seed);
	}

}
