# Jepsar PrimeFaces Theme

Having worked with [PrimeFaces](http://primefaces.org/) for a while I'm increasingly annoiyed by it's community themes:

* They look outdated
* The CSS is overly complicated
* The icons are bitmaps

So I decided to create my own free PrimeFaces theme. While doing so I also came up with a resource handler which you
can use to patch existing themes to replace the [jQuery UI](https://jqueryui.com/) icons with
[Font Awesome](https://fortawesome.github.io/Font-Awesome/) icons.

## Usage

Make sure you've set the FontAwesome context parameter in the `web.xml`:

````xml
<context-param>
	<param-name>primefaces.FONT_AWESOME</param-name>
	<param-value>true</param-value>
</context-param>
````

Add this dependency to your `pom.xml`:

````xml
<dependency>
	<groupId>com.github.jepsar</groupId>
	<artifactId>primefaces-theme-jepsar</artifactId>
	<version>0.9.1</version>
</dependency>
````

## `FontAwesomeResourceHandler`

This resource handler will strip the jQuery UI icons from the community PrimeFaces themes and adds FontAwesome rules to
the theme. You can use it on existing applications without needing to convert all XHTML (for example `ui-icon-gear` to
`fa fa-cog`). The injected CSS will take care of that.

Then, in the `faces-config.xml`, add the handler:

````xml
<application>
	<resource-handler>org.jepsar.primefaces.theme.jepsar.FontAwesomeResourceHandler</resource-handler>
</application>
````

You're done. But wait, maybe you need some custom styling? You can append a custom CSS resource by adding a context
parameter in the `web.xml`:

````xml
<context-param>
	<param-name>org.jepsar.primefaces.theme.APPEND_CSS_RESOURCE</param-name>
	<param-value>custom.css</param-value>
</context-param>
````

## `NoThemeResourceHandler`

If you want to replace the PrimeFaces theme with your own CSS, but don't want to build a theme JAR you can use this
resource handler in combination with the custom CSS resource context parameter. I've found this handler to be useful
when developing a theme.

Add the handler in the `faces-config.xml`:

````xml
<application>
	<resource-handler>org.jepsar.primefaces.theme.jepsar.NoThemeResourceHandler</resource-handler>
</application>
````

And the context parameter in the `web.xml`:

````xml
<context-param>
	<param-name>org.jepsar.primefaces.theme.APPEND_CSS_RESOURCE</param-name>
	<param-value>custom.css</param-value>
</context-param>
````

## Theme

The theme is still work in progress. At this moment it is not ready to share yet, but it will come soon.

## Icon mapping only

If you only want to use the jQuery UI to FontAwesome icon mapping:

````xhtml
<h:outputStylesheet name="/primefaces-jepsar/iconmapping.css"/>
````
