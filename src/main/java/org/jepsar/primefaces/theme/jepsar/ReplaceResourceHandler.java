package org.jepsar.primefaces.theme.jepsar;


import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;


/**
 * Resource handler to detect and return a {@link ReplaceResource} if a resource
 * {@link #isPrimeFacesTheme(java.lang.String, java.lang.String) is a PrimeFaces theme}.
 *
 * @author Jasper de Vries <jepsar@gmail.com>
 * @since 1.0
 */
public class ReplaceResourceHandler extends AbstractResourceHandler
{

	/**
	 *
	 * @param wrapped Wrapped resource handler.
	 */
	public ReplaceResourceHandler(ResourceHandler wrapped)
	{
		super(wrapped);
	}


	/**
	 * Returns a {@link ReplaceResource} if the resource
	 * {@link #isPrimeFacesTheme(java.lang.String, java.lang.String) is a PrimeFaces theme} else the
	 * {@link #getWrapped() wrapped handler} will take care of creating a resource.
	 *
	 * @param resourceName Resource name.
	 * @param libraryName  Library name.
	 *
	 * @return {@link ReplaceResource} if the resource
	 *         {@link #isPrimeFacesTheme(java.lang.String, java.lang.String) is a PrimeFaces theme} else the
	 *         {@link #getWrapped() wrapped handler} will take care of creating a resource.
	 */
	@Override
	public Resource createResource(String resourceName, String libraryName)
	{
		if (isPrimeFacesTheme(resourceName, libraryName)) {
			return new ReplaceResource(super.createResource(resourceName, libraryName), this);
		}
		else {
			return getWrapped().createResource(resourceName, libraryName);
		}
	}


}

