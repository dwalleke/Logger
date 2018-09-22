package nl.orsit.base;

public interface ServiceCallback {

    void cancel(PhpResult phpResult);

    void finish(PhpResult phpResult);
}
