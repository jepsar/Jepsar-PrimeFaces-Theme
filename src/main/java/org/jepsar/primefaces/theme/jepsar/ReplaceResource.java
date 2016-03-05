package org.jepsar.primefaces.theme.jepsar;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;
import static org.jepsar.primefaces.theme.jepsar.AbstractResource.getInitParameter;


/**
 * Resource which reads the input stream from the wrapped resource and
 * {@link #doReplacements(java.lang.String) replaces} the {@link #findValues} with {@link #replaceValues}.
 * The resulting CSS will be {@link #CACHE cached}. If the cache is not empty for {@link #getURL() }, the cached
 * value will be used.
 *
 * <p>
 * {@link #findValues} can be set using {@link #PARAM_NAME_FIND_VALUES}, but is optional. If not set it will default to
 * {@link #DEFAULT_FIND_VALUES}.
 * </p>
 *
 * <p>
 * {@link #replaceValues} should be set using {@link #PARAM_NAME_REPLACE_VALUES}.
 * </p>
 *
 * <p>
 * If you want to set some variations on the same color you can use {@link #COLOR_MOVE} move indicator. This will
 * execute the {@link RgbColor#move(int) move operation} on the previous color value. See for example
 * {@link #VALUE_PRIMARY_COLOR}
 * </p>
 *
 * @author Jasper de Vries <jepsar@gmail.com>
 * @since 1.0
 */
public class ReplaceResource extends AbstractResource
{

	/**
	 * Separator for {@link #PARAM_NAME_FIND_VALUES} values and {@link #PARAM_NAME_REPLACE_VALUES} values.
	 */
	public static final String SEPARATOR = ";";

	/**
	 * Indicated start of {@link RgbColor#move(int) color move} instruction.
	 */
	public static final String COLOR_MOVE = "=";

	/**
	 * Primary color. Normal, lightest, lighter, darker, darkest.
	 */
	public static final String VALUE_PRIMARY_COLOR = "#086CA2;=67;=33;=-33;=-67";

	/**
	 * Secondary color. Normal, lightest, lighter, darker, darkest.
	 */
	public static final String VALUE_SECONDARY_COLOR = "#B90091;=67;=33;=-33;=-67";

	/**
	 * Complementary color. Normal, lightest, lighter, darker, darkest.
	 */
	public static final String VALUE_COMPLEMENTARY_COLOR = "#FF8B00;=67;=33;=-33;=-67";

	/**
	 * Panel color.
	 */
	public static final String VALUE_PANEL_COLOR = "#F4F4F4";

	/**
	 * Border radius.
	 */
	public static final String VALUE_BORDER_RADIUS = "unset";

	/**
	 * Font import.
	 */
	public static final String VALUE_FONT_IMPORT =
														 "@import url(https://fonts.googleapis.com/css?family=Titillium+Web:400,700,400italic)";

	/**
	 * Font family.
	 */
	public static final String VALUE_FONT_FAMILY = "'Titillium Web',sans-serif";

	/**
	 * Default find values.
	 */
	public static final String DEFAULT_FIND_VALUES = VALUE_PRIMARY_COLOR + SEPARATOR +
																									 VALUE_SECONDARY_COLOR + SEPARATOR +
																									 VALUE_COMPLEMENTARY_COLOR + SEPARATOR +
																									 VALUE_PANEL_COLOR + SEPARATOR +
																									 VALUE_BORDER_RADIUS + SEPARATOR +
																									 VALUE_FONT_IMPORT + SEPARATOR +
																									 VALUE_FONT_FAMILY;

	/**
	 * The context parameter name for values to search for. Setting it is optional, doing so will override
	 * {@link #FIND_VALUES}.
	 */
	public static final String PARAM_NAME_FIND_VALUES = "org.jepsar.primefaces.theme.FIND_VALUES";

	/**
	 * The context parameter name for values to replace with.
	 */
	public static final String PARAM_NAME_REPLACE_VALUES = "org.jepsar.primefaces.theme.REPLACE_VALUES";

	/**
	 * Exception thrown if no replacement values were set.
	 */
	private static final String ISE_NO_REPLACEMENTS = "No replacements where set using context parameter %s";

	/**
	 * Exception thrown if find and replace list differ in size.
	 */
	private static final String ISE_SIZE_DIFFERS = "Find and replace list differ in length for resource %s";

	/**
	 * Exception thrown if a move operation was found and no previous color was set.
	 */
	private static final String ISE_NO_PREVIOUS_COLOR = "No previous color was set";

	/**
	 * Cached CSS after replacements have been made.
	 */
	private static final Map<URL, String> CACHE = new HashMap<>(1);

