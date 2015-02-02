package org.richfaces.arquillian.browser;

import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.drone.spi.DronePoint;
import org.jboss.arquillian.drone.spi.event.AfterDroneEnhanced;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

/**
 * <p>
 * Prepares PhantomJS browser to run effectively with fundamental tests
 * </p>
 *
 * <ul>
 * <li>resizes the browser window to 1280x1024</li>
 * </ul>
 *
 * @author Lukas Fryc
 */
public class PrepareBrowserSession {

    public void prepare(@Observes AfterDroneEnhanced event) {

        DronePoint<?> dronePoint = event.getDronePoint();

        if (!dronePoint.conformsTo(WebDriver.class)) {
            // This Drone is not instance of WebDriver, we will not resize the window
            return;
        }
        if (dronePoint.conformsTo(PhantomJSDriver.class)) {
            ((WebDriver) dronePoint).manage().window().setSize(new Dimension(1280, 1024));
        }
    }

}
