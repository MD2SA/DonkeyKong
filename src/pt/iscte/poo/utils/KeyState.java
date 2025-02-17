package pt.iscte.poo.utils;

public class KeyState {

        private final int keyCode;
        private final boolean isShiftDown;

        public KeyState(int keyCode, boolean isShiftDown) {
                this.keyCode = keyCode;
                this.isShiftDown = isShiftDown;
        }

        public int getKeyCode() {
                return keyCode;
        }

        public boolean isShiftDown() {
                return isShiftDown;
        }
}