	/**
	 * Values to search for.
	 */
	private final String findValues;

	/**
	 * Values to replace with.
	 */
	private final String replaceValues;


	/**
	 * Calls super and set {@link #findValues} and {@link #replaceValues}.
	 *
	 * @param wrapped Wrapped resource.
	 * @param handler Handler that created this resource.
	 *
	 * @throws IllegalStateException If no replacement values were set.
	 */
	public ReplaceResource(Resource wrapped, ResourceHandler handler)
	{
		super(wrapped, handler);
		String findValuesParam = getInitParameter(FacesContext.getCurrentInstance(), PARAM_NAME_FIND_VALUES);
		this.findValues = findValuesParam == null ? DEFAULT_FIND_VALUES : findValuesParam;
		this.replaceValues = getInitParameter(FacesContext.getCurrentInstance(), PARAM_NAME_REPLACE_VALUES);
		if (replaceValues == null) {
			throw new IllegalStateException(String.format(ISE_NO_REPLACEMENTS, PARAM_NAME_REPLACE_VALUES));
		}
	}


	/**
	 * Reads the input stream from the wrapped resource and {@link #doReplacements(java.lang.String) replaces} the
	 * {@link #findValues} with {@link #replaceValues}. The resulting CSS will be {@link #CACHE cached}. If the cache
	 * is not empty for {@link #getURL() }, the cached value will be used.
	 *
	 * <p>
	 * If the {@link AbstractResource#PARAM_NAME_APPEND_CSS_RESOURCE} context parameter is set, the contents of that CSS
	 * resource will be appended.
	 * </p>
	 *
	 * @return CSS with replaced values.
	 *
	 * @throws IOException
	 */
	@Override
	public InputStream getInputStream() throws IOException
	{
		String css;
		if (CACHE.containsKey(getURL())) {
			css = CACHE.get(getURL());
		}
		else {
			css = doReplacements(readInputStream(getWrapped().getInputStream()));
			CACHE.put(getURL(), css);
		}
		StringBuilder sb = new StringBuilder(css);

		// Append custom CSS
		appendCss(sb);

		return new ByteArrayInputStream(sb.toString().getBytes(getCharset()));
	}


	/**
	 * {@link #valuesToList(java.lang.String) Creates a list} for {@link #findValues} and {@link #replaceValues} and
	 * replaces each entry in the find list with the entry at the same position in the replace list (if the lists are of
	 * equal size.
	 *
	 * @param css Style sheet perform the replacements on.
	 *
	 * @return CSS with replaced values.
	 *
	 * @throws IllegalStateException If find and replace list differ in size.
	 */
	private String doReplacements(String css)
	{
		List<String> findList = valuesToList(findValues);
		List<String> replaceList = valuesToList(replaceValues);
		if (findList.size() != replaceList.size()) {
			throw new RuntimeException(String.format(ISE_SIZE_DIFFERS, getURL()));
		}
		// Avoid overwriting, do a two step replacement
		for (int i = 0; i < findList.size(); i++) {
			css = css.replace(findList.get(i), SEPARATOR + i + SEPARATOR);
		}
		for (int i = 0; i < findList.size(); i++) {
			css = css.replace(SEPARATOR + i + SEPARATOR, replaceList.get(i));
		}
		return css;
	}


	/**
	 * Split the string on {@link #SEPARATOR} and pas the list on to {@link #handleRelativeColors(java.util.List) }.
	 *
	 * @param values String of values separated by {@link #SEPARATOR}.
	 *
	 * @return Value list with absolute colors.
	 */
	private List<String> valuesToList(String values)
	{
		return handleRelativeColors(Arrays.asList(values.split(SEPARATOR)));
	}


	/**
	 * If the list contains values starting with the {@link #COLOR_MOVE} indicator and a previous color was found,
	 * substitute the value with the color resulting from the {@link RgbColor#move(int) move operation}.
	 *
	 * @param values List of values.
	 *
	 * @return List with absolute colors.
	 *
	 * @throws IllegalStateException If a move operation was found and no previous color was set.
	 */
	private List<String> handleRelativeColors(List<String> values)
	{
		RgbColor previousColor = null;
		for (int i = 0; i < values.size(); i++) {
			try {
				RgbColor color = new RgbColor(values.get(i));
				previousColor = color;
			}
			catch (IllegalArgumentException ex) {
			}
			if (values.get(i).startsWith(COLOR_MOVE)) {
				if (previousColor == null) {
					throw new IllegalStateException(ISE_NO_PREVIOUS_COLOR);
				}
				int move = Integer.valueOf(values.get(i).substring(1));
				values.set(i, previousColor.move(move).toString());
			}
		}
		return values;
	}

}

