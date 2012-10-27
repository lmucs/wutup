package edu.lmu.cs.wutup.ws.model;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.hamcrest.CoreMatchers.equalTo;

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

    private void boundsCheck(double lat, double lon, double radius, String expectedMessage) {
        try {
            new Circle(lat, lon, radius);
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo(expectedMessage));
        }
    }
}
