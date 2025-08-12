// Table class with a synchronized method
class Table {
    synchronized void printTable(int n) {
        for (int i = 1; i <= 5; i++) {
            System.out.println(n * i);
            try {
                // Thread.sleep(400);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}

// Thread that prints table of 5
class MyThread1 extends Thread {
    Table t;

    MyThread1(Table t) {
        this.t = t;
    }

    public void run() {
        t.printTable(5);
    }
}

// Thread that prints table of 100
class MyThread2 extends Thread {
    Table t;

    MyThread2(Table t) {
        this.t = t;
    }

    public void run() {
        t.printTable(100);
    }
}

// Main class
class TestSynchronization {
    public static void main(String[] args) {
        Table obj = new Table(); // Only one Table object shared between threads

        MyThread1 t1 = new MyThread1(obj);
        MyThread2 t2 = new MyThread2(obj);

        t1.start();
        t2.start();
    }
}
