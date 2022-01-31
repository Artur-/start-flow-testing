package com.example.application.views.helloworld;

import com.example.application.Application;
import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.NotificationsKt;
import com.github.mvysny.kaributesting.v10.Routes;

import org.junit.Before;
import org.junit.Test;

public class HelloWorldViewTest {
    private static Routes routes = new Routes().autoDiscoverViews(Application.class.getPackageName());
    private HelloWorldView view;

    @Before
    public void setupVaadin() {
        MockVaadin.setup(routes);
        view = new HelloWorldView();
    }

    @Test
    public void helloWithName() {
        view.name.setValue("Me");
        view.sayHello.click();
        NotificationsKt.expectNotifications("Hello Me");
    }

    @Test
    public void emptyHello() {
        view.sayHello.click();
        NotificationsKt.expectNotifications("Hello ");
    }

}
