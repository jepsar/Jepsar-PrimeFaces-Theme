package org.jepsar.primefaces.theme.jepsar;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.faces.application.Resource;


/**
 * Resource which reads the input stream from the wrapped resource, removes {@link #REGEX_BG_IMG background images} and
 * {@link #REGEX_ICON UI icons} and appends the patch loaded from {@link #FONT_AWESOME_CSS_RESOURCE}.
 *
 * @author Jasper de Vries <jepsar@gmail.com>
 */
public class FontAwesomeResource extends AbstractResource
{

	/**
	 * Resource location of the Font Awesome CSS patch.
	 */
	private static final String FONT_AWESOME_CSS_RESOURCE = "fontawesome.css";

	/**
	 * Expression for icon background image.
	 */
	private static final String REGEX_BG_IMG =
															"background-image:url\\(\"[^\"]*/ui-icons_[0-9a-f]{6}_[0-9]+x[0-9]+\\.png[^\"]+\"\\);";

	/**
	 * Expression for icon position.
	 */
	private static final String REGEX_ICON = ".ui-icon-[^\\{]+\\{background-position:[^;]+;\\}";


	/**
	 *
	 * @param wrapped Wrapped resource.
	 */
	public FontAwesomeResource(Resource wrapped)
	{
		super(wrapped);
	}


	/**
	 * Reads the input stream from the wrapped resource, removes {@link #REGEX_BG_IMG background images} and
	 * {@link #REGEX_ICON UI icons} and appends the patch loaded from {@link #FONT_AWESOME_CSS_RESOURCE}. If the
	 * {@link AbstractResource#PARAM_NAME_APPEND_CSS_FILE} context parameter is set, the contents of that CSS file
	 * will be appended.
	 *
	 * @return Cleaned and patched CSS.
	 *
	 * @throws IOException
	 */
	@Override
	public InputStream getInputStream() throws IOException
	{
		String css = readInputStream(getWrapped().getInputStream());
		css = css.replaceAll(REGEX_BG_IMG, "");
		css = css.replaceAll(REGEX_ICON, "");
		css += readClassResource(FONT_AWESOME_CSS_RESOURCE);
		if (getAppendCssFile() != null) {
			css += readWebappFile(getAppendCssFile());
		}
		return new ByteArrayInputStream(css.getBytes(getCharset()));
	}

}

