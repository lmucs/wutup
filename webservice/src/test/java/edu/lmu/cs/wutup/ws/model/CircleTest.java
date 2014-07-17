package edu.lmu.cs.wutup.ws.model;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;

public class CircleTest {

    @Test
    public void constructorSetsFieldsProperly() {
        Circle c = new Circle(30, -100, 25);
        assertThat(c.centerLatitude, equalTo(30.0));
        assertThat(c.centerLongitude, equalTo(-100.0));
        assertThat(c.radius, equalTo(25.0));
    }

    @Test
    public void constructorWorksAtBounds() {
        new Circle(-90, -180, 0);
        new Circle(-90, 180, 100);
        new Circle(90, -180, 0);
        new Circle(90, 180, 100);
    }

    @Test
    public void latitudeTooLowIsChecked() {
        boundsCheck(-90.015625, 0, 5, "Latitude out of range: -90.015625");
    }

    @Test
    public void latitudeTooHighIsChecked() {
        boundsCheck(90.015625, 0, 5, "Latitude out of range: 90.015625");
    }

    @Test
    public void longitudeTooLowIsChecked() {
        boundsCheck(0, -180.015625, 5, "Longitude out of range: -180.015625");
    }

    @Test
    public void longitudeTooHighIsChecked() {
        boundsCheck(0, 180.015625, 5, "Longitude out of range: 180.015625");
    }

    @Test
    public void radiusTooLowIsChecked() {
        boundsCheck(0, 0, -0.015625, "Radius out of range: -0.015625");
    }

    @Test
    public void radiusTooHighIsChecked() {
        boundsCheck(0, 0, 100.015625, "Radius out of range: 100.015625");
    }

    @Test
    public void equalsUsesAllFields() {
        assertThat(new Circle(1, 1, 10), equalTo(new Circle(1, 1, 10)));
        assertThat(new Circle(1, 1, 10), not(equalTo(new Circle(1.001, 1, 10))));
        assertThat(new Circle(1, 1, 10), not(equalTo(new Circle(1, 1.001, 10))));
        assertThat(new Circle(1, 1, 10), not(equalTo(new Circle(1, 1, 10.001))));
    }

    @Test
    public void hashCodeMakesSense() {
        Circle c1 = new Circle(10, 15, 10);
        Circle c2 = new Circle(10, 15, 10);
        Circle c3 = new Circle(10.000001, 15, 10);
        assertThat(c1.hashCode(), equalTo(c2.hashCode()));
        assertThat(c1.hashCode(), not(equalTo(c3.hashCode())));
    }

    @Test
    public void toStringIncludesAllFields() {
        String result = new Circle(7, 9, 20).toString();
        String expected = "Circle(centerLatitude=7.0, centerLongitude=9.0, radius=20.0)";
        assertThat(result, equalTo(expected));
    }

    private void boundsCheck(double lat, double lon, double radius, String expectedMessage) {
        try {
            new Circle(lat, lon, radius);
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo(expectedMessage));
        }
    }
}
