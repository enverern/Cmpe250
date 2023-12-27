public class Edge {
    private Node Source_Node;
    private Node Destination_Node;
    private double distance;

    public Edge(Node source_Node, Node destination_Node) {
        this.Source_Node = source_Node;
        this.Destination_Node = destination_Node;
        this.distance = calculateDistance();
    }

    private double calculateDistance(){
        double lat1 = Source_Node.getAirport().getLatitude();
        double lon1 = Source_Node.getAirport().getLongitude();
        double lat2 = Destination_Node.getAirport().getLatitude();
        double lon2 = Destination_Node.getAirport().getLongitude();

        double R = 6371; // in km
        double lt1 = Math.toRadians(lat1);
        double lt2 = Math.toRadians(lat2);
        double ltdiff = Math.toRadians(lat2-lat1);
        double lndiff = Math.toRadians(lon2-lon1);

        double a = Math.sin(ltdiff/2) * Math.sin(ltdiff/2) + Math.cos(lt1) * Math.cos(lt2) * Math.sin(lndiff/2) * Math.sin(lndiff/2);

        double d = 2 * R * Math.asin(Math.sqrt(a));

        return d;

    }

    public double getDistance(){
        return distance;
    }

}