package org.jepsar.primefaces.theme.jepsar;


import java.io.Serializable;


/**
 * RGB color class used to {@link #move(int) darken or lighten} colors by a percentage.
 *
 * @author Jasper de Vries <jepsar@gmail.com>
 * @since 1.0
 */
public class RgbColor implements Serializable
{

	private static final long serialVersionUID = 1L;

	/**
	 * Prefix for hex colors.
	 */
	private static final String HEX_COLOR_PREFIX = "#";

	/**
	 * Hex color string.
	 */
	private static final String HEX_COLOR = "#%02X%02X%02X";

	/**
	 * Expression to match a hex color without the {@link #HEX_COLOR_PREFIX}.
	 */
	private static final String HEX_COLOR_REGEX = "^[0-9A-F]{6}$";

	/**
	 * Invalid color argument exception.
	 */
	private static final String IAE_INVALID_COLOR = "Invalid color";

	/**
	 * Invalid percentage argument exception.
	 */
	private static final String IAE_INVALID_PERCENTAGE = "Percentage should be between -100 and 100";

	/**
	 * Red.
	 */
	private int r;

	/**
	 * Green.
	 */
	private int g;

	/**
	 * Blue.
	 */
	private int b;


	/**
	 * Construct a RGB color using a hex string.
	 *
	 * @param rgbColor Hex string with or without {@link #HEX_COLOR_PREFIX}. For example {@code FFFFFF} or
	 *                 {@code #FFFFFF}.
	 */
	public RgbColor(final String rgbColor)
	{
		String hexString = rgbColor.toUpperCase();
		if (hexString.startsWith(HEX_COLOR_PREFIX)) {
			hexString = hexString.substring(1);
		}
		if (!hexString.matches(HEX_COLOR_REGEX)) {
			throw new IllegalArgumentException(IAE_INVALID_COLOR);
		}
		r = hexToInt(hexString.substring(0, 2));
		g = hexToInt(hexString.substring(2, 4));
		b = hexToInt(hexString.substring(4, 6));
	}


	/**
	 * Construct a RGB color using individual components
	 *
	 * @param r Red component. [0..255]
	 * @param g Green component. [0..255]
	 * @param b Blue component. [0..255]
	 */
	public RgbColor(final int r, final int g, final int b)
	{
		this.r = r;
		this.g = g;
		this.b = b;
	}


	/**
	 * Create a new RGB color {@link #moveInt(int, int) moving each component} up (percentage &gt; 0) or down
	 * (percentage &lt; 0).
	 *
	 * @param percentage Percentage to {@link #moveInt(int, int) moving each component} up or down. [-99..99]
	 *
	 * @return New lighter or darker color based on the percentage.
	 */
	public RgbColor move(int percentage)
	{
		if (percentage < -99 || percentage > 99) {
			throw new IllegalArgumentException(IAE_INVALID_PERCENTAGE);
		}
		return new RgbColor(moveInt(r, percentage), moveInt(g, percentage), moveInt(b, percentage));
	}


	/**
	 * Moves integers up or down a percentage.
	 * <p>
	 * Examples:<br>
	 * {@code moveInt(100, -50)} returns {@code 50}.<br>
	 * {@code moveInt(155, 50)} returns {@code 205}.
	 * </p>
	 *
	 * @param integer    Integer to be shifted op or down. [0..255]
	 * @param percentage Percentage to shifting the integer up or down. [-99..99]
	 *
	 * @return Moved integer.
	 */
	private int moveInt(int integer, int percentage)
	{
		if (percentage == 0) {
			return integer;
		}
		int diff = percentage < 0 ? integer : 255 - integer;
		return integer + diff * percentage / 100;
	}


	/**
	 * Convert hex string to integer,
	 *
	 * @param hex Hex string.
	 *
	 * @return Integer value.
	 */
	private int hexToInt(String hex)
	{
		return Integer.parseInt(hex, 16);
	}


	/**
	 * Returns color as upper case hex string starting with {@link #HEX_COLOR_PREFIX}.
	 *
	 * @return Color as upper case hex string.
	 */
	@Override
	public String toString()
	{
		return String.format(HEX_COLOR, r, g, b);
	}

}

