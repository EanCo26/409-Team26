import java.util.List;

interface CarElement {
    void accept(CarElementVisitor visitor);
}

interface CarElementVisitor {
    void visit(Body body);
    void visit(Car car);
    void visit(Engine engine);
    void visit(Wheel wheel);
}

class Wheel implements CarElement {
  private final String name;
  private float tyrePressure = 0f;

  public Wheel(final String name, float tyrePressure) {
      this.name = name;
      this.tyrePressure = tyrePressure;
  }

  public String getName() {
      return name;
  }
  public float getTyrePressure() {
      return tyrePressure;
  }
  @Override
  public void accept(CarElementVisitor visitor) {
      visitor.visit(this);
  }
}

class Body implements CarElement {
  private String bodyType = "";

  public Body(final String bodyType) {
        this.bodyType = bodyType;
  }

  @Override
  public void accept(CarElementVisitor visitor) {
      visitor.visit(this);
  }

  public String getBodyType(){
      return bodyType;
  }
}

class Engine implements CarElement {
  private String oilLevel = "";
  private String engineSize = "";

  public Engine(String oilLevel, String engineSize) {
    this.oilLevel = oilLevel;
    this.engineSize = engineSize;
    }

  @Override
  public void accept(CarElementVisitor visitor) {
      visitor.visit(this);
  }

  public String getOilLevel(){
    return oilLevel;
  }
  public String getEngineLevelSize(){
    return engineSize;
  }
}

class Car implements CarElement {
    private final List<CarElement> elements;

    public Car(float fl, float  fr, float bl, float br, String bT, String oL, String eS) {
        this.elements = List.of(
            new Wheel("front left", fl), new Wheel("front right", fr),
            new Wheel("back left", bl), new Wheel("back right", br),
            new Body(bT), new Engine(oL, eS)
        );
    }

    @Override
    public void accept(CarElementVisitor visitor) {
        for (CarElement element : elements) {
            element.accept(visitor);
        }
        visitor.visit(this);
    }
}

class CarElementDoVisitor implements CarElementVisitor {
    @Override
    public void visit(Body body) {
        System.out.println("Moving my body");
    }

    @Override
    public void visit(Car car) {
        System.out.println("Starting my car");
    }

    @Override
    public void visit(Wheel wheel) {
        System.out.println("Kicking my " + wheel.getName() + " wheel");
    }

    @Override
    public void visit(Engine engine) {
        System.out.println("Starting my engine");
    }
}

class CarElementPrintVisitor implements CarElementVisitor {
    @Override
    public void visit(Body body) {
        System.out.println("Visiting body");
    }

    @Override
    public void visit(Car car) {
        System.out.println("Visiting car");
    }

    @Override
    public void visit(Engine engine) {
        System.out.println("Visiting engine");
    }

    @Override
    public void visit(Wheel wheel) {
        System.out.println("Visiting " + wheel.getName() + " wheel");
    }
}

class SalesVisitor implements CarElementVisitor {
    @Override
    public void visit(Body body) {
        System.out.println("The car is a " + body.getBodyType());
    }

    @Override
    public void visit(Car car) {
        return;
    }

    @Override
    public void visit(Engine engine) {
        System.out.println("The engine size is " + engine.getEngineLevelSize());
    }

    @Override
    public void visit(Wheel wheel) {
        return;
    }
}

class MechanicVisitor implements CarElementVisitor {
    @Override
    public void visit(Body body) {
        return;
    }

    @Override
    public void visit(Car car) {
        return;
    }

    @Override
    public void visit(Engine engine) {
        System.out.println("The engine oil is " + engine.getOilLevel());
    }

    @Override
    public void visit(Wheel wheel) {
        System.out.println("The tyre pressure is " + wheel.getTyrePressure());
    }
}

public class VisitorDemo {

    public static void main(final String[] args) {
        float tPress[] = {30f, 30f, 30f, 30f};
        String bType = "Hatchback";
        String oLevel = "High";
        String eSize = "Big";
        
        for(int i = 0; i < 5; i ++){
            float tPress[] = {30f, 30f, 30f, 30f};
            String bType = "Hatchback";
            String oLevel = "High";
            String eSize = "Big";
        }

        for(int i = 0; i < 5; i ++){

            Car car = new Car(0.5f, 0.5f, 0.5f, 0.5f, "Rover", "Low", "Small");

            car.accept(new CarElementPrintVisitor());
            car.accept(new CarElementDoVisitor());
            car.accept(new MechanicVisitor());
            car.accept(new SalesVisitor());

        }
    }


    public  void Last() {

    }
}