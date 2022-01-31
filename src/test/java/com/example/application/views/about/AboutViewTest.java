package com.example.application.views.about;

import com.example.application.Application;
import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AboutViewTest {
    private static Routes routes = new Routes().autoDiscoverViews(Application.class.getPackageName());
    private AboutView view;

    @Before
    public void setupVaadin() {
        MockVaadin.setup(routes);
        view = new AboutView();

    }

    @Test
    public void plantShown() {
        Assert.assertEquals("images/empty-plant.png", view.image.getSrc());
    }

}
