package edu.lmu.cs.wutup;

import org.apache.wicket.core.request.mapper.PackageMapper;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.mapper.mount.MountMapper;
import org.apache.wicket.util.lang.PackageName;

/**
 * Application object for your web application. If you want to run this application without deploying, run the Start class.
 * 
 * @see edu.lmu.cs.wutup.Start#main(String[])
 */
public class WicketApplication extends WebApplication
{    	
	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends WebPage> getHomePage()
	{
		return Index.class;
	}

	/**
	 * @see org.apache.wicket.Application#init()
	 */
	@Override
	public void init()
	{
		super.init();

		//Configures URLs to be clean.
		getRootRequestMapperAsCompound().add(
	            new MountMapper("/mount/point", new PackageMapper(
	                PackageName.forClass(Index.class))));
	        mountPackage("/", Index.class);
	}
}
