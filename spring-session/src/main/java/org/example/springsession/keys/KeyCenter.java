package org.example.springsession.keys;

public interface KeyCenter {
    class AsyKey {
        private final String pri;
        private final String pub;

        public AsyKey(String pri, String pub) {
            this.pri = pri;
            this.pub = pub;
        }

        public String pri() {
            return pri;
        }

        public String pub() {
            return pub;
        }
    }

    AsyKey ec();
}
