/**
 * @author Alexander J. Schmidt
 * @version 1.0
 * Random map generator
 *
 */
package heros.journey.utils.worldgen;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * The Class RandomWorldGenerator.
 */
public class RandomWorldGenerator {

	/**
	 * The amplitude of the map, this determines how much variation there is in map
	 * levels. The higher the number the more levels and vice versa.
	 *
	 * 80 appears to cap the max at 100 70 appears to cap the max at 87
	 *
	 * Default value: 70f.
	 */
	public float amplitude;

	/**
	 * The octaves is how many tiles around each tile is looked at during the
	 * smoothing process. The higher the octaves the larger the land masses will be.
	 * However increasing octaves will increase the time it takes to generate the
	 * map.
	 *
	 * Default value: 5.
	 */
	public int octaves;

	/**
	 * The roughness is how rough the edges of the land masses will be. The higher
	 * the roughness the more rough the edges are.
	 *
	 * Default value: .4f.
	 */
	public float roughness;

	/**
	 * The seed of the map is whats used during random number generation. If you
	 * input the same seed with the same parameters it will always generate the same
	 * map.
	 *
	 * Default value: randomly generated number.
	 */
	public int seed;

	/**
	 * Island determines if the land should be generated in an island shape. If
	 * island is false it will generate completely random masses. If island is true
	 * it will make it so that the land mass(s) is centered with water around the
	 * edges.
	 *
	 * Default value: false
	 */
	public boolean island;

	/**
	 * The random number generator.
	 */
	private Random random = new Random();

	/**
	 * Instantiates a new random world generator.
	 *
	 * @param size
	 *            the size
	 * @param amplitude
	 *            the amplitude
	 * @param octaves
	 *            the octaves
	 * @param roughness
	 *            the roughness
	 * @param island
	 *            the island
	 */
	public RandomWorldGenerator(float amplitude, int octaves, float roughness, boolean island) {
		this.amplitude = amplitude;
		this.octaves = octaves;
		this.roughness = roughness;
		this.island = island;
		this.seed = random.nextInt(1000000000);
	}

	/**
	 * Instantiates a new random world generator.
	 *
	 * @param seed
	 *            the seed
	 * @param size
	 *            the size
	 * @param amplitude
	 *            the amplitude
	 * @param octaves
	 *            the octaves
	 * @param roughness
	 *            the roughness
	 * @param island
	 *            the island
	 */
	public RandomWorldGenerator(int seed, float amplitude, int octaves, float roughness, boolean island) {
		this(amplitude, octaves, roughness, island);
		this.seed = seed;
	}

	/**
	 * Instantiates a new random world generator.
	 *
	 * @param seed
	 *            the seed
	 * @param island
	 *            the island
	 */
	public RandomWorldGenerator(int seed, boolean island) {
		this(70f, 5, .4f, island);
		this.seed = seed;
	}

	/**
	 * Instantiates a new random world generator.
	 *
	 * @param island
	 *            the island
	 */
	public RandomWorldGenerator(boolean island) {
		this(70f, 5, .4f, island);
	}

	/**
	 * Instantiates a new random world generator.
	 */
	public RandomWorldGenerator() {
		this(70f, 5, .4f, false);
	}

	private int size;

