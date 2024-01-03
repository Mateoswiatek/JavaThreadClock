
import java.time.LocalTime;
import java.util.TimerTask;

public class ClockTask extends TimerTask {
    @Override
    public void run() {
        LocalTime time = LocalTime.now();
        System.out.printf("%02d:%02d:%02d\n",
                time.getHour(),
                time.getMinute(),
                time.getSecond());
    }
}