package greenway_myanmar.org.vo;

public class NetworkState {

    public static final NetworkState LOADED = new NetworkState(Status.SUCCESS);
    public static final NetworkState LOADING = new NetworkState(Status.LOADING);

    public Status status;
    public String msg;

    public NetworkState(Status status) {
        this(status, null);
    }

    public NetworkState(Status status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public static NetworkState error(String msg) {
        return new NetworkState(Status.ERROR, msg);
    }

    public Status getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NetworkState that = (NetworkState) o;

        if (status != that.status) return false;
        return msg != null ? msg.equals(that.msg) : that.msg == null;
    }

    @Override
    public int hashCode() {
        int result = status != null ? status.hashCode() : 0;
        result = 31 * result + (msg != null ? msg.hashCode() : 0);
        return result;
    }
}
