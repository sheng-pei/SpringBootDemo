package org.example.springsession.keys;

public class ConfigKeyCenter implements KeyCenter {

    private AsyKey ec;

    public void setEc(AsyKey ec) {
        this.ec = ec;
    }

    @Override
    public AsyKey ec() {
        return ec;
    }

}
