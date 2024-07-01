package Util;

public class Loop extends Thread{

    private int PS;
    private final Executable e;
    private double deltaTask, task;
    private long now, last, lastPrint;
    private int tasks;

    public Loop(Executable e, int PS){
        this.PS = PS;
        this.e = e;
    }
    @Override
    public void run() {

        last = System.nanoTime();

        deltaTask = 1000000000.0/PS;

        task = 0;

        tasks = 0;
        lastPrint = System.currentTimeMillis();

        while(true){
            now = System.nanoTime();
            task += (now - last)/ deltaTask;
            last = now;

            if (task >= 1){
                e.execute();
                task--;
                tasks++;
            }

            if (lastPrint + 1000 <= System.currentTimeMillis()){
                lastPrint = System.currentTimeMillis();
                if (tasks < PS){
                    System.out.println(tasks + " (" + (tasks-PS) + ")");
                }
                tasks = 0;
            }
        }
    }

    protected void setPS(int PS){
        this.PS = PS;
        deltaTask = 1000000000.0/PS;
    }

    protected void restartLoop(){
        task = 0;
        last = System.nanoTime();
    }
}
