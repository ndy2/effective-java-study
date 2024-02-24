package nam.chapter7.item42;

public class Main {

    interface MyInterface {
        void run();
    }

    public static void main(String[] args) {

        int x = 10;
        x = 30;
        MyInterface myInterface1 = () -> {
            System.out.println(x);
        };


        int y = 20;
        MyInterface myInterface2 = new MyInterface() {
            @Override
            public void run() {
                System.out.println(y);
            }
        };

        System.out.println("myInterface1 = " + myInterface1);
        System.out.println("myInterface2 = " + myInterface2);
    }
}
