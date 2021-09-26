package bank;


public class Account {
    private final int id;
    private final String cardN;
    private final String cardPIN;

    Account(int id) {
        this.id = id;
        cardN = createCardN();
        cardPIN = createCardPIN();
    }

    private String createCardN() {
        java.util.Random random = new java.util.Random();

        int mii = 4;
        int bin = 0;
        int ai = random.nextInt(999_999_999);
        String s = String.format("%d%05d%09d", mii, bin, ai);

        // Luhn Algorithm
        int sum = 0;
        for (int i = 0; i < 15; i++) {
            int num = s.charAt(i) - 48;
            if (i % 2 == 0) {
                int m = num * 2 > 9 ? (num * 2 - 9) : num * 2;
                sum += m;
                continue;
            }
            sum += num;
        }
        int cs = sum % 10 == 0 ? 0 : 10 - sum % 10;
        s += cs;
        return s;
    }

    private String createCardPIN() {

        return String.format("%04d",
                new java.util.Random().nextInt(9999));

    }

     int getId() {
        return id;
    }
//    long getCardN() {
//        return cardN;
//    }

//    String getpin() {
//        return cardPIN;
//    }

    String toDataBase() {
        return String.format("(%d, '%s', '%s')", id, cardN, cardPIN);
    }
}
