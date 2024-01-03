import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;

public class ClockWithGui extends JPanel {

    LocalTime time = LocalTime.now();

    public static void main(String[] args) {
        JFrame frame = new JFrame("Clock");
        ClockWithGui clock = new ClockWithGui();
        frame.setContentPane(clock);
        frame.setSize(700, 700);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setVisible(true);

        //new ClockThread(clock).start();
        Timer timer = new Timer(); // bo timer jest lepszy do periodycznych. sleep na conajmniej 1000ms
        timer.schedule(new ClockTask(clock), 0, 1000);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(getWidth() / 2, getHeight() / 2);

        for (int i = 1; i < 13; i++) {
            AffineTransform at = new AffineTransform();
            at.rotate(2 * Math.PI / 12 * i);
            Point2D src = new Point2D.Float(0, -120);
            Point2D trg = new Point2D.Float();
            at.transform(src, trg);
            g2d.drawString(Integer.toString(i), (int) trg.getX(), (int) trg.getY());
        }

        int hourHandLength = 60;
        int minuteHandLength = 80;
        int secondHandLength = 100;

        int hour = time.getHour() % 12;
        int minute = time.getMinute();
        int second = time.getSecond();

        double hourRotation = (hour + (double) minute / 60) * 2 * Math.PI / 12;
        double minuteRotation = (minute + (double) second / 60) * 2 * Math.PI / 60;
        double secondRotation = second * 2 * Math.PI / 60;

        drawHand(g2d, hourRotation, 0, 0, 0, -hourHandLength);
        drawHand(g2d, minuteRotation, 0, 0, 0, -minuteHandLength);
        drawHand(g2d, secondRotation, 0, 0, 0, -secondHandLength);
    }

    private void drawHand(Graphics2D g2d, double rotation, int x1, int y1, int x2, int y2) {
        AffineTransform saveAT = g2d.getTransform();
        g2d.rotate(rotation);
        g2d.drawLine(x1, y1, x2, y2);
        g2d.setTransform(saveAT);
    }

    static class ClockTask extends TimerTask {
        private final ClockWithGui clock;

        ClockTask(ClockWithGui clock) {
            this.clock = clock;
        }
        @Override
        public void run() {
            while (true) {
                clock.time = LocalTime.now();
                clock.repaint();
            }
        }
    }

    static class ClockThread extends Thread {
        private final ClockWithGui clock;

        ClockThread(ClockWithGui clock) {
            this.clock = clock;
        }

        @Override
        public void run() {
            while (true) {
                clock.time = LocalTime.now();
                clock.repaint();

                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