	/**
	 * Generates a size x size map based on the parameters that are currently put
	 * in.
	 *
	 * @return the int[][]
	 */
	public int[][] generateMap(int size) {
		this.size = size;
		int[][] map = new int[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				map[i][j] = generateTerrain(i, j);
			}
		}
		return map;
	}

	/**
	 * Saves a .png file of the map where it colors in green where terrain is above
	 * the cutOff value and blue were the terrain is below the cutOff value. Throws
	 * an exception if it can not save the image.
	 *
	 * @param map
	 *            the map
	 * @param cutOff
	 *            the cut off value for
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void saveMap(int[][] map, float cutOff) throws IOException {
		if (map.length != map[0].length)
			return;
		BufferedImage image = new BufferedImage(map.length, map.length, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map.length; j++) {
				if (map[i][j] == cutOff)
					image.setRGB(i, j, Color.YELLOW.getRGB());
				else if (map[i][j] > cutOff)
					image.setRGB(i, j, Color.GREEN.getRGB());
				else
					image.setRGB(i, j, Color.BLUE.getRGB());
			}
		}

		File outputfile = new File("saved_map_" + map.length + ".png");
		ImageIO.write(image, "png", outputfile);
	}

	/**
	 * Generates the terrain height at a specific coordinate point.
	 *
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @return the terrain height
	 */
	public int generateTerrain(int x, int y) {
		float total = 0;
		float d = (float) Math.pow(2, (octaves * 2) - 1);
		for (int i = 0; i < octaves; i += 1) {
			float freq = (float) (Math.pow(2, i) / d);
			float amp = (float) Math.pow(roughness, i) * amplitude;
			total += (getInterpolatedNoise(x * freq, y * freq) * amp);
		}
		return (int) ((total) * (island ? quadraticDistance(x, y) : 1));

	}

	/**
	 * Quadratic distance from the center. This finds how far the point is from the
	 * center. If island is turned on this will make it so that values at the center
	 * of the map will stay high will values at the edge will be reduced so the land
	 * mass is guaranteed to generate higher land in the center and lower land at
	 * the edge to create an island like shape.
	 *
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @return the distance from the center
	 */
	private float quadraticDistance(int x, int y) {
		float distance_x = Math.abs(x - size * 0.5f);
		float distance_y = Math.abs(y - size * 0.5f);
		float distance = (float) Math.sqrt(distance_x * distance_x + distance_y * distance_y);

		float max_width = size * 0.5f - (size / 8);
		if (distance <= max_width / 2) {
			return 1;
		}
		float delta = distance / max_width;
		float gradient = delta * delta;

		return Math.max(0.0f, 1.5f - gradient);
	}

	/**
	 * Gets the interpolated noise.
	 *
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @return the interpolated noise
	 */
	private float getInterpolatedNoise(float x, float y) {
		int intX = (int) x;
		int intY = (int) y;
		float fracX = x - intX;
		float fracY = y - intY;

		float v1 = getSmoothNoise(intX, intY);
		float v2 = getSmoothNoise(intX + 1, intY);
		float v3 = getSmoothNoise(intX, intY + 1);
		float v4 = getSmoothNoise(intX + 1, intY + 1);

		float i1 = interpolate(v1, v2, fracX);
		float i2 = interpolate(v3, v4, fracX);

		return interpolate(i1, i2, fracY);
	}

	/**
	 * Uses cosine interpolation to smoothly generate terrain.
	 *
	 * @param a
	 *            the a
	 * @param b
	 *            the b
	 * @param blend
	 *            the blend
	 * @return the interpolated value
	 */
	private float interpolate(float a, float b, float blend) {
		double theta = blend * Math.PI;
		float f = (float) (1f - Math.cos(theta)) * .5f;
		return a * (1f - f) + b * f;
	}

	/**
	 * Gets a smoothed value for the point at the coordinates based on its value,
	 * the points next to it values, and the corners next to it values each having a
	 * diminishing impact of the value. This means that the center will have the
	 * most effect on the final value and the sides with have a medium effect on the
	 * final value, and the corners will have the least effect on the final value.
	 *
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @return the smoothed noise
	 */
	private int getSmoothNoise(int x, int y) {
		float corners = (getNoise(x - 1, y - 1) + getNoise(x + 1, y - 1) + getNoise(x - 1, y + 1) + getNoise(x + 1, y + 1)) / 16;
		float sides = (getNoise(x - 1, y) + getNoise(x + 1, y) + getNoise(x, y + 1) + getNoise(x + 1, y - 1)) / 8;
		float center = getNoise(x, y) / 4;
		return (int) (corners + sides + center);
	}

	/**
	 * Gets a completely random number based on the coordinates and the seed.
	 *
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @return the random noise
	 */
	public float getNoise(int x, int y) {
		random.setSeed(x * 49632 + y * 325176 + seed);
		return random.nextFloat() * 2;
	}

}
