package org.jepsar.primefaces.theme.jepsar;


import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;


/**
 * Resource handler to detect and return a {@link FontAwesomeResource} if a resource
 * {@link #isPrimeFacesTheme(java.lang.String, java.lang.String) is a PrimeFaces theme}.
 *
 * @author Jasper de Vries <jepsar@gmail.com>
 */
public class FontAwesomeResourceHandler extends AbstractResourceHandler
{

	/**
	 *
	 * @param wrapped Wrapped resource handler.
	 */
	public FontAwesomeResourceHandler(ResourceHandler wrapped)
	{
		super(wrapped);
	}


	/**
	 * Returns a {@link FontAwesomeResource} if the resource
	 * {@link #isPrimeFacesTheme(java.lang.String, java.lang.String) is a PrimeFaces theme} else the
	 * {@link #getWrapped() wrapped handler} will take care of creating a resource.
	 *
	 * @param resourceName Resource name.
	 * @param libraryName  Library name.
	 *
	 * @return {@link FontAwesomeResource} if the resource
	 *         {@link #isPrimeFacesTheme(java.lang.String, java.lang.String) is a PrimeFaces theme} else the
	 *         {@link #getWrapped() wrapped handler} will take care of creating a resource.
	 */
	@Override
	public Resource createResource(String resourceName, String libraryName)
	{
		Resource resource = super.createResource(resourceName, libraryName);
		if (isPrimeFacesTheme(resourceName, libraryName)) {
			return new FontAwesomeResource(resource);
		}
		else {
			return getWrapped().createResource(resourceName, libraryName);
		}
	}

}

