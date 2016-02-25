package org.jepsar.primefaces.theme.jepsar;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import javax.faces.application.Resource;
import javax.faces.application.ResourceWrapper;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;


/**
 * Abstract resource. Handles wrapping and reading data.
 *
 * @author Jasper de Vries <jepsar@gmail.com>
 */
public abstract class AbstractResource extends ResourceWrapper
{

	/**
	 * The context parameter name for getting the CSS file to append.
	 */
	public static final String PARAM_NAME_APPEND_CSS_FILE = "org.jepsar.primefaces.theme.APPEND_CSS_FILE";

	/**
	 * Input stream reader buffer size.
	 */
	private static final int BUFFER_SIZE = 1024;

	/**
	 * Wrapped resource.
	 */
	private final Resource wrapped;

	/**
	 * Response encoding charset.
	 */
	private final Charset charset;

	/**
	 * Location of the CSS file to append.
	 */
	private final String appendCssFile;


	/**
	 * Wraps the resource and sets {@link #charset} and {@link #appendCssFile}.
	 *
	 * @param wrapped Wrapped resource.
	 */
	public AbstractResource(Resource wrapped)
	{
		this.wrapped = wrapped;
		String charEncoding = FacesContext.getCurrentInstance().getExternalContext().getResponseCharacterEncoding();
		this.charset = Charset.forName(charEncoding);
		this.appendCssFile = getInitParameter(FacesContext.getCurrentInstance(), PARAM_NAME_APPEND_CSS_FILE);
	}


	/**
	 * Reads data from input stream and returns it as a string using {@link #charset}.
	 *
	 * @param inputStream Input stream to read from.
	 *
	 * @return Data read from input stream.
	 *
	 * @throws IOException
	 */
	protected String readInputStream(final InputStream inputStream) throws IOException
	{
		return readInputStream(inputStream, charset);
	}


	/**
	 * Reads data from input stream and returns it as a string.
	 *
	 * @param inputStream Input stream to read from.
	 * @param charset     Charset used to read the input stream.
	 *
	 * @return Data read from input stream.
	 *
	 * @throws IOException
	 */
	protected String readInputStream(final InputStream inputStream, final Charset charset) throws IOException
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
	 * Reads data from resource and returns it as a string using {@link StandardCharsets#UTF_8}.
	 *
	 * @param resourceName Resource name used in {@link Class#getResource(java.lang.String) }.
	 *
	 * @return Data read from resource name.
	 *
	 * @throws IOException
	 */
	protected String readClassResource(final String resourceName) throws IOException
	{
		InputStream inputStream = getClass().getResourceAsStream(resourceName);
		return readInputStream(inputStream, StandardCharsets.UTF_8);
	}


	/**
	 * Reads the data from a file in the webapp folder using {@link StandardCharsets#UTF_8}.
	 *
	 * @param fileLocation
	 *
	 * @return Data read from file at the given location.
	 *
	 * @throws IOException
	 */
	protected String readWebappFile(final String fileLocation) throws IOException
	{
		ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		String root = context.getRealPath("/");
		File webappFile = new File(root + fileLocation);
		return new String(Files.readAllBytes(webappFile.toPath()), StandardCharsets.UTF_8);
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
	 * Returns {@link #charset}.
	 *
	 * @return {@link #charset}.
	 */
	public Charset getCharset()
	{
		return charset;
	}


	/**
	 * Returns {@link #appendCssFile}.
	 *
	 * @return {@link #appendCssFile}.
	 */
	public String getAppendCssFile()
	{
		return appendCssFile;
	}

}

