import java.util.Objects;

interface PaymentProcessor {
    void charge(long amountInCents);
}

class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String message) {
        super(message);
    }
}

class MedievalCoinPurse {
    private final long goldPieces;
    private final long silverPieces;

    public MedievalCoinPurse(long goldPieces, long silverPieces) {
        this.goldPieces = goldPieces;
        this.silverPieces = silverPieces;
    }

    public long countGoldPieces() { return goldPieces; }
    public long countSilverPieces() { return silverPieces; }
}

class MedievalCoinPurseAdapter implements PaymentProcessor {
    private static final long CENTS_PER_GOLD = 100L;
    private static final long CENTS_PER_SILVER = 10L;

    private final MedievalCoinPurse purse;

    public MedievalCoinPurseAdapter(MedievalCoinPurse purse) {
        this.purse = Objects.requireNonNull(purse, "Purse cannot be null.");
    }

    @Override
    public void charge(long amountInCents) {
        long availableCents = calculateTotalCents(purse.countGoldPieces(), purse.countSilverPieces());

        if (amountInCents < 0) {
            throw new IllegalArgumentException("Charge amount cannot be negative.");
        }
        if (availableCents < amountInCents) {
            throw new InsufficientFundsException("Purse value (" + availableCents + "¢) is less than required (" + amountInCents + "¢).");
        }

        System.out.println("Medieval Adapter: Charged " + amountInCents + " cents successfully.");
    }

    private long calculateTotalCents(long gold, long silver) {
        if (gold < 0 || silver < 0) {
            throw new IllegalStateException("Coin counts cannot be negative.");
        }
        return Math.addExact(Math.multiplyExact(gold, CENTS_PER_GOLD), Math.multiplyExact(silver, CENTS_PER_SILVER));
    }
}

class RomanTreasureChest {
    private final double denarii;
    private final double sestertii;

    public RomanTreasureChest(double denarii, double sestertii) {
        this.denarii = denarii;
        this.sestertii = sestertii;
    }

    public double getDenariiCount() { return denarii; }
    public double getSestertiiCount() { return sestertii; }
}

class RomanTreasureChestAdapter implements PaymentProcessor {
    private static final double CENTS_PER_DENARIUS = 62.5;
    private static final double CENTS_PER_SESTERTIUS = 15.625;

    private final RomanTreasureChest chest;

    public RomanTreasureChestAdapter(RomanTreasureChest chest) {
        this.chest = Objects.requireNonNull(chest, "Chest cannot be null.");
    }

    @Override
    public void charge(long amountInCents) {
        long availableCents = calculateTotalCents(chest.getDenariiCount(), chest.getSestertiiCount());

        if (amountInCents < 0) {
            throw new IllegalArgumentException("Charge amount cannot be negative.");
        }
        if (availableCents < amountInCents) {
            throw new InsufficientFundsException("Roman Chest value (" + availableCents + "¢) is less than required (" + amountInCents + "¢).");
        }

        System.out.println("Roman Adapter: Charged " + amountInCents + " cents successfully.");
    }

    private long calculateTotalCents(double denarii, double sestertii) {
        if (denarii < 0 || sestertii < 0) {
            throw new IllegalStateException("Values cannot be negative.");
        }
        double rawCents = (denarii * CENTS_PER_DENARIUS) + (sestertii * CENTS_PER_SESTERTIUS);
        return Math.round(rawCents);
    }
}

public class Main {
    public static void main(String[] args) {
        PaymentProcessor medievalPayment = new MedievalCoinPurseAdapter(new MedievalCoinPurse(5, 20));

        PaymentProcessor romanPayment = new RomanTreasureChestAdapter(new RomanTreasureChest(10.5, 4.0));

        System.out.println("--- Processing Payments ---");
        processCheckout(medievalPayment, 500);
        processCheckout(romanPayment, 500);
    }

    public static void processCheckout(PaymentProcessor processor, long amount) {
        processor.charge(amount);
    }
}