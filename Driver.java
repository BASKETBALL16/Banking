import java.lang.Math;

public class Driver {
    public static void main(String[] args) {

        Plant normal = new Plant();
        DesertPlant desert = new DesertPlant();
        Cactus cactus = new Cactus();

        int temp = 50;
        int rainFall = 50;
        int humidity = 60;

        for (int i = 0; i < 10; i++) {
            temp += (int)(Math.random() * 76 - 35);
            rainFall += (int)(Math.random() * 51 - 25);
            humidity += (int)(Math.random() * 31 - 15);

            System.out.println("----------------");
            System.out.println("Cycle " + i);
            System.out.println("Temperature: " + temp);
            System.out.println("Rainfall: " + rainFall);
            System.out.println("Humidity: " + humidity);
            System.out.println();

            System.out.print("Normal Plant: ");
            normal.simulateCycle(temp, rainFall);

            System.out.print("Desert Plant: ");
            desert.simulateCycle(temp, rainFall);

            System.out.print("Cactus: ");
            cactus.simulateCycle(temp, rainFall);
            System.out.println();
        }
    }
}

class Plant {
    protected boolean died = false;

    public void simulateCycle(int temp, int rainFall) {
        if (died) {
            System.out.println("This plant is dead.");
        } 
        else if (survivesHeat(temp) && survivesRainfall(rainFall)) {
            System.out.println("This plant survives.");
        }
    }

    protected boolean survivesHeat(int temp) {
        if (temp > 110) {
            die("it was too hot.");
            return false;
        }
        return true;
    }

    protected boolean survivesRainfall(int rainFall) {
        if (rainFall <= 0) {
            die("of a drought.");
            return false;
        }
        return true;
    }

    public void die(String because) {
        System.out.println("This plant died because " + because);
        died = true;
    }
}


class Flower extends Plant {
    public String plantType = "flower";
    int overheatStreak = 0;


    @Override
    protected boolean survivesHeat(int temp) {

        if (temp > 100) {
            overheatStreak++;
            System.out.print("This flower has been overheating for " + overheatStreak + " days. ");
        } else
            overheatStreak = 0;

        
        if (overheatStreak >= 3) {
            die("the flower overheated for too long!");
            return false;
        } else
            return true;
    }
}
class DesertPlant extends Plant {
    protected int droughtStreak = 0;

    @Override
    protected boolean survivesRainfall(int rainFall) {
        if (rainFall <= 0) {
            droughtStreak++;
            System.out.print("Desert plant drought streak: " + droughtStreak + ". ");
        } else {
            droughtStreak = 0;
        }

        if (droughtStreak > 3) {
            die("it went too long without water.");
            return false;
        }
        return true;
    }
}

class Cactus extends DesertPlant {

    @Override
    protected boolean survivesRainfall(int rainFall) {
        // Cactus ignores rainfall
        return true;
    }

    @Override
    protected boolean survivesHeat(int temp) {
        if (temp < 20) {
            die("it froze in the cold.");
            return false;
        }
        return true;
    }
}
