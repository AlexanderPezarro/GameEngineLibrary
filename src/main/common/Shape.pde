public enum Shape {
        CIRCLE (1),
        RECT (2),
        TRIANGLE (4);

        private int value;
        private Shape(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    };