package org.jepsar.primefaces.theme.jepsar;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;


/**
 * Resource which creates empty CSS. If the
 * {@link AbstractResource#PARAM_NAME_APPEND_CSS_RESOURCE} context parameter is set, the contents of that CSS resource
 * will be appended.
 *
 * @author Jasper de Vries <jepsar@gmail.com>
 */
public class NoThemeResource extends AbstractResource
{


	/**
	 * Calls super.
	 *
	 * @param wrapped Wrapped resource.
	 * @param handler Handler that created this resource.
	 */
	public NoThemeResource(Resource wrapped, ResourceHandler handler)
	{
		super(wrapped, handler);
	}


	/**
	 * Creates empty CSS. If the
	 * {@link AbstractResource#PARAM_NAME_APPEND_CSS_RESOURCE} context parameter is set, the contents of that CSS resource
	 * will be appended.
	 *
	 * @return Cleaned and patched CSS.
	 *
	 * @throws IOException
	 */
	@Override
	public InputStream getInputStream() throws IOException
	{
		StringBuilder sb = new StringBuilder("");
		appendCss(sb);
		return new ByteArrayInputStream(sb.toString().getBytes(getCharset()));
	}

}

