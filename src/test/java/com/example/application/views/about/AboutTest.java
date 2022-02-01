package com.example.application.views.about;

import com.example.application.views.AbstractTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AboutTest extends AbstractTest {
    private AboutView view;

    @Before
    public void setup() {
        view = new AboutView();
    }

    @Test
    public void plantShown() {
        Assert.assertEquals("images/empty-plant.png", view.image.getSrc());
    }

}
