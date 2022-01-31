package com.example.application.views;

import com.example.application.Application;
import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.github.mvysny.kaributesting.v10.spring.MockSpringServlet;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.spring.SpringServlet;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import kotlin.jvm.functions.Function0;

@SpringBootTest
public class AbstractViewTest {

    @Autowired
    private ApplicationContext ctx;

    protected static Routes routes;

    @BeforeAll
    public static void staticSetup() {
        routes = new Routes().autoDiscoverViews(Application.class.getPackageName());
    }

    @BeforeEach
    public void setupMocks() {
        final Function0<UI> uiFactory = UI::new;
        final SpringServlet servlet = new MockSpringServlet(routes, ctx, uiFactory);
        MockVaadin.setup(uiFactory, servlet);

    }

}
