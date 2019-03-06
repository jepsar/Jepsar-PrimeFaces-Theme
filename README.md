# Jepsar PrimeFaces Theme

**Note:** PrimeFaces 7 resolved the issues that made me create this project. If you are using PF 7 don't bother to use this.

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
