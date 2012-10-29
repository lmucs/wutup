package edu.lmu.cs.wutup;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class PageWrapper extends WebPage {
	private static final long serialVersionUID = 1L;

    public PageWrapper(final PageParameters parameters) {
	super(parameters);

	//add(new Label("version", getApplication().getFrameworkSettings().getVersion()));

        // TODO Add your page's components here

    }
}
