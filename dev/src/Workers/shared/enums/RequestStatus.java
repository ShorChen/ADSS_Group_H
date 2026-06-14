package Workers.shared.enums;

public enum RequestStatus {
    PENDING("Pending"), ACCEPT("Accepted"), REJECT("Rejected");
    public final String status;

    RequestStatus(String status) {
        this.status = status;
    }

    public static RequestStatus fromArgs(String status){
        return valueOf(status.toUpperCase());
    }

    @Override
    public String toString() {
        return status;
    }
}
