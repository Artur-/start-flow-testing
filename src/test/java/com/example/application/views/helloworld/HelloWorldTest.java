package com.example.application.views.helloworld;

import com.example.application.views.AbstractTest;
import com.github.mvysny.kaributesting.v10.NotificationsKt;

import org.junit.Before;
import org.junit.Test;

public class HelloWorldTest extends AbstractTest {
    private HelloWorldView view;

    @Before
    public void setup() {
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
