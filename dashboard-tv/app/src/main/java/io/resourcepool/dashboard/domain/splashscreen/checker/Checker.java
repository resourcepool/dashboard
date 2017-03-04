package io.resourcepool.dashboard.domain.splashscreen.checker;

/**
 * Created by loicortola on 04/03/2017.
 */

public abstract class Checker {

    private boolean started;
    private boolean ready;
    private final int checkerId;
    private final CheckerStatusListener listener;

    protected Checker(int checkerId, CheckerStatusListener listener) {
        this.checkerId = checkerId;
        this.listener = listener;
    }

    public final int getCheckerId() {
        return checkerId;
    }

    public boolean isReady() {
        return ready;
    }

    public final boolean isStarted() {
        return started;
    }

    public abstract int getStatusMessage();

    public void start() {
        started = true;
    }

    public void stop() {
        started = false;
    }

    protected final void setReady(boolean ready) {
        this.ready = ready;
        listener.onCheckerStatusChanged(checkerId, ready, null);
    }

    protected final void setReady(boolean ready, Object result) {
        this.ready = ready;
        listener.onCheckerStatusChanged(checkerId, ready, result);
    }

    protected final void setFailed() {
        setFailed(0);
    }

    protected final void setFailed(int code) {
        this.ready = false;
        stop();
        listener.onCheckerStatusFailed(checkerId, code);
    }

    public interface CheckerStatusListener {
        void onCheckerStatusChanged(int checkerId, boolean ready, Object result);

        void onCheckerStatusFailed(int checkerId, int code);
    }
}
