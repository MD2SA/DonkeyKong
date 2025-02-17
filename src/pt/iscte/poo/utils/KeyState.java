package pt.iscte.poo.utils;

public class KeyState {

        private static KeyState instance;

        private int keyCode = -1;
        private boolean isShiftDown = false;

        private KeyState() {}

        public static KeyState getInstance() {
                if( instance == null )
                        instance = new KeyState();
                return instance;
        }

        public int getKeyCode() {
                return keyCode;
        }

        public boolean isShiftDown() {
                return isShiftDown;
        }

        public void updateState(int keyCode, boolean isShiftDown) {
                this.keyCode = keyCode;
                this.isShiftDown = isShiftDown;
        }
}
