package com.example.application.views.masterdetail;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import com.example.application.data.entity.SamplePerson;
import com.example.application.views.AbstractViewTest;
import com.github.mvysny.kaributesting.v10.GridKt;
import com.github.mvysny.kaributesting.v10.LocatorJ;
import com.github.mvysny.kaributesting.v10.NotificationsKt;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.QuerySortOrder;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MasterDetailViewTest extends AbstractViewTest {

    private MasterDetailView view;
    private Grid<SamplePerson> grid;

    @BeforeEach
    public void setupVaadin() {
        UI.getCurrent().navigate(MasterDetailView.class);
        refreshViewReferences();
        grid.getDataCommunicator().setPagingEnabled(false);
        // Data needs to be handled properly for the tests to be reliable.
        // There are two main approaches for this:
        // 1 - Initialize data before each test
        // 2 - Ensure each test is independent of the current data
        // Here we have chosen option 2 because the data handling is quite simple.
        // For cases where you need specific data in each test, you would use the other
        // approach.

    }

    private void refreshViewReferences() {
        MasterDetailView view = LocatorJ._get(MasterDetailView.class);
        this.view = view;
        this.grid = view.grid;
    }

    @Test
    public void gridContainsExpectedData() {
        assertRow(0, "Eula", "Lane", "eula.lane@jigrormo.ye");

        SamplePerson tenthPerson = GridKt._get(grid, 9);
        Assert.assertEquals("Mabel", tenthPerson.getFirstName());
        Assert.assertEquals("Leach", tenthPerson.getLastName());
        Assert.assertEquals("mabel.leach@lisohuje.vi", tenthPerson.getEmail());
    }

    @Test
    public void gridSortingWorks() {
        List<SamplePerson> data = GridKt._findAll(grid);

        data.sort((a, b) -> a.getLastName().compareTo(b.getLastName()));
        SamplePerson firstAccordingToLastName = data.get(0);
        SamplePerson secondAccordingToLastName = data.get(1);

        GridKt.sort(grid, QuerySortOrder.asc("lastName").build().toArray(QuerySortOrder[]::new));
        assertRow(0, firstAccordingToLastName.getFirstName(), firstAccordingToLastName.getLastName(),
                firstAccordingToLastName.getEmail());
        assertRow(1, secondAccordingToLastName.getFirstName(), secondAccordingToLastName.getLastName(),
                secondAccordingToLastName.getEmail());

        data.sort((a, b) -> a.getOccupation().compareTo(b.getOccupation()));
        SamplePerson firstAccordingToOccupation = data.get(0);
        SamplePerson secondAccordingToOccupation = data.get(1);

        GridKt.sort(grid, QuerySortOrder.asc("occupation").build().toArray(QuerySortOrder[]::new));
        assertRow(0, firstAccordingToOccupation.getFirstName(), firstAccordingToOccupation.getLastName(),
                firstAccordingToOccupation.getEmail());
        assertRow(1, secondAccordingToOccupation.getFirstName(), secondAccordingToOccupation.getLastName(),
                secondAccordingToOccupation.getEmail());

        data.sort((a, b) -> Boolean.compare(a.isImportant(), b.isImportant()));

        GridKt.sort(grid, QuerySortOrder.desc("important").build().toArray(QuerySortOrder[]::new));
        assertRow(0, "Bobby", "Pearson", "bobby.pearson@ib.kg");
    }

    @Test
    public void clickingInGridShowsPerson() {
        SamplePerson row5Person = GridKt._get(grid, 5);
        String expectedFirst = row5Person.getFirstName();
        String expectedLast = row5Person.getLastName();
        String expectedEmail = row5Person.getEmail();
        String expectedPhone = row5Person.getPhone();
        LocalDate expectedDate = row5Person.getDateOfBirth();
        boolean expectedImportant = row5Person.isImportant();
        selectInGrid(5);

        Assert.assertEquals(Collections.singleton(row5Person), grid.getSelectedItems());

        Assert.assertEquals(expectedFirst, view.firstName.getValue());
        Assert.assertEquals(expectedLast, view.lastName.getValue());
        Assert.assertEquals(expectedEmail, view.email.getValue());
        Assert.assertEquals(expectedPhone, view.phone.getValue());
        Assert.assertEquals(expectedDate, view.dateOfBirth.getValue());
        Assert.assertEquals(expectedImportant, view.important.getValue());
    }

    private void selectInGrid(int row) {
        GridKt._clickItem(grid, row);
        refreshViewReferences(); // Clicking in the grid navigates so we need to check the new view instance
    }

    @Test
    public void formSaveUpdatesGrid() {
        selectInGrid(7);
        view.firstName.setValue("New First");
        view.lastName.setValue("New Last");
        view.email.setValue("New email");
        view.phone.setValue("New email");
        view.dateOfBirth.setValue(LocalDate.of(2020, 1, 2));
        view.important.setValue(true);
        LocatorJ._click(view.save);

        NotificationsKt.expectNotifications("SamplePerson details stored.");
        assertFormClear();
        assertRow(7, "New First", "New Last", "New email");
    }

    @Test
    public void formCancelRetainsGrid() {
        assertRow(7, "Gene", "Goodman", "gene.goodman@kem.tl");

        selectInGrid(7);
        view.firstName.setValue("New First");
        view.lastName.setValue("New Last");
        view.email.setValue("New email");
        view.phone.setValue("New phone");
        view.dateOfBirth.setValue(LocalDate.of(2020, 1, 2));
        view.important.setValue(true);
        LocatorJ._click(view.cancel);

        assertFormClear();
        assertRow(7, "Gene", "Goodman", "gene.goodman@kem.tl");
    }

    @Test
    public void validationErrorPreventsSave() {
        assertRow(12, "Rose", "Gray", "rose.gray@kagu.hr");

        selectInGrid(12);
        view.firstName.setValue("New First");
        view.lastName.setValue("");
        view.email.setValue("New email");
        view.phone.setValue("New phone");
        view.dateOfBirth.setValue(LocalDate.of(2020, 1, 2));
        view.important.setValue(true);
        LocatorJ._click(view.save);

        Assert.assertEquals("New First", view.firstName.getValue());
        Assert.assertEquals("", view.lastName.getValue());
        Assert.assertEquals("New email", view.email.getValue());
        Assert.assertEquals("New phone", view.phone.getValue());
        Assert.assertEquals(LocalDate.of(2020, 1, 2), view.dateOfBirth.getValue());
        Assert.assertEquals(true, view.important.getValue());
        assertRow(12, "Rose", "Gray", "rose.gray@kagu.hr");
    }

    @Test
    public void addUpdatesGrid() {
        Assert.assertEquals(100, GridKt._size(grid));
        view.firstName.setValue("New First");
        view.lastName.setValue("New Last");
        view.email.setValue("New email");
        view.phone.setValue("New email");
        view.dateOfBirth.setValue(LocalDate.of(2020, 1, 2));
        view.important.setValue(true);
        LocatorJ._click(view.save);

        NotificationsKt.expectNotifications("SamplePerson details stored.");
        assertFormClear();
        assertRow(100, "New First", "New Last", "New email");
    }

    private void assertFormClear() {
        Assert.assertTrue(view.firstName.isEmpty());
        Assert.assertTrue(view.lastName.isEmpty());
        Assert.assertTrue(view.email.isEmpty());
        Assert.assertTrue(view.phone.isEmpty());
        Assert.assertTrue(view.dateOfBirth.isEmpty());
        Assert.assertTrue(view.important.isEmpty());
    }

    private void assertRow(int rowIndex, String firstName, String lastName, String email) {
        SamplePerson person = GridKt._get(grid, rowIndex);
        Assert.assertEquals(firstName, person.getFirstName());
        Assert.assertEquals(lastName, person.getLastName());
        Assert.assertEquals(email, person.getEmail());
    }

}
