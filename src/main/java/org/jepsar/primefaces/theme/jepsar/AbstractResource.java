package org.jepsar.primefaces.theme.jepsar;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceWrapper;
import javax.faces.context.FacesContext;


/**
 * Abstract resource. Handles wrapping and reading data.
 *
 * @author Jasper de Vries <jepsar@gmail.com>
 */
public abstract class AbstractResource extends ResourceWrapper
{

	/**
	 * The context parameter name for getting the CSS resource to append.
	 */
	public static final String PARAM_NAME_APPEND_CSS_RESOURCE = "org.jepsar.primefaces.theme.APPEND_CSS_RESOURCE";

	/**
	 * Input stream reader buffer size.
	 */
	private static final int BUFFER_SIZE = 1024;

	/**
	 * Wrapped resource.
	 */
	private final Resource wrapped;

	/**
	 * Handler that created this resource.
	 */
	private final ResourceHandler handler;

	/**
	 * Response encoding charset.
	 */
	private final Charset charset;

	/**
	 * Name of the CSS resource to append.
	 */
	private final String appendCssResource;


	/**
	 * Wraps the resource and sets {@link #charset} and {@link #appendCssResource}.
	 *
	 * @param wrapped Wrapped resource.
	 * @param handler Handler that created this resource.
	 */
	public AbstractResource(Resource wrapped, ResourceHandler handler)
	{
		this.wrapped = wrapped;
		this.handler = handler;
		String charEncoding = FacesContext.getCurrentInstance().getExternalContext().getResponseCharacterEncoding();
		this.charset = Charset.forName(charEncoding);
		this.appendCssResource = getInitParameter(FacesContext.getCurrentInstance(), PARAM_NAME_APPEND_CSS_RESOURCE);
	}


	/**
	 * If the {@link #PARAM_NAME_APPEND_CSS_RESOURCE} context parameter is set, the contents of that CSS resource
	 * will be appended to the {@code StringBuilder}.
	 *
	 * @param sb
	 *
	 * @return Given {@code StringBuilder}
	 *
	 * @throws IOException
	 */
	protected StringBuilder appendCss(StringBuilder sb) throws IOException
	{
		if (appendCssResource != null) {
			Resource resource = handler.createResource(appendCssResource);
			sb.append(readInputStream(resource.getInputStream()));
		}
		return sb;
	}


	/**
	 * Reads data from input stream and returns it as a string.
	 *
	 * @param inputStream Input stream to read from.
	 *
	 * @return Data read from input stream.
	 *
	 * @throws IOException
	 */
	protected String readInputStream(final InputStream inputStream) throws IOException
	{
		final char[] buffer = new char[BUFFER_SIZE];
		final StringBuilder sb = new StringBuilder(BUFFER_SIZE);
		try (Reader in = new InputStreamReader(inputStream, charset)) {
			int read;
			while ((read = in.read(buffer, 0, buffer.length)) > -1) {
				sb.append(buffer, 0, read);
			}
		}
		return sb.toString();
	}


	/**
	 * Return the value of the specified application initialization parameter (if any).
	 *
	 * @param context Faces context
	 * @param name    Name of the requested initialization parameter
	 *
	 * @return Value of the specified application initialization parameter (if any).
	 */
	static String getInitParameter(FacesContext context, String name)
	{
		return context.getExternalContext().getInitParameter(name);
	}


	/**
	 * Returns the {@link #wrapped wrapped resource}.
	 *
	 * @return {@link #wrapped Wrapped resource}.
	 */
	@Override
	public Resource getWrapped()
	{
		return wrapped;
	}


	/**
	 * Return the {@link #handler handler that created this resource}.
	 *
	 * @return {@link #handler}.
	 */
	public ResourceHandler getHandler()
	{
		return handler;
	}


	/**
	 * Returns {@link #charset}.
	 *
	 * @return {@link #charset}.
	 */
	public Charset getCharset()
	{
		return charset;
	}

}

