import Modules.Car;
import Modules.Race;
import Modules.Road;
import Modules.Tunnel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GoRace {

    public static final int CARS_COUNT = 4;

    public static void main(String[] args) {
        System.out.println("¬¿∆ÕŒ≈ Œ¡⁄ﬂ¬À≈Õ»≈ >>> œÓ‰„ÓÚÓ‚Í‡!!!");
        Race race = new Race(new Road(60), new Tunnel(), new Road(40));
        Car[] cars = new Car[CARS_COUNT];
        ExecutorService service = Executors.newFixedThreadPool(CARS_COUNT);
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10));
        }
        for (Car car : cars) service.execute(car);
        race.start();
        race.finish();
        service.shutdown();
    }
}
