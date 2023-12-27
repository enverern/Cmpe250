public class Airport {
    private final String AirportCode;
    private final String AirfieldName;
    private final double Latitude;
    private final double Longitude;
    private final double ParkingCost;

    public Airport(String airportCode, String airfieldName, double latitude, double longitude, int parkingCost) {
        this.AirportCode = airportCode;
        this.AirfieldName = airfieldName;
        this.Latitude = latitude;
        this.Longitude = longitude;
        this.ParkingCost = parkingCost;
    }

    public String getAirportCode() {
        return AirportCode;
    }

    public String getAirfieldName() {
        return AirfieldName;
    }

    public double getLatitude() {
        return Latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public double getParkingCost() {
        return ParkingCost;
    }

}