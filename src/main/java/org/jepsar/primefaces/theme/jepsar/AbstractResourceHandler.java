package org.jepsar.primefaces.theme.jepsar;


import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;


/**
 * Abstract resource handler. Handles wrapping.
 *
 * @author Jasper de Vries <jepsar@gmail.com>
 */
public abstract class AbstractResourceHandler extends ResourceHandlerWrapper
{

	/**
	 * PrimeFaces theme name.
	 */
	protected static final String PRIMEFACES_THEME = "theme.css";

	/**
	 * PrimeFaces library name prefix.
	 */
	protected static final String PRIMEFACES_LIBRARY_PREFIX = "primefaces-";

	/**
	 * Wrapped resource handler.
	 */
	private final ResourceHandler wrapped;


	/**
	 * Wraps the resource handler.
	 *
	 * @param wrapped Wrapped resource handler.
	 */
	public AbstractResourceHandler(ResourceHandler wrapped)
	{
		this.wrapped = wrapped;
	}


	/**
	 * Returns {@code true} if the resource is a PrimeFaces theme. The {@code resourceName} should equal
	 * {@link #PRIMEFACES_THEME} and the {@code libraryName} should start with {@link #PRIMEFACES_LIBRARY_PREFIX}.
	 *
	 * @param resourceName Resource name.
	 * @param libraryName  Library name.
	 *
	 * @return {@code true} if the resource is a PrimeFaces theme.
	 */
	protected boolean isPrimeFacesTheme(final String resourceName, final String libraryName)
	{
		return libraryName != null && libraryName.startsWith(PRIMEFACES_LIBRARY_PREFIX) &&
					 PRIMEFACES_THEME.equals(resourceName);
	}


	/**
	 * Returns the {@link #wrapped wrapped resource handler}.
	 *
	 * @return {@link #wrapped Wrapped resource handler}.
	 */
	@Override
	public ResourceHandler getWrapped()
	{
		return wrapped;
	}

}

