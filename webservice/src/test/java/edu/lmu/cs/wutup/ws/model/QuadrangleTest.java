package edu.lmu.cs.wutup.ws.model;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class QuadrangleTest {

    @Test
    public void constructorArgumentsAreSwappedIfNecessary() {
        Quadrangle q = new Quadrangle(10, 3, 4, -8);
        assertThat(q.getMinLatitude(), is(4.0));
        assertThat(q.getMaxLatitude(), is(10.0));
        assertThat(q.getMinLongitude(), is(-8.0));
        assertThat(q.getMaxLongitude(), is(3.0));
    }

    @Test
    public void constructorArgumentsAreNotSwappedWhenOkay() {
        Quadrangle q = new Quadrangle(4, -8, 3, 10);
        assertThat(q.getMinLatitude(), is(3.0));
        assertThat(q.getMaxLatitude(), is(4.0));
        assertThat(q.getMinLongitude(), is(-8.0));
        assertThat(q.getMaxLongitude(), is(10.0));
    }

    @Test
    public void canReadQuadrangleFromDescriptor() {
        Quadrangle q = Quadrangle.fromString("34,-92,80,-104");
        assertThat(q.getMinLatitude(), is(34.0));
        assertThat(q.getMaxLatitude(), is(80.0));
        assertThat(q.getMinLongitude(), is(-104.0));
        assertThat(q.getMaxLongitude(), is(-92.0));
    }

    @Test
    public void quadranglesAreConvertedToStringsCorrectly() {
        assertThat(new Quadrangle(3.5, -9.125, -11.25, 89.75).toString(), is("<11.25S,9.125W;3.5N,89.75E>"));
        assertThat(new Quadrangle(0, 0, 0, 1).toString(), is("<0.0N,0.0E;0.0N,1.0E>"));
    }

    @Test(expected=IllegalArgumentException.class)
    public void firstLatitudeTooLowIsChecked() {
        new Quadrangle(-90.0000003, 0, 0, 0);
    }

    @Test(expected=IllegalArgumentException.class)
    public void secondLatitudeTooLowIsChecked() {
        new Quadrangle(0, 0, -90.0000003, 0);
    }

    @Test(expected=IllegalArgumentException.class)
    public void firstLatitudeTooHighIsChecked() {
        new Quadrangle(90.0000003, 0, 0, 0);
    }

    @Test(expected=IllegalArgumentException.class)
    public void secondLatitudeTooHighIsChecked() {
        new Quadrangle(0, 0, 90.0000003, 0);
    }

    @Test(expected=IllegalArgumentException.class)
    public void firstLongitudeTooLowIsChecked() {
        new Quadrangle(0, -180.0000003, 0, 0);
    }

    @Test(expected=IllegalArgumentException.class)
    public void secondLongitudeTooLowIsChecked() {
        new Quadrangle(0, 0, 0, -180.0000003);
    }

    @Test(expected=IllegalArgumentException.class)
    public void firstLongitudeTooHighIsChecked() {
        new Quadrangle(0, 180.0000003, 0, 0);
    }

    @Test(expected=IllegalArgumentException.class)
    public void secondLongitudeTooHighIsChecked() {
        new Quadrangle(0, 0, 0, 180.0000003);
    }
}
