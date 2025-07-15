import java.util.Date;


public class Main {

    public static Long printStartTime() {
        Date start = new Date();
        long begin = start.getTime();
        System.out.println(start);
        System.out.println();
        return begin;
    }

    public static void printEndTime(Long startTime) {
        Date finish = new Date();
        long end = finish.getTime();
        System.out.println();
        System.out.println(finish);
        System.out.println("Time diff millis: " + (end - startTime));
    }

    public static void main(String[] args) throws Exception {
        long begin = printStartTime();

        McElieceService.run();

        printEndTime(begin);
    }



}
