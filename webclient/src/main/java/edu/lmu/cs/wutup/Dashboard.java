package edu.lmu.cs.wutup;

import org.apache.wicket.request.mapper.parameter.PageParameters;

public class Dashboard extends PageWrapper {
	private static final long serialVersionUID = 1L;

    public Dashboard(final PageParameters parameters) {
	super(parameters);

	//add(new Label("version", getApplication().getFrameworkSettings().getVersion()));

        // TODO Add your page's components here

    }
}
