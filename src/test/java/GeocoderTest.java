import org.example.networking.Geocoder;
import org.junit.Test;


import java.util.List;


import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class GeocoderTest {

    private final Geocoder geocoder = new Geocoder();

    @Test
    public void getLatLng() throws Exception {
        String json = geocoder.getData(List.of("1600 Amphitheatre Parkway", "Mountain View", "CA"));
        assertThat(json, containsString("lat"));
        assertThat(json, containsString("lon"));

        double[] coords = geocoder.extractLatLon(json);
        System.out.printf("Lat: %.5f, Lon: %.5f%n", coords[0], coords[1]);

        // Assert Google HQ coords are close to ~37.42, -122.08
        assertThat(coords[0], is(org.hamcrest.number.IsCloseTo.closeTo(37.42, 0.1)));
        assertThat(coords[1], is(org.hamcrest.number.IsCloseTo.closeTo(-122.08, 0.1)));
    }

}
