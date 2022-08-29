public class WaitNotify {
    private char currentLetter = 'a';

    public static void main(String[] args) {
        WaitNotify obj = new WaitNotify();
        new Thread(() -> obj.print('a', 'b')).start();
        new Thread(() -> obj.print('b', 'c')).start();
        new Thread(() -> obj.print('c', 'a')).start();
    }

    public void print(char current, char next) {
        synchronized (this) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (currentLetter != current) {
                        wait();
                    }
                    System.out.print(current);
                    currentLetter = next;
                    notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}