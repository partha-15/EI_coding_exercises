// Legacy temperature system (Adaptee)
class LegacyTemperatureSystem {
    public double getTemperatureFahrenheit() {
        // Returns temperature in Fahrenheit
        return 98.6;
    }
}

// Modern temperature system (Target)
interface ModernTemperatureSystem {
    double getTemperatureCelsius();
}

// Adapter
class TemperatureAdapter implements ModernTemperatureSystem {
    private LegacyTemperatureSystem legacyTemperatureSystem;

    public TemperatureAdapter(LegacyTemperatureSystem legacyTemperatureSystem) {
        this.legacyTemperatureSystem = legacyTemperatureSystem;
    }

    @Override
    public double getTemperatureCelsius() {
        double tempFahrenheit = legacyTemperatureSystem.getTemperatureFahrenheit();
        return convertFahrenheitToCelsius(tempFahrenheit);
    }

    private double convertFahrenheitToCelsius(double fahrenheit) {
        return (fahrenheit - 32) * 5 / 9;
    }
}

// Main class to test the pattern
public class AdapterPatternDemo {
    public static void main(String[] args) {
        LegacyTemperatureSystem legacySystem = new LegacyTemperatureSystem();
        ModernTemperatureSystem modernSystem = new TemperatureAdapter(legacySystem);

        System.out.println("Temperature in Celsius: " + modernSystem.getTemperatureCelsius());
    }
}
